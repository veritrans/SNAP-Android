package com.midtrans.sdk.corekit.api.model

import com.google.gson.annotations.SerializedName

/**
 * Created by ziahaqi on 7/19/16.
 */
class ExpiryModel {
    @SerializedName("start_time")
    var startTime: String? = null

    @SerializedName("unit")
    var unit: String? = null

    @SerializedName("duration")
    var duration = 0

    companion object {
        const val UNIT_HOUR = "hours"
        const val UNIT_MINUTE = "minutes"
        const val UNIT_DAY = "days"
    }
}
