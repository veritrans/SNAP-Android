package com.midtrans.sdk.uikit.internal.presentation.conveniencestore

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.internal.di.DaggerUiKitComponent
import com.midtrans.sdk.uikit.internal.model.CustomerInfo
import com.midtrans.sdk.uikit.internal.view.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class ConvenienceStoreActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ConvenienceStoreViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ConvenienceStoreViewModel::class.java)
    }

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
                barCode = viewModel.barCodeBitmapLiveData.observeAsState(initial = null),
                paymentType = paymentType,
                paymentCodeState = viewModel.paymentCodeLiveData.observeAsState(initial = null),
                pdfUrl = viewModel.pdfUrlLiveData.observeAsState(initial = null),
                errorState = viewModel.errorLiveData.observeAsState(initial = null),
                viewModel = viewModel,
                clipboardManager = LocalClipboardManager.current
            )
        }
        charge()
        setResult(RESULT_OK)
    }

    private fun charge(){
        viewModel.chargeConvenienceStorePayment(
            snapToken = snapToken,
            paymentType = paymentType
        )
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
        barCode: State<Bitmap?>,
        paymentType: String,
        customerInfo: CustomerInfo?,
        paymentCodeState:State<String?>,
        remainingTimeState: State<String>,
        pdfUrl: State<String?>,
        errorState: State<Int?>,
        clipboardManager: ClipboardManager? = null,
        viewModel: ConvenienceStoreViewModel? = null
    ) {
        val remainingTime by remember { remainingTimeState }
        var expanding by remember {
            mutableStateOf(false)
        }
        var loading by remember { mutableStateOf(true) }
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
                    var paymentCodeCopied by remember {
                        mutableStateOf(false)
                    }

                    paymentInstruction[paymentType]?.let {
                        Text(
                            text = stringResource(id = it),
                            style = SnapTypography.STYLES.snapTextMediumRegular,
                            color = SnapColors.getARGBColor(SnapColors.TEXT_SECONDARY)
                        )
                    }

                    SnapCopyableInfoListItem(
                        title = stringResource(id = R.string.indomaret_payment_code_title),
                        info = paymentCodeState.value,
                        copied = paymentCodeCopied,
                        withDivider = false,
                        onCopyClicked = { label ->
                            paymentCodeCopied = true
                            clipboardManager?.setText(AnnotatedString(text = label))
                        }
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .height(70.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        errorState.value?.let {
                            Row(
                                modifier = Modifier.fillMaxWidth(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(id = R.string.alfa_group_failed_to_load_payment_code),
                                    style = SnapTypography.STYLES.snapTextSmallRegular,
                                    color = SnapColors.getARGBColor(SnapColors.SUPPORT_DANGER_DEFAULT),
                                    modifier = Modifier.weight(1f)
                                )
                                SnapButton(
                                    style = SnapButton.Style.TERTIARY,
                                    text = stringResource(id = R.string.qr_reload),
                                    onClick = {
                                        viewModel?.resetError()
                                        loading = true
                                        charge()
                                    }
                                )
                            }

                        }
                        if(errorState.value == null){
                            if (barCode.value != null) {
                                AsyncImage(
                                    model = barCode.value, contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth(1f)
                                        .height(70.dp),
                                    onError = {
                                        loading = false
                                    },
                                    onSuccess = {
                                        loading = false
                                    },
                                    onLoading = {
                                        loading = true
                                    },
                                    contentScale = ContentScale.FillBounds
                                )
                            }
                        }
                        if (loading && errorState.value == null) {
                            Row(
                                modifier = Modifier.fillMaxWidth(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(id = R.string.alfa_group_loading_payment_code),
                                    style = SnapTypography.STYLES.snapTextSmallRegular,
                                    color = SnapColors.getARGBColor(SnapColors.TEXT_SECONDARY),
                                    modifier = Modifier.weight(1f)
                                )
                                GifImage(
                                    gifResId = R.drawable.gif_loading_ios,
                                    modifier = Modifier.width(40.dp).height(40.dp)
                                )
                            }
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
                                val instruction = paymentHowToPay
                                instruction[paymentType]?.let {
                                    SnapNumberedList(list = stringArrayResource(id = it).toList())
                                }
                            }
                        }
                    )
                }
            }
            SnapButton(
                text = stringResource(id = R.string.indomaret_cta_1),
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                enabled = errorState.value==null && !loading,
                style = SnapButton.Style.TERTIARY
            ) {

                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(pdfUrl.value)
                    )
                )

            }
            SnapButton(
                text = stringResource(id = R.string.indomaret_cta_2),
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(16.dp),
                enabled = errorState.value==null && !loading,
                style = if (errorState.value==null && !loading) SnapButton.Style.PRIMARY else SnapButton.Style.PRIMARY
            ) {

                onBackPressed()

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
            barCode = remember { mutableStateOf(null) },
            pdfUrl = remember { mutableStateOf(null) },
            paymentCodeState = remember { mutableStateOf("1234")},
            errorState = remember { mutableStateOf(null)}
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

    private val paymentHowToPay by lazy {
        mapOf(
            Pair(PaymentType.INDOMARET, R.array.indomaret_how_to_pay),
            Pair(PaymentType.ALFAMART, R.array.alfa_group_how_to_pay)
        )
    }

    private val paymentInstruction by lazy {
        mapOf(
            Pair(PaymentType.INDOMARET, R.string.indomaret_instruction),
            Pair(PaymentType.ALFAMART, R.string.alfa_group_instruction)
        )
    }

    private val title by lazy {
        mapOf(
            Pair(PaymentType.INDOMARET, R.string.payment_summary_indomaret),
            Pair(PaymentType.ALFAMART, R.string.payment_summary_alfamart),
        )
    }

    companion object {
        private const val EXTRA_TOTAL_AMOUNT = "convenience_store.extra.total_amount"
        private const val EXTRA_ORDER_ID = "convenience_store.extra.order_id"
        private const val EXTRA_CUSTOMER_DETAIL = "convenience_store.extra.customer_detail"
        private const val EXTRA_PAYMENTTYPE = "convenience_store.extra.payment_type"
        private const val EXTRA_SNAPTOKEN = "convenience_store.extra.snap_token"

        fun getIntent(
            activityContext: Context,
            snapToken: String,
            paymentType: String,
            totalAmount: String,
            orderId: String,
            customerInfo: CustomerInfo? = null

        ): Intent {
            return Intent(activityContext, ConvenienceStoreActivity::class.java).apply {
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