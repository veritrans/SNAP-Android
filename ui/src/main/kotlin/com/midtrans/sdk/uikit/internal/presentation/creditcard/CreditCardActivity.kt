package com.midtrans.sdk.uikit.internal.presentation.creditcard

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rxjava2.subscribeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import com.midtrans.sdk.corekit.api.model.CreditCard
import com.midtrans.sdk.corekit.internal.network.model.response.Merchant
import com.midtrans.sdk.corekit.internal.network.model.response.TransactionDetails
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.internal.di.DaggerUiKitComponent
import com.midtrans.sdk.uikit.internal.model.CustomerInfo
import com.midtrans.sdk.uikit.internal.presentation.SuccessScreenActivity
import com.midtrans.sdk.uikit.internal.presentation.errorcard.ErrorCard
import com.midtrans.sdk.uikit.internal.util.SnapCreditCardUtil
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import com.midtrans.sdk.uikit.internal.view.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class CreditCardActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: CreditCardViewModel

    private var previousEightDigitNumber = ""

    private val transactionDetails: TransactionDetails? by lazy {
        intent.getParcelableExtra(CreditCardActivity.EXTRA_TRANSACTION_DETAILS) as? TransactionDetails
    }

    private val totalAmount: String by lazy {
        intent.getStringExtra(CreditCardActivity.EXTRA_TOTAL_AMOUNT)
            ?: throw RuntimeException("Total amount must not be empty")
    }

    private val customerDetail: CustomerInfo? by lazy {
        intent.getParcelableExtra(CreditCardActivity.EXTRA_CUSTOMER_DETAIL) as? CustomerInfo
    }

    private val creditCard: CreditCard? by lazy {
        intent.getParcelableExtra(CreditCardActivity.EXTRA_CREDIT_CARD) as? CreditCard
    }

    private val snapToken: String by lazy {
        intent.getStringExtra(CreditCardActivity.EXTRA_SNAP_TOKEN)
            ?: throw RuntimeException("Snaptoken must not be empty")
    }

    private val expiryTime: String? by lazy {
        intent.getStringExtra(EXTRA_EXPIRY_TIME)
    }

    private val withCustomerPhoneEmail: Boolean by lazy {
        merchant?.showCreditCardCustomerInfo ?: false
    }

    private val allowRetry: Boolean by lazy {
        merchant?.allowRetry ?: false
    }

    private val merchant: Merchant? by lazy {
        intent.getParcelableExtra(EXTRA_MERCHANT_DATA) as? Merchant
    }

    companion object {
        private const val EXTRA_SNAP_TOKEN = "card.extra.snap_token"
        private const val EXTRA_TRANSACTION_DETAILS = "card.extra.transaction_details"
        private const val EXTRA_TOTAL_AMOUNT = "card.extra.total_amount"
        private const val EXTRA_CUSTOMER_DETAIL = "card.extra.customer_detail"
        private const val EXTRA_CREDIT_CARD = "card.extra.credit_card"
        private const val EXTRA_EXPIRY_TIME = "card.extra.expiry_time"
        private const val EXTRA_MERCHANT_DATA = "card.extra.merchantdata"

        fun getIntent(
            activityContext: Context,
            snapToken: String,
            totalAmount: String,
            transactionDetails: TransactionDetails?,
            customerInfo: CustomerInfo? = null,
            creditCard: CreditCard?,
            expiryTime: String?,
            withMerchantData: Merchant? = null
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
                putExtra(EXTRA_EXPIRY_TIME, expiryTime)
                withMerchantData?.let { putExtra(EXTRA_MERCHANT_DATA, withMerchantData) }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerUiKitComponent.builder().applicationContext(this.applicationContext).build()
            .inject(this)
        viewModel.setExpiryTime(expiryTime)
        viewModel.setAllowRetry(allowRetry)
        initTransactionResultScreenObserver()
        setContent {
            CreditCardPageStateFull(
                transactionDetails = transactionDetails,
                customerDetail = customerDetail,
                creditCard = creditCard,
                viewModel = viewModel,
                bankCodeIdState = viewModel.bankIconId.observeAsState(null),
                totalAmount = totalAmount,
                remainingTimeState = updateExpiredTime().subscribeAsState(initial = "00:00"),
                withCustomerPhoneEmail = withCustomerPhoneEmail,
                errorTypeState = viewModel.getErrorLiveData().observeAsState(initial = null)
            )
        }
    }

    private fun initTransactionResultScreenObserver() {
        viewModel.getTransactionResponseLiveData().observe(this, Observer {
            if (it.statusCode != UiKitConstants.STATUS_CODE_201 && it.redirectUrl.isNullOrEmpty()) {
                val intent = SuccessScreenActivity.getIntent(
                    activityContext = this@CreditCardActivity,
                    total = totalAmount,
                    orderId = it?.orderId.toString()
                )
                resultLauncher.launch(intent)
            }
        })
        viewModel.getTransactionStatusLiveData().observe(this, Observer {
            var intent: Intent
            when (it.statusCode) {
                UiKitConstants.STATUS_CODE_200 -> {
                    intent = SuccessScreenActivity.getIntent(
                        activityContext = this@CreditCardActivity,
                        total = totalAmount,
                        orderId = it?.orderId.toString()
                    )
                    resultLauncher.launch(intent)
                }
            }
        })
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                setResult(RESULT_OK)
                finish()
            }
        }

    @Composable
    private fun CreditCardPageStateFull(
        transactionDetails: TransactionDetails? = null,
        customerDetail: CustomerInfo? = null,
        withCustomerPhoneEmail: Boolean = false,
        creditCard: CreditCard?,
        totalAmount: String,
        bankCodeIdState: State<Int?>,
        viewModel: CreditCardViewModel?,
        remainingTimeState: State<String>,
        errorTypeState: State<Int?>
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
                principalIconId = null,
                customerEmail = TextFieldValue(),
                customerPhone = TextFieldValue()
            )
        }
        val transactionResponse = viewModel?.getTransactionResponseLiveData()?.observeAsState()
        val bankCodeId by bankCodeIdState
        var isExpanding by remember { mutableStateOf(false) }

        if (transactionResponse?.value?.statusCode == UiKitConstants.STATUS_CODE_201 && !transactionResponse.value?.redirectUrl.isNullOrEmpty()) {
            transactionResponse.value?.redirectUrl?.let {
                SnapThreeDsWebView(
                    url = it,
                    transactionResponse = transactionResponse.value,
                    onPageStarted = {},
                    onPageFinished = {
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
                remainingTimeState = remainingTimeState,
                onExpand = { isExpanding = it },
                onCardNumberValueChange = {

                    state.cardNumber = it
                    var cardNumberWithoutSpace = SnapCreditCardUtil.getCardNumberFromTextField(it)
                    if (cardNumberWithoutSpace.length >= SnapCreditCardUtil.SUPPORTED_MAX_BIN_NUMBER) {
                        var eightDigitNumber = cardNumberWithoutSpace.substring(
                            0,
                            SnapCreditCardUtil.SUPPORTED_MAX_BIN_NUMBER
                        )
                        if (eightDigitNumber != previousEightDigitNumber) {
                            previousEightDigitNumber = eightDigitNumber
                            viewModel?.getBankIconImage(
                                binNumber = eightDigitNumber
                            )
                        }
                    } else {
                        viewModel?.setBankIconToNull()
                        previousEightDigitNumber = cardNumberWithoutSpace
                    }
                },
                onClick = {
                    viewModel?.chargeUsingCreditCard(
                        transactionDetails = transactionDetails,
                        cardNumber = state.cardNumber,
                        cardExpiry = state.expiry,
                        cardCvv = state.cvv,
                        isSavedCard = state.isSavedCardChecked,
                        customerEmail = state.customerEmail.text,
                        customerPhone = state.customerPhone.text,
                        snapToken = snapToken
                    )
                },
                withCustomerPhoneEmail = withCustomerPhoneEmail
            )
        }
        val errorState by errorTypeState
        errorState?.let {
            val clicked = remember {
                mutableStateOf(false)
            }
            ErrorCard(
                type = it,
                getErrorCta(type = it, state = state, clicked = clicked)
            ).apply {
                show()
                if (clicked.value) {
                    clicked.value = false
                    hide()
                    viewModel?.resetError()
                }
            }
        }
    }

    private fun getErrorCta(type: Int, state: NormalCardItemState, clicked: MutableState<Boolean>): () -> Unit{
        return when(type){
            ErrorCard.CARD_ERROR_DECLINED_DISALLOW_RETRY, ErrorCard.SYSTEM_ERROR_DIALOG_DISALLOW_RETRY -> { ->
                setResult(RESULT_OK)
                clicked.value = true
                finish()
            }

            ErrorCard.TID_MID_ERROR_OTHER_PAY_METHOD_AVAILABLE, ErrorCard.TIMEOUT_ERROR_DIALOG_FROM_BANK -> { ->
                setResult(RESULT_CANCELED)
                clicked.value = true
                finish()
            }

            ErrorCard.SYSTEM_ERROR_DIALOG_ALLOW_RETRY -> { ->
                viewModel.chargeUsingCreditCard(
                    transactionDetails = transactionDetails,
                    cardNumber = state.cardNumber,
                    cardExpiry = state.expiry,
                    cardCvv = state.cvv,
                    isSavedCard = state.isSavedCardChecked,
                    customerEmail = state.customerEmail.text,
                    customerPhone = state.customerPhone.text,
                    snapToken = snapToken
                )
                clicked.value = true
            }
            else -> {-> clicked.value = true}
        }
    }

    @Composable
    private fun CreditCardPageStateLess(
        state: NormalCardItemState,
        isExpandingState: Boolean,
        withCustomerPhoneEmail: Boolean = false,
        totalAmount: String,
        orderId: String,
        customerDetail: CustomerInfo? = null,
        creditCard: CreditCard?,
        bankCodeState: Int?,
        remainingTimeState: State<String>,
        onExpand: (Boolean) -> Unit,
        onCardNumberValueChange: (TextFieldValue) -> Unit,
        onClick: () -> Unit
    ) {
        Column(
            modifier = Modifier.background(SnapColors.getARGBColor(SnapColors.BACKGROUND_FILL_PRIMARY)),
        ) {
            val remainingTime by remember { remainingTimeState }
            SnapAppBar(
                title = stringResource(id = R.string.payment_summary_cc_dc),
                iconResId = R.drawable.ic_arrow_left
            ) {
                onBackPressed()
            }
            val scrollState = rememberScrollState()
            var emailAddress by remember { mutableStateOf(TextFieldValue()) }

            SnapOverlayExpandingBox(
                isExpanded = isExpandingState,
                mainContent = {
                    SnapTotal(
                        amount = totalAmount,
                        orderId = orderId,
                        remainingTime = remainingTime,
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
                            .padding(top = 24.dp)
                    ) {
                        var phoneNumber by remember { mutableStateOf(TextFieldValue()) }
                        var phoneNumberFieldFocused by remember { mutableStateOf(false) }
                        var emailAddressFieldFocused by remember { mutableStateOf(false) }
                        if (withCustomerPhoneEmail) {
                            Text(
                                text = stringResource(id = R.string.cc_dc_main_screen_tlp),
                                modifier = Modifier.padding(top = 16.dp, bottom = 4.dp),
                                style = SnapTypography.STYLES.snapTextSmallRegular
                            )
                            SnapTextField(
                                value = phoneNumber,
                                onValueChange = {
                                    phoneNumber = it
                                    state.customerPhone = it
                                },
                                isFocused = phoneNumberFieldFocused,
                                onFocusChange = { phoneNumberFieldFocused = it },
                                modifier = Modifier.fillMaxWidth(1f),
                                hint = stringResource(id = R.string.cc_dc_main_screen_placeholder_phone),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                            Text(
                                text = stringResource(id = R.string.cc_dc_main_screen_email),
                                modifier = Modifier.padding(top = 16.dp, bottom = 4.dp),
                                style = SnapTypography.STYLES.snapTextSmallRegular
                            )
                            SnapTextField(
                                value = emailAddress,
                                onValueChange = {
                                    state.customerEmail = it
                                    emailAddress = it
                                },
                                isFocused = emailAddressFieldFocused,
                                onFocusChange = { emailAddressFieldFocused = it },
                                modifier = Modifier
                                    .fillMaxWidth(1f),
                                hint = stringResource(id = R.string.cc_dc_main_screen_placeholder_email),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                            )

                            if (!emailAddressFieldFocused && emailAddress.text.isNotBlank() && !SnapCreditCardUtil.isValidEmail(emailAddress.text)) {
                                    Text(
                                        text = stringResource(id = R.string.cc_dc_main_screen_email_invalid),
                                        style = SnapTypography.STYLES.snapTextSmallRegular,
                                        color = SnapColors.getARGBColor(SnapColors.SUPPORT_DANGER_DEFAULT)
                                    )
                            }
                            Box(modifier = Modifier.padding(8.dp))
                        }
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
                            onExpiryTextFieldFocusedChange = {
                                state.isExpiryTextFieldFocused = it
                            },
                            onCvvTextFieldFocusedChange = { state.isCvvTextFieldFocused = it },
                            onSavedCardCheckedChange = { state.isSavedCardChecked = it }
                        )


                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(all = 16.dp)
            )

            SnapButton(
                text = stringResource(id = R.string.cc_dc_main_screen_cta),
                style = SnapButton.Style.PRIMARY,
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                enabled = !(state.isCardNumberInvalid ||
                        state.isExpiryInvalid ||
                        state.isCvvInvalid ||
                        state.cardNumber.text.isEmpty() ||
                        state.expiry.text.isEmpty() ||
                        state.cvv.text.isEmpty())
                    .or(!SnapCreditCardUtil.isValidEmail(emailAddress.text).or(emailAddress.text.isBlank())),
                onClick = { onClick() }
            )
        }
    }

    private fun updateExpiredTime(): Observable<String> {
        return Observable
            .interval(1L, TimeUnit.SECONDS)
            .map { viewModel.getExpiredHour() }
            .observeOn(AndroidSchedulers.mainThread())
    }

    @Preview
    @Composable
    private fun forPreview() {
        CreditCardPageStateFull(
            transactionDetails = TransactionDetails(
                orderId = "orderID",
                grossAmount = 5000.0,
                currency = "IDR"
            ),
            customerDetail = CustomerInfo(
                "Ari Bhakti",
                "087788778212",
                listOf("Jl. ABC", "Rumah DEF")
            ),
            creditCard = CreditCard(),
            viewModel = null,
            bankCodeIdState = remember {
                mutableStateOf(null)
            },
            totalAmount = "5000",
            remainingTimeState = remember { mutableStateOf("00:00") },
            withCustomerPhoneEmail = true,
            errorTypeState = remember { mutableStateOf(null) }
        )
    }
}

