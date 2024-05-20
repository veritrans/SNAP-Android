package com.midtrans.sdk.uikit.internal.presentation.ewallet

import android.app.Activity
import android.content.ClipData.Item
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rxjava2.subscribeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.IntentCompat.getParcelableExtra
import coil.compose.AsyncImage
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
import com.midtrans.sdk.uikit.internal.util.UiKitConstants.STATUS_CODE_201
import com.midtrans.sdk.uikit.internal.util.UiKitConstants.STATUS_PENDING
import com.midtrans.sdk.uikit.internal.view.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class WalletActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: WalletViewModel

    private val totalAmount: String by lazy {
        intent.getStringExtra(EXTRA_TOTAL_AMOUNT)
            ?: throw RuntimeException("Total amount must not be empty")
    }

    private val orderId: String by lazy {
        intent.getStringExtra(EXTRA_ORDER_ID)
            ?: throw RuntimeException("Order ID must not be empty")
    }

    private val customerInfo: CustomerInfo? by lazy {
        getParcelableExtra(intent, EXTRA_CUSTOMER_DETAIL, CustomerInfo::class.java)
    }

    private val itemInfo: ItemInfo? by lazy {
        getParcelableExtra(intent, EXTRA_ITEM_INFO, ItemInfo::class.java)
    }

    private val paymentType: String by lazy {
        intent.getStringExtra(EXTRA_PAYMENT_TYPE)
            ?: throw RuntimeException("Payment Type must not be empty")
    }

    private val snapToken: String by lazy {
        intent.getStringExtra(EXTRA_SNAP_TOKEN).orEmpty()
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

    private val deepLinkLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            setResult(result.resultCode, result.data)
            finish()
        }

    private val errorScreenLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            setResult(result.resultCode, result?.data)
            finish()
            isFirstInit = false
        }

    private var deepLinkUrl: String? = null
    private var isFirstInit = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UiKitApi.getDefaultInstance().daggerComponent.inject(this)
        viewModel.trackPageViewed(paymentType, currentStepNumber)
        viewModel.setDefaultExpiryTime(expiryTime)
        var isTablet = isTabletDevice()

        transactionResult?.let { result ->
            isTablet = when (result.chargeType) {
                PaymentType.QRIS -> {
                    true
                }
                PaymentType.GOPAY, PaymentType.SHOPEEPAY -> {
                    false
                }
                else -> {
                    isTabletDevice()
                }
            }
            viewModel.getUsedToken(result)
            if (result.statusCode == STATUS_CODE_201) {
                if (!isTablet) {
                    openDeepLink(result.deeplinkUrl)
                }
            }
        } ?: run {
            chargeQrPayment()
            observeLiveData(isTablet)
        }

        if (DateTimeUtil.getExpiredSeconds(viewModel.getExpiredHour()) <= 0L && isFirstInit) {
            launchExpiredErrorScreen()
        } else {
            setContent {
                Content(
                    totalAmount = totalAmount,
                    orderId = orderId,
                    customerInfo = customerInfo,
                    isChargeError = viewModel.isQrChargeErrorLiveData.observeAsState(initial = false),
                    itemInfo = itemInfo,
                    remainingTimeState = updateExpiredTime().subscribeAsState(initial = "00:00"),
                    qrCodeUrl = viewModel.qrCodeUrlLiveData.observeAsState(initial = ""),
                    paymentType = paymentType,
                    isTablet = isTablet
                )
            }
        }
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
                    activityContext = this@WalletActivity,
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

    private fun chargeQrPayment() {
        viewModel.chargeQrPayment(
            snapToken = snapToken,
            paymentType = paymentType
        )
    }

    private fun observeLiveData(isTablet: Boolean) {
        if (!isTablet) {
            observeDeepLinkUrl()
        }
        observeChargeResult()
    }

    private fun observeDeepLinkUrl() {
        viewModel.deepLinkUrlLiveData.observe(this) { url ->
            deepLinkUrl = url
        }
    }

    private fun openDeepLink(url: String?) {
        url?.let {
            viewModel.trackOpenDeeplink(paymentType)
            val intent = DeepLinkActivity.getIntent(
                activityContext = this,
                paymentType = paymentType,
                url = it,
                snapToken = snapToken,
                stepNumber = currentStepNumber + 1,
                amount = totalAmount,
                orderId = orderId
            )
            deepLinkLauncher.launch(intent)
        }
    }

    private fun observeChargeResult() {
        viewModel.chargeResultLiveData.observe(this) {
            setResult(it)
        }
    }

    private fun setResult(data: TransactionResult) {
        val resultIntent = Intent().putExtra(UiKitConstants.KEY_TRANSACTION_RESULT, data)
        setResult(Activity.RESULT_OK, resultIntent)
    }

    private fun updateExpiredTime(): Observable<String> {
        return Observable
            .interval(1L, TimeUnit.SECONDS)
            .map { viewModel.getExpiredHour() }
            .observeOn(AndroidSchedulers.mainThread())
    }

    @Composable
    private fun Content(
        totalAmount: String,
        orderId: String,
        qrCodeUrl: State<String>,
        paymentType: String,
        isChargeError: State<Boolean>,
        customerInfo: CustomerInfo?,
        itemInfo: ItemInfo?,
        remainingTimeState: State<String>,
        isTablet: Boolean = false
    ) {
        val remainingTime by remember { remainingTimeState }
        var expanding by remember {
            mutableStateOf(false)
        }
        var error by remember { mutableStateOf(false) }
        var loading by remember { mutableStateOf(false) }

        if (DateTimeUtil.getExpiredSeconds(remainingTime) <= 0L && isFirstInit) {
            if (viewModel.isExpired.value == true) {
                launchExpiredErrorScreen()
            } else {
                val data = Intent()
                data.putExtra(
                    UiKitConstants.KEY_TRANSACTION_RESULT,
                    TransactionResult(
                        status = STATUS_PENDING,
                        transactionId = viewModel.chargeResultLiveData.value?.transactionId ?: STATUS_PENDING,
                        paymentType = paymentType
                    )
                )
                setResult(RESULT_OK, data)
                finish()
            }
        }

        Column(
            modifier = Modifier
                .fillMaxHeight(1f)
                .background(color = SnapColors.getARGBColor(SnapColors.backgroundFillPrimary))
        ) {
            title[paymentType]?.let {
                SnapAppBar(title = stringResource(id = it), iconResId = R.drawable.ic_cross) {
                    onBackPressed()
                }
            }

            SnapOverlayExpandingBox(
                modifier = Modifier
                    .padding(16.dp)
                    .padding(bottom = 5.dp)
                    .weight(1f),
                isExpanded = expanding,
                mainContent = {
                    SnapTotal(
                        amount = totalAmount,
                        orderId = orderId,
                        canExpand = customerInfo != null || itemInfo != null,
                        remainingTime = remainingTime
                    ) {
                        expanding = it
                    }
                },
                expandingContent = {
                    viewModel.trackOrderDetailsViewed(paymentType)
                    SnapPaymentOrderDetails(
                        customerInfo = customerInfo,
                        itemInfo = itemInfo
                    )
                }
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 28.dp)
                        .fillMaxWidth(1f)
                        .verticalScroll(
                            state = rememberScrollState()
                        ),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .height(if (isTablet || isChargeError.value) 300.dp else 1.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (error || isChargeError.value) {
                            Column(
                                modifier = Modifier.fillMaxWidth(1f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                SnapButton(
                                    style = SnapButton.Style.TERTIARY,
                                    text = stringResource(id = R.string.qr_reload),
                                    onClick = {
                                        viewModel.trackSnapButtonClicked(
                                            ctaName = getStringResourceInEnglish(R.string.qr_reload),
                                            paymentType = paymentType
                                        )
                                        viewModel.trackReloadClicked(paymentType = paymentType)
                                        error = false
                                        if (isChargeError.value) {
                                            chargeQrPayment()
                                        }
                                    }
                                )
                                Text(
                                    text = stringResource(id = R.string.qr_failed_load),
                                    style = SnapTypography.STYLES.snapTextSmallRegular,
                                    color = SnapColors.getARGBColor(SnapColors.textSecondary)
                                )
                            }

                        } else {
                            if (qrCodeUrl.value.isNotBlank()) {
                                AsyncImage(
                                    model = qrCodeUrl.value, contentDescription = null,
                                    modifier = Modifier
                                        .width(if (isTablet) 300.dp else 1.dp)
                                        .height(if (isTablet) 300.dp else 1.dp),
                                    onError = {
                                        error = true
                                        loading = false
                                    },
                                    onSuccess = {
                                        error = false
                                        loading = false
                                    },
                                    onLoading = {
                                        error = false
                                        loading = true
                                    }
                                )
                            }
                        }
                        if (loading) {
                            GifImage(
                                gifResId = R.drawable.gif_loading_ios,
                                modifier = Modifier
                                    .width(50.dp)
                                    .height(50.dp)
                            )
                        }
                    }

                    var isExpanded by remember { mutableStateOf(false) }
                    SnapInstructionButton(
                        modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                        isExpanded = isExpanded,
                        iconResId = R.drawable.ic_help,
                        title = stringResource(id = R.string.payment_instruction_how_to_pay_title),
                        onExpandClick = {
                            viewModel.trackHowToPayClicked(paymentType)
                            isExpanded = !isExpanded
                        },
                        expandingContent = {
                            AnimatedVisibility(visible = isExpanded) {
                                val instruction =
                                    if (isTablet) paymentInstructionQr else paymentInstructionDeepLink
                                instruction[paymentType]?.let {
                                    SnapNumberedList(list = stringArrayResource(id = it).toList())
                                }
                            }
                        }
                    )
                }
            }

            val ctaId =
                if (isTablet) R.string.i_have_already_paid else R.string.redirection_instruction_gopay_cta
            SnapButton(
                text = stringResource(ctaId),
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(16.dp),
                enabled = !error && !loading,
                style = if (!error && !loading) SnapButton.Style.PRIMARY else SnapButton.Style.PRIMARY
            ) {
                viewModel.trackSnapButtonClicked(
                    ctaName = getStringResourceInEnglish(ctaId),
                    paymentType = paymentType
                )
                if (!isTablet) {
                    openDeepLink(deepLinkUrl)
                } else {
                    onBackPressed()
                }
            }
        }
    }

    @Composable
    @Preview
    fun ForPreview() {
        Content(
            totalAmount = "Rp.199.000",
            orderId = "#1231231233121",
            customerInfo = CustomerInfo(
                name = "ari bakti",
                phone = "4123123123123",
                addressLines = listOf("jalan", "jalan", "jalan")
            ),
            isChargeError = remember { mutableStateOf(false) },
            itemInfo = null,
            paymentType = PaymentType.GOPAY,
            remainingTimeState = remember { mutableStateOf("00:00") },
            qrCodeUrl = remember { mutableStateOf("http://kkkk") },
            isTablet = true
        )

    }

    private val paymentInstructionQr by lazy {
        mapOf(
            Pair(PaymentType.GOPAY_QRIS, R.array.scan_qr_instruction_gopay),
            Pair(PaymentType.SHOPEEPAY_QRIS, R.array.scan_qr_instruction_shopeepay_message)
        )
    }

    private val paymentInstructionDeepLink by lazy {
        mapOf(
            Pair(PaymentType.GOPAY, R.array.redirection_instruction_gopay_message),
            Pair(PaymentType.SHOPEEPAY, R.array.redirection_instruction_shopeepay_message)
        )
    }

    private val title by lazy {
        mapOf(
            Pair(PaymentType.GOPAY, R.string.payment_title_gopay),
            Pair(PaymentType.GOPAY_QRIS, R.string.payment_title_gopay),
            Pair(PaymentType.SHOPEEPAY, R.string.payment_title_shopeepay),
            Pair(PaymentType.SHOPEEPAY_QRIS, R.string.payment_title_shopeepay)
        )
    }

    companion object {
        private const val EXTRA_TOTAL_AMOUNT = "wallet.extra.total_amount"
        private const val EXTRA_ORDER_ID = "wallet.extra.order_id"
        private const val EXTRA_CUSTOMER_DETAIL = "wallet.extra.customer_detail"
        private const val EXTRA_ITEM_INFO = "wallet.extra.item_info"
        private const val EXTRA_PAYMENT_TYPE = "wallet.extra.payment_type"
        private const val EXTRA_SNAP_TOKEN = "wallet.extra.snap_token"
        private const val EXTRA_STEP_NUMBER = "wallet.extra.step_number"
        private const val EXTRA_TRANSACTION_RESULT = "wallet.extra.transaction_result"
        private const val EXTRA_EXPIRY_TIME = "wallet.extra.expiry_time"

        fun getIntent(
            activityContext: Context,
            snapToken: String,
            paymentType: String,
            totalAmount: String,
            orderId: String,
            customerInfo: CustomerInfo? = null,
            itemInfo: ItemInfo? = null,
            stepNumber: Int,
            result: TransactionResponse?,
            expiryTime: String?
        ): Intent {
            return Intent(activityContext, WalletActivity::class.java).apply {
                putExtra(EXTRA_TOTAL_AMOUNT, totalAmount)
                putExtra(EXTRA_ORDER_ID, orderId)
                putExtra(EXTRA_SNAP_TOKEN, snapToken)
                putExtra(EXTRA_CUSTOMER_DETAIL, customerInfo)
                putExtra(EXTRA_ITEM_INFO, itemInfo)
                putExtra(EXTRA_PAYMENT_TYPE, paymentType)
                putExtra(EXTRA_STEP_NUMBER, stepNumber)
                putExtra(EXTRA_TRANSACTION_RESULT, result)
                putExtra(EXTRA_EXPIRY_TIME, expiryTime)
            }
        }
    }
}