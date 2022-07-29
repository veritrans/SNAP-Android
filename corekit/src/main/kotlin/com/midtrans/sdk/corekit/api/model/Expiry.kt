package com.midtrans.sdk.corekit.api.model

data class Expiry(
    val startTime: String,
    val unit: String,
    val duration: Int
) {

    companion object {
        val UNIT_HOUR = "hours"
        val UNIT_MINUTE = "minutes"
        val UNIT_DAY = "days"
    }
}
