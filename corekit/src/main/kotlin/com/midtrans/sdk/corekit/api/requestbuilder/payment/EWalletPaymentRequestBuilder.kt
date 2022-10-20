package com.midtrans.sdk.corekit.api.requestbuilder.payment

import com.midtrans.sdk.corekit.api.exception.InvalidPaymentTypeException
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.internal.network.model.request.PaymentParam
import com.midtrans.sdk.corekit.internal.network.model.request.PaymentRequest

class EWalletPaymentRequestBuilder : PaymentRequestBuilder() {
    private lateinit var paymentType: String

    fun withPaymentType(@PaymentType.Def value: String): EWalletPaymentRequestBuilder = apply {
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
            PaymentType.GOPAY_QRIS -> {
                PaymentRequest(
                    paymentType = PaymentType.QRIS,
                    paymentParams = PaymentParam(acquirer = listOf(PaymentType.GOPAY))
                )
            }
            PaymentType.SHOPEEPAY,
            PaymentType.GOPAY -> PaymentRequest(paymentType = paymentType)
            else -> throw InvalidPaymentTypeException("Supported PaymentType are: GOPAY, SHOPEEPAY, SHOPEEPAY_QRIS")
        }
    }
}