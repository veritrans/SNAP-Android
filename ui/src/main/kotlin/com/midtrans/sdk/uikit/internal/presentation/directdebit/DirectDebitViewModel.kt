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
import javax.inject.Inject

internal class DirectDebitViewModel @Inject constructor(
    private val snapCore: SnapCore
): ViewModel() {
    private val redirectUrl = MutableLiveData<String>()

    fun getRedirectUrl(): LiveData<String> = redirectUrl

    fun payDirectDebit(
        snapToken: String,
        paymentType: String,
        userId: String?
    ) {
        val builder = DirectDebitPaymentRequestBuilder()
            .withPaymentType(paymentType)
            .withKlikBcaUserId(userId)

        SnapCore.getInstance()?.pay(//TODO integrate with DI from pak wahyu
            snapToken = snapToken,
            paymentRequestBuilder = builder,
            callback = object : Callback<TransactionResponse> {
                override fun onSuccess(result: TransactionResponse) {
                    Log.d("DirectDebitPay", result.redirectUrl.orEmpty())
                    redirectUrl.value = result.redirectUrl
                }

                override fun onError(error: SnapError) {
                    Log.e("DirectDebitPay", error.javaClass.name)
                }
            }
        )
    }
}