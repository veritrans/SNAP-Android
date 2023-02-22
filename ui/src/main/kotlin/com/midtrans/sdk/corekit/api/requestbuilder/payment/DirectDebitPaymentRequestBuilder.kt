package com.midtrans.sdk.corekit.api.requestbuilder.payment

import com.midtrans.sdk.corekit.api.exception.InvalidPaymentTypeException
import com.midtrans.sdk.corekit.api.exception.MissingParameterException
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.internal.network.model.request.PaymentParam
import com.midtrans.sdk.corekit.internal.network.model.request.PaymentRequest

class DirectDebitPaymentRequestBuilder : PaymentRequestBuilder() {
    private lateinit var paymentType: String
    private var userId: String? = null

    fun withPaymentType(@PaymentType.Def value: String): DirectDebitPaymentRequestBuilder = apply {
        paymentType = value
    }

    fun withKlikBcaUserId(value: String): DirectDebitPaymentRequestBuilder = apply {
        userId = value
    }

    override fun build(): PaymentRequest {
        return when (paymentType) {
            PaymentType.KLIK_BCA -> {
                if (userId == null)
                    throw MissingParameterException("userId required")
                PaymentRequest(paymentType = paymentType, paymentParams = PaymentParam(userId))
            }
            PaymentType.BCA_KLIKPAY,
            PaymentType.CIMB_CLICKS,
            PaymentType.DANAMON_ONLINE,
            PaymentType.BRI_EPAY,
            PaymentType.UOB_EZPAY -> PaymentRequest(paymentType)
            else -> throw InvalidPaymentTypeException()
        }
    }
}
