package com.midtrans.sdk.corekit.api.requestbuilder

import com.midtrans.sdk.corekit.api.exception.MissingParameterException

class BasicCardTokenRequestBuilder: CreditCardTokenRequestBuilder() {


    override fun build(): Map<String, Any> {
        val result = mutableMapOf<String, Any>()
        appendQueryParam(target = result,
            key = CARD_NUMBER,
            value = cardNumber,
            errorMessage = "Card number required"
        )
        appendQueryParam(target = result,
            key = CARD_CVV,
            value = cardCvv,
            errorMessage = "Card cvv required"
        )
        appendQueryParam(target = result,
            key = CARD_EXPIRY_MONTH,
            value = cardExpMonth,
            errorMessage = "Card expiry required"
        )
        appendQueryParam(target = result,
            key = CARD_EXPIRY_YEAR,
            value = cardExpYear,
            errorMessage = "Card expiry year required"
        )
        appendQueryParam(target = result,
            key = CLIENT_KEY,
            value = clientKey,
            errorMessage = "Client key required"
        )
        appendQueryParam(target = result,
            key = GROSS_AMOUNT,
            value = grossAmount,
            errorMessage = "Gross amount required"
        )
        return result
    }

    private fun appendQueryParam(target: MutableMap<String, Any>, key: String, value: Any?, errorMessage: String) {

        target.apply {
            value?.also {
                put(key, it)
            }?:throw MissingParameterException(errorMessage)
        }
    }
}