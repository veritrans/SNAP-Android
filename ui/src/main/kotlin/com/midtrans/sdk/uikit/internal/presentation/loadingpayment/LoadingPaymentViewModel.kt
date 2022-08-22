package com.midtrans.sdk.uikit.internal.presentation.loadingpayment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.exception.SnapError
import com.midtrans.sdk.corekit.api.model.CreditCard
import com.midtrans.sdk.corekit.api.model.CustomerDetails
import com.midtrans.sdk.corekit.api.model.Expiry
import com.midtrans.sdk.corekit.api.model.GopayPaymentCallback
import com.midtrans.sdk.corekit.api.model.ItemDetails
import com.midtrans.sdk.corekit.api.model.PaymentCallback
import com.midtrans.sdk.corekit.api.model.PaymentOption
import com.midtrans.sdk.corekit.api.model.PromoRequest
import com.midtrans.sdk.corekit.api.model.SnapTransactionDetail
import com.midtrans.sdk.corekit.api.requestbuilder.snaptoken.SnapTokenRequestBuilder
import com.midtrans.sdk.corekit.internal.network.model.request.BankTransferRequest

//TODO will add UT and SnapCore manager after DI is setup
class LoadingPaymentViewModel : ViewModel() {
    private val _paymentOptionLiveData = MutableLiveData<PaymentOption>()
    private val _error = MutableLiveData<SnapError>()

    fun getPaymentOptionLiveData(): LiveData<PaymentOption> = _paymentOptionLiveData
    fun getErrorLiveData(): LiveData<SnapError> = _error

    fun getPaymentOption(
        transactionDetails: SnapTransactionDetail,
        snapToken: String?,
        customerDetails: CustomerDetails?,
        itemDetails: List<ItemDetails>?,
        creditCard: CreditCard?,
        userId: String?,
        permataVa: BankTransferRequest?,
        bcaVa: BankTransferRequest?,
        bniVa: BankTransferRequest?,
        briVa: BankTransferRequest?,
        enabledPayments: List<String>?,
        expiry: Expiry?,
        promoRequest: PromoRequest?,
        customField1: String?,
        customField2: String?,
        customField3: String?,
        gopayCallback: GopayPaymentCallback?,
        shopeepayCallback: PaymentCallback?,
        uobEzpayCallback: PaymentCallback?
    ) {
        val builder = SnapTokenRequestBuilder()
            .withTransactionDetails(transactionDetails)
            .withCustomerDetails(customerDetails)
            .withItemDetails(itemDetails)
            .withCreditCard(creditCard)
            .withUserId(userId)
            .withPermataVa(permataVa)
            .withBcaVa(bcaVa)
            .withBniVa(bniVa)
            .withBriVa(briVa)
            .withEnabledPayments(enabledPayments)
            .withExpiry(expiry)
            .withPromo(promoRequest)
            .withCustomField1(customField1)
            .withCustomField2(customField2)
            .withCustomField3(customField3)
            .withGopayCallback(gopayCallback)
            .withShopeepayCallback(shopeepayCallback)
            .withUobEzpayCallback(uobEzpayCallback)

        SnapCore.getInstance()
            ?.getPaymentOption(snapToken = snapToken, builder = builder, object :
                Callback<PaymentOption> {
                override fun onSuccess(result: PaymentOption) {
                    _paymentOptionLiveData.postValue(result)
                }

                override fun onError(error: SnapError) {
                    _error.postValue(error)
                }
            })
    }

    fun getAmountInString(transactionDetails: SnapTransactionDetail): String {
        return transactionDetails.grossAmount.toString()
    }

    fun getOrderId(transactionDetails: SnapTransactionDetail): String {
        return transactionDetails.orderId
    }
}