package com.midtrans.sdk.uikit.internal.presentation.directdebit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.internal.di.DaggerUiKitComponent
import com.midtrans.sdk.uikit.internal.model.CustomerInfo

class UobPaymentActivity : BaseActivity() {

    private val snapToken: String by lazy {
        intent.getStringExtra(EXTRA_SNAP_TOKEN)
            ?: throw RuntimeException("Snap token must not be empty")
    }

    private val uobMode: String by lazy {
        intent.getStringExtra(EXTRA_UOB_MODE)
            ?: throw RuntimeException("Uob mode must not be empty")
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerUiKitComponent.builder()
            .applicationContext(this.applicationContext)
            .build()
            .inject(this)
    }

    @Composable
    private fun getTitleId(uobMode: String): Int {
        return when (uobMode) {
            PaymentType.UOB_EZPAY_APP -> R.string.uob_tmrw_method_name
            PaymentType.UOB_EZPAY_WEB -> R.string.uob_web_method_name
            else -> 0
        }
    }

    @Composable
    private fun getInstructionId(uobMode: String): Int {
        return when (uobMode) {
            PaymentType.UOB_EZPAY_WEB -> R.string.uob_web_instruction
            PaymentType.UOB_EZPAY_APP -> R.string.uob_tmrw_instruction
            else -> 0
        }
    }

    @Composable
    private fun getHowToPayId(uobMode: String): Int {
        return when (uobMode) {
            PaymentType.UOB_EZPAY_WEB -> R.array.uob_web_how_to_pay
            PaymentType.UOB_EZPAY_APP -> R.array.uob_tmrw_how_to_pay
            else -> 0
        }
    }

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