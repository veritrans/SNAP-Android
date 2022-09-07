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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.model.TransactionResult
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.internal.model.CustomerInfo
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import com.midtrans.sdk.uikit.internal.view.*

class UobSelectionActivity : BaseActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UobSelectionContent(
                amount = amount,
                orderId = orderId,
                customerInfo = customerInfo
            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun UobSelectionContent(
        amount: String = "Rp500",
        orderId: String = "order-123456",
        customerInfo: CustomerInfo? = CustomerInfo(name="Harry","Phone", listOf("address"))
    ) {
        var isExpanded by remember { mutableStateOf(false) }

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
                        remainingTime = null
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
                                    getOnSelectedItemClick(
                                        snapToken = snapToken,
                                        orderId = orderId,
                                        totalAmount = amount,
                                        customerInfo = customerInfo
                                    )[item.first]?.invoke()
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
        Column(modifier = Modifier
            .height(54.dp)
            .clickable(onClick = onClick)) {
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

    private fun getOnSelectedItemClick(
        snapToken: String,
        totalAmount: String,
        orderId: String,
        customerInfo: CustomerInfo?
    ): Map<String, () -> Unit> {
        return mapOf(
            Pair(PaymentType.UOB_EZPAY_WEB) {
                resultLauncher.launch(
                    DirectDebitActivity.getIntent(
                        activityContext = this,
                        snapToken = snapToken,
                        paymentType = PaymentType.UOB_EZPAY,
                        amount = totalAmount,
                        orderId = orderId,
                        customerInfo = customerInfo
                    )
                )
            }
        )
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result?.data?.let {
                    val transactionResult = it.getParcelableExtra<TransactionResult>(UiKitConstants.KEY_TRANSACTION_RESULT) as TransactionResult
                    UiKitApi.getDefaultInstance().paymentCallback.onSuccess(transactionResult) //TODO temporary for direct debit, revisit after real callback like the one in MidtransSdk implemented
                }
                finish()
            }
        }

    companion object {
        private const val EXTRA_SNAP_TOKEN = "uobSelection.extra.snap_token"
        private const val EXTRA_AMOUNT = "uobSelection.extra.amount"
        private const val EXTRA_ORDER_ID = "uobSelection.extra.order_id"
        private const val EXTRA_CUSTOMER_INFO = "uobSelection.extra.customer_info"
        private const val EXTRA_UOB_MODES = "uobSelection.extra.uob_modes"

        fun getIntent(
            activityContext: Context,
            snapToken: String,
            uobModes: ArrayList<String>,
            amount: String,
            orderId: String,
            customerInfo: CustomerInfo?
        ): Intent {
            return Intent(activityContext, UobSelectionActivity::class.java).apply {
                putExtra(EXTRA_SNAP_TOKEN, snapToken)
                putExtra(EXTRA_AMOUNT, amount)
                putExtra(EXTRA_ORDER_ID, orderId)
                putExtra(EXTRA_CUSTOMER_INFO, customerInfo)
                putStringArrayListExtra(EXTRA_UOB_MODES, uobModes)
            }
        }
    }
}
