package com.midtrans.sdk.uikit.internal.presentation.paylater

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.exception.SnapError
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.api.requestbuilder.payment.PayLaterPaymentRequestBuilder
import com.midtrans.sdk.uikit.internal.util.DateTimeUtil
import javax.inject.Inject

internal class PayLaterViewModel @Inject constructor(
    private val snapCore: SnapCore,
    private val dateTimeUtil: DateTimeUtil
): ViewModel() {
    private val _transactionResponseLiveData = MutableLiveData<TransactionResponse>()
    private var expiredTime = dateTimeUtil.plusDateBy(dateTimeUtil.getCurrentMillis(), 1) //TODO later get value from request snap if set

    val transactionResponseLiveData: LiveData<TransactionResponse> = _transactionResponseLiveData

    fun payPayLater(
        snapToken: String,
        paymentType: String
    ) {
        val builder = PayLaterPaymentRequestBuilder()
            .withPaymentType(paymentType)

        snapCore.pay(
            snapToken = snapToken,
            paymentRequestBuilder = builder,
            callback = object : Callback<TransactionResponse> {
                override fun onSuccess(result: TransactionResponse) {
                    _transactionResponseLiveData.value = result
                }

                override fun onError(error: SnapError) {
                    TODO("Not yet implemented")
                }
            }
        )
    }

    fun getExpiredHour() = dateTimeUtil.getExpiredHour(expiredTime)
}