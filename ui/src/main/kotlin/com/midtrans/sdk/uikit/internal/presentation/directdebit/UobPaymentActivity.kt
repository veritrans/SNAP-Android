package com.midtrans.sdk.uikit.internal.presentation.directdebit

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rxjava2.subscribeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.api.model.TransactionResult
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.internal.model.CustomerInfo
import com.midtrans.sdk.uikit.internal.presentation.statusscreen.SuccessScreenActivity
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import com.midtrans.sdk.uikit.internal.view.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UobPaymentActivity : BaseActivity() {

    @Inject
    internal lateinit var vmFactory: ViewModelProvider.Factory

    private val snapToken: String by lazy {
        intent.getStringExtra(EXTRA_SNAP_TOKEN)
            ?: throw RuntimeException("Snap token must not be empty")
    }

    private val uobMode: String by lazy {
        intent.getStringExtra(EXTRA_UOB_MODE)
            ?: throw RuntimeException("Uob mode must not be empty")
    }

    private val amount: String by lazy {
        intent.getStringExtra(EXTRA_AMOUNT)
            ?: throw RuntimeException("Total amount must not be empty")
    }

    private val orderId: String by lazy {
        intent.getStringExtra(EXTRA_ORDER_ID)
            ?: throw RuntimeException("Order ID must not be empty")
    }

    private val remainingTime: Long by lazy {
        intent.getLongExtra(EXTRA_REMAINING_TIME, 0)
    }

    private val customerInfo: CustomerInfo? by lazy {
        intent.getParcelableExtra(EXTRA_CUSTOMER_INFO) as? CustomerInfo
    }

    private val viewModel: UobPaymentViewModel by lazy {
        ViewModelProvider(this, vmFactory).get(UobPaymentViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        UiKitApi.getDefaultInstance().daggerComponent.inject(this)

        setContent {
            UobPaymentContent(
                uobMode = uobMode,
                amount = amount,
                orderId = orderId,
                customerInfo = customerInfo,
                response = viewModel.getTransactionResponse().observeAsState().value,
                remainingTimeState = updateExpiredTime().subscribeAsState(initial = "00:00")
            )
        }
        observeTransactionStatus()
    }

    private fun observeTransactionStatus() {
        viewModel.getTransactionResult().observe(this) { result ->
            val status = result.first
            val transactionId = result.second
            when (status) {
                UiKitConstants.STATUS_SUCCESS,
                UiKitConstants.STATUS_SETTLEMENT -> {
                    goToSuccessScreen(
                        amount,
                        orderId,
                        TransactionResult(
                            status = status,
                            transactionId = transactionId,
                            paymentType = PaymentType.UOB_EZPAY
                        )
                    )
                }
                UiKitConstants.STATUS_PENDING,
                UiKitConstants.STATUS_FAILED -> {
                    val data = Intent()
                    data.putExtra(
                        UiKitConstants.KEY_TRANSACTION_RESULT,
                        TransactionResult(
                            status = status,
                            transactionId = transactionId,
                            paymentType = PaymentType.UOB_EZPAY
                        )
                    )
                    setResult(RESULT_OK, data)
                    finish()
                }
            }
        }
    }

    private val successScreenLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                setResult(result.resultCode, result?.data)
                finish()
        }

    private val webLinkLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.checkStatus(snapToken)
        }

    private fun goToSuccessScreen(
        amount: String,
        orderId: String,
        transactionResult: TransactionResult
    ) {
        successScreenLauncher.launch(
            SuccessScreenActivity.getIntent(
                activityContext = this,
                total = amount,
                orderId = orderId,
                transactionResult = transactionResult
            )
        )
    }

    @Composable
    private fun UobPaymentContent(
        uobMode: String,
        amount: String,
        orderId: String,
        customerInfo: CustomerInfo?,
        response: TransactionResponse?,
        remainingTimeState: State<String>
    ) {
        var isCustomerDetailExpanded by remember { mutableStateOf(false) }
        var isInstructionExpanded by remember { mutableStateOf(false) }
        val title = stringResource(getTitleId(uobMode = uobMode))
        val url = getUobDeeplinkUrl(uobMode, response)
        val remainingTime by remember { remainingTimeState }

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
                            canExpand = customerInfo != null,
                            remainingTime = remainingTime
                        ) {
                            isCustomerDetailExpanded = it
                        }
                    },
                    expandingContent = customerInfo?.let {
                        {
                            SnapCustomerDetail(
                                name = customerInfo.name,
                                phone = customerInfo.phone,
                                addressLines = customerInfo.addressLines
                            )
                        }
                    },
                    followingContent = {
                        Column(
                            modifier = Modifier
                                .verticalScroll(state = rememberScrollState())
                                .padding(top = 16.dp)
                                .fillMaxWidth()

                        ) {
                            SnapText(stringResource(getInstructionId(uobMode)))
                            SnapInstructionButton(
                                modifier = Modifier.padding(top = 28.dp),
                                isExpanded = isInstructionExpanded,
                                iconResId = R.drawable.ic_help,
                                title = stringResource(R.string.payment_instruction_how_to_pay_title),
                                onExpandClick = {
                                    viewModel.trackHowToPayClicked()
                                    isInstructionExpanded = !isInstructionExpanded
                                },
                                expandingContent = {
                                    Column {
                                        AnimatedVisibility(visible = isInstructionExpanded) {
                                            SnapNumberedList(
                                                list = stringArrayResource(getHowToPayId(uobMode)).toList()
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
                    enabled = true,
                    text = stringResource(id = getUobCta(uobMode)),
                    style = SnapButton.Style.PRIMARY
                ) {
                    viewModel.trackSnapButtonClicked(
                        ctaName = getStringResourceInEnglish(getUobCta(uobMode))
                    )
                    viewModel.payUob(snapToken)
                }
            }
        } else {
            response?.run {
                openDeeplink(
                    uobMode,
                    url,
                    TransactionResult(
                        status = transactionStatus.orEmpty(),
                        transactionId = transactionId.orEmpty(),
                        paymentType = paymentType.orEmpty()
                    )
                )
            }
        }
    }

    private fun getUobDeeplinkUrl(
        uobMode: String,
        response: TransactionResponse?
    ): String {
        return when (uobMode) {
            PaymentType.UOB_EZPAY_WEB -> response?.uobEzpayWebUrl.orEmpty()
            PaymentType.UOB_EZPAY_APP -> response?.uobEzpayDeeplinkUrl.orEmpty()
            else -> ""
        }
    }

    private fun openDeeplink(
        uobMode: String,
        url: String,
        transactionResult: TransactionResult
    ) {
        if (uobMode == PaymentType.UOB_EZPAY_WEB) {
            try {
                intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)

                val resultIntent = Intent().putExtra(
                    UiKitConstants.KEY_TRANSACTION_RESULT, transactionResult
                )
                setResult(RESULT_OK, resultIntent)
                finish()
            } catch (e: Throwable) {
                //TODO implement error handling later
            }
        } else {
            try {
                intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                webLinkLauncher.launch(intent)
            } catch (e: Throwable) {
                //TODO implement error handling later
            }
        }
    }

    private fun getTitleId(uobMode: String): Int {
        return when (uobMode) {
            PaymentType.UOB_EZPAY_APP -> R.string.uob_tmrw_method_name
            PaymentType.UOB_EZPAY_WEB -> R.string.uob_web_method_name
            else -> 0
        }
    }

    private fun getInstructionId(uobMode: String): Int {
        return when (uobMode) {
            PaymentType.UOB_EZPAY_WEB -> R.string.uob_web_instruction
            PaymentType.UOB_EZPAY_APP -> R.string.uob_tmrw_instruction
            else -> 0
        }
    }

    private fun getHowToPayId(uobMode: String): Int {
        return when (uobMode) {
            PaymentType.UOB_EZPAY_WEB -> R.array.uob_web_how_to_pay
            PaymentType.UOB_EZPAY_APP -> R.array.uob_tmrw_how_to_pay
            else -> 0
        }
    }

    private fun getUobCta(uobMode: String): Int {
        return when (uobMode) {
            PaymentType.UOB_EZPAY_WEB -> R.string.uob_web_cta
            PaymentType.UOB_EZPAY_APP -> R.string.uob_tmrw_cta
            else -> 0
        }
    }

    private fun updateExpiredTime(): Observable<String> {
        return Observable
            .interval(1L, TimeUnit.SECONDS)
            .map { viewModel.getExpiredHour(remainingTime) }
            .observeOn(AndroidSchedulers.mainThread())
    }

    companion object {
        private const val EXTRA_SNAP_TOKEN = "directDebit.uobPayment.extra.snap_token"
        private const val EXTRA_UOB_MODE = "directDebit.uobPayment.extra.uob_mode"
        private const val EXTRA_AMOUNT = "directDebit.uobPayment.extra.amount"
        private const val EXTRA_ORDER_ID = "directDebit.uobPayment.extra.order_id"
        private const val EXTRA_CUSTOMER_INFO = "directDebit.uobPayment.extra.customer_info"
        private const val EXTRA_REMAINING_TIME = "directDebit.uobPayment.extra.remaining_time"

        fun getIntent(
            activityContext: Context,
            snapToken: String,
            uobMode: String,
            amount: String,
            orderId: String,
            customerInfo: CustomerInfo?,
            remainingTime: Long
        ): Intent {
            return Intent(activityContext, UobPaymentActivity::class.java).apply {
                putExtra(EXTRA_SNAP_TOKEN, snapToken)
                putExtra(EXTRA_UOB_MODE, uobMode)
                putExtra(EXTRA_AMOUNT, amount)
                putExtra(EXTRA_ORDER_ID, orderId)
                putExtra(EXTRA_CUSTOMER_INFO, customerInfo)
                putExtra(EXTRA_REMAINING_TIME, remainingTime)
            }
        }
    }
}