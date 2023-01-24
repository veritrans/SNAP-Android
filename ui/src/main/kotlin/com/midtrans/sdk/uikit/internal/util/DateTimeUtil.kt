package com.midtrans.sdk.uikit.internal.util

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.*
import java.util.*
import java.util.concurrent.TimeUnit

internal object DateTimeUtil {


    fun getTimeDiffInMillis(startTime: Long, stopTime: Long): Long {
        return TimeUnit.MILLISECONDS.toMillis(stopTime - startTime)
    }


    fun getDate(
        date: String,
        dateFormat: String,
        timeZone: TimeZone = TimeZone.getDefault(),
        locale: Locale = Locale.getDefault()
    ): Date {
        val sdf = SimpleDateFormat(dateFormat, locale)
        sdf.timeZone = timeZone
        return sdf.parse(date)
    }


    /**
     * Returns a calendar object for the given date.
     * The calendar represents the date at 0 o'clock.
     *
     * @param date date to use for the calendar time
     * @return a calendar object
     */
    fun getCalendar(date: Date? = null): Calendar {
        var dateTime = LocalDateTime.now()

        if (date != null) {
            dateTime =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(date.time), ZoneId.systemDefault())
        }
        val dateCopy = dateTime.withHour(0).withMinute(0).withSecond(0).withNano(0)

        return Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())
            .apply {
                date?.let {
                    time = Date.from(dateCopy.toInstant(OffsetTime.now().offset))
                }
            }
    }

    fun plusDateBy(time: Long, next: Int): Long {
        return getCalendar(Date(time + TimeUnit.DAYS.toMillis(next.toLong()))).timeInMillis
    }


    fun getCurrentMillis(): Long {
        val clock = Clock.systemDefaultZone()

        // get Instant Object of Clock object
        // in milliseconds using millis() method

        // get Instant Object of Clock object
        // in milliseconds using millis() method
        return clock.millis()
    }


    fun getDuration(millis: Long): Duration {
        return Duration.ofMillis(millis)
    }

    fun getExpiredHour(expiredTime: Long): String {
        val duration = getDuration(
            getTimeDiffInMillis(
                getCurrentMillis(),
                expiredTime
            )
        )
        return String.format(
            "%02d:%02d:%02d",
            duration.toHours(),
            duration.seconds % 3600 / 60,
            duration.seconds % 60
        )
    }

    fun getExpiredSeconds(remainingTime: String): Long {
        return try {
            val dateFormat: DateFormat = SimpleDateFormat(TIME_FORMAT)
            dateFormat.timeZone = TIME_ZONE_UTC
            val date = dateFormat.parse(remainingTime) as Date
            TimeUnit.MILLISECONDS.toSeconds(date.time)
        } catch (e: Exception) {
            INVALID_FORMAT
        }
    }

    private const val INVALID_FORMAT: Long = 100
    const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss Z"
    const val TIME_FORMAT = "HH:mm:ss"
    val TIME_ZONE_UTC: TimeZone = TimeZone.getTimeZone("UTC")
}