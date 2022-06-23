package com.midtrans.sdk.corekit.api.requestbuilder

import com.midtrans.sdk.corekit.api.exception.InvalidPaymentTypeException
import com.midtrans.sdk.corekit.api.model.PaymentType
import org.junit.Assert
import org.junit.Test

internal class EWalletRequestBuilderTest {

    @Test
    fun shouldConstructGopayRequest() {
        val obj = EWalletPaymentRequestBuilder().withPaymentType(PaymentType.GOPAY).build()
        Assert.assertEquals(PaymentType.GOPAY, obj.paymentType)
    }

    @Test
    fun shouldConstructShoopepayRequest() {
        val obj = EWalletPaymentRequestBuilder().withPaymentType(PaymentType.SHOPEEPAY).build()
        Assert.assertEquals(PaymentType.SHOPEEPAY, obj.paymentType)
    }

    @Test
    fun shouldConstructShoopepayQrisRequest() {
        val obj = EWalletPaymentRequestBuilder().withPaymentType(PaymentType.SHOPEEPAY_QRIS).build()
        Assert.assertEquals(PaymentType.QRIS, obj.paymentType)
        Assert.assertEquals(PaymentType.SHOPEEPAY, obj.paymentParams!!.acquirer!![0])
    }

    @Test
    fun shouldErrorOnUnsupportedPaymentType() {
        Assert.assertThrows(
            InvalidPaymentTypeException::class.java
        ) { EWalletPaymentRequestBuilder().withPaymentType(PaymentType.BNI_VA).build() }
    }
}