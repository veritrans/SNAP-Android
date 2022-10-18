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
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.internal.di.DaggerUiKitComponent
import com.midtrans.sdk.uikit.internal.model.CustomerInfo
import com.midtrans.sdk.uikit.internal.model.PaymentMethodItem
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

    private val uobModes: List<String> by lazy {
        intent.getStringArrayListExtra(EXTRA_UOB_MODES)
            ?: throw RuntimeException("Missing Uob modes")
    }

    private val paymentType: PaymentMethodItem? by lazy {
        intent.getParcelableExtra(EXTRA_PAYMENT_TYPE)
    }

    private val viewModel: UobSelectionViewModel by lazy {
        ViewModelProvider(this, vmFactory).get(UobSelectionViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerUiKitComponent.builder()
            .applicationContext(this.applicationContext)
            .build()
            .inject(this)

        paymentType?.let { paymentType ->
            resultLauncher.launch(
                UobPaymentActivity.getIntent(
                    activityContext = this@UobSelectionActivity,
                    snapToken = snapToken,
                    uobMode = paymentType.methods[0],
                    amount = amount,
                    orderId = orderId,
                    customerInfo = customerInfo,
                    remainingTime = viewModel.getExpiredTime()
                )
            )
        } ?: run {
            setContent {
                UobSelectionContent(
                    amount = amount,
                    orderId = orderId,
                    customerInfo = customerInfo,
                    remainingTimeState = updateExpiredTime().subscribeAsState(initial = "00:00")
                )
            }
        }

    }

    @Preview(showBackground = true)
    @Composable
    private fun UobSelectionContent(
        amount: String = "Rp500",
        orderId: String = "order-123456",
        customerInfo: CustomerInfo? = CustomerInfo(name = "Harry", "Phone", listOf("address")),
        remainingTimeState: State<String> = remember { mutableStateOf("00:00") }
    ) {
        var isExpanded by remember { mutableStateOf(false) }
        val remainingTime by remember { remainingTimeState }

        Column(modifier = Modifier.background(SnapColors.getARGBColor(SnapColors.OVERLAY_WHITE))) {
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
                        canExpand = customerInfo != null,
                        remainingTime = remainingTime
                    ) {
                        isExpanded = it
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
                                            remainingTime = viewModel.getExpiredTime()
                                        )
                                    )
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
                color = SnapColors.getARGBColor(SnapColors.LINE_LIGHT_MUTED)
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
                setResult(RESULT_OK, result?.data)
                finish()
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
        private const val EXTRA_UOB_MODES = "uobSelection.extra.uob_modes"
        private const val EXTRA_PAYMENT_TYPE = "uobSelection.extra.payment_type"

        fun getIntent(
            activityContext: Context,
            snapToken: String,
            uobModes: ArrayList<String>,
            amount: String,
            orderId: String,
            customerInfo: CustomerInfo?,
            paymentType: PaymentMethodItem?
        ): Intent {
            return Intent(activityContext, UobSelectionActivity::class.java).apply {
                putExtra(EXTRA_SNAP_TOKEN, snapToken)
                putExtra(EXTRA_AMOUNT, amount)
                putExtra(EXTRA_ORDER_ID, orderId)
                putExtra(EXTRA_CUSTOMER_INFO, customerInfo)
                putExtra(EXTRA_PAYMENT_TYPE, paymentType)
                putStringArrayListExtra(EXTRA_UOB_MODES, uobModes)
            }
        }
    }
}
