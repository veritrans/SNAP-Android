package com.midtrans.sdk.corekit.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
open class Expiry(
    val startTime: String,
    val unit: String,
    val duration: Int
) : Parcelable {

    companion object {
        val UNIT_HOUR = "hours"
        val UNIT_MINUTE = "minutes"
        val UNIT_DAY = "days"
    }
}
