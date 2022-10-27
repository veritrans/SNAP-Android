package com.midtrans.sdk.uikit.internal.base

import androidx.lifecycle.ViewModel
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.internal.analytics.PageName
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

internal open class BaseViewModel(
    snapCore: SnapCore
) : ViewModel() {
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val eventAnalytics = snapCore.getEventAnalytics()

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

    protected fun trackSnapChargeRequest(
        pageName: String,
        paymentMethodName: String,
        promoInfo: Map<String, String> = mapOf()
    ) {
        eventAnalytics.trackSnapChargeRequest(
            pageName = pageName,
            paymentMethodName = paymentMethodName,
            promoInfo = promoInfo
        )
    }

    protected fun trackSnapChargeResult(
        response: TransactionResponse,
        pageName: String
    ) {
        eventAnalytics.trackSnapChargeResult(
            pageName = pageName,
            transactionStatus = response.transactionStatus.orEmpty(),
            fraudStatus = response.fraudStatus.orEmpty(),
            currency = response.currency.orEmpty(),
            statusCode = response.statusCode.orEmpty(),
            transactionId = response.transactionId.orEmpty(),
            paymentMethodName = response.paymentType.orEmpty()
        )
    }
}