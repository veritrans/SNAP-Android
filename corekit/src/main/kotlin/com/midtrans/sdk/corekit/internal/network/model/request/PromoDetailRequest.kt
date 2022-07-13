package com.midtrans.sdk.corekit.internal.network.model.request

data class PromoDetailRequest(
    val discountedGrossAmount: Double? = null,
    val promoId: String? = null,
)
