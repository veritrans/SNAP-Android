package com.midtrans.sdk.uikit.internal.presentation.paylater

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rxjava2.subscribeAsState
import androidx.compose.ui.Modifier
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
import com.midtrans.sdk.uikit.internal.model.ItemInfo
import com.midtrans.sdk.uikit.internal.presentation.statusscreen.ErrorScreenActivity
import com.midtrans.sdk.uikit.internal.util.DateTimeUtil
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import com.midtrans.sdk.uikit.internal.view.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PayLaterActivity : BaseActivity() {

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
        intent.getParcelableExtra(EXTRA_CUSTOMER_INFO) as? CustomerInfo
    }

    private val itemInfo: ItemInfo? by lazy {
        intent.getParcelableExtra(EXTRA_ITEM_INFO) as? ItemInfo
    }

    private val currentStepNumber: Int by lazy {
        intent.getIntExtra(EXTRA_STEP_NUMBER, 0)
    }

    private val transactionResult: TransactionResponse? by lazy {
        intent.getParcelableExtra(EXTRA_TRANSACTION_RESULT) as? TransactionResponse
    }

    private val expiryTime: String? by lazy {
        intent.getStringExtra(EXTRA_EXPIRY_TIME)
    }

    private val viewModel: PayLaterViewModel by lazy {
        ViewModelProvider(this, vmFactory).get(PayLaterViewModel::class.java)
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

        transactionResult?.let { result ->
            viewModel.getUsedToken(result)
        }

        if (DateTimeUtil.getExpiredSeconds(viewModel.getExpiredHour()) <= 0L && isFirstInit) {
            launchExpiredErrorScreen()
        } else {
            setContent {
                PayLaterContent(
                    paymentType = paymentType,
                    amount = amount,
                    orderId = orderId,
                    customerInfo = customerInfo,
                    itemInfo = itemInfo,
                    response = viewModel.transactionResponseLiveData.observeAsState().value,
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
                    activityContext = this@PayLaterActivity,
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
    private fun PayLaterContent(
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
        val url = response?.redirectUrl.orEmpty()
        val remainingTime by remember { remainingTimeState }
        val transactionId = viewModel.transactionId.observeAsState()

        if (DateTimeUtil.getExpiredSeconds(remainingTime) <= 0L && isFirstInit) launchExpiredErrorScreen()

        transactionResult?.let { result ->
            viewModel.checkStatus(snapToken)
            SnapWebView(
                title = title,
                paymentType = paymentType,
                url = result.redirectUrl.toString(),
                onPageStarted = {
                    finishPayLater(
                        status = result.transactionStatus,
                        transactionId = transactionId.value
                    )
                },
                onPageFinished = { }
            )
        } ?: run {
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
                            SnapInstruction(
                                paymentType = paymentType,
                                isInstructionExpanded = isInstructionExpanded,
                                onExpandClick = {
                                    viewModel.trackHowToPayClicked(paymentType)
                                    isInstructionExpanded = !isInstructionExpanded
                                }
                            )
                        }
                    )

                    SnapButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
                        enabled = true,
                        text = stringResource(getCta(paymentType)),
                        style = SnapButton.Style.PRIMARY
                    ) {
                        viewModel.trackSnapButtonClicked(
                            ctaName = getStringResourceInEnglish(getCta(paymentType)),
                            paymentType = paymentType
                        )
                        viewModel.payPayLater(
                            snapToken = snapToken,
                            paymentType = paymentType
                        )
                    }
                }
            } else {
                val status = response?.transactionStatus
                val transactionId = response?.transactionId

                viewModel.trackOpenWebView(paymentType)
                SnapWebView(
                    title = title,
                    paymentType = paymentType,
                    url = url,
                    onPageStarted = {
                        finishPayLater(
                            status = status,
                            transactionId = transactionId
                        )
                    },
                    onPageFinished = { }
                )
            }
        }
    }

    private fun finishPayLater(
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

    private fun getTitleId(paymentType: String): Int {
        return when (paymentType) {
            PaymentType.AKULAKU -> R.string.akulaku_title
            else -> 0
        }
    }

    private fun getCta(paymentType: String): Int {
        return when (paymentType) {
            PaymentType.AKULAKU -> R.string.akulaku_cta
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
        private const val EXTRA_SNAP_TOKEN = "payLater.extra.snap_token"
        private const val EXTRA_PAYMENT_TYPE = "payLater.extra.payment_type"
        private const val EXTRA_AMOUNT = "payLater.extra.amount"
        private const val EXTRA_ORDER_ID = "payLater.extra.order_id"
        private const val EXTRA_CUSTOMER_INFO = "payLater.extra.customer_info"
        private const val EXTRA_ITEM_INFO = "payLater.extra.item_info"
        private const val EXTRA_STEP_NUMBER = "payLater.extra.step_number"
        private const val EXTRA_TRANSACTION_RESULT = "payLater.extra.transaction_result"
        private const val EXTRA_EXPIRY_TIME = "payLater.extra.expiry_time"

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
            return Intent(activityContext, PayLaterActivity::class.java).apply {
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
