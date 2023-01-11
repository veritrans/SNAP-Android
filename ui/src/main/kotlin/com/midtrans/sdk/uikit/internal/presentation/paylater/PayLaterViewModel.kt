package com.midtrans.sdk.uikit.internal.presentation.paylater

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.exception.SnapError
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.api.requestbuilder.payment.PayLaterPaymentRequestBuilder
import com.midtrans.sdk.corekit.internal.analytics.PageName
import com.midtrans.sdk.uikit.internal.base.BaseViewModel
import com.midtrans.sdk.uikit.internal.util.DateTimeUtil
import java.util.*
import javax.inject.Inject

internal class PayLaterViewModel @Inject constructor(
    private val snapCore: SnapCore,
    private val dateTimeUtil: DateTimeUtil
): BaseViewModel() {

    init {
        eventAnalytics = snapCore.getEventAnalytics()
    }

    private val _transactionResponseLiveData = MutableLiveData<TransactionResponse>()
    private var expireTimeInMillis = 0L

    val transactionResponseLiveData: LiveData<TransactionResponse> = _transactionResponseLiveData
    private val _transactionId = MutableLiveData<String>()
    val transactionId: LiveData<String> = _transactionId

    fun checkStatus(snapToken: String) {
        snapCore.getTransactionStatus(
            snapToken = snapToken,
            callback = object : Callback<TransactionResponse> {
                override fun onSuccess(result: TransactionResponse) {
                    result.run {
                        _transactionId.value = transactionId.orEmpty()
                        trackErrorStatusCode(
                            pageName = PageName.AKULAKU_PAGE,
                            paymentMethodName = PaymentType.AKULAKU,
                            errorMessage = result.statusMessage.orEmpty(),
                            statusCode = result.statusCode.orEmpty()
                        )
                    }
                }
                override fun onError(error: SnapError) {
                    trackSnapError(
                        pageName = PageName.AKULAKU_PAGE,
                        paymentMethodName = PaymentType.AKULAKU,
                        error = error
                    )
                }
            }
        )
    }

    fun payPayLater(
        snapToken: String,
        paymentType: String
    ) {
        val builder = PayLaterPaymentRequestBuilder()
            .withPaymentType(paymentType)

        trackSnapChargeRequest(
            pageName = PageName.AKULAKU_PAGE,
            paymentMethodName = paymentType
        )
        snapCore.pay(
            snapToken = snapToken,
            paymentRequestBuilder = builder,
            callback = object : Callback<TransactionResponse> {
                override fun onSuccess(result: TransactionResponse) {
                    trackSnapChargeResult(
                        response = result,
                        pageName = PageName.AKULAKU_PAGE,
                        paymentMethodName = PaymentType.AKULAKU
                    )
                    trackErrorStatusCode(
                        pageName = PageName.AKULAKU_PAGE,
                        paymentMethodName = PaymentType.AKULAKU,
                        statusCode = result.statusCode.orEmpty(),
                        errorMessage = result.statusMessage.orEmpty()
                    )
                    _transactionResponseLiveData.value = result
                }

                override fun onError(error: SnapError) {
                    //TODO need to handle error dialog?
                    trackSnapError(
                        pageName = PageName.AKULAKU_PAGE,
                        paymentMethodName = PaymentType.AKULAKU,
                        error = error
                    )
                }
            }
        )
    }

    fun getUsedToken(result: TransactionResponse) {
        _transactionResponseLiveData.value = result
    }

    fun trackSnapButtonClicked(
        ctaName: String,
        paymentType: String
    ) {
        trackCtaClicked(
            ctaName = ctaName,
            paymentMethodName = paymentType,
            pageName = PageName.AKULAKU_PAGE
        )
    }

    fun trackHowToPayClicked(paymentType: String) {
        trackHowToPayViewed(
            paymentMethodName = paymentType,
            pageName = PageName.AKULAKU_PAGE
        )
    }

    fun trackOpenWebView(paymentType: String) {
        trackOpenDeeplink(
            pageName = PageName.AKULAKU_PAGE,
            paymentMethodName = paymentType
        )
    }

    fun trackOrderDetailsViewed(paymentType: String) {
        trackOrderDetailsViewed(
            pageName = PageName.AKULAKU_PAGE,
            paymentMethodName = paymentType
        )
    }

    fun trackPageViewed(
        paymentType: String,
        stepNumber: Int
    ) {
        trackPageViewed(
            pageName = PageName.AKULAKU_PAGE,
            paymentMethodName = paymentType,
            stepNumber = stepNumber.toString()
        )
    }

    private fun parseTime(dateString: String): Long {
        val date = dateTimeUtil.getDate(
            date = dateString,
            dateFormat = DATE_FORMAT,
            timeZone = timeZoneUtc
        )
        return date.time
    }

    fun setExpiryTime(expireTime: String?) {
        expireTime?.let {
            expireTimeInMillis = parseTime(it)
        }
    }

    fun getExpiredHour() : String {
        val duration = dateTimeUtil.getDuration(
            dateTimeUtil.getTimeDiffInMillis(
                dateTimeUtil.getCurrentMillis(),
                expireTimeInMillis
            )
        )
        return String.format(
            TIME_FORMAT,
            duration.toHours(),
            duration.seconds % 3600 / 60,
            duration.seconds % 60
        )
    }

    companion object {
        private const val DATE_FORMAT = "yyyy-MM-dd hh:mm:ss Z"
        private const val TIME_FORMAT = "%02d:%02d:%02d"
        private val timeZoneUtc = TimeZone.getTimeZone("UTC")
    }
}