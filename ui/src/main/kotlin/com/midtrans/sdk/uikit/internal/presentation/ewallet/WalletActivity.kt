package com.midtrans.sdk.uikit.internal.presentation.ewallet

import android.app.Activity
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
import coil.compose.AsyncImage
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.model.TransactionResult
import com.midtrans.sdk.uikit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.model.CustomerInfo
import com.midtrans.sdk.uikit.internal.model.ItemInfo
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import com.midtrans.sdk.uikit.internal.view.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class WalletActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: WalletViewModel
    var deepLinkUrl: String? = null

    private val deepLinkLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            setResult(result.resultCode, result.data)
            finish()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UiKitApi.getDefaultInstance().daggerComponent.inject(this)
        setContent {
            Content(
                totalAmount = totalAmount,
                orderId = orderId,
                customerInfo = customerInfo,
                itemInfo = itemInfo,
                remainingTimeState = updateExpiredTime().subscribeAsState(initial = "00:00"),
                qrCodeUrl = viewModel.qrCodeUrlLiveData.observeAsState(initial = ""),
                paymentType = paymentType,
                isTablet = isTabletDevice()
            )
        }
        viewModel.chargeQrPayment(
            snapToken = snapToken,
            paymentType = paymentType
        )
        observeLiveData()
    }

    private fun observeLiveData() {
        if (!isTabletDevice()) {
            observeDeepLinkUrl()
        }
        observeChargeResult()
    }

    private fun observeDeepLinkUrl() {
        viewModel.deepLinkUrlLiveData.observe(this) { url ->
            deepLinkUrl = url
        }
    }

    private fun openDeepLink() {
        deepLinkUrl?.let {
            viewModel.trackOpenDeeplink(paymentType)
            val intent = DeepLinkActivity.getIntent(
                activityContext = this,
                paymentType = paymentType,
                url = it,
                snapToken = snapToken
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
                            .height(if (isTablet) 300.dp else 1.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (error) {
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
                                        error = false
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
                        title = stringResource(id = R.string.kredivo_how_to_pay_title),
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

            val ctaId = if (isTablet) R.string.i_have_already_paid else R.string.redirection_instruction_gopay_cta
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
                    openDeepLink()
                }else{
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
            itemInfo = null,
            paymentType = PaymentType.GOPAY,
            remainingTimeState = remember { mutableStateOf("00:00") },
            qrCodeUrl = remember { mutableStateOf("http://kkkk") },
            isTablet = true
        )

    }


    private val totalAmount: String by lazy {
        intent.getStringExtra(EXTRA_TOTAL_AMOUNT)
            ?: throw RuntimeException("Total amount must not be empty")
    }

    private val orderId: String by lazy {
        intent.getStringExtra(EXTRA_ORDER_ID)
            ?: throw RuntimeException("Order ID must not be empty")
    }

    private val customerInfo: CustomerInfo? by lazy {
        intent.getParcelableExtra(EXTRA_CUSTOMER_DETAIL) as? CustomerInfo
    }

    private val itemInfo: ItemInfo? by lazy {
        intent.getParcelableExtra(EXTRA_ITEM_INFO) as? ItemInfo
    }

    private val paymentType: String by lazy {
        intent.getStringExtra(EXTRA_PAYMENT_TYPE)
            ?: throw RuntimeException("Payment Type must not be empty")
    }

    private val snapToken: String by lazy {
        intent.getStringExtra(EXTRA_SNAP_TOKEN).orEmpty()
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

        fun getIntent(
            activityContext: Context,
            snapToken: String,
            paymentType: String,
            totalAmount: String,
            orderId: String,
            customerInfo: CustomerInfo? = null,
            itemInfo: ItemInfo? = null
        ): Intent {
            return Intent(activityContext, WalletActivity::class.java).apply {
                putExtra(EXTRA_TOTAL_AMOUNT, totalAmount)
                putExtra(EXTRA_ORDER_ID, orderId)
                putExtra(EXTRA_SNAP_TOKEN, snapToken)
                putExtra(EXTRA_CUSTOMER_DETAIL, customerInfo)
                putExtra(EXTRA_ITEM_INFO, itemInfo)
                putExtra(EXTRA_PAYMENT_TYPE, paymentType)
            }
        }
    }
}