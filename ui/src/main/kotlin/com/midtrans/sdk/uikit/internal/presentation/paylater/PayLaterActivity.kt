package com.midtrans.sdk.uikit.internal.presentation.paylater

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.uikit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.internal.model.CustomerInfo
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

    private val viewModel: PayLaterViewModel by lazy {
        ViewModelProvider(this, vmFactory).get(PayLaterViewModel::class.java)
    }

    companion object {
        private const val EXTRA_SNAP_TOKEN = "payLater.extra.snap_token"
        private const val EXTRA_PAYMENT_TYPE = "payLater.extra.payment_type"
        private const val EXTRA_AMOUNT = "payLater.extra.amount"
        private const val EXTRA_ORDER_ID = "payLater.extra.order_id"
        private const val EXTRA_CUSTOMER_INFO = "payLater.extra.customer_info"

        fun getIntent(
            activityContext: Context,
            snapToken: String,
            @PaymentType.Def paymentType: String,
            amount: String,
            orderId: String,
            customerInfo: CustomerInfo?
        ): Intent {
            return Intent(activityContext, PayLaterActivity::class.java).apply {
                putExtra(EXTRA_SNAP_TOKEN, snapToken)
                putExtra(EXTRA_PAYMENT_TYPE, paymentType)
                putExtra(EXTRA_AMOUNT, amount)
                putExtra(EXTRA_ORDER_ID, orderId)
                putExtra(EXTRA_CUSTOMER_INFO, customerInfo)
            }
        }
    }
}
