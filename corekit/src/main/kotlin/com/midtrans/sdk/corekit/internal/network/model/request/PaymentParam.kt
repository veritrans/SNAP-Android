package com.midtrans.sdk.corekit.internal.network.model.request

internal data class PaymentParam(
    val userId: String? = null,
    var acquirer: List<String>? = null
)
