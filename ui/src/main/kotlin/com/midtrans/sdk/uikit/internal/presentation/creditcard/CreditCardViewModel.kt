package com.midtrans.sdk.uikit.internal.presentation.creditcard

import android.util.Log
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.exception.SnapError
import com.midtrans.sdk.corekit.api.model.BinResponse
import com.midtrans.sdk.corekit.api.model.CardTokenResponse
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.api.requestbuilder.cardtoken.NormalCardTokenRequestBuilder
import com.midtrans.sdk.corekit.api.requestbuilder.payment.CreditCardPaymentRequestBuilder
import com.midtrans.sdk.uikit.R
import javax.inject.Inject

class CreditCardViewModel @Inject constructor(
    private val snapCore: SnapCore
) : ViewModel() {

    val bankIconId = MutableLiveData<Int>()


    //TODO: should this be move to repository?
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

    fun setBankIconToNull() {
        bankIconId.value = null
    }

    fun chargeUsingCreditCard(
        grossAmount: String,
        cardNumber: TextFieldValue,
        cardExpiry: TextFieldValue,
        cardCvv: TextFieldValue,
        orderId: String,
        isSavedCard: Boolean,
        customerEmail: String,
        snapToken: String
    ) {
        snapCore.getCardToken(
            cardTokenRequestBuilder = NormalCardTokenRequestBuilder()
                .withGrossAmount(grossAmount.toDouble())
                .withCardNumber(getCardNumberFromTextField(cardNumber))
                .withCardExpMonth(getExpMonthFromTextField(cardExpiry))
                .withCardExpYear(getExpYearFromTextField(cardExpiry))
                .withCardCvv(cardCvv.text)
                .withOrderId(orderId)
                .withCurrency("IDR"),
            callback = object : Callback<CardTokenResponse> {
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
                                Log.e(
                                    "transaction succes",
                                    "transaction status ${result.transactionStatus}"
                                )
                            }

                            override fun onError(error: SnapError) {
                                Log.e("error, error, error", "error charge ${error.message}")
                            }
                        }
                    )
                }

                override fun onError(error: SnapError) {
                    Log.e("error, error, error", "error get card token ${error.message}")
                }
            }
        )
    }

    private fun getCardNumberFromTextField(value: TextFieldValue): String {
        return value.text.replace(" ", "")
    }

    private fun getExpMonthFromTextField(value: TextFieldValue): String {
        return value.text.substring(0, 2)
    }

    private fun getExpYearFromTextField(value: TextFieldValue): String {
        return return value.text.substring(3, 5)
    }

    private fun getBankIcon(bank: String): Int? {

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
