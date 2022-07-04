package com.midtrans.sdk.corekit.api.requestbuilder

import com.midtrans.sdk.corekit.api.exception.InvalidPaymentTypeException
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.requestbuilder.payment.CreditCardPaymentRequestBuilder
import org.junit.Assert
import org.junit.Test

internal class CreditCardPaymentRequestBuilderTest {

    private val cardToken = "cardToken"

    @Test
    fun shouldConstructCreditCardPaymentRequest() {
        val request = CreditCardPaymentRequestBuilder()
            .withPaymentType(PaymentType.CREDIT_CARD)
            .withCardToken(cardToken)
            .build()
        Assert.assertEquals(PaymentType.CREDIT_CARD, request.paymentType)
        Assert.assertEquals(cardToken, request.paymentParams?.cardToken)
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