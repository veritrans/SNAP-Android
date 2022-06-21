package com.midtrans.sdk.sample

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.exception.SnapError
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.model.TransactionResponse

class SampleViewModel : ViewModel() {
    var helloLiveData = MutableLiveData<String>()
    private val coreKit: SnapCore = SnapCore.getInstance()!!

    fun getHelloFromSnap() {
        helloLiveData.value = coreKit.hello()
    }

    fun chargeBniVa(snapToken: String) {
        coreKit.paymentUsingBankTransfer(
            snapToken = snapToken,
            paymentType = PaymentType.BCA_VA,
            email = "bayar@bayar.com",
            callback = object : Callback<TransactionResponse> {
                override fun onSuccess(result: TransactionResponse) {}
                override fun onError(error: SnapError) {}
            }
        )
    }
}