package com.midtrans.sdk.corekit.api.requestbuilder

import com.midtrans.sdk.corekit.api.requestbuilder.cardtoken.CreditCardTokenRequestBuilder
import com.midtrans.sdk.corekit.api.requestbuilder.cardtoken.NormalCardTokenRequestBuilder
import com.midtrans.sdk.corekit.internal.network.model.request.InstallmentRequest
import com.midtrans.sdk.corekit.internal.util.NumberUtil
import org.junit.Assert
import org.junit.Test

internal class NormalCardTokenRequestBuilderTest {
    private val clientKey = "clientKey"
    private val grossAmount = 150000.0
    private val cardNumber = "cardNumber"
    private val expMonth = "12"
    private val expYear = "24"
    private val cvv = "123"
    private val orderId = "orderId"
    private val currency = "IDR"
    private val installment = InstallmentRequest(installment = true, bank = "offline", installmentTerm = 3)

    @Test
    fun shouldConstructCardTokenRequestBasic() {
        val request = NormalCardTokenRequestBuilder()
            .withClientKey(clientKey)
            .withGrossAmount(grossAmount)
            .withCardNumber(cardNumber)
            .withCardExpMonth(expMonth)
            .withCardExpYear(expYear)
            .withCardCvv(cvv)
            .withOrderId(orderId)
            .withCurrency(currency)
            .build()

        Assert.assertEquals(clientKey, request.getValue(CreditCardTokenRequestBuilder.CLIENT_KEY))
        Assert.assertEquals(grossAmount?.let { NumberUtil.formatDoubleToString(it) }, request.getValue(CreditCardTokenRequestBuilder.GROSS_AMOUNT))
        Assert.assertEquals(cardNumber, request.getValue(CreditCardTokenRequestBuilder.CARD_NUMBER))
        Assert.assertEquals(expMonth, request.getValue(CreditCardTokenRequestBuilder.CARD_EXPIRY_MONTH))
        Assert.assertEquals(expYear, request.getValue(CreditCardTokenRequestBuilder.CARD_EXPIRY_YEAR))
        Assert.assertEquals(cvv, request.getValue(CreditCardTokenRequestBuilder.CARD_CVV))
        Assert.assertEquals(orderId, request.getValue(CreditCardTokenRequestBuilder.ORDER_ID))
        Assert.assertEquals(currency, request.getValue(CreditCardTokenRequestBuilder.CURRENCY))
    }

    @Test
    fun shouldConstructCardTokenRequestWithInstallment() {
        val request = NormalCardTokenRequestBuilder()
            .withClientKey(clientKey)
            .withGrossAmount(grossAmount)
            .withCardNumber(cardNumber)
            .withCardExpMonth(expMonth)
            .withCardExpYear(expYear)
            .withCardCvv(cvv)
            .withOrderId(orderId)
            .withCurrency(currency)
            .withInstallment(
                value = installment.installment,
                installmentTerm = installment.installmentTerm,
                bank = installment.bank
            )
            .build()

        Assert.assertEquals(clientKey, request.getValue(CreditCardTokenRequestBuilder.CLIENT_KEY))
        Assert.assertEquals(grossAmount?.let { NumberUtil.formatDoubleToString(it) }, request.getValue(CreditCardTokenRequestBuilder.GROSS_AMOUNT))
        Assert.assertEquals(cardNumber, request.getValue(CreditCardTokenRequestBuilder.CARD_NUMBER))
        Assert.assertEquals(expMonth, request.getValue(CreditCardTokenRequestBuilder.CARD_EXPIRY_MONTH))
        Assert.assertEquals(expYear, request.getValue(CreditCardTokenRequestBuilder.CARD_EXPIRY_YEAR))
        Assert.assertEquals(cvv, request.getValue(CreditCardTokenRequestBuilder.CARD_CVV))
        Assert.assertEquals(orderId, request.getValue(CreditCardTokenRequestBuilder.ORDER_ID))
        Assert.assertEquals(currency, request.getValue(CreditCardTokenRequestBuilder.CURRENCY))
        Assert.assertEquals(installment.installmentTerm.toString(), request.getValue(CreditCardTokenRequestBuilder.INSTALLMENT_TERM))
    }
}