package com.midtrans.sdk.corekit

import android.content.Context
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.model.*
import com.midtrans.sdk.corekit.api.requestbuilder.cardtoken.CreditCardTokenRequestBuilder
import com.midtrans.sdk.corekit.api.requestbuilder.payment.PaymentRequestBuilder
import com.midtrans.sdk.corekit.internal.di.DaggerSnapComponent
import com.midtrans.sdk.corekit.internal.di.SnapComponent
import com.midtrans.sdk.corekit.internal.usecase.PaymentUsecase
import javax.inject.Inject

class SnapCore private constructor(builder: Builder) {

    @Inject
    internal lateinit var paymentUsecase: PaymentUsecase

    init {
        buildDaggerComponent(builder.context).inject(this)
    }

    fun hello(): String {
        return "hello snap"
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
    ){
        paymentUsecase.deleteSavedCard(snapToken, maskedCard, callback)
    }

    fun getBinData(
        binNumber: String,
        clientKey: String,
        callback: Callback<BinResponse>
    ){
        paymentUsecase.getBinData(binNumber, clientKey, callback)
    }

    fun getBankPoint(
        snapToken: String,
        cardToken: String,
        grossAmount: Double,
        callback: Callback<BankPointResponse>
    ){
        paymentUsecase.getBankPoint(snapToken, cardToken, grossAmount, callback)
    }

    companion object {
        private var INSTANCE: SnapCore? = null

        internal fun buildDaggerComponent(applicationContext: Context): SnapComponent {
            return DaggerSnapComponent.builder()
                .applicationContext(applicationContext)
                .build()
        }

        fun getInstance(): SnapCore? = INSTANCE
    }

    class Builder {
        internal lateinit var context: Context

        fun withContext(context: Context) = apply {
            this.context = context
        }

        @Throws(RuntimeException::class)
        fun build(): SnapCore {
            INSTANCE = SnapCore(this)
            return INSTANCE!!
        }
    }

}