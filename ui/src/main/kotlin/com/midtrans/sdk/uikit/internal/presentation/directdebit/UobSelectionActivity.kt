package com.midtrans.sdk.uikit.internal.presentation.directdebit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.internal.model.CustomerInfo
import com.midtrans.sdk.uikit.internal.view.*

class UobSelectionActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UobSelectionContent(
                amount = ,
                orderId = ,
                customerInfo =
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

                }
            )
        }

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
