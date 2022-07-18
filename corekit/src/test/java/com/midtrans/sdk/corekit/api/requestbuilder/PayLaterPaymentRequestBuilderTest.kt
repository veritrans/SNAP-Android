package com.midtrans.sdk.corekit.api.requestbuilder

import com.midtrans.sdk.corekit.api.exception.InvalidPaymentTypeException
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.requestbuilder.payment.PayLaterPaymentRequestBuilder
import org.junit.Assert
import org.junit.Test

internal class PayLaterPaymentRequestBuilderTest {

    @Test
    fun shouldConstructAkulakuPaymentRequest() {
        val request = PayLaterPaymentRequestBuilder().withPaymentType(PaymentType.AKULAKU).build()
        Assert.assertEquals(PaymentType.AKULAKU, request.paymentType)
    }

    @Test
    fun shouldThrowInvalidPaymentTypeExceptionWhenPaymentTypeIsNotSupported() {
        Assert.assertThrows(
            InvalidPaymentTypeException::class.java
        ) { PayLaterPaymentRequestBuilder().withPaymentType(PaymentType.KLIK_BCA).build() }
    }
}