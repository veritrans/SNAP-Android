package com.midtrans.sdk.uikit.internal.presentation.ewallet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.exception.SnapError
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.api.model.TransactionResult
import com.midtrans.sdk.corekit.api.requestbuilder.payment.EWalletPaymentRequestBuilder
import com.midtrans.sdk.corekit.core.Logger
import com.midtrans.sdk.corekit.internal.analytics.PageName
import com.midtrans.sdk.uikit.internal.base.BaseViewModel
import com.midtrans.sdk.uikit.internal.util.DateTimeUtil
import com.midtrans.sdk.uikit.internal.util.DateTimeUtil.TIME_ZONE_UTC
import javax.inject.Inject

internal class WalletViewModel @Inject constructor(
    private val snapCore: SnapCore,
    private val datetimeUtil: DateTimeUtil
) : BaseViewModel() {

    init {
        eventAnalytics = snapCore.getEventAnalytics()
    }

    private val _qrCodeUrlLiveData = MutableLiveData<String>()
    private val _deepLinkUrlLiveData = MutableLiveData<String>()
    private val _chargeResultLiveData = MutableLiveData<TransactionResult>()
    private var _transactionId: String? = null
    private val _isQrChargeErrorLiveData = MutableLiveData<Boolean>()
    private val _isExpired = MutableLiveData<Boolean>()

    val qrCodeUrlLiveData: LiveData<String> = _qrCodeUrlLiveData
    val deepLinkUrlLiveData: LiveData<String> = _deepLinkUrlLiveData
    val chargeResultLiveData: LiveData<TransactionResult> = _chargeResultLiveData
    var expiredTime = 0L
    val isQrChargeErrorLiveData: LiveData<Boolean> = _isQrChargeErrorLiveData
    val isExpired: LiveData<Boolean> = _isExpired

    fun chargeQrPayment(
        snapToken: String,
        @PaymentType.Def paymentType: String
    ) {
        val pageName = getPageName(paymentType)
        val requestBuilder = EWalletPaymentRequestBuilder().withPaymentType(paymentType)
        trackSnapChargeRequest(
            pageName = pageName,
            paymentMethodName = paymentType
        )

        snapCore.pay(
            snapToken = snapToken,
            paymentRequestBuilder = requestBuilder,
            callback = object : Callback<TransactionResponse> {
                override fun onSuccess(result: TransactionResponse) {
                    Logger.d("Wallet pay successfully")
                    _isExpired.value = result.validationMessages?.get(0)?.contains("expired") == true
                    result.run {
                        _transactionId = transactionId
                        qrCodeUrl?.let { _qrCodeUrlLiveData.value = it }
                        qrisUrl?.let { _qrCodeUrlLiveData.value = it }
                        deeplinkUrl?.let { _deepLinkUrlLiveData.value = it }
                        gopayExpirationRaw?.let { expiredTime = parseTime(it) }
                        shopeepayExpirationRaw?.let { expiredTime = parseTime(it) }
                        qrisExpirationRaw?.let { expiredTime = parseTime(it) }
                        _chargeResultLiveData.value = TransactionResult(
                            status = transactionStatus.orEmpty(),
                            transactionId = transactionId.orEmpty(),
                            paymentType = paymentType
                        )
                    }
                    trackSnapChargeResult(
                        response = result,
                        pageName = pageName,
                        paymentMethodName = paymentType
                    )
                    trackErrorStatusCode(
                        pageName = pageName,
                        paymentMethodName = paymentType,
                        statusCode = result.statusCode.orEmpty(),
                        errorMessage = result.statusMessage.orEmpty()
                    )
                    _isQrChargeErrorLiveData.value = !isChargeResponseSuccess(result.statusCode.orEmpty())
                }

                override fun onError(error: SnapError) {
                    Logger.e("Wallet error pay")
                    // TODO: error dialog etc
                    trackSnapError(
                        pageName = pageName,
                        paymentMethodName = paymentType,
                        error = error
                    )
                    _isQrChargeErrorLiveData.value = true
                }
            }
        )
    }

    fun getUsedToken(result: TransactionResponse) {
        result.qrCodeUrl?.let { _qrCodeUrlLiveData.value = it }
        result.qrisUrl?.let { _qrCodeUrlLiveData.value = it }
        result.deeplinkUrl?.let { _deepLinkUrlLiveData.value = it }
        result.gopayExpirationRaw?.let { expiredTime = parseTime(it) }
        result.shopeepayExpirationRaw?.let { expiredTime = parseTime(it) }
        result.qrisUrl?.let { expiredTime = parseTime(it) }
    }

    private fun parseTime(dateString: String): Long {
        val date = datetimeUtil.getDate(
            date = dateString,
            dateFormat = DATE_FORMAT,
            timeZone = TIME_ZONE_UTC
        )
        return date.time
    }

    private fun getPageName(paymentType: String): String {
        return when (paymentType) {
            PaymentType.GOPAY -> PageName.GOPAY_DEEPLINK_PAGE
            PaymentType.GOPAY_QRIS -> PageName.GOPAY_QR_PAGE
            PaymentType.SHOPEEPAY -> PageName.SHOPEEPAY_DEEPLINK_PAGE
            PaymentType.SHOPEEPAY_QRIS -> PageName.SHOPEEPAY_QR_PAGE
            else -> ""
        }
    }

    fun trackSnapButtonClicked(
        ctaName: String,
        paymentType: String
    ) {
        trackCtaClicked(
            ctaName = ctaName,
            paymentMethodName = paymentType,
            pageName = getPageName(paymentType)
        )
    }

    fun trackHowToPayClicked(paymentType: String) {
        trackHowToPayViewed(
            paymentMethodName = paymentType,
            pageName = getPageName(paymentType)
        )
    }

    fun trackOpenDeeplink(paymentType: String) {
        trackOpenDeeplink(
            pageName = getPageName(paymentType),
            paymentMethodName = paymentType
        )
    }

    fun trackOrderDetailsViewed(paymentType: String) {
        trackOrderDetailsViewed(
            pageName = getPageName(paymentType),
            paymentMethodName = paymentType,
            transactionId = _transactionId
        )
    }

    fun trackPageViewed(
        paymentType: String,
        stepNumber: Int
    ) {
        trackPageViewed(
            pageName = getPageName(paymentType),
            paymentMethodName = paymentType,
            transactionId = _transactionId,
            stepNumber = stepNumber.toString()
        )
    }

    fun trackReloadClicked(paymentType: String) {
        eventAnalytics?.trackSnapPaymentNumberButtonRetried(
            paymentMethodName = paymentType,
            pageName = getPageName(paymentType)
        )
    }

    fun getExpiredHour(): String = datetimeUtil.getExpiredHour(expiredTime)

    fun setDefaultExpiryTime(expiryTime: String?) {
        expiryTime?.let {
            expiredTime = parseTime(it)
        }
    }

    companion object {
        private const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss Z"
    }
}