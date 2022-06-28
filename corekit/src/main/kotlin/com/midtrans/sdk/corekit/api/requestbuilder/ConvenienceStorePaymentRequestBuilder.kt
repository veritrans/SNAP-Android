package com.midtrans.sdk.corekit.api.requestbuilder

import com.midtrans.sdk.corekit.api.exception.InvalidPaymentTypeException
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.internal.network.model.request.PaymentRequest

class ConvenienceStorePaymentRequestBuilder : PaymentRequestBuilder() {
    private lateinit var paymentType: String

    fun withPaymentType(@PaymentType.Def value: String): ConvenienceStorePaymentRequestBuilder = apply {
        paymentType = value
    }

    override fun build(): PaymentRequest {
        return when(paymentType) {
            PaymentType.ALFAMART,
            PaymentType.INDOMARET -> PaymentRequest(paymentType = paymentType)
            else -> throw InvalidPaymentTypeException()
        }
    }
}