package com.midtrans.sdk.uikit.external

import android.content.Context
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.model.*
import com.midtrans.sdk.uikit.internal.model.PaymentMethodItem
import com.midtrans.sdk.uikit.internal.presentation.loadingpayment.LoadingPaymentActivity

class UiKitApi { //TODO revisit this implementation, currently for getting callback in Sample App
    lateinit var paymentCallback: Callback<TransactionResult>

    init {
        setInstance(this)
    }

    fun startPayment(
        activityContext: Context,
        transactionDetails: SnapTransactionDetail,
        customerDetails: CustomerDetails,
        creditCard: CreditCard,
        userId: String,
        uobEzpayCallback: PaymentCallback,
        paymentCallback: Callback<TransactionResult>,
        paymentType: PaymentMethodItem? = null
    ) {
        this.paymentCallback = paymentCallback

        val intent = LoadingPaymentActivity.getLoadingPaymentIntent(
            activityContext = activityContext,
            transactionDetails = transactionDetails,
            customerDetails = customerDetails,
            creditCard = creditCard,
            userId = userId,
            uobEzpayCallback = uobEzpayCallback,
            paymentType = paymentType
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
