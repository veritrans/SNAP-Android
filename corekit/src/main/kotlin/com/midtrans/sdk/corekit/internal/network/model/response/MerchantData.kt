package com.midtrans.sdk.corekit.internal.network.model.response

import com.google.gson.annotations.SerializedName

/**
 * @author rakawm
 */
internal data class MerchantData(
    val preference: MerchantPreferences? = null,

    @SerializedName("client_key")
    val clientKey: kotlin.String? = null,

    @SerializedName("enabled_principles")
    val enabledPrinciples: kotlin.collections.List<kotlin.String>? = null,

    @SerializedName("point_banks")
    val pointBanks: java.util.ArrayList<kotlin.String>? = null,

    @SerializedName("merchant_id")
    val merchantId: kotlin.String? = null,

    @SerializedName("acquiring_banks")
    val acquiringBanks: kotlin.collections.List<kotlin.String>? = null,

    @SerializedName("priority_card_feature")
    val priorityCardFeature: kotlin.String? = null,

    @SerializedName("recurring_mid_is_active")
    val recurringMidIsActive: kotlin.Boolean? = null

)
