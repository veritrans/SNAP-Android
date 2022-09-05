package com.midtrans.sdk.uikit.internal.presentation.creditcard

import android.content.Context
import android.util.Log
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.exception.SnapError
import com.midtrans.sdk.corekit.api.model.*
import com.midtrans.sdk.corekit.api.requestbuilder.cardtoken.NormalCardTokenRequestBuilder
import com.midtrans.sdk.corekit.api.requestbuilder.cardtoken.TwoClickCardTokenRequestBuilder
import com.midtrans.sdk.corekit.api.requestbuilder.payment.CreditCardPaymentRequestBuilder
import com.midtrans.sdk.corekit.api.requestbuilder.payment.OneClickCardPaymentRequestBuilder
import com.midtrans.sdk.corekit.internal.network.model.response.TransactionDetails
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.presentation.SuccessScreenActivity
import com.midtrans.sdk.uikit.internal.view.SavedCreditCardFormData
import javax.inject.Inject

class SavedCardViewModel @Inject constructor(
    private val snapCore: SnapCore
) : ViewModel()  {

    val bankIconId = MutableLiveData<Int>()
    private val _transactionResponse = MutableLiveData<TransactionResponse>()
    private val _error = MutableLiveData<SnapError>()

    fun getTransactionResponseLiveData(): LiveData<TransactionResponse> = _transactionResponse
    fun getErrorLiveData(): LiveData<SnapError> = _error

    fun getBankIconImage(binNumber: String) {
        snapCore.getBinData(
            binNumber = binNumber,
            callback = object : Callback<BinResponse> {
                override fun onSuccess(result: BinResponse) {
                    result.run {
                        data?.bankCode?.let {
                            bankIconId.value = getBankIcon(it?.lowercase())
                        }
                    }
                }
                override fun onError(error: SnapError) {
                    TODO("Not yet implemented")
                }
            }
        )
    }

    fun setBankIconToNull(){
        bankIconId.value = null
    }

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

    fun chargeUsingSavedCard(
        formData: SavedCreditCardFormData,
        snapToken: String,
        transactionDetails: TransactionDetails?,
        cardCVV: String,
        customerEmail: String,
    ){
        if (formData.tokenType == SavedToken.ONE_CLICK){
            snapCore.pay(
                snapToken = snapToken,
                paymentRequestBuilder = OneClickCardPaymentRequestBuilder()
                    .withPaymentType(PaymentType.CREDIT_CARD)
                    .withMaskedCard(formData.displayedMaskedCard),
                callback = object : Callback<TransactionResponse> {
                    override fun onSuccess(result: TransactionResponse) {
                        _transactionResponse.value = result
                    }
                    override fun onError(error: SnapError) {
                        _error.value = error
                    }
                }
            )
        } else {

            var tokenRequest = TwoClickCardTokenRequestBuilder()
                .withCardCvv(cardCVV)
                .withTokenId(formData.tokenId)

            transactionDetails?.currency?.let {
                tokenRequest.withCurrency(it)
            }
            transactionDetails?.grossAmount?.let {
                tokenRequest.withGrossAmount(it)
            }
            transactionDetails?.orderId?.let {
                tokenRequest.withOrderId(it)
            }
            snapCore.getCardToken(cardTokenRequestBuilder = tokenRequest ,
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
                                    _transactionResponse.value = result
                                }
                                override fun onError(error: SnapError) {
                                    _error.value = error
                                }
                            }
                        )
                    }
                    override fun onError(error: SnapError) {
                        //TODO: Need to confirm how to handle get token error on UI
                        Log.e("error get 2click token", "error, error, error")
                    }
                }
            )
        }
    }

    fun chargeUsingOtherCard(
        transactionDetails: TransactionDetails?,
        cardNumber: TextFieldValue,
        cardExpiry: TextFieldValue,
        cardCvv: TextFieldValue,
        isSavedCard: Boolean,
        customerEmail: String,
        snapToken: String
    ){
        var tokenRequest = NormalCardTokenRequestBuilder()
            .withCardNumber(getCardNumberFromTextField(cardNumber))
            .withCardExpMonth(getExpMonthFromTextField(cardExpiry))
            .withCardExpYear(getExpYearFromTextField(cardExpiry))
            .withCardCvv(cardCvv.text)

        transactionDetails?.currency?.let {
            tokenRequest.withCurrency(it)
        }
        transactionDetails?.grossAmount?.let {
            tokenRequest.withGrossAmount(it)
        }
        transactionDetails?.orderId?.let {
            tokenRequest.withOrderId(it)
        }
        snapCore.getCardToken(
            cardTokenRequestBuilder = tokenRequest,
            callback = object : Callback<CardTokenResponse>{
                override fun onSuccess(result: CardTokenResponse) {

                    var ccRequestBuilder = CreditCardPaymentRequestBuilder()
                        .withSaveCard(isSavedCard)
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
                                _transactionResponse.value = result
                            }
                            override fun onError(error: SnapError) {
                                _error.value = error
                            }
                        }
                    )
                }
                override fun onError(error: SnapError) {
                    //TODO:Need to confirm how to handle card token error on UI
                }
            }
        )
    }

    private fun getCardNumberFromTextField(value: TextFieldValue) : String{
        return value.text.replace(" ", "")
    }
    private fun getExpMonthFromTextField(value: TextFieldValue) : String{
        return value.text.substring(0, 2)
    }
    private fun getExpYearFromTextField(value: TextFieldValue) : String{
        return return value.text.substring(3, 5)
    }

    fun getBankIcon(bank: String): Int? {

        return when (bank.lowercase()) {
            "bri" -> R.drawable.ic_outline_bri_24
            "bni" -> R.drawable.ic_bank_bni_24
            "mandiri" -> R.drawable.ic_bank_mandiri_24
            "bca" -> R.drawable.ic_bank_bca_24
            "cimb" -> R.drawable.ic_bank_cimb_24
            "mega" -> R.drawable.ic_bank_mega_24
            else -> null
        }
    }
}