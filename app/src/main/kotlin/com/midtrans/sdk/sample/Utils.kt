package com.midtrans.sdk.sample

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    fun getFormattedTime(time: Long): String {
        // Quoted "Z" to indicate UTC, no timezone offset
        val df: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z")
        df.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
        return df.format(Date(time))
    }
}