package com.midtrans.sdk.uikit.internal.presentation.creditcard

import android.util.Log
import androidx.lifecycle.ViewModel
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.exception.SnapError
import com.midtrans.sdk.corekit.api.model.*
import com.midtrans.sdk.corekit.api.requestbuilder.cardtoken.TwoClickCardTokenRequestBuilder
import com.midtrans.sdk.corekit.api.requestbuilder.payment.BankTransferPaymentRequestBuilder
import com.midtrans.sdk.corekit.api.requestbuilder.payment.CreditCardPaymentRequestBuilder
import com.midtrans.sdk.corekit.api.requestbuilder.payment.OneClickCardPaymentRequestBuilder
import com.midtrans.sdk.uikit.internal.view.FormData
import com.midtrans.sdk.uikit.internal.view.SavedCreditCardFormData
import javax.inject.Inject

class SavedCardViewModel @Inject constructor(
    private val snapCore: SnapCore
) : ViewModel()  {

    fun deleteSavedCard(snapToken: String, maskedCard: String){
        snapCore.deleteSavedCard(
            snapToken = snapToken,
            maskedCard = maskedCard,
            callback = object : Callback<DeleteSavedCardResponse> {
                override fun onSuccess(result: DeleteSavedCardResponse) {
                    Log.e("Delete Card Success", "Delete Card Success")
                }
                override fun onError(error: SnapError) {
                    Log.e("Delete Card Error", "Delete Card Error")
                }
            }
        )
    }

    fun chargeUsingCreditCard(
        formData: FormData,
        snapToken: String,
        orderId: String,
        grossAmount: Double,
        cardCVV: String,
        customerEmail: String
    ){
        when (formData){
            is SavedCreditCardFormData -> {
                if (formData.tokenType == SavedToken.ONE_CLICK){
                    snapCore.pay(
                        snapToken = snapToken,
                        paymentRequestBuilder = OneClickCardPaymentRequestBuilder()
                            .withPaymentType(PaymentType.CREDIT_CARD)
                            .withMaskedCard(formData.displayedMaskedCard),
                        callback = object : Callback<TransactionResponse> {
                            override fun onSuccess(result: TransactionResponse) {
                                Log.e("sucess", "sucess, sucess, sucess")
                            }
                            override fun onError(error: SnapError) {
                                Log.e("error, error, error", "error, error, error")
                            }
                        }
                    )
                } else {
                    snapCore.getCardToken(cardTokenRequestBuilder = TwoClickCardTokenRequestBuilder()
                        .withGrossAmount(grossAmount)
                        .withCardCvv(cardCVV)
                        .withTokenId(formData.tokenId)
                        .withOrderId(orderId)
                        .withCurrency("IDR"),
                        callback = object : Callback<CardTokenResponse> {
                            override fun onSuccess(result: CardTokenResponse) {
                                var ccRequestBuilder = CreditCardPaymentRequestBuilder()
                                    .withPaymentType(PaymentType.CREDIT_CARD)
                                    .withCustomerEmail(customerEmail)

                                result?.tokenId?.let {
                                    ccRequestBuilder.withCardToken(it)
                                }

                                snapCore.pay(
                                    snapToken = snapToken,
                                    paymentRequestBuilder = ccRequestBuilder,
                                    callback = object : Callback<TransactionResponse> {
                                        override fun onSuccess(result: TransactionResponse) {
                                            Log.e("transaction succes", "transaction status ${result.transactionStatus}")
                                        }
                                        override fun onError(error: SnapError) {
                                            Log.e("transaction error", "error, error, ${error.message}")
                                        }
                                    }
                                )
                            }
                            override fun onError(error: SnapError) {
                                Log.e("error get 2click token", "error, error, error")
                            }
                        }
                    )
                }
            }
        }
    }
}