package com.midtrans.sdk.corekit.internal.network.model.request

internal data class PaymentParam(
    val userId: String? = null,
    val cardToken: String? = null,
    val saveCard: Boolean = false,
    var acquirer: List<String>? = null,
    val point: Double? = null,
    val bank: String? = null,
    val installment: String? = null,
    val maskedCard: String? = null
)
