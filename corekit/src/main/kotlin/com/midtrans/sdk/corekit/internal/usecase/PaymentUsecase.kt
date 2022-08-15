package com.midtrans.sdk.corekit.internal.usecase

import android.annotation.SuppressLint
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.exception.SnapError
import com.midtrans.sdk.corekit.api.model.BankPointResponse
import com.midtrans.sdk.corekit.api.model.BinResponse
import com.midtrans.sdk.corekit.api.model.CardTokenResponse
import com.midtrans.sdk.corekit.api.model.DeleteSavedCardResponse
import com.midtrans.sdk.corekit.api.model.PaymentMethod
import com.midtrans.sdk.corekit.api.model.PaymentOption
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.api.requestbuilder.cardtoken.CreditCardTokenRequestBuilder
import com.midtrans.sdk.corekit.api.requestbuilder.payment.PaymentRequestBuilder
import com.midtrans.sdk.corekit.api.requestbuilder.snaptoken.SnapTokenRequestBuilder
import com.midtrans.sdk.corekit.internal.data.repository.CoreApiRepository
import com.midtrans.sdk.corekit.internal.data.repository.MerchantApiRepository
import com.midtrans.sdk.corekit.internal.data.repository.SnapRepository
import com.midtrans.sdk.corekit.internal.network.model.response.EnabledPayment
import com.midtrans.sdk.corekit.internal.scheduler.BaseSdkScheduler
import io.reactivex.Single

internal class PaymentUsecase(
    private val scheduler: BaseSdkScheduler,
    private val snapRepository: SnapRepository,
    private val coreApiRepository: CoreApiRepository,
    private val merchantApiRepository: MerchantApiRepository,
    private val clientKey: String
) {

    @SuppressLint("CheckResult")
    fun getPaymentOption(
        snapToken: String?,
        requestBuilder: SnapTokenRequestBuilder,
        callback: Callback<PaymentOption>
    ) {
        if (snapToken.isNullOrBlank()) {
            merchantApiRepository
                .getSnapToken(requestBuilder.build())
                .onErrorResumeNext {
                    Single.error(
                        SnapError(
                            cause = it,
                            message = "Failed on getting snap token"
                        )
                    )
                }
                .flatMap { response ->
                    snapRepository
                        .getTransactionDetail(response.token.orEmpty())
                        .map {
                            Pair(response.token, it)
                        }
                }
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(
                    { data ->
                        val token = data.first
                        val responseData = data.second
                        val methods = mutableListOf<PaymentMethod>()
                        responseData.enabledPayments?.forEach {
                            addPaymentMethod(it, methods)
                        }
                        callback.onSuccess(
                            PaymentOption(
                                token.orEmpty(),
                                methods
                            )
                        )
                    },
                    {
                        deliverError(it, callback)
                    }
                )
        } else {
            snapRepository.getTransactionDetail(snapToken)
                .subscribe(
                    { responseData ->
                        val methods = mutableListOf<PaymentMethod>()
                        responseData.enabledPayments?.forEach {
                            addPaymentMethod(it, methods)
                        }
                        callback.onSuccess(
                            PaymentOption(
                                snapToken,
                                methods
                            )
                        )
                    },
                    {
                        deliverError(it, callback)
                    }
                )
        }
    }

    private fun addPaymentMethod(payment: EnabledPayment, methods: MutableList<PaymentMethod>) {
        if (payment.status == EnabledPayment.STATUS_DOWN)
            return

        if (payment.category == PaymentType.BANK_TRANSFER) {
            val index = methods.indexOfFirst { method ->
                method.type == PaymentType.BANK_TRANSFER
            }

            if (index == -1) {
                methods.add(
                    PaymentMethod(
                        type = PaymentType.BANK_TRANSFER,
                        channels = listOf(payment.type)
                    )
                )
            } else {
                methods[index] = PaymentMethod(
                    type = PaymentType.BANK_TRANSFER,
                    channels = methods[index]
                        .channels
                        .toMutableList()
                        .apply { add(payment.type) }
                )
            }
        } else if (payment.category == PaymentType.CSTORE) {
            methods.add(
                PaymentMethod(
                    type = payment.type,
                    channels = emptyList()
                )
            )
        } else if (payment.type == PaymentType.QRIS) {
            if (payment.acquirer == PaymentType.SHOPEEPAY) {
                methods.add(
                    PaymentMethod(
                        type = PaymentType.SHOPEEPAY_QRIS,
                        channels = emptyList()
                    )
                )
            }
        } else {
            methods.add(
                PaymentMethod(
                    type = payment.type,
                    channels = if (payment.type == PaymentType.UOB_EZPAY) {
                        payment.mode
                    } else {
                        emptyList()
                    }
                )
            )
        }
    }

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
            cardTokenRequestBuilder.withClientKey(clientKey)
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
        callback: Callback<BinResponse>
    ) {
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

    @SuppressLint("CheckResult")
    fun getBankPoint(
        snapToken: String,
        cardToken: String,
        grossAmount: Double,
        callback: Callback<BankPointResponse>
    ) {
        try {
            snapRepository.getBankPoint(snapToken, cardToken, grossAmount)
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