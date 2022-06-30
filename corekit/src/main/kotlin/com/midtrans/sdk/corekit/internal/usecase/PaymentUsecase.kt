package com.midtrans.sdk.corekit.internal.usecase

import android.annotation.SuppressLint
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.exception.SnapError
import com.midtrans.sdk.corekit.api.model.BinResponse
import com.midtrans.sdk.corekit.api.model.CardTokenResponse
import com.midtrans.sdk.corekit.api.model.DeleteSavedCardResponse
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.api.requestbuilder.cardtoken.CreditCardTokenRequestBuilder
import com.midtrans.sdk.corekit.api.requestbuilder.payment.PaymentRequestBuilder
import com.midtrans.sdk.corekit.internal.data.repository.CoreApiRepository
import com.midtrans.sdk.corekit.internal.data.repository.SnapRepository
import com.midtrans.sdk.corekit.internal.scheduler.SdkScheduler

internal class PaymentUsecase(
    private val scheduler: SdkScheduler,
    private val snapRepository: SnapRepository,
    private val coreApiRepository: CoreApiRepository
) {

    @SuppressLint("CheckResult")
    fun pay(
        snapToken: String,
        paymentRequestBuilder: PaymentRequestBuilder,
        callback: Callback<TransactionResponse>
    ) {
        try {
            val paymentRequest = paymentRequestBuilder.build()
            snapRepository.pay(snapToken, paymentRequest)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(
                    {
                        callback.onSuccess(it)
                    },
                    {
                        deliverError(it, callback)
                    }
                )
        } catch (error: Throwable) {
            deliverError(error, callback)
        }
    }

    private fun <T> deliverError(error: Throwable, callback: Callback<T>) {
        if (error is SnapError)
            callback.onError(error)
        else callback.onError(SnapError(cause = error))
    }

    @SuppressLint("CheckResult")
    fun getCardToken(
        cardTokenRequestBuilder: CreditCardTokenRequestBuilder,
        callback: Callback<CardTokenResponse>
    ) {
        try {
            val cardTokenRequest = cardTokenRequestBuilder.build()
            coreApiRepository.getCardToken(cardTokenRequest)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(
                    {
                        callback.onSuccess(it)
                    },
                    {
                        deliverError(it, callback)
                    }
                )
        } catch (error: Throwable) {
            deliverError(error, callback)
        }

    }

    @SuppressLint("CheckResult")
    fun getBinData(
        binNumber: String,
        clientKey: String,
        callback: Callback<BinResponse>
    ){
        try {
            coreApiRepository.getBinData(binNumber, clientKey)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(
                    {
                        callback.onSuccess(it)
                    },
                    {
                        deliverError(it, callback)
                    }
                )
        } catch (error: Throwable) {
            deliverError(error, callback)
        }
    }

    @SuppressLint("CheckResult")
    fun deleteSavedCard(
        snapToken: String,
        maskedCard: String,
        callback: Callback<DeleteSavedCardResponse>
    ) {
        try {
            snapRepository.deleteSavedCard(snapToken, maskedCard)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(
                    {
                        callback.onSuccess(it)
                    },
                    {
                        deliverError(it, callback)
                    }
                )
        } catch (error: Throwable) {
            deliverError(error, callback)
        }
    }
}