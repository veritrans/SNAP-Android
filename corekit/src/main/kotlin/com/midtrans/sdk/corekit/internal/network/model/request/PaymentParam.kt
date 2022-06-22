package com.midtrans.sdk.corekit.internal.network.model.request

internal data class PaymentParam(
    val userId: String? = null,
    val cardToken: String? = null,
    val saveCard: Boolean = false
)
