package com.midtrans.sdk.uikit.internal.presentation.banktransfer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.exception.SnapError
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.api.requestbuilder.payment.BankTransferPaymentRequestBuilder
import javax.inject.Inject

class BankTransferDetailViewModel @Inject constructor(
    private val snapCore: SnapCore
) : ViewModel() {

    val vaNumberLiveData = MutableLiveData<String>()
    val companyCodeLiveData = MutableLiveData<String>()
    val billingNumberLiveData = MutableLiveData<String>()
    val bankCodeLiveData = MutableLiveData<String>()

    fun chargeBankTransfer(snapToken: String, @PaymentType.Def paymentType: String, customerEmail: String? = null) {
        val requestBuilder = BankTransferPaymentRequestBuilder().withPaymentType(paymentType)
        customerEmail?.let {
            requestBuilder.withCustomerEmail(it)
        }
        snapCore.pay(
            snapToken = snapToken,
            paymentRequestBuilder = requestBuilder,
            callback = object : Callback<TransactionResponse> {
                override fun onSuccess(result: TransactionResponse) {
                    result.run {
                        bcaVaNumber?.let { vaNumberLiveData.value = it }
                        bniVaNumber?.let { vaNumberLiveData.value = it }
                        briVaNumber?.let { vaNumberLiveData.value = it }
                        permataVaNumber?.let { vaNumberLiveData.value = it }
                        billerCode?.let { companyCodeLiveData.value = it }
                        billKey?.let { billingNumberLiveData.value = it }
                    }
                }

                override fun onError(error: SnapError) {
                    TODO("Not yet implemented")
                }
            }
        )
    }
}