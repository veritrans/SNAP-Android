package com.midtrans.sdk.corekit.api.requestbuilder

abstract class CreditCardTokenRequestBuilder {
    protected var clientKey: String? = null
    protected var grossAmount: Double? = null
    protected var cardNumber: String? = null
    protected var cardExpMonth: String? = null
    protected var cardExpYear: String? = null
    protected var cardCvv: String? = null

    fun withClientKey(value: String): CreditCardTokenRequestBuilder = apply {
        clientKey = value
    }

    fun withGrossAmount(value: Double): CreditCardTokenRequestBuilder = apply {
        grossAmount = value
    }
    fun withCardNumber(value: String): CreditCardTokenRequestBuilder = apply {
        cardNumber = value
    }
    fun withCardExpMonth(value: String): CreditCardTokenRequestBuilder = apply {
        cardExpMonth = value
    }
    fun withCardExpYear(value: String): CreditCardTokenRequestBuilder = apply {
        cardExpYear = value
    }
    fun withCardCvv(value: String): CreditCardTokenRequestBuilder = apply {
        cardCvv = value
    }

    internal abstract fun build():Map<String,Any>

    companion object{
        const val CARD_NUMBER = "card_number"
        const val CARD_CVV = "card_cvv"
        const val CARD_EXPIRY_MONTH = "card_exp_month"
        const val CARD_EXPIRY_YEAR = "card_exp_year"
        const val CLIENT_KEY = "client_key"
        const val GROSS_AMOUNT = "gross_amount"
    }
}