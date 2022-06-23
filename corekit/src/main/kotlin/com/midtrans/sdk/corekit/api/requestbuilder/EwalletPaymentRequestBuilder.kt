package com.midtrans.sdk.corekit.api.requestbuilder

import com.midtrans.sdk.corekit.api.exception.InvalidPaymentTypeException
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.internal.network.model.request.PaymentParam
import com.midtrans.sdk.corekit.internal.network.model.request.PaymentRequest

class EwalletPaymentRequestBuilder : PaymentRequestBuilder() {
    private lateinit var paymentType: String

    fun withPaymentType(@PaymentType.Def value: String): EwalletPaymentRequestBuilder = apply {
        paymentType = value
    }

    override fun build(): PaymentRequest {
        return when (paymentType) {
            PaymentType.SHOPEEPAY_QRIS -> {
                PaymentRequest(
                    paymentType = PaymentType.QRIS,
                    paymentParams = PaymentParam(acquirer = listOf(PaymentType.SHOPEEPAY))
                )
            }
            PaymentType.SHOPEEPAY,
            PaymentType.GOPAY -> PaymentRequest(paymentType = paymentType)
            else -> throw InvalidPaymentTypeException()
        }
    }
}