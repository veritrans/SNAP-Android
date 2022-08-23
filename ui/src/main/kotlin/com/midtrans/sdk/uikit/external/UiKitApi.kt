package com.midtrans.sdk.uikit.external

import android.content.Context
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.model.CustomerDetails
import com.midtrans.sdk.corekit.api.model.SnapTransactionDetail
import com.midtrans.sdk.corekit.api.model.TransactionResult
import com.midtrans.sdk.uikit.internal.presentation.loadingpayment.LoadingPaymentActivity

class UiKitApi {
    lateinit var paymentCallback: Callback<TransactionResult>

    init {
        setInstance(this)
    }

    fun startPayment( //TODO revisit this implementation, currently for showing status in Sample App
        activityContext: Context,
        transactionDetails: SnapTransactionDetail,
        customerDetails: CustomerDetails,
        paymentCallback: Callback<TransactionResult>
    ) {
        this.paymentCallback = paymentCallback

        val intent = LoadingPaymentActivity.getLoadingPaymentIntent(
            activityContext = activityContext,
            transactionDetails = transactionDetails,
            customerDetails = customerDetails
        )
        activityContext.startActivity(intent)
    }

    companion object {
        private lateinit var instance: UiKitApi

        fun getDefaultInstance(): UiKitApi {
            return instance
        }

        private fun setInstance(uiKitApi: UiKitApi) {
            instance = uiKitApi
        }
    }
}
