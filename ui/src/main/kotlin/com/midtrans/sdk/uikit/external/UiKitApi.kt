package com.midtrans.sdk.uikit.external

import android.content.Context
import androidx.compose.ui.text.font.FontFamily
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.uikit.api.callback.Callback
import com.midtrans.sdk.uikit.api.model.*
import com.midtrans.sdk.uikit.internal.di.DaggerUiKitComponent
import com.midtrans.sdk.uikit.internal.di.UiKitComponent
import com.midtrans.sdk.uikit.internal.model.PaymentTypeItem
import com.midtrans.sdk.uikit.internal.presentation.loadingpayment.LoadingPaymentActivity
import java.lang.ref.WeakReference

class UiKitApi private constructor(val builder: Builder){ //TODO revisit this implementation, currently for getting callback in Sample App

    init {
        setInstance(this)
        buildCoreKit(builder)
    }

    private fun buildCoreKit(builder: Builder){
        builder.run {
            SnapCore.Builder()
                .withContext(context)
                .withMerchantUrl(merchantUrl)
                .withMerchantClientKey(merchantClientKey)
                .build()
        }
    }

    internal val daggerComponent: UiKitComponent by lazy {
        DaggerUiKitComponent.builder().applicationContext(builder.context).build()
    }

    internal val paymentCallback = UiKitApi.paymentCallback
    internal val customColors = builder.customColors
    internal val customFontFamily = builder.fontFamily

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
        internal lateinit var context: Context
        internal lateinit var merchantUrl: String
        internal lateinit var merchantClientKey: String
        internal var customColors: CustomColors? = null
        internal var fontFamily: FontFamily? = null

        fun withContext(context: Context) = apply {
            this.context = context.applicationContext
        }

        fun withMerchantUrl(merchantUrl: String) = apply {
            this.merchantUrl = merchantUrl
        }

        fun withMerchantClientKey(merchantClientKey: String) = apply {
            this.merchantClientKey = merchantClientKey
        }

        fun withCustomColors(customColors: CustomColors) = apply {
            this.customColors = customColors
        }

        fun withFontFamily(fontFamily: FontFamily) = apply {
            this.fontFamily = fontFamily
        }

        @Throws(RuntimeException::class)
        fun build(): UiKitApi {
            if(!this::context.isInitialized){
                throw Throwable(message = "context is required")
            }
            if(!this::merchantUrl.isInitialized){
                throw Throwable(message = "merchantUrl is required")
            }
            if(!this::merchantClientKey.isInitialized){
                throw Throwable(message = "merchantClientKey is required")
            }
            UiKitApi(this)

            return instance
        }
    }

    companion object {
        private var paymentCallbackWeakReference: WeakReference<Callback<TransactionResult>?> =
            WeakReference(null)
        private var paymentCallback: Callback<TransactionResult>?
            private set(value) {
                paymentCallbackWeakReference = WeakReference(value)
            }
            get() = paymentCallbackWeakReference.get()

        private lateinit var instance: UiKitApi


        fun getDefaultInstance(): UiKitApi {
            return instance
        }

        private fun setInstance(uiKitApi: UiKitApi) {
            instance = uiKitApi
        }
    }
}
