package com.midtrans.sdk.corekit.internal.network.model.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by rakawm on 1/2/17.
 */
internal class PromoResponse : Serializable {
    var id = 0
    var bins: List<String>? = null

    @SerializedName("discount_amount")
    var discountAmount = 0

    @SerializedName("start_date")
    var startDate: String? = null

    @SerializedName("end_date")
    var endDate: String? = null

    @SerializedName("discount_type")
    var discountType: String? = null

    @SerializedName("promo_code")
    var promoCode: String? = null

    @SerializedName("sponsor_name")
    var sponsorName: String? = null

    @SerializedName("sponsor_message_en")
    var sponsorMessageEn: String? = null

    @SerializedName("sponsor_message_id")
    var sponsorMessageId: String? = null
}