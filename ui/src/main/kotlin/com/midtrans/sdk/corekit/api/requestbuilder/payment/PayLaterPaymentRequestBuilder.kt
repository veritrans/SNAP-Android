package com.midtrans.sdk.corekit.api.requestbuilder.payment

import com.midtrans.sdk.corekit.api.exception.InvalidPaymentTypeException
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.internal.network.model.request.PaymentRequest

class PayLaterPaymentRequestBuilder : PaymentRequestBuilder() {
    private lateinit var paymentType: String

    fun withPaymentType(@PaymentType.Def value: String): PayLaterPaymentRequestBuilder = apply {
        paymentType = value
    }

    override fun build(): PaymentRequest {
        return when (paymentType) {
            PaymentType.AKULAKU,
            PaymentType.KREDIVO -> PaymentRequest(paymentType = paymentType)
            else -> throw InvalidPaymentTypeException()
        }
    }
}