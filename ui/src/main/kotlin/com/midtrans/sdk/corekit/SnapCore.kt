package com.midtrans.sdk.corekit

import android.content.Context
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.model.*
import com.midtrans.sdk.corekit.api.requestbuilder.cardtoken.CreditCardTokenRequestBuilder
import com.midtrans.sdk.corekit.api.requestbuilder.payment.PaymentRequestBuilder
import com.midtrans.sdk.corekit.api.requestbuilder.snaptoken.SnapTokenRequestBuilder
import com.midtrans.sdk.corekit.internal.analytics.ClickstreamEventAnalytics
import com.midtrans.sdk.corekit.internal.analytics.EventAnalytics
import com.midtrans.sdk.corekit.internal.di.DaggerSnapComponent
import com.midtrans.sdk.corekit.internal.di.SnapComponent
import com.midtrans.sdk.corekit.internal.usecase.PaymentUsecase
import javax.inject.Inject

class SnapCore private constructor(builder: Builder) {

    @Inject
    internal lateinit var paymentUsecase: PaymentUsecase

    @Inject
    internal lateinit var eventAnalytics: EventAnalytics

    @Inject
    internal lateinit var clickStreamEventAnalytics: ClickstreamEventAnalytics

    init {
        buildDaggerComponent(
            builder.context,
            builder.merchantUrl,
            builder.merchantClientKey,
            builder.enableLog
        ).inject(this)
    }

    fun hello(): String {
        return "hello snap"
    }

    fun getPaymentOption(
        snapToken: String?,
        builder: SnapTokenRequestBuilder,
        callback: Callback<PaymentOption>
    ) {
        paymentUsecase.getPaymentOption(snapToken, builder, callback)
    }

    fun pay(
        snapToken: String,
        paymentRequestBuilder: PaymentRequestBuilder,
        callback: Callback<TransactionResponse>
    ) {
        paymentUsecase.pay(snapToken, paymentRequestBuilder, callback)
    }

    fun getCardToken(
        cardTokenRequestBuilder: CreditCardTokenRequestBuilder,
        callback: Callback<CardTokenResponse>
    ) {
        paymentUsecase.getCardToken(cardTokenRequestBuilder, callback)
    }

    fun deleteSavedCard(
        snapToken: String,
        maskedCard: String,
        callback: Callback<DeleteSavedCardResponse>
    ) {
        paymentUsecase.deleteSavedCard(snapToken, maskedCard, callback)
    }

    fun getBinData(
        binNumber: String,
        callback: Callback<BinResponse>
    ) {
        paymentUsecase.getBinData(binNumber, callback)
    }

    fun getBankPoint(
        snapToken: String,
        cardToken: String,
        grossAmount: Double,
        callback: Callback<BankPointResponse>
    ) {
        paymentUsecase.getBankPoint(snapToken, cardToken, grossAmount, callback)
    }

    fun getTransactionStatus(
        snapToken: String,
        callback: Callback<TransactionResponse>
    ){
        paymentUsecase.getTransactionStatus(snapToken, callback)
    }

    fun getEventAnalytics() = eventAnalytics
    fun getClickStreamEventAnalytics() = clickStreamEventAnalytics

    companion object {
        private var INSTANCE: SnapCore? = null

        internal fun buildDaggerComponent(
            applicationContext: Context,
            merchantUrl: String,
            merchantClientKey: String,
            enableLog: Boolean
        ): SnapComponent {
            return DaggerSnapComponent.builder()
                .applicationContext(applicationContext)
                .merchantUrl(merchantUrl)
                .merchantClientKey(merchantClientKey)
                .enableLog(enableLog)
                .build()
        }

        fun getInstance(): SnapCore? = INSTANCE
    }

    class Builder {
        internal lateinit var context: Context
        internal lateinit var merchantUrl: String
        internal lateinit var merchantClientKey: String
        internal var enableLog: Boolean = false

        fun withContext(context: Context) = apply {
            this.context = context
        }

        fun withMerchantUrl(merchantUrl: String) = apply {
            this.merchantUrl = merchantUrl
        }

        fun withMerchantClientKey(merchantClientKey: String) = apply {
            this.merchantClientKey = merchantClientKey
        }

        fun enableLog(enableLog: Boolean) =  apply {
            this.enableLog = enableLog
        }

        @Throws(RuntimeException::class)
        fun build(): SnapCore {
            INSTANCE = SnapCore(this)
            return INSTANCE!!
        }
    }

}