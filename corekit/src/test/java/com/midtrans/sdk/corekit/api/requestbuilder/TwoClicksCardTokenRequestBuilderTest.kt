package com.midtrans.sdk.corekit.api.requestbuilder

import com.midtrans.sdk.corekit.api.requestbuilder.cardtoken.CreditCardTokenRequestBuilder
import com.midtrans.sdk.corekit.api.requestbuilder.cardtoken.TwoClickCardTokenRequestBuilder
import com.midtrans.sdk.corekit.internal.network.model.request.InstallmentRequest
import com.midtrans.sdk.corekit.internal.util.NumberUtil
import org.junit.Assert
import org.junit.Test

class TwoClicksCardTokenRequestBuilderTest {
    private val tokenId = "tokenId"
    private val clientKey = "clientKey"
    private val grossAmount = 150000.0
    private val cvv = "123"
    private val orderId = "orderId"
    private val currency = "IDR"
    private val installment =
        InstallmentRequest(installment = true, bank = "offline", installmentTerm = 3)

    @Test
    fun shouldConstructCardTokenRequestBasic() {
        val request = TwoClickCardTokenRequestBuilder()
            .withTokenId(tokenId)
            .withGrossAmount(grossAmount)
            .withCardCvv(cvv)
            .withOrderId(orderId)
            .withCurrency(currency)
            .withClientKey(clientKey)
            .build()

        Assert.assertEquals(tokenId, request.getValue(CreditCardTokenRequestBuilder.TOKEN_ID))
        Assert.assertEquals(clientKey, request.getValue(CreditCardTokenRequestBuilder.CLIENT_KEY))
        Assert.assertEquals(
            NumberUtil.formatDoubleToString(grossAmount), request.getValue(
                CreditCardTokenRequestBuilder.GROSS_AMOUNT
            )
        )
        Assert.assertEquals(cvv, request.getValue(CreditCardTokenRequestBuilder.CARD_CVV))
        Assert.assertEquals(orderId, request.getValue(CreditCardTokenRequestBuilder.ORDER_ID))
        Assert.assertEquals(currency, request.getValue(CreditCardTokenRequestBuilder.CURRENCY))
    }

    @Test
    fun shouldConstructCardTokenRequestWithInstallment() {
        val request = TwoClickCardTokenRequestBuilder()
            .withTokenId(tokenId)
            .withGrossAmount(grossAmount)
            .withCardCvv(cvv)
            .withOrderId(orderId)
            .withCurrency(currency)
            .withInstallment(
                value = installment.installment,
                installmentTerm = installment.installmentTerm,
                bank = installment.bank
            )
            .withClientKey(clientKey)
            .build()

        Assert.assertEquals(tokenId, request.getValue(CreditCardTokenRequestBuilder.TOKEN_ID))
        Assert.assertEquals(clientKey, request.getValue(CreditCardTokenRequestBuilder.CLIENT_KEY))
        Assert.assertEquals(
            NumberUtil.formatDoubleToString(grossAmount), request.getValue(
                CreditCardTokenRequestBuilder.GROSS_AMOUNT
            )
        )
        Assert.assertEquals(cvv, request.getValue(CreditCardTokenRequestBuilder.CARD_CVV))
        Assert.assertEquals(orderId, request.getValue(CreditCardTokenRequestBuilder.ORDER_ID))
        Assert.assertEquals(currency, request.getValue(CreditCardTokenRequestBuilder.CURRENCY))
        Assert.assertEquals(
            installment.installmentTerm.toString(), request.getValue(
                CreditCardTokenRequestBuilder.INSTALLMENT_TERM
            )
        )
    }
}