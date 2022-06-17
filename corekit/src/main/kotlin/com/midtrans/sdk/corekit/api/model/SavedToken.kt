package com.midtrans.sdk.corekit.api.model

import com.google.gson.annotations.SerializedName

/**
 * @author rakawm
 */
data class SavedToken(
    val token: String? = null,

    @SerializedName("token_type")
    val tokenType: String? = null,

    @SerializedName("masked_card")
    val maskedCard: String? = null,

    @SerializedName("expires_at")
    val expiresAt: String? = null,
    val fromHostApp: Boolean = false
) {
    companion object {
        const val ONE_CLICK = "one_click"
        const val TWO_CLICKS = "two_clicks"
    }
}