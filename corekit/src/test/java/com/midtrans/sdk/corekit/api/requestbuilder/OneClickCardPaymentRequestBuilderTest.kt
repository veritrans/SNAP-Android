package com.midtrans.sdk.corekit.api.requestbuilder

import com.midtrans.sdk.corekit.api.exception.InvalidPaymentTypeException
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.requestbuilder.payment.OneClickCardPaymentRequestBuilder
import com.midtrans.sdk.corekit.internal.network.model.request.PromoDetailRequest
import org.junit.Assert
import org.junit.Test

internal class OneClickCardPaymentRequestBuilderTest{
    private val maskedCard = "maskedCard"
    private val installment = "offline_3"
    private val promo = PromoDetailRequest(discountedGrossAmount = 145000.0, promoId = "431" )

    @Test
    fun shouldConstructCreditCardPaymentRequestBasic() {
        val request = OneClickCardPaymentRequestBuilder()
            .withPaymentType(PaymentType.CREDIT_CARD)
            .withMaskedCard(maskedCard)
            .build()
        Assert.assertEquals(PaymentType.CREDIT_CARD, request.paymentType)
        Assert.assertEquals(maskedCard, request.paymentParams?.maskedCard)
    }

    @Test
    fun shouldConstructCreditCardPaymentRequestWithInstallment() {
        val request = OneClickCardPaymentRequestBuilder()
            .withPaymentType(PaymentType.CREDIT_CARD)
            .withMaskedCard(maskedCard)
            .withInstallment(installment)
            .build()
        Assert.assertEquals(PaymentType.CREDIT_CARD, request.paymentType)
        Assert.assertEquals(maskedCard, request.paymentParams?.maskedCard)
        Assert.assertEquals(installment, request.paymentParams?.installment)
    }

    @Test
    fun shouldConstructCreditCardPaymentRequestWithPromo() {
        val request = OneClickCardPaymentRequestBuilder()
            .withPaymentType(PaymentType.CREDIT_CARD)
            .withMaskedCard(maskedCard)
            .withPromo(promo)
            .build()
        Assert.assertEquals(PaymentType.CREDIT_CARD, request.paymentType)
        Assert.assertEquals(maskedCard, request.paymentParams?.maskedCard)
        Assert.assertEquals(promo.discountedGrossAmount, request.promoDetails?.discountedGrossAmount)
        Assert.assertEquals(promo.promoId, request.promoDetails?.promoId)
    }

    @Test
    fun shouldThrowUninitializedPropertyAccessExceptionWhenCardTokenIsNotInitialize() {
        Assert.assertThrows(
            UninitializedPropertyAccessException::class.java
        ) { OneClickCardPaymentRequestBuilder()
            .withPaymentType(PaymentType.CREDIT_CARD)
            .build() }
    }

    @Test
    fun shouldThrowInvalidPaymentTypeExceptionWhenPaymentTypeIsNotSupported() {
        Assert.assertThrows(
            InvalidPaymentTypeException::class.java
        ) { OneClickCardPaymentRequestBuilder()
            .withPaymentType(PaymentType.KLIK_BCA)
            .withMaskedCard(maskedCard)
            .build() }
    }
}