package com.midtrans.sdk.uikit.internal.util
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
        dateTime.withHour(0)

        return Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())
            .apply {
                date?.let {
                    time = Date(dateTime.toInstant(ZoneOffset.UTC).toEpochMilli())
                }
            }
    }

    fun plusDateBy(time: Long, next: Int): Long {
        return time + TimeUnit.DAYS.toMillis(next.toLong())
    }


    fun getCurrentMillis(): Long {
        val clock = Clock.systemDefaultZone()

        // get Instant Object of Clock object
        // in milliseconds using millis() method

        // get Instant Object of Clock object
        // in milliseconds using millis() method
        val milliseconds = clock.millis()
        return milliseconds
    }


    fun getDuration(millis: Long): Duration {
        return Duration.ofMillis(millis)
    }
}