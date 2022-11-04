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
import com.midtrans.sdk.corekit.internal.analytics.PageName
import com.midtrans.sdk.uikit.internal.base.BaseViewModel
import com.midtrans.sdk.uikit.internal.util.DateTimeUtil
import com.midtrans.sdk.uikit.internal.util.DateTimeUtil.TIME_ZONE_UTC
import java.util.concurrent.TimeUnit
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
    val qrCodeUrlLiveData: LiveData<String> = _qrCodeUrlLiveData
    val deepLinkUrlLiveData: LiveData<String> = _deepLinkUrlLiveData
    val chargeResultLiveData: LiveData<TransactionResult> = _chargeResultLiveData
    var expiredTime = datetimeUtil.getCurrentMillis() + TimeUnit.MINUTES.toMillis(15)

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
                    result.run {
                        qrCodeUrl?.let { _qrCodeUrlLiveData.value = it }
                        qrisUrl?.let { _qrCodeUrlLiveData.value = it }
                        deeplinkUrl?.let { _deepLinkUrlLiveData.value = it }
                        gopayExpirationRaw?.let { expiredTime = parseTime(it) }
                        _chargeResultLiveData.value = TransactionResult(
                            status = transactionStatus.orEmpty(),
                            transactionId = transactionId.orEmpty(),
                            paymentType = paymentType.orEmpty()
                        )
                    }
                    trackSnapChargeResult(
                        response = result,
                        pageName = pageName
                    )
                }

                override fun onError(error: SnapError) {
                    // TODO: error dialog etc
                }
            }
        )
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

    fun getExpiredHour(): String = datetimeUtil.getExpiredHour(expiredTime)

    companion object {
        private const val DATE_FORMAT = "yyyy-MM-dd hh:mm Z"
    }
}