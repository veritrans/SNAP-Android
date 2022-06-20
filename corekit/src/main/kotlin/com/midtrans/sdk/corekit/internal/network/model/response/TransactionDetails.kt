package com.midtrans.sdk.corekit.internal.network.model.response

internal data class TransactionDetails(
    var orderId: String? = null,
    var grossAmount: Double = 0.0,
    val currency: String? = null
)