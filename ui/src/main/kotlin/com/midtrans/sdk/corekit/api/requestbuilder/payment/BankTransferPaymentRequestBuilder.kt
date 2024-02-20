package com.midtrans.sdk.corekit.api.requestbuilder.payment

import com.midtrans.sdk.corekit.api.exception.InvalidPaymentTypeException
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.internal.network.model.request.CustomerDetailRequest
import com.midtrans.sdk.corekit.internal.network.model.request.PaymentRequest

class BankTransferPaymentRequestBuilder: PaymentRequestBuilder() {
    private lateinit var paymentType: String
    private var customerEmail: String? = null

    fun withPaymentType(@PaymentType.Def value: String): BankTransferPaymentRequestBuilder = apply {
        paymentType = value
    }

    fun withCustomerEmail(value: String): BankTransferPaymentRequestBuilder = apply {
        customerEmail = value
    }

    override fun build(): PaymentRequest {
        return when (paymentType) {
            PaymentType.BCA_VA,
            PaymentType.PERMATA_VA,
            PaymentType.BNI_VA,
            PaymentType.CIMB_VA,
            PaymentType.BRI_VA,
            PaymentType.OTHER_VA,
            PaymentType.E_CHANNEL -> PaymentRequest(
                paymentType = paymentType,
                customerDetails = customerEmail?.let { CustomerDetailRequest(email = customerEmail) }
            )
            else -> throw InvalidPaymentTypeException()
        }
    }
}