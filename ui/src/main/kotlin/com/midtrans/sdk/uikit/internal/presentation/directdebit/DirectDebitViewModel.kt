package com.midtrans.sdk.uikit.internal.presentation.directdebit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.exception.SnapError
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.api.requestbuilder.payment.DirectDebitPaymentRequestBuilder
import com.midtrans.sdk.corekit.internal.analytics.PageName
import com.midtrans.sdk.uikit.internal.base.BaseViewModel
import com.midtrans.sdk.uikit.internal.presentation.creditcard.CreditCardViewModel
import com.midtrans.sdk.uikit.internal.util.DateTimeUtil
import java.util.*
import javax.inject.Inject

internal class DirectDebitViewModel @Inject constructor(
    private val snapCore: SnapCore,
    private val dateTimeUtil: DateTimeUtil
): BaseViewModel() {

    init {
        eventAnalytics = snapCore.getEventAnalytics()
    }

    private val transactionResponse = MutableLiveData<TransactionResponse>()
    private val exception = MutableLiveData<SnapError>()

    private var expireTimeInMillis = 0L

    fun getTransactionResponse(): LiveData<TransactionResponse> = transactionResponse
    fun getException(): LiveData<SnapError> = exception //TODO what is expected in direct debit activity
    private val _transactionId = MutableLiveData<String>()
    val transactionId: LiveData<String> = _transactionId

    fun checkStatus(snapToken: String, paymentType: String) {
        snapCore.getTransactionStatus(
            snapToken = snapToken,
            callback = object : Callback<TransactionResponse> {
                override fun onSuccess(result: TransactionResponse) {
                    result.run {
                        _transactionId.value = transactionId.orEmpty()
                        trackErrorStatusCode(
                            pageName = getPageName(paymentType),
                            paymentMethodName = paymentType,
                            errorMessage = result.statusMessage.orEmpty(),
                            statusCode = result.statusCode.orEmpty()
                        )
                    }
                }
                override fun onError(error: SnapError) {
                    trackSnapError(
                        pageName = getPageName(paymentType),
                        paymentMethodName = paymentType,
                        error = error
                    )
                }
            }
        )
    }

    fun payDirectDebit(
        snapToken: String,
        paymentType: String,
        userId: String?
    ) {
        val builder = DirectDebitPaymentRequestBuilder()
            .withPaymentType(paymentType)
            .withKlikBcaUserId(userId.orEmpty())
        val pageName = getPageName(paymentType)

        trackSnapChargeRequest(
            pageName = pageName,
            paymentMethodName = paymentType
        )
        snapCore.pay(
            snapToken = snapToken,
            paymentRequestBuilder = builder,
            callback = object : Callback<TransactionResponse> {
                override fun onSuccess(result: TransactionResponse) {
                    transactionResponse.value = result
                    trackSnapChargeResult(
                        response = result,
                        pageName = pageName,
                        paymentMethodName = paymentType
                    )
                    trackErrorStatusCode(
                        pageName = getPageName(paymentType),
                        paymentMethodName = paymentType,
                        errorMessage = result.statusMessage.orEmpty(),
                        statusCode = result.statusCode.orEmpty()
                    )
                }

                override fun onError(error: SnapError) {
                    trackSnapError(
                        pageName = getPageName(paymentType),
                        paymentMethodName = paymentType,
                        error = error
                    )
                    exception.value = error
                }
            }
        )
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

    fun trackOpenRedirectionUrl(paymentType: String) {
        trackOpenDeeplink(
            pageName = getPageName(paymentType),
            paymentMethodName = paymentType
        )
    }

    fun trackOrderDetailsViewed(paymentType: String) {
        trackOrderDetailsViewed(
            pageName = getPageName(paymentType),
            paymentMethodName = paymentType
        )
    }

    fun trackPageViewed(
        paymentType: String,
        stepNumber: Int
    ) {
        trackPageViewed(
            pageName = getPageName(paymentType),
            paymentMethodName = paymentType,
            stepNumber = stepNumber.toString()
        )
    }

    fun trackSnapNotice(
        paymentType: String,
        statusText: String
    ) {
        trackSnapNotice(
            pageName = getPageName(paymentType),
            paymentMethodName = paymentType,
            statusText = statusText
        )
    }

    private fun getPageName(paymentType: String): String {
        return when(paymentType) {
            PaymentType.KLIK_BCA -> PageName.KLIK_BCA_PAGE
            PaymentType.BCA_KLIKPAY-> PageName.BCA_KLIK_PAY_PAGE
            PaymentType.BRI_EPAY -> PageName.BRIMO_PAGE
            PaymentType.DANAMON_ONLINE -> PageName.DANAMON_ONLINE_PAGE
            PaymentType.CIMB_CLICKS -> PageName.OCTO_CLICKS_PAGE
            else -> ""
        }
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
