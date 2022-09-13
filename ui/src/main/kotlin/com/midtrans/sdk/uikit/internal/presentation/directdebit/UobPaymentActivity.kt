package com.midtrans.sdk.uikit.internal.presentation.directdebit

import android.content.Context
import android.content.Intent
import com.midtrans.sdk.uikit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.internal.model.CustomerInfo

class UobPaymentActivity : BaseActivity() {
    companion object {
        private const val EXTRA_SNAP_TOKEN = "directDebit.uobPayment.extra.snap_token"
        private const val EXTRA_UOB_MODE = "directDebit.uobPayment.extra.uob_mode"
        private const val EXTRA_AMOUNT = "directDebit.uobPayment.extra.amount"
        private const val EXTRA_ORDER_ID = "directDebit.uobPayment.extra.order_id"
        private const val EXTRA_CUSTOMER_INFO = "directDebit.uobPayment.extra.customer_info"

        fun getIntent(
            activityContext: Context,
            snapToken: String,
            uobMode: String = "",
            amount: String,
            orderId: String,
            customerInfo: CustomerInfo?
        ): Intent {
            return Intent(activityContext, UobPaymentActivity::class.java).apply {
                putExtra(EXTRA_SNAP_TOKEN, snapToken)
                putExtra(EXTRA_UOB_MODE, uobMode)
                putExtra(EXTRA_AMOUNT, amount)
                putExtra(EXTRA_ORDER_ID, orderId)
                putExtra(EXTRA_CUSTOMER_INFO, customerInfo)
            }
        }
    }
}