package com.midtrans.sdk.corekit.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SavedToken(
    val token: String? = null,
    val tokenType: String? = null,
    val maskedCard: String? = null,
    val expiresAt: String? = null,
    val fromHostApp: Boolean = false
) : Parcelable {
    companion object {
        const val ONE_CLICK = "one_click"
        const val TWO_CLICKS = "two_clicks"
    }
}
