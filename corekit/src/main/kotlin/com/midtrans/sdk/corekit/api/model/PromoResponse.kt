package com.midtrans.sdk.corekit.api.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PromoResponse(
    val id: Int = 0,
    val bins: List<String>? = null,
    val paymentTypes: List<String>? = null,
    val discountAmount: Double = 0.0,
    val startDate: String? = null,
    val endDate: String? = null,
    val discountType: String? = null,
    val promoCode: String? = null,
    val sponsorName: String? = null,
    val sponsorMessageEn: String? = null,
    val sponsorMessageId: String? = null
): Parcelable
