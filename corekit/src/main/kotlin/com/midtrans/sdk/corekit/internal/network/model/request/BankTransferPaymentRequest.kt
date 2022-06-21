package com.midtrans.sdk.corekit.internal.network.model.request

internal data class BankTransferPaymentRequest(
    val paymentType: String, val customerDetails: CustomerDetailRequest
)
