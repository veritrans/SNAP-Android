package com.midtrans.sdk.uikit.internal.presentation.creditcard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import com.midtrans.sdk.corekit.api.model.CreditCard
import com.midtrans.sdk.corekit.internal.network.model.response.TransactionDetails
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.internal.di.DaggerUiKitComponent
import com.midtrans.sdk.uikit.internal.model.CustomerInfo
import com.midtrans.sdk.uikit.internal.presentation.ErrorScreenActivity
import com.midtrans.sdk.uikit.internal.presentation.SuccessScreenActivity
import com.midtrans.sdk.uikit.internal.util.SnapCreditCardUtil
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import com.midtrans.sdk.uikit.internal.view.*
import javax.inject.Inject

class SavedCardActivity: BaseActivity() {

    @Inject
    lateinit var viewModel: SavedCardViewModel

    private val creditCard: CreditCard? by lazy {
        intent.getParcelableExtra(SavedCardActivity.EXTRA_CREDIT_CARD) as? CreditCard
    }

    private val transactionDetails: TransactionDetails? by lazy {
        intent.getParcelableExtra(SavedCardActivity.EXTRA_TRANSACTION_DETAILS) as? TransactionDetails
    }

    private val snapToken: String by lazy {
        intent.getStringExtra(SavedCardActivity.EXTRA_SNAP_TOKEN).orEmpty()
    }

    private val totalAmount: String by lazy {
        intent.getStringExtra(SavedCardActivity.EXTRA_TOTAL_AMOUNT)
            ?: throw RuntimeException("Total amount must not be empty")
    }

    private val customerDetail: CustomerInfo? by lazy {
        intent.getParcelableExtra(SavedCardActivity.EXTRA_CUSTOMER_DETAIL) as? CustomerInfo
    }

    companion object {
        private const val EXTRA_SNAP_TOKEN = "savedCard.extra.snap_token"
        private const val EXTRA_TOTAL_AMOUNT = "savedCard.extra.total_amount"
        private const val EXTRA_TRANSACTION_DETAILS = "savedCard.extra.transaction_details"
        private const val EXTRA_CUSTOMER_DETAIL = "savedCard.extra.customer_detail"
        private const val EXTRA_CREDIT_CARD = "savedCard.extra.credit_card"

        fun getIntent(
            activityContext: Context,
            transactionDetails: TransactionDetails?,
            snapToken: String,
            totalAmount: String,
            customerInfo: CustomerInfo? = null,
            creditCard: CreditCard?
        ): Intent {
            return Intent(activityContext, SavedCardActivity::class.java).apply {
                putExtra(EXTRA_SNAP_TOKEN, snapToken)
                putExtra(EXTRA_TOTAL_AMOUNT, totalAmount)
                putExtra(EXTRA_TRANSACTION_DETAILS, transactionDetails)
                putExtra(EXTRA_CUSTOMER_DETAIL, customerInfo)
                putExtra(EXTRA_CREDIT_CARD, creditCard)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerUiKitComponent.builder().applicationContext(this.applicationContext).build().inject(this)
        initTransactionResultScreenObserver()
        setContent {
            CreditCardPage(
                totalAmount = totalAmount,
                orderId = transactionDetails?.orderId.toString(),
                customerDetail = customerDetail
            )
        }
    }

    private fun initTransactionResultScreenObserver(){
        //TODO: Need to be revisit on handling all the payment status
        viewModel.getTransactionResponseLiveData().observe(this, Observer {
            if (it.statusCode != UiKitConstants.STATUS_CODE_201 && it.redirectUrl.isNullOrEmpty()) {
                val intent = SuccessScreenActivity.getIntent(
                    activityContext = this@SavedCardActivity,
                    total = totalAmount,
                    orderId = it?.orderId.toString()
                )
                startActivity(intent)
            }
        })
        viewModel.getTransactionStatusLiveData().observe(this, Observer {
            var intent = Intent()
            when (it.statusCode) {
                UiKitConstants.STATUS_CODE_200 -> {
                    intent = SuccessScreenActivity.getIntent(
                        activityContext = this@SavedCardActivity,
                        total = totalAmount,
                        orderId = it?.orderId.toString()
                    )
                }
                else -> {
                    intent = ErrorScreenActivity.getIntent(
                        activityContext = this@SavedCardActivity,
                        title = it.statusCode.toString(),
                        content = it.transactionStatus.toString()
                    )
                }
            }
            startActivity(intent)
        })
        viewModel.getErrorLiveData().observe(this, Observer {
            val intent = ErrorScreenActivity.getIntent(
                activityContext = this@SavedCardActivity,
                title = it.cause.toString(),
                content = it.message.toString()
            )
            startActivity(intent)
        })
    }

    private fun formatMaskedCard(maskedCard: String): String {
        val lastFourDigit = maskedCard.substring(startIndex = maskedCard.length - 4, endIndex = maskedCard.length)
        return "**** **** **** $lastFourDigit"
    }

    @Composable
    private fun CreditCardPage(
        totalAmount: String,
        orderId: String,
        customerDetail: CustomerInfo?
    ){
        var previousEightDigitNumber = ""
        val bankCodeId by viewModel.bankIconId.observeAsState(null)
        var isCvvSavedCardInvalid by remember { mutableStateOf(false)}
        var savedTokenList = mutableListOf<FormData>()
        var isExpanding by remember { mutableStateOf(false) }
        val state = remember {
            NormalCardItemState(
                cardNumber = TextFieldValue(),
                expiry = TextFieldValue(),
                cvv = TextFieldValue(),
                isCardNumberInvalid = false,
                isExpiryInvalid = false,
                isCvvInvalid = false,
                isCardTexFieldFocused = false,
                isExpiryTextFieldFocused = false,
                isCvvTextFieldFocused = false,
                principalIconId = null,
                isSaveCardChecked = true,
                customerPhone = TextFieldValue(),
                customerEmail = TextFieldValue()
            )
        }

        creditCard?.savedTokens?.forEachIndexed { index, savedToken ->
            savedTokenList.add(
                SavedCreditCardFormData(
                    savedCardIdentifier = SnapCreditCardUtil.SAVED_CARD_IDENTIFIER + index.toString(),
                    inputTitle = stringResource(id = R.string.cc_dc_saved_card_enter_cvv),
                    endIcon = R.drawable.ic_trash,
                    startIcon = SnapCreditCardUtil.getBankIcon(savedToken.binDetail?.bankCode.toString()),
                    errorText = remember { mutableStateOf("") },
                    maskedCardNumber = formatMaskedCard(savedToken.maskedCard.toString()),
                    displayedMaskedCard = savedToken.maskedCard.toString(),
                    tokenType = savedToken.tokenType.toString(),
                    tokenId = savedToken.token.toString(),
                    cvvSavedCardTextField = TextFieldValue(),
                    isCvvSavedCardInvalid = isCvvSavedCardInvalid
                )
            )
        }
        savedTokenList.add(NewCardFormData(
            newCardIdentifier = SnapCreditCardUtil.NEW_CARD_FORM_IDENTIFIER,
        ))
        var savedTokenListState = savedTokenList.toMutableStateList()
        var selectedFormData : FormData? = savedTokenList.first()
        var isSelectedSavedCardCvvInvalid by remember { mutableStateOf(false) }
        var selectedCvvTextFieldValue by remember{ mutableStateOf(TextFieldValue())}
        val transactionResponse by viewModel.getTransactionResponseLiveData().observeAsState()

        if (transactionResponse?.statusCode == UiKitConstants.STATUS_CODE_201 && !transactionResponse?.redirectUrl.isNullOrEmpty()){
            transactionResponse?.redirectUrl?.let {
                SnapThreeDsWebView(
                    url = it,
                    transactionResponse = transactionResponse,
                    onPageStarted = {},
                    onPageFinished = {
                        finish()
                        viewModel.getTransactionStatus(snapToken)
                    }
                )
            }
        } else {
            Column(
                modifier = Modifier.background(SnapColors.getARGBColor(SnapColors.OVERLAY_WHITE))
            ) {
                SnapAppBar(
                    title = stringResource(id = R.string.payment_summary_cc_dc),
                    iconResId = R.drawable.ic_arrow_left
                ) {
                    onBackPressed()
                }
                var scrollState = rememberScrollState()
                SnapOverlayExpandingBox(
                    isExpanded = isExpanding,
                    mainContent = {
                        SnapTotal(
                            amount = totalAmount,
                            orderId = orderId,
                            remainingTime = null,
                            canExpand = customerDetail != null,
                        ) {
                            isExpanding = it
                        }
                    },
                    expandingContent = {
                        customerDetail?.let {
                            SnapCustomerDetail(
                                name = customerDetail.name,
                                phone = customerDetail.phone,
                                addressLines = customerDetail.addressLines
                            )
                        }
                    },
                    followingContent = {
                        Column(
                            modifier = Modifier.verticalScroll(scrollState)
                        ) {
                            SnapSavedCardRadioGroup(
                                modifier = Modifier
                                    .padding(top = 24.dp),
                                listStates = savedTokenListState,
                                normalCardItemState = state,
                                bankIconState = bankCodeId,
                                creditCard = creditCard,
                                onItemRemoveClicked = {
                                    viewModel.deleteSavedCard(snapToken = snapToken, maskedCard = it.displayedMaskedCard)
                                    savedTokenListState.remove(it)
                                },
                                onCvvSavedCardValueChange = {
                                    selectedCvvTextFieldValue = it
                                    isSelectedSavedCardCvvInvalid = selectedCvvTextFieldValue.text.length < SnapCreditCardUtil.FORMATTED_MAX_CVV_LENGTH
                                },
                                onCardNumberOtherCardValueChange = {
                                    state.cardNumber = it
                                    var cardNumberWithoutSpace = SnapCreditCardUtil.getCardNumberFromTextField(it)
                                    if(cardNumberWithoutSpace.length >= SnapCreditCardUtil.SUPPORTED_MAX_BIN_NUMBER){
                                        var eightDigitNumber = cardNumberWithoutSpace.substring(0, SnapCreditCardUtil.SUPPORTED_MAX_BIN_NUMBER)
                                        if (eightDigitNumber != previousEightDigitNumber){
                                            previousEightDigitNumber = eightDigitNumber
                                            viewModel.getBankIconImage(
                                                binNumber = eightDigitNumber
                                            )
                                        }
                                    } else {
                                        viewModel.setBankIconToNull()
                                        previousEightDigitNumber = cardNumberWithoutSpace
                                    }
                                },
                                onExpiryOtherCardValueChange =  {state.expiry = it},
                                onSavedCardRadioSelected = { selectedFormData = it },
                                onIsCvvSavedCardInvalidValueChange = { isSelectedSavedCardCvvInvalid = it },
                                onCvvOtherCardValueChange = {
                                    state.cvv = it
                                },
                                onSavedCardCheckedChange = { state.isSavedCardChecked = it }
                            )
                            SnapButton(
                                text = stringResource(id = R.string.cc_dc_main_screen_cta),
                                style = SnapButton.Style.PRIMARY,
                                modifier = Modifier
                                    .fillMaxWidth(1f),
                                enabled = checkIsPayButtonEnabled(
                                    selectedFormData = selectedFormData,
                                    isSelectedSavedCardCvvInvalid = isSelectedSavedCardCvvInvalid,
                                    selectedCvvTextFieldValue = selectedCvvTextFieldValue,
                                    isCardNumberInvalid = state.isCardNumberInvalid,
                                    isExpiryInvalid = state.isExpiryInvalid,
                                    isCvvInvalid = state.isCvvInvalid,
                                    cardNumber = state.cardNumber,
                                    expiry = state.expiry,
                                    cvv = state.cvv
                                ),
                                onClick = {
                                    selectedFormData?.let {
                                        when (selectedFormData){
                                            is SavedCreditCardFormData -> {
                                                viewModel.chargeUsingSavedCard(
                                                    formData = it as SavedCreditCardFormData,
                                                    snapToken = snapToken,
                                                    cardCVV = selectedCvvTextFieldValue.text,
                                                    customerEmail = "johndoe@midtrans.com",
                                                    transactionDetails = transactionDetails
                                                )
                                            }
                                            is NewCardFormData -> {
                                                viewModel.chargeUsingOtherCard(
                                                    transactionDetails = transactionDetails,
                                                    cardNumber = state.cardNumber,
                                                    cardExpiry = state.expiry,
                                                    cardCvv = state.cvv,
                                                    isSavedCard = state.isSavedCardChecked,
                                                    customerEmail = "johndoe@midtrans.com",
                                                    snapToken = snapToken
                                                )
                                            }
                                        }
                                    }
                                }
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxHeight(1f)
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                )
            }
        }
    }
    private fun checkIsPayButtonEnabled(
        selectedFormData: FormData?,
        isSelectedSavedCardCvvInvalid: Boolean,
        selectedCvvTextFieldValue: TextFieldValue,
        isCardNumberInvalid: Boolean,
        isExpiryInvalid: Boolean,
        isCvvInvalid: Boolean,
        cardNumber: TextFieldValue,
        expiry: TextFieldValue,
        cvv: TextFieldValue
    ): Boolean{
        var output = false
        selectedFormData?.let {
            when (it){
                is SavedCreditCardFormData -> {
                    output = !(isSelectedSavedCardCvvInvalid ||
                            selectedCvvTextFieldValue.text.isEmpty())
                }
                is NewCardFormData -> {
                    output = !(isCardNumberInvalid ||
                            isExpiryInvalid ||
                            isCvvInvalid ||
                            cardNumber.text.isEmpty() ||
                            expiry.text.isEmpty() ||
                            cvv.text.isEmpty())
                }
            }
        }
        return output
    }

    @Preview
    @Composable
    private fun Preview() {
        CreditCardPage(
            totalAmount = "10000",
            orderId = "Order ID #34345445554",
            customerDetail = CustomerInfo(
                "Ari Bhakti",
                "087788778212",
                listOf("Jl. ABC", "Rumah DEF")
            )
        )
    }
}