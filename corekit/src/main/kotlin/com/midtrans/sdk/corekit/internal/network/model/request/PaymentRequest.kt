package com.midtrans.sdk.corekit.internal.network.model.request

internal data class PaymentRequest(
    val paymentType: String,
    val customerDetails: CustomerDetailRequest? = null,
    val paymentParams: PaymentParam? = null
)
