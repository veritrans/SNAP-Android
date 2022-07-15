package com.midtrans.sdk.corekit.api.requestbuilder.cardtoken

import com.midtrans.sdk.corekit.api.exception.MissingParameterException
import com.midtrans.sdk.corekit.internal.network.model.request.InstallmentRequest
import com.midtrans.sdk.corekit.internal.util.NumberUtil

class TwoClickCardTokenRequestBuilder : CreditCardTokenRequestBuilder() {
    private var twoClick: Boolean = true
    private var tokenId: String? = null
    private var cardCvv: String? = null
    private var clientKey: String? = null
    private var grossAmount: Double? = null
    private var currency: String? = null
    private var orderId: String? = null
    private var installmentRequest: InstallmentRequest? = null

    fun withCardCvv(value: String): TwoClickCardTokenRequestBuilder = apply {
        cardCvv = value
    }

    fun withClientKey(value: String): TwoClickCardTokenRequestBuilder = apply {
        clientKey = value
    }

    fun withGrossAmount(value: Double): TwoClickCardTokenRequestBuilder = apply {
        grossAmount = value
    }

    fun withTokenId(value: String): TwoClickCardTokenRequestBuilder = apply {
        tokenId = value
    }

    fun withOrderId(value: String): TwoClickCardTokenRequestBuilder = apply {
        orderId = value
    }

    fun withCurrency(value: String): TwoClickCardTokenRequestBuilder = apply {
        currency = value
    }

    fun withInstallment(value: Boolean, bank: String, installmentTerm: Int): TwoClickCardTokenRequestBuilder = apply {
        installmentRequest = InstallmentRequest(
            installment = value,
            bank = bank,
            installmentTerm = installmentTerm
        )
    }

    override fun build(): Map<String, String> {
        val result = mutableMapOf<String, String>()
        appendQueryParam(
            target = result,
            key = CARD_CVV,
            value = cardCvv,
            errorMessage = "Card cvv required"
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
        appendQueryParam(
            target = result,
            key = TOKEN_ID,
            value = tokenId,
            errorMessage = "Saved token id required"
        )
        appendQueryParam(
            target = result,
            key = TWO_CLICK,
            value = twoClick.toString(),
            errorMessage = "Two click required"
        )
        appendQueryParam(
            target = result,
            key = ORDER_ID,
            value = orderId,
            errorMessage = "Order Id required"
        )
        appendQueryParam(
            target = result,
            key = CURRENCY,
            value = currency,
            errorMessage = "Currency is required"
        )

        constructInstallmentRequest(installmentRequest = installmentRequest, target = result)

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

    private fun constructInstallmentRequest(installmentRequest: InstallmentRequest?, target: MutableMap<String, String>){

        installmentRequest?.let {
            appendQueryParam(
                target = target,
                key = BANK,
                value = installmentRequest.bank,
                errorMessage = "Bank is required"
            )
            appendQueryParam(
                target = target,
                key = INSTALLMENT,
                value = installmentRequest.installment.toString(),
                errorMessage = "Installment is required"
            )
            appendQueryParam(
                target = target,
                key = INSTALLMENT_TERM,
                value = installmentRequest.installmentTerm.toString(),
                errorMessage = "Installment term is required"
            )
        }
    }
}