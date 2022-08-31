package com.midtrans.sdk.uikit.internal.presentation.directdebit

import android.content.Context
import android.content.Intent
import com.midtrans.sdk.uikit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.internal.model.CustomerInfo

class UobSelectionActivity : BaseActivity() {

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
