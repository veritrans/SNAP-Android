package com.midtrans.sdk.corekit.api.requestbuilder

import com.midtrans.sdk.corekit.api.exception.InvalidPaymentTypeException
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.requestbuilder.payment.OneClickCardPaymentRequestBuilder
import com.midtrans.sdk.corekit.internal.network.model.request.PromoDetailRequest
import com.midtrans.sdk.corekit.internal.util.NumberUtil
import org.junit.Assert
import org.junit.Test

internal class OneClickCardPaymentRequestBuilderTest{
    private val maskedCard = "maskedCard"
    private val installment = "offline_3"
    private val discountedGrossAmount = 145000.0
    private val promoId = "promoId"

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
            .withPromo(discountedGrossAmount = discountedGrossAmount, promoId = promoId)
            .build()
        Assert.assertEquals(PaymentType.CREDIT_CARD, request.paymentType)
        Assert.assertEquals(maskedCard, request.paymentParams?.maskedCard)
        Assert.assertEquals(discountedGrossAmount?.let { NumberUtil.formatDoubleToString(it) }, request.promoDetails?.discountedGrossAmount)
        Assert.assertEquals(promoId, request.promoDetails?.promoId)
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