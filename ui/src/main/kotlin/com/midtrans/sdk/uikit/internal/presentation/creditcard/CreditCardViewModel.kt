package com.midtrans.sdk.uikit.internal.presentation.creditcard

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.exception.SnapError
import com.midtrans.sdk.corekit.api.model.BinResponse
import com.midtrans.sdk.corekit.api.model.CardTokenResponse
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.api.requestbuilder.cardtoken.CreditCardTokenRequestBuilder
import com.midtrans.sdk.corekit.api.requestbuilder.payment.CreditCardPaymentRequestBuilder
import com.midtrans.sdk.uikit.R
import javax.inject.Inject

class CreditCardViewModel @Inject constructor(
    private val snapCore: SnapCore
) : ViewModel()  {

    val bankIconId = MutableLiveData<Int>()

    @SuppressLint("CheckResult")
    fun getBankName(binNumber: String,
                    clientKey: String) {
        snapCore.getBinData(
            binNumber = binNumber,
            clientKey = clientKey,
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

    fun getCardToken(
        cardTokenRequestBuilder: CreditCardTokenRequestBuilder,
        callback: Callback<CardTokenResponse>
    ){
        snapCore.getCardToken(
            cardTokenRequestBuilder = cardTokenRequestBuilder,
            callback = callback
        )
    }

    fun chargeUsingCard(
        paymentRequestBuilder: CreditCardPaymentRequestBuilder,
        snapToken: String,
        callback: Callback<TransactionResponse>
    ){
        snapCore.pay(
            paymentRequestBuilder = paymentRequestBuilder,
            snapToken = snapToken,
            callback = callback
        )
    }

    fun setBankIconIdToNull(){
        bankIconId.value = null
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
