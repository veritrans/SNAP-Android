package com.midtrans.sdk.corekit.api.requestbuilder.cardtoken

abstract class CreditCardTokenRequestBuilder {
    internal abstract fun build(): Map<String, String>

    companion object {
        const val CARD_NUMBER = "card_number"
        const val CARD_CVV = "card_cvv"
        const val CARD_EXPIRY_MONTH = "card_exp_month"
        const val CARD_EXPIRY_YEAR = "card_exp_year"
        const val CLIENT_KEY = "client_key"
        const val CURRENCY = "currency"
        const val GROSS_AMOUNT = "gross_amount"
        const val INSTALLMENT = "installment"
        const val INSTALLMENT_TERM = "installment_term"
        const val ORDER_ID = "order_id"
        const val TOKEN_ID = "token_id"
        const val TWO_CLICK = "two_click"
    }
}