package com.midtrans.sdk.corekit.api.model

data class PaymentMethod(
    val type: String,
    val channels: List<String>
)