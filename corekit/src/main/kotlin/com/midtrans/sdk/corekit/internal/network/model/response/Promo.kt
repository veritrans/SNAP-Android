package com.midtrans.sdk.corekit.internal.network.model.response

internal data class Promo(
    val id: Long = 0,
    val name: String? = null,
    val bins: List<String>? = null,
    val paymentTypes: List<String>? = null,
    val calculatedDiscountAmount: Double = 0.0,
    val discountedGrossAmount: Double = 0.0,
    val discountType: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val promoCode: String? = null,
    val sponsorName: String? = null,
    val sponsorMessageEn: String? = null,
    val sponsorMessageId: String? = null,
    val promoToken: String? = null,
    val isSelected: Boolean = false
)
