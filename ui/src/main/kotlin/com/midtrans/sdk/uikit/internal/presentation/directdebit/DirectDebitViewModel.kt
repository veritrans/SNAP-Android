package com.midtrans.sdk.uikit.internal.presentation.directdebit

import android.util.Log
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
import com.midtrans.sdk.uikit.internal.util.DateTimeUtil
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

    private var expiredTime = dateTimeUtil.plusDateBy(dateTimeUtil.getCurrentMillis(), 1) //TODO temporary is 24H, later get value from request snap if set

    fun getTransactionResponse(): LiveData<TransactionResponse> = transactionResponse
    fun getException(): LiveData<SnapError> = exception

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
                        pageName = pageName
                    )
                }

                override fun onError(error: SnapError) {
                    Log.e("DirectDebitPay", error.javaClass.name)
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

    fun getExpiredHour() = dateTimeUtil.getExpiredHour(expiredTime)
}