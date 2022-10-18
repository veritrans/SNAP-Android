package com.midtrans.sdk.uikit.external

import android.content.Context
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.uikit.api.callback.Callback
import com.midtrans.sdk.uikit.api.model.*
import com.midtrans.sdk.uikit.internal.di.DaggerUiKitComponent
import com.midtrans.sdk.uikit.internal.di.UiKitComponent
import com.midtrans.sdk.uikit.internal.model.PaymentTypeItem
import com.midtrans.sdk.uikit.internal.presentation.loadingpayment.LoadingPaymentActivity
import java.lang.ref.WeakReference

class UiKitApi { //TODO revisit this implementation, currently for getting callback in Sample App

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
        paymentType: PaymentTypeItem? = null
    ) {
        UiKitApi.paymentCallback = paymentCallback

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

    class Builder {
        private lateinit var context: Context
        private lateinit var merchantUrl: String
        private lateinit var merchantClientKey: String

        fun withContext(context: Context) = apply {
            this.context = context
        }

        fun withMerchantUrl(merchantUrl: String) = apply {
            this.merchantUrl = merchantUrl
        }

        fun withMerchantClientKey(merchantClientKey: String) = apply {
            this.merchantClientKey = merchantClientKey
        }

        @Throws(RuntimeException::class)
        fun build(): UiKitApi {
            SnapCore.Builder()
                .withContext(context)
                .withMerchantUrl(merchantUrl)
                .withMerchantClientKey(merchantClientKey)
                .build()
            daggerUiKitComponent = DaggerUiKitComponent.builder().applicationContext(context.applicationContext).build()
            UiKitApi()
            return instance
        }
    }

    companion object {
        private var paymentCallbackWeakReference: WeakReference<Callback<TransactionResult>?> = WeakReference(null)
        internal var paymentCallback: Callback<TransactionResult>?
        private set(value) {
            paymentCallbackWeakReference = WeakReference(value)
        }
            get() = paymentCallbackWeakReference.get()

        internal lateinit var daggerUiKitComponent: UiKitComponent
            private set

        private lateinit var instance: UiKitApi

        fun getDefaultInstance(): UiKitApi {
            return instance
        }

        private fun setInstance(uiKitApi: UiKitApi) {
            instance = uiKitApi
        }
    }
}
