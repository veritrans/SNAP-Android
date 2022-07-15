package com.midtrans.sdk.corekit.api.requestbuilder

import com.midtrans.sdk.corekit.api.exception.InvalidPaymentTypeException
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.requestbuilder.payment.CreditCardPaymentRequestBuilder
import com.midtrans.sdk.corekit.internal.util.NumberUtil
import org.junit.Assert
import org.junit.Test

internal class CreditCardPaymentRequestBuilderTest {

    private val cardToken = "cardToken"
    private val installment = "offline_3"
    private val discountedGrossAmount = 145000.0
    private val promoId = "promoId"

    @Test
    fun shouldConstructCreditCardPaymentRequestBasic() {
        val request = CreditCardPaymentRequestBuilder()
            .withPaymentType(PaymentType.CREDIT_CARD)
            .withCardToken(cardToken)
            .build()
        Assert.assertEquals(PaymentType.CREDIT_CARD, request.paymentType)
        Assert.assertEquals(cardToken, request.paymentParams?.cardToken)
    }

    @Test
    fun shouldConstructCreditCardPaymentRequestWithInstallment() {
        val request = CreditCardPaymentRequestBuilder()
            .withPaymentType(PaymentType.CREDIT_CARD)
            .withCardToken(cardToken)
            .withInstallment(installment)
            .build()
        Assert.assertEquals(PaymentType.CREDIT_CARD, request.paymentType)
        Assert.assertEquals(cardToken, request.paymentParams?.cardToken)
        Assert.assertEquals(installment, request.paymentParams?.installment)
    }

    @Test
    fun shouldConstructCreditCardPaymentRequestWithPromo() {
        val request = CreditCardPaymentRequestBuilder()
            .withPaymentType(PaymentType.CREDIT_CARD)
            .withCardToken(cardToken)
            .withPromo(discountedGrossAmount = discountedGrossAmount, promoId = promoId)
            .build()
        Assert.assertEquals(PaymentType.CREDIT_CARD, request.paymentType)
        Assert.assertEquals(cardToken, request.paymentParams?.cardToken)
        Assert.assertEquals(discountedGrossAmount?.let { NumberUtil.formatDoubleToString(it) }, request.promoDetails?.discountedGrossAmount)
        Assert.assertEquals(promoId, request.promoDetails?.promoId)
    }

    @Test
    fun shouldThrowUninitializedPropertyAccessExceptionWhenCardTokenIsNotInitialize() {
        Assert.assertThrows(
            UninitializedPropertyAccessException::class.java
        ) { CreditCardPaymentRequestBuilder()
            .withPaymentType(PaymentType.CREDIT_CARD)
            .build() }
    }

    @Test
    fun shouldThrowInvalidPaymentTypeExceptionWhenPaymentTypeIsNotSupported() {
        Assert.assertThrows(
            InvalidPaymentTypeException::class.java
        ) { CreditCardPaymentRequestBuilder()
            .withPaymentType(PaymentType.KLIK_BCA)
            .withCardToken(cardToken)
            .build() }
    }
}