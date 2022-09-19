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

class CreditCardActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: CreditCardViewModel

    private var previousEightDigitNumber = ""

    private val transactionDetails: TransactionDetails? by lazy {
        intent.getParcelableExtra(CreditCardActivity.EXTRA_TRANSACTION_DETAILS) as? TransactionDetails
    }

    private val totalAmount: String by lazy {
        intent.getStringExtra(CreditCardActivity.EXTRA_TOTAL_AMOUNT) ?: throw RuntimeException("Total amount must not be empty")
    }

    private val customerDetail: CustomerInfo? by lazy {
        intent.getParcelableExtra(CreditCardActivity.EXTRA_CUSTOMER_DETAIL) as? CustomerInfo
    }

    private val creditCard: CreditCard? by lazy {
        intent.getParcelableExtra(CreditCardActivity.EXTRA_CREDIT_CARD) as? CreditCard
    }

    private val snapToken: String by lazy {
        intent.getStringExtra(CreditCardActivity.EXTRA_SNAP_TOKEN)?: throw RuntimeException("Snaptoken must not be empty")
    }


    companion object {
        private const val EXTRA_SNAP_TOKEN = "card.extra.snap_token"
        private const val EXTRA_TRANSACTION_DETAILS = "card.extra.transaction_details"
        private const val EXTRA_TOTAL_AMOUNT = "card.extra.total_amount"
        private const val EXTRA_CUSTOMER_DETAIL = "card.extra.customer_detail"
        private const val EXTRA_CREDIT_CARD = "card.extra.credit_card"

        fun getIntent(
            activityContext: Context,
            snapToken: String,
            totalAmount: String,
            transactionDetails: TransactionDetails?,
            customerInfo: CustomerInfo? = null,
            creditCard: CreditCard?,
        ): Intent {
            return Intent(activityContext, CreditCardActivity::class.java).apply {
                putExtra(EXTRA_SNAP_TOKEN, snapToken)
                putExtra(EXTRA_TRANSACTION_DETAILS, transactionDetails)
                putExtra(EXTRA_TOTAL_AMOUNT, totalAmount)
                putExtra(
                    EXTRA_CUSTOMER_DETAIL,
                    customerInfo
                )
                putExtra(EXTRA_CREDIT_CARD, creditCard)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerUiKitComponent.builder().applicationContext(this.applicationContext).build().inject(this)
        initTransactionResultScreenObserver()
        setContent {
            CreditCardPageStateFull(
                transactionDetails = transactionDetails,
                customerDetail = customerDetail,
                creditCard = creditCard
            )
        }
    }

    private fun initTransactionResultScreenObserver(){
        //TODO: Need to be revisit on handling all the payment status
        viewModel.getTransactionResponseLiveData().observe(this, Observer {
            if (it.statusCode != UiKitConstants.STATUS_CODE_201 && it.redirectUrl.isNullOrEmpty()) {
                val intent = SuccessScreenActivity.getIntent(
                    activityContext = this@CreditCardActivity,
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
                        activityContext = this@CreditCardActivity,
                        total = totalAmount,
                        orderId = it?.orderId.toString()
                    )
                }
                else -> {
                    intent = ErrorScreenActivity.getIntent(
                        activityContext = this@CreditCardActivity,
                        title = it.statusCode.toString(),
                        content = it.transactionStatus.toString()
                    )
                }
            }
            startActivity(intent)
        })
        viewModel.getErrorLiveData().observe(this, Observer {
            val intent = ErrorScreenActivity.getIntent(
                activityContext = this@CreditCardActivity,
                title = it.cause.toString(),
                content = it.message.toString()
            )
            startActivity(intent)
        })
    }

    @Composable
    private fun CreditCardPageStateFull(
        transactionDetails: TransactionDetails? = null,
        customerDetail: CustomerInfo? = null,
        creditCard: CreditCard?,
    ) {
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
                isSaveCardChecked = true,
                principalIconId = null
            )
        }
        val transactionResponse by viewModel.getTransactionResponseLiveData().observeAsState()
        val bankCodeId by viewModel.bankIconId.observeAsState(null)
        var isExpanding by remember { mutableStateOf(false) }

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
            CreditCardPageStateLess(
                state = state,
                isExpandingState = isExpanding,
                totalAmount = totalAmount,
                orderId = transactionDetails?.orderId.toString(),
                customerDetail = customerDetail,
                creditCard = creditCard,
                bankCodeState = bankCodeId,
                onExpand = { isExpanding = it },
                onCardNumberValueChange = {

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
                onClick = {
                    viewModel.chargeUsingCreditCard(
                        transactionDetails = transactionDetails,
                        cardNumber = state.cardNumber,
                        cardExpiry = state.expiry,
                        cardCvv = state.cvv,
                        isSavedCard = state.isSavedCardChecked,
                        customerEmail = "johndoe@midtrans.com",
                        snapToken = snapToken
                    )
                }
            )
        }
    }

    @Composable
    private fun CreditCardPageStateLess(
        state: NormalCardItemState,
        isExpandingState: Boolean,
        totalAmount: String,
        orderId: String,
        customerDetail: CustomerInfo? = null,
        creditCard: CreditCard?,
        bankCodeState: Int?,
        onExpand: (Boolean) -> Unit,
        onCardNumberValueChange: (TextFieldValue) -> Unit,
        onClick: () -> Unit
    ){
        Column() {
            SnapAppBar(
                title = stringResource(id = R.string.payment_summary_cc_dc),
                iconResId = R.drawable.ic_arrow_left) {
                onBackPressed()
            }
            val scrollState = rememberScrollState()

            SnapOverlayExpandingBox(
                isExpanded = isExpandingState,
                mainContent = {
                    SnapTotal(
                        amount = totalAmount,
                        orderId = orderId,
                        remainingTime = null,
                        canExpand = customerDetail != null,
                    ) {
                        onExpand(it)
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
                        modifier = Modifier
                            .verticalScroll(scrollState)
                            .background(SnapColors.getARGBColor(SnapColors.OVERLAY_WHITE))
                    ) {

                        NormalCardItem(
                            state = state,
                            bankIcon = bankCodeState,
                            creditCard = creditCard,
                            onCardNumberValueChange = {
                                onCardNumberValueChange(it)
                            },
                            onExpiryDateValueChange = { state.expiry = it },
                            onCvvValueChange = { state.cvv = it },
                            onCardTextFieldFocusedChange = { state.isCardTexFieldFocused = it },
                            onExpiryTextFieldFocusedChange = { state.isExpiryTextFieldFocused = it },
                            onCvvTextFieldFocusedChange = { state.isCvvTextFieldFocused = it },
                            onSavedCardCheckedChange = { state.isSavedCardChecked = it }
                        )

                        SnapButton(
                            text = stringResource(id = R.string.cc_dc_main_screen_cta),
                            style = SnapButton.Style.PRIMARY,
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(top = 16.dp, bottom = 16.dp),
                            enabled = !(state.isCardNumberInvalid ||
                                    state.isExpiryInvalid ||
                                    state.isCvvInvalid ||
                                    state.cardNumber.text.isEmpty() ||
                                    state.expiry.text.isEmpty() ||
                                    state.cvv.text.isEmpty()),
                            onClick = { onClick() }
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(all = 16.dp)
            )
        }
    }

    @Preview
    @Composable
    private fun forPreview() {
        CreditCardPageStateFull(
            transactionDetails = transactionDetails,
            customerDetail = CustomerInfo(
                "Ari Bhakti",
                "087788778212",
                listOf("Jl. ABC", "Rumah DEF")
            ),
            creditCard = CreditCard()
        )
    }
}

