package com.midtrans.sdk.corekit.api.requestbuilder.cardtoken

import com.midtrans.sdk.corekit.api.exception.MissingParameterException
import com.midtrans.sdk.corekit.api.model.InstallmentBank
import com.midtrans.sdk.corekit.internal.network.model.request.InstallmentRequest
import com.midtrans.sdk.corekit.internal.util.NumberUtil

class NormalCardTokenRequestBuilder : CreditCardTokenRequestBuilder() {
    private var grossAmount: Double? = null
    private var cardNumber: String? = null
    private var cardExpMonth: String? = null
    private var cardExpYear: String? = null
    private var cardCvv: String? = null
    private var currency: String? = null
    private var orderId: String? = null
    private var installmentRequest: InstallmentRequest? = null

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

    override fun withGrossAmount(value: Double): NormalCardTokenRequestBuilder = apply {
        grossAmount = value
    }

    override fun withOrderId(value: String): NormalCardTokenRequestBuilder = apply {
        orderId = value
    }

    override fun withCurrency(value: String): NormalCardTokenRequestBuilder = apply {
        currency = value
    }

    fun withInstallment(value: Boolean, @InstallmentBank.Def bank: String , installmentTerm: Int): NormalCardTokenRequestBuilder = apply {
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