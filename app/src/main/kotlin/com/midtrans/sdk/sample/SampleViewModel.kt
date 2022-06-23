package com.midtrans.sdk.sample

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.exception.SnapError
import com.midtrans.sdk.corekit.api.model.CardTokenResponse
import com.midtrans.sdk.corekit.api.model.DeleteSavedCardResponse
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.api.requestbuilder.BankTransferPaymentRequestBuilder
import com.midtrans.sdk.corekit.api.requestbuilder.BasicCardTokenRequestBuilder
import com.midtrans.sdk.corekit.api.requestbuilder.CreditCardPaymentRequestBuilder
import com.midtrans.sdk.corekit.api.requestbuilder.CreditCardTokenRequestBuilder

class SampleViewModel : ViewModel() {
    var helloLiveData = MutableLiveData<String>()
    private val coreKit: SnapCore = SnapCore.getInstance()!!
    private var cardTokenResponse : CardTokenResponse? = null
    private var transactionResponse : TransactionResponse? = null

    fun getHelloFromSnap() {
        helloLiveData.value = coreKit.hello()
    }

    fun chargeBniVa(snapToken: String) {
        coreKit.pay(
            snapToken = snapToken,
            paymentRequestBuilder = BankTransferPaymentRequestBuilder()
                .withPaymentType(PaymentType.BCA_VA)
                .withCustomerEmail("belajar@example.com"),
            callback = object : Callback<TransactionResponse> {
                override fun onSuccess(result: TransactionResponse) {}
                override fun onError(error: SnapError) {
                    Log.e("error, error, error", "error, error, error")
                }
            }
        )
    }

    fun getCardTokenBasic(){
        coreKit.getCardToken(cardTokenRequestBuilder = BasicCardTokenRequestBuilder()
            .withClientKey("SB-Mid-client-Y1-C6UEY5qGZTAEt")
            .withGrossAmount(10000.3333)
            .withCardNumber("4811 1111 1111 1114")
            .withCardExpMonth("12")
            .withCardExpYear("24")
            .withCardCvv("123"),
            callback = object : Callback<CardTokenResponse> {
                override fun onSuccess(result: CardTokenResponse) {
                    cardTokenResponse = result
                }
                override fun onError(error: SnapError) {
                    Log.e("error, error, error", "error, error, error")
                }
            }
        )
    }

    fun chargeUsingCreditCard(snapToken: String) {
        coreKit.pay(
            snapToken = snapToken,
            paymentRequestBuilder = CreditCardPaymentRequestBuilder()
                .withCardToken(cardTokenResponse?.tokenId.toString())
                .withSaveCard(true)
                .withPaymentType(PaymentType.CREDIT_CARD)
                .withCustomerEmail("belajar@example.com"),
            callback = object : Callback<TransactionResponse> {
                override fun onSuccess(result: TransactionResponse) {
                    transactionResponse = result
                }
                override fun onError(error: SnapError) {
                    Log.e("error, error, error", "error, error, error")
                }
            }
        )
    }
    fun deleteSavedCard(){
        coreKit.deleteSavedCard(
            snapToken = "a5de6d5e-096b-4de7-a3d9-cadbb67ddfa0",
            maskedCard = "481111-1114",
            callback = object : Callback<DeleteSavedCardResponse> {
                override fun onSuccess(result: DeleteSavedCardResponse) {
                    Log.e("DELETE SUKSES EUY", "DELETE CARD SUKSES")
                }

                override fun onError(error: SnapError) {
                    Log.e("error, error, error", "delete error, error, error")
                }

            }

        )
    }
}