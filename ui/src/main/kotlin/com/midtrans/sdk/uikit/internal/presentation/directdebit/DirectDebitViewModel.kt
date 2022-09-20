package com.midtrans.sdk.uikit.internal.presentation.directdebit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.exception.SnapError
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.api.requestbuilder.payment.DirectDebitPaymentRequestBuilder
import com.midtrans.sdk.uikit.internal.presentation.banktransfer.BankTransferDetailViewModel
import com.midtrans.sdk.uikit.internal.util.DateTimeUtil
import java.util.*
import javax.inject.Inject

internal class DirectDebitViewModel @Inject constructor(
    private val snapCore: SnapCore,
    private val dateTimeUtil: DateTimeUtil
): ViewModel() {
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

        snapCore.pay(
            snapToken = snapToken,
            paymentRequestBuilder = builder,
            callback = object : Callback<TransactionResponse> {
                override fun onSuccess(result: TransactionResponse) {
                    transactionResponse.value = result
                }

                override fun onError(error: SnapError) {
                    Log.e("DirectDebitPay", error.javaClass.name)
                    exception.value = error
                }
            }
        )
    }

    fun getExpiredHour() = dateTimeUtil.getExpiredHour(expiredTime)
}