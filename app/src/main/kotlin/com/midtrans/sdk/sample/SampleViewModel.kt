package com.midtrans.sdk.sample

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.exception.SnapError
import com.midtrans.sdk.corekit.api.model.*
import com.midtrans.sdk.corekit.api.requestbuilder.cardtoken.NormalCardTokenRequestBuilder
import com.midtrans.sdk.corekit.api.requestbuilder.cardtoken.TwoClickCardTokenRequestBuilder
import com.midtrans.sdk.corekit.api.requestbuilder.payment.BankTransferPaymentRequestBuilder
import com.midtrans.sdk.corekit.api.requestbuilder.payment.CreditCardPaymentRequestBuilder

class SampleViewModel : ViewModel() {
    var helloLiveData = MutableLiveData<String>()
    private val coreKit: SnapCore = SnapCore.getInstance()!!
    var cardTokenResponse : CardTokenResponse? = null
    var transactionResponse : TransactionResponse? = null
    val clientKeyForPoint = "SB-Mid-client-hOWJXiCCDRvT0RGr"
    val clientKeyForPromo = "VT-client-yrHf-c8Sxr-ck8tx"
    val ccSavedTokenIdBniTwoClickPoint = "410505fpOTKmsvZJRVolnxHuApec1467"
    val ccSavedTokenIdTwoClickPromo = "481111YgYEIkDdzJNVqfEdcAkkue1114"
    val defaultCcNumber = "4811 1111 1111 1114"
    val bniCcNumber = "4105 0586 8948 1467"

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
        coreKit.getCardToken(cardTokenRequestBuilder = NormalCardTokenRequestBuilder()
            .withClientKey(clientKeyForPoint)
            .withGrossAmount(150000.0)
            .withCardNumber(bniCcNumber)
            .withCardExpMonth("12")
            .withCardExpYear("24")
            .withCardCvv("123")
            .withOrderId("cobacoba-4")
            .withCurrency("IDR"),
            callback = object : Callback<CardTokenResponse> {
                override fun onSuccess(result: CardTokenResponse) {
                    cardTokenResponse = result
                }
                override fun onError(error: SnapError) {
                    Log.e("error, error, error", "error, error, error ${error.message} ${error.cause}")
                }
            }
        )
    }

    fun getTwoClickToken(){
        coreKit.getCardToken(cardTokenRequestBuilder = TwoClickCardTokenRequestBuilder()
            .withClientKey(clientKeyForPoint)
            .withGrossAmount(150000.0)
            .withCardCvv("123")
            .withTokenId(ccSavedTokenIdBniTwoClickPoint)
            .withOrderId("cobacoba-4")
            .withCurrency("IDR"),
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

    fun getCardTokenNormalWithInstallment(){
        coreKit.getCardToken(cardTokenRequestBuilder = NormalCardTokenRequestBuilder()
            .withClientKey(clientKeyForPoint)
            .withGrossAmount(150000.0)
            .withCardNumber(bniCcNumber)
            .withCardExpMonth("12")
            .withCardExpYear("24")
            .withCardCvv("123")
            .withOrderId("cobacoba-4")
            .withCurrency("IDR")
            .withBank("offline")
            .withInstallment(true)
            .withInstallmentTerm(3),
            callback = object : Callback<CardTokenResponse> {
                override fun onSuccess(result: CardTokenResponse) {
                    cardTokenResponse = result
                }
                override fun onError(error: SnapError) {
                    Log.e("error, error, error", "error, error, error ${error.message} ${error.cause}")
                }
            }
        )
    }

    fun getTwoClickTokenWithInstallment(){
        coreKit.getCardToken(cardTokenRequestBuilder = TwoClickCardTokenRequestBuilder()
            .withClientKey(clientKeyForPoint)
            .withGrossAmount(150000.0)
            .withCardCvv("123")
            .withTokenId(ccSavedTokenIdBniTwoClickPoint)
            .withOrderId("cobacoba-4")
            .withCurrency("IDR")
            .withBank("offline")
            .withInstallment(true)
            .withInstallmentTerm(3),
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

    fun chargeUsingCreditCardWithPromo(snapToken: String) {
        coreKit.pay(
            snapToken = snapToken,
            paymentRequestBuilder = CreditCardPaymentRequestBuilder()
                .withCardToken(cardTokenResponse?.tokenId.toString())
                .withSaveCard(true)
                .withPaymentType(PaymentType.CREDIT_CARD)
                .withDiscountedGrossAmount(145000.0)
                .withPromoId("431")
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

    fun chargeUsingCreditCardWithBniPoint(snapToken: String) {
        coreKit.pay(
            snapToken = snapToken,
            paymentRequestBuilder = CreditCardPaymentRequestBuilder()
                .withCardToken(cardTokenResponse?.tokenId.toString())
                .withSaveCard(true)
                .withPaymentType(PaymentType.CREDIT_CARD)
                .withCustomerEmail("belajar@example.com")
                .withBank("bni")
                .withPoint(100000.0)
            ,
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

    fun chargeUsingCreditCardWithBniPointAndPromo(snapToken: String) {
        coreKit.pay(
            snapToken = snapToken,
            paymentRequestBuilder = CreditCardPaymentRequestBuilder()
                .withCardToken(cardTokenResponse?.tokenId.toString())
                .withSaveCard(true)
                .withPaymentType(PaymentType.CREDIT_CARD)
                .withCustomerEmail("belajar@example.com")
                .withBank("bni")
                .withPoint(100000.0)
                .withDiscountedGrossAmount(149850.0)
                .withPromoId("436")
            ,
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

    fun chargeUsingInstallment(snapToken: String) {
        coreKit.pay(
            snapToken = snapToken,
            paymentRequestBuilder = CreditCardPaymentRequestBuilder()
                .withCardToken(cardTokenResponse?.tokenId.toString())
                .withPaymentType(PaymentType.CREDIT_CARD)
                .withCustomerEmail("belajar@example.com")
                .withInstallment("offline_3")
            ,
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

    fun chargeUsingInstallmentAndPromo(snapToken: String) {
        coreKit.pay(
            snapToken = snapToken,
            paymentRequestBuilder = CreditCardPaymentRequestBuilder()
                .withCardToken(cardTokenResponse?.tokenId.toString())
                .withPaymentType(PaymentType.CREDIT_CARD)
                .withCustomerEmail("belajar@example.com")
                .withInstallment("offline_3")
                .withPromoId("436")
                .withDiscountedGrossAmount(149850.0)
            ,
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

    fun getBinData(binNumber: String){
        coreKit.getBinData(
            binNumber = binNumber,
            clientKey = "VT-client-yrHf-c8Sxr-ck8tx",
            callback = object : Callback<BinResponse> {
                override fun onSuccess(result: BinResponse) {
                }

                override fun onError(error: SnapError) {
                    Log.e("error, error, error", "get exbin error, error, error")
                }
            }
        )
    }

    fun getBankPoint(
    ){
        coreKit.getBankPoint(
            snapToken = "fe23915e-bae3-4dcb-b17c-82b7dd80d233",
            cardToken = cardTokenResponse?.tokenId.toString(),
            grossAmount = 150000.0,
            callback = object : Callback<BankPointResponse> {
                override fun onSuccess(result: BankPointResponse) {
                }
                override fun onError(error: SnapError) {
                    Log.e("error, error, error", "get point error, error, error")
                }
            }
        )
    }
}