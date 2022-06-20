package com.midtrans.sdk.corekit.internal.network.model.response

internal data class PromoResponse(
    val id: Int = 0,
    val bins: List<String>? = null,
    val discountAmount: Double = 0.0,
    val startDate: String? = null,
    val endDate: String? = null,
    val discountType: String? = null,
    val promoCode: String? = null,
    val sponsorName: String? = null,
    val sponsorMessageEn: String? = null,
    val sponsorMessageId: String? = null
)
