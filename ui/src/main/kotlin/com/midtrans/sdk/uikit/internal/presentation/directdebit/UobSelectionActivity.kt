package com.midtrans.sdk.uikit.internal.presentation.directdebit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.internal.model.CustomerInfo
import com.midtrans.sdk.uikit.internal.view.*

class UobSelectionActivity : BaseActivity() {

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

    @Composable
    private fun UobSelectionContent(
        amount: String,
        orderId: String,
        customerInfo: CustomerInfo?
    ) {
        var isExpanded by remember { mutableStateOf(false) }

        Column(modifier = Modifier.background(SnapColors.getARGBColor(SnapColors.OVERLAY_WHITE))) {
            SnapAppBar(
                title = stringResource(id = R.string.uob_ez_pay_title),
                iconResId = R.drawable.ic_arrow_left
            ) {
                onBackPressed()
            }

            SnapOverlayExpandingBox(
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
                    LazyColumn {
                        getUobTypes().forEach { (type, title) ->
                            item {
                                SelectionListItem(title = stringResource(title)) {
                                    Log.d("UobSelection", type)
                                    //TODO call direct debit activity based on uob type
                                }
                            }
                        }
                    }
                }
            )
        }
    }

    @Composable
    @Preview(showBackground = true)
    private fun SelectionListItem(
        title: String = "HEHE",
        onClick: () -> Unit = { }
    ) {
        Column(modifier = Modifier.height(54.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(all = 16.dp)
                    .clickable(onClick = onClick)
            ) {
                Text(
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

    private fun getUobTypes(): List<Pair<String, Int>> {
        return listOf(
            Pair(PaymentType.UOB_WEB, R.string.uob_web_method_name),
            Pair(PaymentType.UOB_APP, R.string.uob_tmrw_method_name)
        )
    }

    companion object {
        private const val EXTRA_SNAP_TOKEN = "uobSelection.extra.snap_token"
        private const val EXTRA_AMOUNT = "uobSelection.extra.amount"
        private const val EXTRA_ORDER_ID = "uobSelection.extra.order_id"
        private const val EXTRA_CUSTOMER_INFO = "uobSelection.extra.customer_info"

        fun getIntent(
            activityContext: Context,
            snapToken: String,
            amount: String,
            orderId: String,
            customerInfo: CustomerInfo?
        ): Intent {
            return Intent(activityContext, UobSelectionActivity::class.java).apply {
                putExtra(EXTRA_SNAP_TOKEN, snapToken)
                putExtra(EXTRA_AMOUNT, amount)
                putExtra(EXTRA_ORDER_ID, orderId)
                putExtra(EXTRA_CUSTOMER_INFO, customerInfo)
            }
        }
    }
}
