package com.midtrans.sdk.corekit.internal.network.model.request

import com.google.gson.annotations.SerializedName

/**
 * @author rakawm
 */
class SavedToken {
    var token: String? = null

    @SerializedName("token_type")
    var tokenType: String? = null

    @SerializedName("masked_card")
    var maskedCard: String? = null

    @SerializedName("expires_at")
    var expiresAt: String? = null
    private val fromHostApp = false

    companion object {
        const val ONE_CLICK = "one_click"
        const val TWO_CLICKS = "two_clicks"
    }
}