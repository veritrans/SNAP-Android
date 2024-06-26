package com.midtrans.sdk.uikit.internal.base

import androidx.lifecycle.ViewModel
import com.midtrans.sdk.corekit.api.exception.SnapError
import com.midtrans.sdk.corekit.api.model.BinData
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.internal.analytics.EventAnalytics
import com.midtrans.sdk.corekit.internal.analytics.PageName
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
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

    protected fun trackCreditDebitCardExbinResponse(binData: BinData?) {
        eventAnalytics?.trackSnapExbinResponse(
            pageName = PageName.CREDIT_DEBIT_CARD_PAGE,
            paymentMethodName = PaymentType.CREDIT_CARD,
            registrationRequired = binData?.registrationRequired,
            countryCode = binData?.countryCode,
            channel = binData?.channel,
            brand = binData?.brand,
            binType = binData?.binType,
            binClass = binData?.binClass,
            bin = binData?.bin,
            bankCode = binData?.bankCode,
        )
    }

    protected fun trackPageClosed(pageName: String) {
        eventAnalytics?.trackSnapPageClosed(pageName)
    }

    protected fun trackOpenDeeplink(
        pageName: String,
        paymentMethodName: String
    ) {
        eventAnalytics?.trackSnapOpenDeeplink(
            pageName = pageName,
            paymentMethodName = paymentMethodName
        )
    }

    protected fun trackCreditCard3DsResult(
        transactionStatus: String?,
        cardType: String?,
        bank: String?,
        threeDsVersion: String?,
        channelResponseCode: String?,
        eci: String?
    ) {
        eventAnalytics?.trackSnap3DsResult(
            transactionStatus = transactionStatus,
            cardType = cardType,
            bank = bank,
            threeDsVersion = threeDsVersion,
            channelResponseCode = channelResponseCode,
            eci = eci,
            paymentMethodName = PaymentType.CREDIT_CARD
        )
    }

    protected fun trackOrderDetailsViewed(
        pageName: String? = null,
        paymentMethodName: String? = null,
        transactionId: String? = null,
        netAmount: String? = null
    ) {
        eventAnalytics?.trackSnapOrderDetailsViewed(
            pageName = pageName,
            paymentMethodName = paymentMethodName,
            transactionId = transactionId,
            netAmount = netAmount
        )
    }

    protected fun trackPageViewed(
        pageName: String? = null,
        paymentMethodName: String? = null,
        transactionId: String? = null,
        stepNumber: String? = null
    ) {
        eventAnalytics?.trackSnapPageViewed(
            pageName = pageName,
            paymentMethodName = paymentMethodName,
            transactionId = transactionId,
            stepNumber = stepNumber
        )
    }

    protected fun trackCreditCardTokenizationResult(
        statusCode: String,
        tokenId: String
    ) {
        eventAnalytics?.trackSnapTokenizationResult(
            pageName = PageName.CREDIT_DEBIT_CARD_PAGE,
            paymentMethodName = PaymentType.CREDIT_CARD,
            statusCode = statusCode,
            tokenId = tokenId
        )
    }

    protected fun trackCreditCardCustomerDataInput(
        email: String?,
        phoneNumber: String?,
        displayField: String
    ) {
        eventAnalytics?.trackSnapCustomerDataInput(
            pageName = PageName.CREDIT_DEBIT_CARD_PAGE,
            paymentMethodName = PaymentType.CREDIT_CARD,
            email = email,
            phoneNumber = phoneNumber,
            displayField = displayField
        )
    }

    protected fun trackSnapError(
        pageName: String,
        paymentMethodName: String,
        error: SnapError
    ) {
        eventAnalytics?.trackSnapError(
            pageName = pageName,
            paymentMethodName = paymentMethodName,
            statusCode = error.getHttpStatusCode()?.toString(),
            errorMessage = error.getErrorInformation()
        )
    }

    protected fun trackErrorStatusCode(
        pageName: String,
        paymentMethodName: String,
        errorMessage: String,
        statusCode: String
    ) {
        if (!isChargeResponseSuccess(statusCode)) {
            eventAnalytics?.trackSnapError(
                pageName = pageName,
                paymentMethodName = paymentMethodName,
                statusCode = statusCode,
                errorMessage = errorMessage
            )
        }
    }

    protected fun isChargeResponseSuccess(statusCode: String) =
        statusCode == UiKitConstants.STATUS_CODE_200 || statusCode == UiKitConstants.STATUS_CODE_201

    protected fun trackSnapNotice(
        pageName: String,
        paymentMethodName: String,
        statusText: String,
        noticeMessage: String? = null
    ) {
        eventAnalytics?.trackSnapNotice(
            pageName = pageName,
            paymentMethodName = paymentMethodName,
            statusText = statusText,
            noticeMessage = noticeMessage
        )
    }
}