package com.midtrans.sdk.uikit.internal.presentation.directdebit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.exception.SnapError
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.api.requestbuilder.payment.DirectDebitPaymentRequestBuilder
import com.midtrans.sdk.corekit.core.Logger
import com.midtrans.sdk.corekit.internal.analytics.PageName
import com.midtrans.sdk.uikit.internal.base.BaseViewModel
import com.midtrans.sdk.uikit.internal.util.DateTimeUtil
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import java.util.*
import javax.inject.Inject

internal class UobPaymentViewModel @Inject constructor(
    private val snapCore: SnapCore,
    private val dateTimeUtil: DateTimeUtil
): BaseViewModel() {

    init {
        eventAnalytics = snapCore.getEventAnalytics()
    }

    private val transactionResponse = MutableLiveData<TransactionResponse>()
    private val transactionResult = MutableLiveData<Pair<String, String>>()

    private var expireTimeInMillis = 0L

    fun getTransactionResponse(): LiveData<TransactionResponse> = transactionResponse
    fun getTransactionResult(): LiveData<Pair<String, String>> = transactionResult

    fun payUob(snapToken: String) {
        val builder = DirectDebitPaymentRequestBuilder()
            .withPaymentType(PaymentType.UOB_EZPAY)

        trackSnapChargeRequest(
            pageName = PageName.UOB_PAGE,
            paymentMethodName = PaymentType.UOB_EZPAY
        )
        snapCore.pay(
            snapToken = snapToken,
            paymentRequestBuilder = builder,
            callback = object : Callback<TransactionResponse> {
                override fun onSuccess(result: TransactionResponse) {
                    Logger.d("UOB Payment pay successfully")
                    transactionResponse.value = result
                    trackSnapChargeResult(
                        response = result,
                        pageName = PageName.UOB_PAGE,
                        paymentMethodName = PaymentType.UOB_EZPAY
                    )
                    trackErrorStatusCode(
                        pageName = PageName.UOB_PAGE,
                        paymentMethodName = PaymentType.UOB_EZPAY,
                        errorMessage = result.statusMessage.orEmpty(),
                        statusCode = result.statusCode.orEmpty()
                    )
                }

                override fun onError(error: SnapError) {
                    Logger.e("UOB Payment error pay")
                    trackSnapError(
                        pageName = PageName.UOB_PAGE,
                        paymentMethodName = PaymentType.UOB_EZPAY,
                        error = error
                    )
                }
            }
        )
    }

    fun checkStatus(snapToken: String) {
        snapCore.getTransactionStatus(
            snapToken = snapToken,
            callback = object : Callback<TransactionResponse> {
                override fun onSuccess(result: TransactionResponse) {
                    Logger.d("Uob Payment get transaction status succesfully")
                    transactionResult.value = Pair(result.statusCode.orEmpty(), result.transactionId.orEmpty())
                    trackErrorStatusCode(
                        pageName = PageName.UOB_PAGE,
                        paymentMethodName = PaymentType.UOB_EZPAY,
                        errorMessage = result.statusMessage.orEmpty(),
                        statusCode = result.statusCode.orEmpty()
                    )
                }

                override fun onError(error: SnapError) {
                    Logger.e("Uob Payment get transaction status succesfully")
                    trackSnapError(
                        pageName = PageName.UOB_PAGE,
                        paymentMethodName = PaymentType.UOB_EZPAY,
                        error = error
                    )
                }
            }
        )
    }

    private fun getTransactionStatus(response: TransactionResponse): String {
        return response.transactionStatus?.let { status ->
            when {
                status.contains(UiKitConstants.STATUS_SUCCESS, true) -> UiKitConstants.STATUS_SUCCESS
                status.contains(UiKitConstants.STATUS_SETTLEMENT, true) -> UiKitConstants.STATUS_SETTLEMENT
                status.contains(UiKitConstants.STATUS_PENDING, true) -> UiKitConstants.STATUS_PENDING
                status.contains(UiKitConstants.STATUS_FAILED, true) -> UiKitConstants.STATUS_FAILED
                else -> UiKitConstants.STATUS_FAILED
            }
        } ?: UiKitConstants.STATUS_FAILED
    }

    fun trackSnapButtonClicked(ctaName: String) {
        trackCtaClicked(
            ctaName = ctaName,
            paymentMethodName = PaymentType.UOB_EZPAY,
            pageName = PageName.UOB_PAGE
        )
    }

    fun trackHowToPayClicked() {
        trackHowToPayViewed(
            paymentMethodName = PaymentType.UOB_EZPAY,
            pageName = PageName.UOB_PAGE
        )
    }

    fun trackOpenDeeplink() {
        trackOpenDeeplink(
            pageName = PageName.UOB_PAGE,
            paymentMethodName = PaymentType.UOB_EZPAY
        )
    }

    fun trackOrderDetailsViewed() {
        trackOrderDetailsViewed(
            pageName = PageName.UOB_PAGE,
            paymentMethodName = PaymentType.UOB_EZPAY
        )
    }

    fun trackPageViewed(stepNumber: Int) {
        trackPageViewed(
            pageName = PageName.UOB_PAGE,
            paymentMethodName = PaymentType.UOB_EZPAY,
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
        private const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss Z"
        private const val TIME_FORMAT = "%02d:%02d:%02d"
        private val timeZoneUtc = TimeZone.getTimeZone("UTC")
    }
}
