package com.midtrans.sdk.corekit.internal.network.model.response

import com.google.gson.annotations.SerializedName

/**
 * Created by ziahaqi on 12/22/17.
 */
internal class Promo : Cloneable {
    @SerializedName("id")
    val id: Long = 0

    @SerializedName("name")
    val name: String? = null

    @SerializedName("bins")
    val bins: List<String>? = null

    @SerializedName("payment_types")
    val paymentTypes: List<String>? = null

    @SerializedName("calculated_discount_amount")
    val calculatedDiscountAmount = 0.0

    @SerializedName("discounted_gross_amount")
    val discountedGrossAmount = 0.0

    @SerializedName("discount_type")
    val discountType: String? = null

    @SerializedName("start_date")
    val startDate: String? = null

    @SerializedName("end_date")
    val endDate: String? = null

    @SerializedName("promo_code")
    val promoCode: String? = null

    @SerializedName("sponsor_name")
    val sponsorName: String? = null

    @SerializedName("sponsor_message_en")
    val sponsorMessageEn: String? = null

    @SerializedName("sponsor_message_id")
    val sponsorMessageId: String? = null

    @SerializedName("promo_token")
    val promoToken: String? = null
    var isSelected = false

    @Throws(CloneNotSupportedException::class)
    public override fun clone(): Any {
        return super.clone()
    }
}
