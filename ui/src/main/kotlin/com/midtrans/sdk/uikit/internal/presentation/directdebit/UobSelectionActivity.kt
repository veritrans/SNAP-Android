package com.midtrans.sdk.uikit.internal.presentation.directdebit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.rxjava2.subscribeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.model.TransactionResult
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.internal.model.CustomerInfo
import com.midtrans.sdk.uikit.internal.model.ItemInfo
import com.midtrans.sdk.uikit.internal.model.PaymentTypeItem
import com.midtrans.sdk.uikit.internal.presentation.statusscreen.ErrorScreenActivity
import com.midtrans.sdk.uikit.internal.util.DateTimeUtil
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import com.midtrans.sdk.uikit.internal.view.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UobSelectionActivity : BaseActivity() {

    @Inject
    internal lateinit var vmFactory: ViewModelProvider.Factory

    private val snapToken: String by lazy {
        intent.getStringExtra(EXTRA_SNAP_TOKEN)
            ?: throw RuntimeException("Snap token must not be empty")
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

    private val uobModes: List<String> by lazy {
        intent.getStringArrayListExtra(EXTRA_UOB_MODES)
            ?: throw RuntimeException("Missing Uob modes")
    }

    private val paymentTypeItem: PaymentTypeItem? by lazy {
        intent.getParcelableExtra(EXTRA_PAYMENT_TYPE_ITEM)
    }

    private val currentStepNumber: Int by lazy {
        intent.getIntExtra(EXTRA_STEP_NUMBER, 0)
    }

    private val expiryTime: String? by lazy {
        intent.getStringExtra(EXTRA_EXPIRY_TIME)
    }

    private val viewModel: UobSelectionViewModel by lazy {
        ViewModelProvider(this, vmFactory)[UobSelectionViewModel::class.java]
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
        viewModel.trackPageViewed(currentStepNumber)
        viewModel.setExpiryTime(expiryTime)
        paymentTypeItem?.let { paymentType ->
            paymentType.method?.let { uobMode ->
                resultLauncher.launch(
                    UobPaymentActivity.getIntent(
                        activityContext = this@UobSelectionActivity,
                        snapToken = snapToken,
                        uobMode = uobMode,
                        amount = amount,
                        orderId = orderId,
                        customerInfo = customerInfo,
                        itemInfo = itemInfo,
                        remainingTime = expiryTime,
                        stepNumber = currentStepNumber + 1
                    )
                )
                isFirstInit = false
            }
        } ?: run {
            if (DateTimeUtil.getExpiredSeconds(viewModel.getExpiredHour()) <= 0L && isFirstInit) {
                launchExpiredErrorScreen()
            } else {
                setContent {
                    UobSelectionContent(
                        amount = amount,
                        orderId = orderId,
                        customerInfo = customerInfo,
                        itemInfo = itemInfo,
                        remainingTimeState = updateExpiredTime().subscribeAsState(initial = "00:00")
                    )
                }
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
            paymentType = PaymentType.UOB_EZPAY,
            message = resources.getString(R.string.expired_desc)
        )
        if (isShowPaymentStatusPage()) {
            errorScreenLauncher.launch(
                ErrorScreenActivity.getIntent(
                    activityContext = this@UobSelectionActivity,
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

    @Preview(showBackground = true)
    @Composable
    private fun UobSelectionContent(
        amount: String = "Rp500",
        orderId: String = "order-123456",
        customerInfo: CustomerInfo? = CustomerInfo(name = "Harry", "Phone", listOf("address")),
        itemInfo: ItemInfo? = null,
        remainingTimeState: State<String> = remember { mutableStateOf("00:00") }
    ) {
        var isExpanded by remember { mutableStateOf(false) }
        val remainingTime by remember { remainingTimeState }

        if (DateTimeUtil.getExpiredSeconds(remainingTime) <= 0L && isFirstInit) launchExpiredErrorScreen()

        Column(modifier = Modifier.background(SnapColors.getARGBColor(SnapColors.overlayWhite))) {
            SnapAppBar(
                title = stringResource(R.string.uob_ez_pay_title),
                iconResId = R.drawable.ic_arrow_left
            ) {
                onBackPressed()
            }

            SnapOverlayExpandingBox(
                modifier = Modifier
                    .weight(1f)
                    .padding(all = 16.dp),
                isExpanded = isExpanded,
                mainContent = {
                    SnapTotal(
                        amount = amount,
                        orderId = orderId,
                        canExpand = customerInfo != null || itemInfo != null,
                        remainingTime = remainingTime
                    ) {
                        isExpanded = it
                    }
                },
                expandingContent = {
                    viewModel.trackOrderDetailsViewed()
                    SnapPaymentOrderDetails(
                        customerInfo = customerInfo,
                        itemInfo = itemInfo
                    )
                },
                followingContent = {
                    LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
                        items(
                            items = getUobModeAndTitle(),
                            key = { it.first },
                            itemContent = { item ->
                                SelectionListItem(title = stringResource(item.second)) {
                                    resultLauncher.launch(
                                        UobPaymentActivity.getIntent(
                                            activityContext = this@UobSelectionActivity,
                                            snapToken = snapToken,
                                            uobMode = item.first,
                                            amount = amount,
                                            orderId = orderId,
                                            customerInfo = customerInfo,
                                            itemInfo = itemInfo,
                                            remainingTime = expiryTime,
                                            stepNumber = currentStepNumber + 1
                                        )
                                    )
                                    isFirstInit = false
                                }
                            }
                        )
                    }
                }
            )
        }
    }

    @Composable
    private fun SelectionListItem(
        modifier: Modifier = Modifier,
        title: String,
        onClick: () -> Unit
    ) {
        Column(
            modifier = Modifier
                .height(54.dp)
                .clickable(onClick = onClick)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
                modifier = modifier.weight(1f)
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 16.dp),
                    text = title,
                    style = SnapTypography.STYLES.snapTextBigRegular
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_chevron_right),
                    tint = Color.Unspecified,
                    contentDescription = null
                )
            }
            Divider(
                thickness = 1.dp,
                color = SnapColors.getARGBColor(SnapColors.lineLightMuted)
            )
        }
    }

    private fun getUobModeAndTitle(): List<Pair<String, Int>> {
        val modes = mutableListOf<Pair<String, Int>>()

        uobModes.find { it.contains(PaymentType.UOB_EZPAY_WEB, true) }
            ?.let { modes.add(Pair(it, R.string.uob_web_method_name)) }
        uobModes.find { it.contains(PaymentType.UOB_EZPAY_APP, true) }
            ?.let { modes.add(Pair(it, R.string.uob_tmrw_method_name)) }

        return modes
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                setResult(Activity.RESULT_OK, result.data)
                finish()
            } else {
                setResult(Activity.RESULT_CANCELED)
            }
        }

    private fun updateExpiredTime(): Observable<String> {
        return Observable
            .interval(1L, TimeUnit.SECONDS)
            .map { viewModel.getExpiredHour() }
            .observeOn(AndroidSchedulers.mainThread())
    }

    companion object {
        private const val EXTRA_SNAP_TOKEN = "uobSelection.extra.snap_token"
        private const val EXTRA_AMOUNT = "uobSelection.extra.amount"
        private const val EXTRA_ORDER_ID = "uobSelection.extra.order_id"
        private const val EXTRA_CUSTOMER_INFO = "uobSelection.extra.customer_info"
        private const val EXTRA_ITEM_INFO = "uobSelection.extra.item_info"
        private const val EXTRA_UOB_MODES = "uobSelection.extra.uob_modes"
        private const val EXTRA_PAYMENT_TYPE_ITEM = "uobSelection.extra.payment_type_item"
        private const val EXTRA_STEP_NUMBER = "uobSelection.extra.step_number"
        private const val EXTRA_EXPIRY_TIME = "uobSelection.extra.expiry_time"

        fun getIntent(
            activityContext: Context,
            snapToken: String,
            uobModes: ArrayList<String>,
            amount: String,
            orderId: String,
            customerInfo: CustomerInfo?,
            itemInfo: ItemInfo?,
            paymentTypeItem: PaymentTypeItem?,
            stepNumber: Int,
            expiryTime: String?
        ): Intent {
            return Intent(activityContext, UobSelectionActivity::class.java).apply {
                putExtra(EXTRA_SNAP_TOKEN, snapToken)
                putExtra(EXTRA_AMOUNT, amount)
                putExtra(EXTRA_ORDER_ID, orderId)
                putExtra(EXTRA_CUSTOMER_INFO, customerInfo)
                putExtra(EXTRA_ITEM_INFO, itemInfo)
                putExtra(EXTRA_PAYMENT_TYPE_ITEM, paymentTypeItem)
                putExtra(EXTRA_STEP_NUMBER, stepNumber)
                putStringArrayListExtra(EXTRA_UOB_MODES, uobModes)
                putExtra(EXTRA_EXPIRY_TIME, expiryTime)
            }
        }
    }
}
