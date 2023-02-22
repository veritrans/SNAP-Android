package com.midtrans.sdk.corekit.internal.network.model.request

internal data class PromoDetailRequest(
    val discountedGrossAmount: String,
    val promoId: String
)
