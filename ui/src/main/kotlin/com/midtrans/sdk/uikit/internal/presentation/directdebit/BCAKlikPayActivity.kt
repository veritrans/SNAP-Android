package com.midtrans.sdk.uikit.internal.presentation.directdebit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import com.midtrans.sdk.corekit.internal.base.BaseActivity

class BCAKlikPayActivity : BaseActivity() {

    private val amount: String by lazy {
        intent.getStringExtra(EXTRA_AMOUNT) as String
    }

    private val orderId: String by lazy {
        intent.getStringExtra(EXTRA_ORDER_ID) as String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { }
    }

    companion object {
        private const val EXTRA_AMOUNT = "EXTRA_AMOUNT"
        private const val EXTRA_ORDER_ID = "EXTRA_ORDER_ID"

        fun getIntent(
            activityContext: Context,
            amount: String,
            orderId: String
        ): Intent {
            return Intent(activityContext, BCAKlikPayActivity::class.java).apply {
                putExtra(EXTRA_AMOUNT, amount)
                putExtra(EXTRA_ORDER_ID, orderId)
            }
        }
    }
}