package com.midtrans.sdk.uikit.internal.presentation.directdebit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.exception.SnapError
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.api.requestbuilder.payment.DirectDebitPaymentRequestBuilder
import com.midtrans.sdk.uikit.internal.util.DateTimeUtil
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import javax.inject.Inject

internal class UobPaymentViewModel @Inject constructor(
    private val snapCore: SnapCore,
    private val dateTimeUtil: DateTimeUtil
): ViewModel() {
    private val transactionResponse = MutableLiveData<TransactionResponse>()
    private val transactionResult = MutableLiveData<Pair<String, String>>()

//    private var expiredTime = dateTimeUtil.plusDateBy(dateTimeUtil.getCurrentMillis(), 1) //TODO temporary is 24H, later get value from request snap if set

    fun getTransactionResponse(): LiveData<TransactionResponse> = transactionResponse
    fun getTransactionResult(): LiveData<Pair<String, String>> = transactionResult

    fun payUob(snapToken: String) {
        val builder = DirectDebitPaymentRequestBuilder()
            .withPaymentType(PaymentType.UOB_EZPAY)

        snapCore.pay(
            snapToken = snapToken,
            paymentRequestBuilder = builder,
            callback = object : Callback<TransactionResponse> {
                override fun onSuccess(result: TransactionResponse) {
                    transactionResponse.value = result
                }

                override fun onError(error: SnapError) {
                    Log.e("Uob Payment", error.javaClass.name)
                }
            }
        )
    }

    fun checkStatus(snapToken: String) {
        snapCore.getTransactionStatus(
            snapToken = snapToken,
            callback = object : Callback<TransactionResponse> {
                override fun onSuccess(result: TransactionResponse) {
                    transactionResult.value = Pair(getTransactionResult(result), result.transactionId.orEmpty())
                }

                override fun onError(error: SnapError) {
                    Log.e("Uob Payment Status", error.javaClass.name)
                }
            }
        )
    }

    private fun getTransactionResult(response: TransactionResponse): String {
        return response.transactionStatus?.let { status ->
            when {
                status.contains(UiKitConstants.STATUS_SUCCESS, true) -> UiKitConstants.STATUS_SUCCESS
                status.contains(UiKitConstants.STATUS_SETTLEMENT, true) -> UiKitConstants.STATUS_SETTLEMENT
                status.contains(UiKitConstants.STATUS_PENDING, true) -> UiKitConstants.STATUS_PENDING
                status.contains(UiKitConstants.STATUS_FAILED, true) -> UiKitConstants.STATUS_FAILED
                else -> UiKitConstants.STATUS_FAILED
            }
        } ?: ""
    }

    fun getExpiredHour(remainingTime: Long) = dateTimeUtil.getExpiredHour(remainingTime)
}
