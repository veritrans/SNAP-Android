package com.midtrans.sdk.corekit.internal.network.model.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by rakawm on 1/2/17.
 */
internal data class PromoResponse(
    val id: Int = 0,
    val bins: List<String>? = null,

    @SerializedName("discount_amount")
    val discountAmount: Double = 0.0,

    @SerializedName("start_date")
    val startDate: String? = null,

    @SerializedName("end_date")
    val endDate: String? = null,

    @SerializedName("discount_type")
    val discountType: String? = null,

    @SerializedName("promo_code")
    val promoCode: String? = null,

    @SerializedName("sponsor_name")
    val sponsorName: String? = null,

    @SerializedName("sponsor_message_en")
    val sponsorMessageEn: String? = null,

    @SerializedName("sponsor_message_id")
    val sponsorMessageId: String? = null
)
