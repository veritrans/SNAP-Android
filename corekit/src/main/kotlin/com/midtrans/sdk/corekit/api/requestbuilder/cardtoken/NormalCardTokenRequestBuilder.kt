package com.midtrans.sdk.corekit.api.requestbuilder.cardtoken

import com.midtrans.sdk.corekit.api.exception.MissingParameterException
import com.midtrans.sdk.corekit.internal.util.NumberUtil

class NormalCardTokenRequestBuilder : CreditCardTokenRequestBuilder() {
    private var clientKey: String? = null
    private var grossAmount: Double? = null
    private var cardNumber: String? = null
    private var cardExpMonth: String? = null
    private var cardExpYear: String? = null
    private var cardCvv: String? = null

    fun withCardNumber(value: String): NormalCardTokenRequestBuilder = apply {
        cardNumber = value
    }

    fun withCardExpMonth(value: String): NormalCardTokenRequestBuilder = apply {
        cardExpMonth = value
    }

    fun withCardExpYear(value: String): NormalCardTokenRequestBuilder = apply {
        cardExpYear = value
    }

    fun withCardCvv(value: String): NormalCardTokenRequestBuilder = apply {
        cardCvv = value
    }

    fun withClientKey(value: String): NormalCardTokenRequestBuilder = apply {
        clientKey = value
    }

    fun withGrossAmount(value: Double): NormalCardTokenRequestBuilder = apply {
        grossAmount = value
    }

    override fun build(): Map<String, String> {
        val result = mutableMapOf<String, String>()
        appendQueryParam(
            target = result,
            key = CARD_NUMBER,
            value = cardNumber,
            errorMessage = "Card number required"
        )
        appendQueryParam(
            target = result,
            key = CARD_CVV,
            value = cardCvv,
            errorMessage = "Card cvv required"
        )
        appendQueryParam(
            target = result,
            key = CARD_EXPIRY_MONTH,
            value = cardExpMonth,
            errorMessage = "Card expiry month required"
        )
        appendQueryParam(
            target = result,
            key = CARD_EXPIRY_YEAR,
            value = cardExpYear,
            errorMessage = "Card expiry year required"
        )
        appendQueryParam(
            target = result,
            key = CLIENT_KEY,
            value = clientKey,
            errorMessage = "Client key required"
        )
        appendQueryParam(
            target = result,
            key = GROSS_AMOUNT,
            value = grossAmount?.let { NumberUtil.formatDoubleToString(it) },
            errorMessage = "Gross amount required"
        )
        return result
    }

    private fun appendQueryParam(
        target: MutableMap<String, String>,
        key: String,
        value: String?,
        errorMessage: String
    ) {

        target.apply {
            value?.also {
                put(key, it)
            } ?: throw MissingParameterException(errorMessage)
        }
    }
}