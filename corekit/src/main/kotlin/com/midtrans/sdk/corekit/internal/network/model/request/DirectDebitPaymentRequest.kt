package com.midtrans.sdk.corekit.internal.network.model.request

internal data class DirectDebitPaymentRequest(
    val paymentType: String,
    val paymentParams: PaymentRequestParam? = null
)
