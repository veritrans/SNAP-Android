package com.midtrans.sdk.sample.util

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object DemoUtils {

    fun getFormattedTime(time: Long): String {
        // Quoted "Z" to indicate UTC, no timezone offset
        val df: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z")
        df.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
        return df.format(Date(time))
    }
}