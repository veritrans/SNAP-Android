package com.midtrans.sdk.uikit.internal.presentation.ewallet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
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
import com.midtrans.sdk.uikit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.di.DaggerUiKitComponent
import com.midtrans.sdk.uikit.internal.model.CustomerInfo
import com.midtrans.sdk.uikit.internal.view.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class WalletActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: WalletViewModel
    var deepLinkUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO: create Dagger component holder for global access
        DaggerUiKitComponent.builder().applicationContext(this.applicationContext).build()
            .inject(this)
        setContent {
            Content(
                totalAmount = totalAmount,
                orderId = orderId,
                customerInfo = customerInfo,
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
        if (!isTabletDevice()) {
            observerDeepLinkUrl()
        }
        setResult(RESULT_OK)
    }

    private fun observerDeepLinkUrl() {
        viewModel.deepLinkUrlLiveData.observe(this) { url ->
            deepLinkUrl = url
        }
    }

    private fun openDeepLink() {
        deepLinkUrl?.let {
            val intent = DeepLinkActivity.getIntent(this, paymentType, it)
            startActivity(intent)
        }
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
                .background(color = SnapColors.getARGBColor(SnapColors.BACKGROUND_FILL_PRIMARY))
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
                        canExpand = customerInfo != null,
                        remainingTime = remainingTime
                    ) {
                        expanding = it
                    }
                },
                expandingContent = {
                    customerInfo?.run {
                        SnapCustomerDetail(
                            name = name,
                            phone = phone,
                            addressLines = addressLines
                        )
                    }
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
                                        error = false
                                    }
                                )
                                Text(
                                    text = stringResource(id = R.string.qr_failed_load),
                                    style = SnapTypography.STYLES.snapTextSmallRegular,
                                    color = SnapColors.getARGBColor(SnapColors.TEXT_SECONDARY)
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
                            AnimatedIcon(resId = R.drawable.ic_midtrans_animated).start()
                        }
                    }

                    var isExpanded by remember { mutableStateOf(false) }
                    SnapInstructionButton(
                        modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                        isExpanded = isExpanded,
                        iconResId = R.drawable.ic_help,
                        title = stringResource(id = R.string.kredivo_how_to_pay_title),
                        onExpandClick = { isExpanded = !isExpanded },
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

            SnapButton(
                text = stringResource(id = if (isTablet) R.string.i_have_already_paid else R.string.redirection_instruction_gopay_cta),
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(16.dp),
                enabled = !error && !loading,
                style = if (!error && !loading) SnapButton.Style.PRIMARY else SnapButton.Style.PRIMARY
            ) {
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

    private val paymentType: String by lazy {
        intent.getStringExtra(EXTRA_PAYMENTTYPE)
            ?: throw RuntimeException("Payment Type must not be empty")
    }

    private val snapToken: String by lazy {
        intent.getStringExtra(EXTRA_SNAPTOKEN).orEmpty()
    }

    private val paymentInstructionQr by lazy {
        mapOf(
            Pair(PaymentType.GOPAY, R.array.scan_qr_instruction_gopay),
            Pair(PaymentType.SHOPEEPAY, R.array.scan_qr_instruction_shopeepay_message)
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
            Pair(PaymentType.SHOPEEPAY, R.string.payment_title_shopeepay),
        )
    }

    companion object {
        private const val EXTRA_TOTAL_AMOUNT = "wallet.extra.total_amount"
        private const val EXTRA_ORDER_ID = "wallet.extra.order_id"
        private const val EXTRA_CUSTOMER_DETAIL = "wallet.extra.customer_detail"
        private const val EXTRA_PAYMENTTYPE = "wallet.extra.paymenttype"
        private const val EXTRA_SNAPTOKEN = "wallet.extra.snaptoken"

        fun getIntent(
            activityContext: Context,
            snapToken: String,
            paymentType: String,
            totalAmount: String,
            orderId: String,
            customerInfo: CustomerInfo? = null

        ): Intent {
            return Intent(activityContext, WalletActivity::class.java).apply {
                putExtra(EXTRA_TOTAL_AMOUNT, totalAmount)
                putExtra(EXTRA_ORDER_ID, orderId)
                putExtra(EXTRA_SNAPTOKEN, snapToken)
                putExtra(
                    EXTRA_CUSTOMER_DETAIL,
                    customerInfo
                )
                putExtra(EXTRA_PAYMENTTYPE, paymentType)
            }
        }
    }
}