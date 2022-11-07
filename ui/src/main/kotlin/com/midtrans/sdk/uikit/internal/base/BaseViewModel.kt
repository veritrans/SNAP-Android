package com.midtrans.sdk.uikit.internal.base

import androidx.lifecycle.ViewModel
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.internal.analytics.EventAnalytics
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

internal open class BaseViewModel : ViewModel() {

    protected var eventAnalytics: EventAnalytics? = null

    private var requestTime = 0L
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    protected fun dispose() {
        compositeDisposable.dispose()
        compositeDisposable = CompositeDisposable()
    }

    protected fun observe(execute: () -> Disposable) {
        compositeDisposable.add(execute())
    }

    private fun initRequestTime() {
        requestTime = System.currentTimeMillis()
    }

    private fun getResponseTime(): String {
        return (System.currentTimeMillis() - requestTime).toString()
    }

    protected fun trackSnapChargeRequest(
        pageName: String,
        paymentMethodName: String,
        promoName: String? = null,
        promoAmount: String? = null,
        promoId: String? = null,
        creditCardPoint: String? = null
    ) {
        eventAnalytics?.trackSnapChargeRequest(
            pageName = pageName,
            paymentMethodName = paymentMethodName,
            promoName = promoName,
            promoAmount = promoAmount,
            promoId = promoId,
            creditCardPoint = creditCardPoint
        )?.apply {
            initRequestTime()
        }
    }

    protected fun trackSnapChargeResult(
        response: TransactionResponse,
        pageName: String,
        paymentMethodName: String
    ) {
        eventAnalytics?.trackSnapChargeResult(
            pageName = pageName,
            transactionStatus = response.transactionStatus.orEmpty(),
            fraudStatus = response.fraudStatus.orEmpty(),
            currency = response.currency.orEmpty(),
            statusCode = response.statusCode.orEmpty(),
            transactionId = response.transactionId.orEmpty(),
            paymentMethodName = paymentMethodName,
            responseTime = getResponseTime(),
            bank = response.bank,
            channelResponseCode = response.channelResponseCode,
            channelResponseMessage = response.channelResponseMessage,
            cardType = response.cardType,
            threeDsVersion = response.threeDsVersion
        )
    }

    protected fun trackCtaClicked(
        ctaName: String,
        pageName: String,
        paymentMethodName: String
    ) {
        eventAnalytics?.trackSnapCtaClicked(
            ctaName = ctaName,
            pageName = pageName,
            paymentMethodName = paymentMethodName
        )
    }

    protected fun trackHowToPayViewed(
        pageName: String,
        paymentMethodName: String
    ) {
        eventAnalytics?.trackSnapHowToPayViewed(
            pageName = pageName,
            paymentMethodName = paymentMethodName
        )
    }
}