package com.midtrans.sdk.corekit.api.requestbuilder

import com.midtrans.sdk.corekit.api.exception.InvalidPaymentTypeException
import com.midtrans.sdk.corekit.api.exception.MissingParameterException
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.requestbuilder.payment.DirectDebitPaymentRequestBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class DirectDebitPaymentRequestBuilderTest {

    @Test
    fun shouldConstructKlikBcaPaymentRequest() {
        val request = DirectDebitPaymentRequestBuilder()
            .withPaymentType(PaymentType.KLIK_BCA)
            .withKlikBcaUserId("User ID")
            .build()
        assertEquals(PaymentType.KLIK_BCA, request.paymentType)
        assertEquals("User ID", request.paymentParams?.userId)
    }

    @Test
    fun shouldThrowMissingParameterExceptionWhenUserIdKlikBcaIsNull() {
        assertThrows(MissingParameterException::class.java) {
            DirectDebitPaymentRequestBuilder()
                .withPaymentType(PaymentType.KLIK_BCA)
                .build()
        }
    }

    @Test
    fun shouldConstructDirectDebitPaymentRequest() {
        val klikPayRequest = DirectDebitPaymentRequestBuilder()
            .withPaymentType(PaymentType.BCA_KLIKPAY)
            .build()
        assertEquals(PaymentType.BCA_KLIKPAY, klikPayRequest.paymentType)

        val cimbRequest = DirectDebitPaymentRequestBuilder()
            .withPaymentType(PaymentType.CIMB_CLICKS)
            .build()
        assertEquals(PaymentType.CIMB_CLICKS, cimbRequest.paymentType)

        val danamonRequest = DirectDebitPaymentRequestBuilder()
            .withPaymentType(PaymentType.DANAMON_ONLINE)
            .build()
        assertEquals(PaymentType.DANAMON_ONLINE, danamonRequest.paymentType)

        val briEpayRequest = DirectDebitPaymentRequestBuilder()
            .withPaymentType(PaymentType.BRI_EPAY)
            .build()
        assertEquals(PaymentType.BRI_EPAY, briEpayRequest.paymentType)
    }

    @Test
    fun shouldThrowInvalidPaymentTypeExceptionWhenPaymentTypeIsNotSupported() {
        assertThrows(InvalidPaymentTypeException::class.java) {
            DirectDebitPaymentRequestBuilder()
                .withPaymentType(PaymentType.AKULAKU)
                .build()
        }
    }
}