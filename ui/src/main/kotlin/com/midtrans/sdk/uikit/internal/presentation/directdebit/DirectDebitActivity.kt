package com.midtrans.sdk.uikit.internal.presentation.directdebit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.IntentCompat.getParcelableExtra
import androidx.lifecycle.ViewModelProvider
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.api.model.TransactionResult
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.internal.model.CustomerInfo
import com.midtrans.sdk.uikit.internal.model.ItemInfo
import com.midtrans.sdk.uikit.internal.presentation.statusscreen.ErrorScreenActivity
import com.midtrans.sdk.uikit.internal.util.DateTimeUtil
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import com.midtrans.sdk.uikit.internal.view.*
import com.midtrans.sdk.uikit.internal.view.SnapColors.supportDangerDefault
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DirectDebitActivity : BaseActivity() {

    @Inject
    internal lateinit var vmFactory: ViewModelProvider.Factory

    private val snapToken: String by lazy {
        intent.getStringExtra(EXTRA_SNAP_TOKEN)
            ?: throw RuntimeException("Snap token must not be empty")
    }

    private val paymentType: String by lazy {
        intent.getStringExtra(EXTRA_PAYMENT_TYPE)
            ?: throw RuntimeException("Payment type must not be empty")
    }

    private val amount: String by lazy {
        intent.getStringExtra(EXTRA_AMOUNT)
            ?: throw RuntimeException("Total amount must not be empty")
    }

    private val orderId: String by lazy {
        intent.getStringExtra(EXTRA_ORDER_ID)
            ?: throw RuntimeException("Order ID must not be empty")
    }

    private val customerInfo: CustomerInfo? by lazy {
        getParcelableExtra(intent, EXTRA_CUSTOMER_INFO, CustomerInfo::class.java)
    }

    private val itemInfo: ItemInfo? by lazy {
        getParcelableExtra(intent, EXTRA_ITEM_INFO, ItemInfo::class.java)
    }

    private val currentStepNumber: Int by lazy {
        intent.getIntExtra(EXTRA_STEP_NUMBER, 0)
    }

    private val transactionResult: TransactionResponse? by lazy {
        getParcelableExtra(intent, EXTRA_TRANSACTION_RESULT, TransactionResponse::class.java)
    }

    private val expiryTime: String? by lazy {
        intent.getStringExtra(EXTRA_EXPIRY_TIME)
    }

    private val viewModel: DirectDebitViewModel by lazy {
        ViewModelProvider(this, vmFactory)[DirectDebitViewModel::class.java]
    }

    private var isFirstInit = true

    private val errorScreenLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            setResult(result.resultCode, result?.data)
            finish()
            isFirstInit = false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        UiKitApi.getDefaultInstance().daggerComponent.inject(this)
        viewModel.trackPageViewed(paymentType, currentStepNumber)
        viewModel.setExpiryTime(expiryTime)

        if (DateTimeUtil.getExpiredSeconds(viewModel.getExpiredHour()) <= 0L && isFirstInit) {
            launchExpiredErrorScreen()
        } else {
            setContent {
                DirectDebitContent(
                    paymentType = paymentType,
                    amount = amount,
                    orderId = orderId,
                    customerInfo = customerInfo,
                    itemInfo = itemInfo,
                    response = viewModel.getTransactionResponse().observeAsState().value,
                    remainingTimeState = updateExpiredTime().subscribeAsState(initial = "00:00")
                )
            }
        }
    }

    private fun setResult(data: TransactionResult) {
        val resultIntent = Intent().putExtra(UiKitConstants.KEY_TRANSACTION_RESULT, data)
        setResult(Activity.RESULT_OK, resultIntent)
    }

    private fun launchExpiredErrorScreen() {
        val expiredTransactionResult = TransactionResult(
            status = UiKitConstants.STATUS_FAILED,
            paymentType = paymentType,
            message = resources.getString(R.string.expired_desc)
        )
        if (isShowPaymentStatusPage()) {
            errorScreenLauncher.launch(
                ErrorScreenActivity.getIntent(
                    activityContext = this@DirectDebitActivity,
                    title = resources.getString(R.string.expired_title),
                    content = resources.getString(R.string.expired_desc),
                    transactionResult = expiredTransactionResult
                )
            )
        } else {
            setResult(expiredTransactionResult)
            finish()
        }
    }

    @Composable
    @Preview(showBackground = true)
    fun PreviewOnly() {
        DirectDebitContent(
            paymentType = PaymentType.KLIK_BCA,
            amount = "Rp.123999",
            orderId = "order-id",
            customerInfo = CustomerInfo(
                name = "John Joe",
                phone = "081234567890",
                addressLines = listOf("address one", "address two")
            ),
            itemInfo = null,
            response = TransactionResponse(
                transactionStatus = "pending"
            ),
            remainingTimeState = remember { mutableStateOf("00:00") }
        )
    }

    @Composable
    private fun DirectDebitContent(
        paymentType: String,
        amount: String,
        orderId: String,
        customerInfo: CustomerInfo?,
        itemInfo: ItemInfo?,
        response: TransactionResponse?,
        remainingTimeState: State<String>
    ) {
        var isCustomerDetailExpanded by remember { mutableStateOf(false) }
        var isInstructionExpanded by remember { mutableStateOf(false) }
        val title = stringResource(getTitleId(paymentType = paymentType))
        var userId by remember { mutableStateOf("") }
        val url = response?.redirectUrl.orEmpty()
        val remainingTime by remember { remainingTimeState }
        val transactionId = viewModel.transactionId.observeAsState()

        transactionResult?.let { result ->
            result.redirectUrl?.let { url ->
                if (result.chargeType == PaymentType.KLIK_BCA) {
                    viewModel.checkStatus(snapToken, PaymentType.KLIK_BCA)
                    transactionId.value?.let {
                        openWebLink(url, result.transactionStatus, it)
                    }
                }
            }
        }

        if (DateTimeUtil.getExpiredSeconds(remainingTime) <= 0L && isFirstInit) {
            launchExpiredErrorScreen()
        }

        if (url.isEmpty()) {
            Column(
                modifier = Modifier
                    .background(SnapColors.getARGBColor(SnapColors.overlayWhite))
                    .fillMaxHeight(1f)
            ) {
                SnapAppBar(
                    title = title,
                    iconResId = R.drawable.ic_arrow_left
                ) {
                    onBackPressed()
                }
                SnapOverlayExpandingBox(
                    modifier = Modifier
                        .weight(1f)
                        .padding(all = 16.dp),
                    isExpanded = isCustomerDetailExpanded,
                    mainContent = {
                        SnapTotal(
                            amount = amount,
                            orderId = orderId,
                            canExpand = customerInfo != null || itemInfo != null,
                            remainingTime = remainingTime
                        ) {
                            isCustomerDetailExpanded = it
                        }
                    },
                    expandingContent = {
                        viewModel.trackOrderDetailsViewed(paymentType)
                        SnapPaymentOrderDetails(
                            customerInfo = customerInfo,
                            itemInfo = itemInfo
                        )
                    },
                    followingContent = {
                        Column(
                            modifier = Modifier
                                .verticalScroll(state = rememberScrollState())
                                .padding(top = 16.dp)
                                .fillMaxWidth()

                        ) {
                            SnapText(stringResource(getInstructionId(paymentType = paymentType)))
                            KlikBcaUserIdTextField(paymentType = paymentType) { userId = it }
                            SnapInstructionButton(
                                modifier = Modifier.padding(top = 28.dp),
                                isExpanded = isInstructionExpanded,
                                iconResId = R.drawable.ic_help,
                                title = stringResource(R.string.payment_instruction_how_to_pay_title),
                                onExpandClick = {
                                    viewModel.trackHowToPayClicked(paymentType)
                                    isInstructionExpanded = !isInstructionExpanded
                                },
                                expandingContent = {
                                    Column {
                                        AnimatedVisibility(visible = isInstructionExpanded) {
                                            SnapNumberedList(
                                                list = stringArrayResource(getHowToPayId(paymentType = paymentType)).toList()
                                            )
                                        }
                                    }
                                }
                            )
                        }
                    }
                )

                SnapButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
                    enabled = enableButton(paymentType, userId),
                    text = stringResource(getCta(paymentType)),
                    style = SnapButton.Style.PRIMARY
                ) {
                    viewModel.trackSnapButtonClicked(
                        ctaName = getStringResourceInEnglish(getCta(paymentType)),
                        paymentType = paymentType
                    )
                    viewModel.payDirectDebit(
                        snapToken = snapToken,
                        paymentType = paymentType,
                        userId = userId
                    )
                }
            }
        } else {
            val status = response?.transactionStatus
            val transactionId = response?.transactionId
            isFirstInit = false
            viewModel.trackOpenRedirectionUrl(paymentType)
            if (paymentType == PaymentType.KLIK_BCA) {
                openWebLink(
                    url = url,
                    status = status,
                    transactionId = transactionId
                )
            } else {
                SnapWebView(
                    title = title,
                    paymentType = paymentType,
                    url = url,
                    onPageStarted = {
                        finishDirectDebitPayment(
                            status = status,
                            transactionId = transactionId
                        )
                    },
                    onPageFinished = { }
                )
            }
        }
    }

    private fun finishDirectDebitPayment(
        status: String?,
        transactionId: String?
    ) {
        if (status != null && transactionId != null) {
            val data = Intent()
            data.putExtra(
                UiKitConstants.KEY_TRANSACTION_RESULT,
                TransactionResult(
                    status = status,
                    transactionId = transactionId,
                    paymentType = paymentType
                )
            )
            setResult(RESULT_OK, data)
            finish()
        }
    }

    private fun openWebLink(
        url: String,
        status: String?,
        transactionId: String?
    ) {
        intent = Intent(Intent.ACTION_VIEW)
        intent.data = (Uri.parse(url))
        startActivity(intent)
        finishDirectDebitPayment(
            status = status,
            transactionId = transactionId
        )
    }

    private fun enableButton(paymentType: String, userId: String): Boolean {
        return when (paymentType) {
            PaymentType.KLIK_BCA -> userId.isNotEmpty()
            else -> true
        }
    }

    @Composable
    private fun KlikBcaUserIdTextField(
        paymentType: String,
        onUserIdChanged: (String) -> Unit
    ) {
        if (paymentType == PaymentType.KLIK_BCA) {
            var userId by remember { mutableStateOf(TextFieldValue()) }
            var isError by remember { mutableStateOf(false) }
            var isFocused by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(top = 12.dp)
                    .background(color = SnapColors.getARGBColor(SnapColors.supportNeutralFill))
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(top = 8.dp, bottom = 12.dp, start = 16.dp, end = 16.dp)
                ) {
                    SnapText(
                        text = stringResource(id = R.string.klik_bca_id_field_label),
                        style = SnapText.Style.SMALL
                    )
                    SnapTextField(
                        modifier = Modifier.fillMaxWidth(1f),
                        value = userId,
                        hint = stringResource(id = R.string.klik_bca_placeholder),
                        onValueChange = {
                            userId = it
                            onUserIdChanged(userId.text)
                            isError = userId.text.isEmpty()
                            isFocused = !isError
                        },
                        isError = isError,
                        isFocused = isFocused,
                        onFocusChange = { isFocused = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )
                    if (isError) {
                        Text(
                            text = stringResource(id = R.string.klik_bca_validation_error),
                            style = SnapTypography.STYLES.snapTextSmallRegular,
                            color = SnapColors.getARGBColor(supportDangerDefault)
                        )
                        viewModel.trackSnapNotice(
                            paymentType = paymentType,
                            statusText = getStringResourceInEnglish(R.string.klik_bca_validation_error)
                        )
                    }
                }
            }
        }
    }

    private fun getTitleId(paymentType: String): Int {
        return when (paymentType) {
            PaymentType.KLIK_BCA -> R.string.klik_bca_title
            PaymentType.BCA_KLIKPAY -> R.string.bca_klik_pay_title
            PaymentType.CIMB_CLICKS -> R.string.octo_click_title
            PaymentType.DANAMON_ONLINE -> R.string.danamon_title
            PaymentType.BRI_EPAY -> R.string.brimo_title
            else -> 0
        }
    }

    private fun getInstructionId(paymentType: String): Int {
        return when (paymentType) {
            PaymentType.KLIK_BCA -> R.string.klik_bca_instruction
            PaymentType.BCA_KLIKPAY -> R.string.bca_klik_pay_instruction
            PaymentType.CIMB_CLICKS -> R.string.octo_click_instruction
            PaymentType.DANAMON_ONLINE -> R.string.danamon_instruction
            PaymentType.BRI_EPAY -> R.string.brimo_instruction
            else -> 0
        }
    }

    private fun getHowToPayId(paymentType: String): Int {
        return when (paymentType) {
            PaymentType.KLIK_BCA -> R.array.klik_bca_how_to_pay
            PaymentType.BCA_KLIKPAY -> R.array.bca_klik_pay_how_to_pay
            PaymentType.CIMB_CLICKS -> R.array.octo_click_how_to_pay
            PaymentType.DANAMON_ONLINE -> R.array.danamon_how_to_pay
            PaymentType.BRI_EPAY -> R.array.brimo_how_to_pay
            else -> 0
        }
    }

    private fun getCta(paymentType: String): Int {
        return when (paymentType) {
            PaymentType.KLIK_BCA -> R.string.klik_bca_cta
            PaymentType.BCA_KLIKPAY -> R.string.bca_klik_pay_cta
            PaymentType.CIMB_CLICKS -> R.string.octo_click_cta
            PaymentType.DANAMON_ONLINE -> R.string.danamon_cta
            PaymentType.BRI_EPAY -> R.string.brimo_cta
            else -> 0
        }
    }

    private fun updateExpiredTime(): Observable<String> {
        return Observable
            .interval(1L, TimeUnit.SECONDS)
            .map { viewModel.getExpiredHour() }
            .observeOn(AndroidSchedulers.mainThread())
    }

    companion object {
        private const val EXTRA_SNAP_TOKEN = "directDebit.extra.snap_token"
        private const val EXTRA_PAYMENT_TYPE = "directDebit.extra.payment_type"
        private const val EXTRA_AMOUNT = "directDebit.extra.amount"
        private const val EXTRA_ORDER_ID = "directDebit.extra.order_id"
        private const val EXTRA_CUSTOMER_INFO = "directDebit.extra.customer_info"
        private const val EXTRA_ITEM_INFO = "directDebit.extra.item_info"
        private const val EXTRA_STEP_NUMBER = "directDebit.extra.step_number"
        private const val EXTRA_TRANSACTION_RESULT = "directDebit.extra.transaction_result"
        private const val EXTRA_EXPIRY_TIME = "directDebit.extra.expiry_time"

        fun getIntent(
            activityContext: Context,
            snapToken: String,
            @PaymentType.Def paymentType: String,
            amount: String,
            orderId: String,
            customerInfo: CustomerInfo?,
            itemInfo: ItemInfo?,
            stepNumber: Int,
            result: TransactionResponse?,
            expiryTime: String?
        ): Intent {
            return Intent(activityContext, DirectDebitActivity::class.java).apply {
                putExtra(EXTRA_SNAP_TOKEN, snapToken)
                putExtra(EXTRA_PAYMENT_TYPE, paymentType)
                putExtra(EXTRA_AMOUNT, amount)
                putExtra(EXTRA_ORDER_ID, orderId)
                putExtra(EXTRA_CUSTOMER_INFO, customerInfo)
                putExtra(EXTRA_ITEM_INFO, itemInfo)
                putExtra(EXTRA_STEP_NUMBER, stepNumber)
                putExtra(EXTRA_TRANSACTION_RESULT, result)
                putExtra(EXTRA_EXPIRY_TIME, expiryTime)
            }
        }
    }
}
