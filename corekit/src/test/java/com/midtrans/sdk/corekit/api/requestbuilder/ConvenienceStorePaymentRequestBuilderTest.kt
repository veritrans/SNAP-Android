package com.midtrans.sdk.corekit.api.requestbuilder

import com.midtrans.sdk.corekit.api.model.PaymentType
import org.junit.Assert
import org.junit.Test

internal class ConvenienceStorePaymentRequestBuilderTest {

    @Test
    fun shouldConstructIndomaretPaymentRequest() {
        val request = ConvenienceStorePaymentRequestBuilder().withPaymentType(PaymentType.INDOMARET).build()
        Assert.assertEquals(PaymentType.INDOMARET, request.paymentType)
    }

    @Test
    fun shouldConstructAlfamartPaymentRequest() {
        val request = ConvenienceStorePaymentRequestBuilder().withPaymentType(PaymentType.ALFAMART).build()
        Assert.assertEquals(PaymentType.ALFAMART, request.paymentType)
    }
}