package com.midtrans.sdk.corekit.api.model

data class Promo(
    val enabled: Boolean,
    val allowedPromoCodes: List<String>
)
