package com.midtrans.sdk.uikit.internal.util
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.Duration
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.ISODateTimeFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

internal object DateTimeUtil : TimeProvider {
    private val indonesiaLocale = Locale("id")
    private const val DAY_FORMATTER = "ddMMMyyyy"
    private val dayFormatter = DateTimeFormat
        .forPattern(DAY_FORMATTER)
        .withLocale(indonesiaLocale)

    private const val COMPLETE_DATE_FORMATTER = "dd MMM yyyy"
    private val completeDateFormatter = DateTimeFormat
        .forPattern(COMPLETE_DATE_FORMATTER)
        .withLocale(indonesiaLocale)

    private const val COMPLETE_DATE_TIME_FORMATTER = "dd MMM yyyy HH:mm:ss"
    private val completeDateTimeFormatter = DateTimeFormat
        .forPattern(COMPLETE_DATE_TIME_FORMATTER)
        .withLocale(indonesiaLocale)

    private const val SHORT_TRANSACTION_TIME_FORMAT_PATTERN = "dd/MM/yy HH:mm"
    private val shortTransactionTimeFormatter = DateTimeFormat
        .forPattern(SHORT_TRANSACTION_TIME_FORMAT_PATTERN)
        .withLocale(indonesiaLocale)

    private const val TRANSACTION_TIME_FORMAT_PATTERN = "HH:mm"
    private val transactionTimeFormatter = DateTimeFormat
        .forPattern(TRANSACTION_TIME_FORMAT_PATTERN)
        .withLocale(indonesiaLocale)

    const val MILLIS_PER_DAY = 1 * 24 * 3600 * 1000

    const val minHour = "00:00:00"
    const val maxHour = "23:59:59"

    const val TIME_ZONE_UTC = "UTC"
    const val TIME_ZONE_JAKARTA = "Asia/Jakarta"

    const val DATE_TIME_FORMAT_COMPLETE = "dd-MM-yyyy HH:mm:ss"
    const val DATE_TIME_FORMAT_COMPLETE_2 = "dd MMM yyyy HH:mm:ss"
    const val DATE_TIME_FORMAT_COMPLETE_3 = "yyyy-MM-dd'T'HH:mm:ss"
    const val DATE_TIME_FORMAT_COMPLETE_4 = "dd MMM yyyy HH:mm:ss"
    const val DATE_TIME_FORMAT_COMPLETE_5 = "yyyy-MM-dd HH:mm:ss"

    const val DATE_TIME_FORMAT_COMPLETE_3_WITH_TIME_ZONE = "yyyy-MM-dd'T'HH:mm:ssZ"

    const val DATE_TIME_FORMAT_SIMPLE = "HH:mm, dd MMM"
    const val DATE_TIME_FORMAT_SIMPLE_2 = "dd MMM HH:mm a"
    const val CHART_TITLE_DAY_FORMAT = "EEE, dd MMM yyyy"
    const val CHART_TITLE_MONTHLY_FORMAT = "MMM yyyy"

    const val DATE_FORMAT = "dd MMM yyyy"
    const val CHART_DATE_FORMAT = "EEE\ndd"
    const val CHART_WEEKLY_FORMAT = "dd\nMMM"
    const val CHART_MONTHLY_FORMAT = "MMM"
    const val TIME_FORMAT = "HH:mm"


    fun generateCurrentTimeInIso8601(): String {
        val dateTime = ISODateTimeFormat.dateTime()
        return dateTime.print(DateTime.now(DateTimeZone.UTC))
    }

    fun generateCurrentDate(): String {
        return completeDateFormatter.print(LocalDateTime.now())
    }

    fun getFormattedDate(isoTime: String): String {
        return completeDateFormatter.print(
            getDateFromIso8601Time(isoTime)
        )
    }

    fun getFormattedDateTime(isoTime: String): String {
        return completeDateTimeFormatter.print(
            getDateFromIso8601Time(isoTime)
        )
    }

    fun getShortFormattedDateTime(isoTime: String): String {
        return shortTransactionTimeFormatter.print(
            getDateFromIso8601Time(isoTime)
        )
    }

    fun getIso8601DateFormatted(date: Date): String {
        return ISODateTimeFormat
            .dateTime()
            .print(DateTime(date))
    }

    fun getDateFromIso8601Time(iso8601time: String): DateTime {
        return if (iso8601time.contains(".")) {
            ISODateTimeFormat
                .dateTime()
                .parseDateTime(iso8601time)
        } else {
            ISODateTimeFormat
                .dateTimeNoMillis()
                .parseDateTime(iso8601time)
        }
    }

    fun getCurrentDayTime(): String = dayFormatter.print(DateTime.now(DateTimeZone.UTC))

    fun getCurrentTimeOnly(): String {
        val isoTime = DateTime.now(DateTimeZone.getDefault()).toString()
        val time = getDateFromIso8601Time(isoTime).millis
        return transactionTimeFormatter.print(time)
    }

    fun getTimeDiff(startTime: Long, stopTime: Long): Long {
        return TimeUnit.MILLISECONDS.toSeconds(stopTime - startTime)
    }

    fun getTimeDiffInMillis(startTime: Long, stopTime: Long): Long {
        return TimeUnit.MILLISECONDS.toMillis(stopTime - startTime)
    }

    fun getCurrentDateAndTime(): String {
        val simpleDateFormat = SimpleDateFormat(DATE_TIME_FORMAT_COMPLETE, Locale.getDefault())
        simpleDateFormat.timeZone = DateTimeZone.getDefault().toTimeZone()
        val dateTime = DateTime.now(DateTimeZone.getDefault())
        return simpleDateFormat.format(dateTime.toDate())
    }

    fun getCurrentDateTimeInFormat(format: String, locale: Locale = Locale.getDefault()): String {
        val simpleDateFormat = SimpleDateFormat(format, locale)
        return simpleDateFormat.format(Date())
    }

    fun getDate(
        date: String,
        dateFormat: String,
        timeZone: TimeZone = DateTimeZone.getDefault().toTimeZone(),
        locale: Locale = Locale.getDefault()
    ): Date {
        val sdf = SimpleDateFormat(dateFormat, locale)
        sdf.timeZone = timeZone
        return sdf.parse(date)
    }

    fun formatDateFromMillis(
        millis: Long,
        dateFormat: String,
        timeZone: TimeZone = DateTimeZone.getDefault().toTimeZone(),
        locale: Locale = Locale.getDefault()
    ): String {
        return formatDate(Date(millis), dateFormat, timeZone, locale)
    }

    /**
     * Format date object to another format.
     *
     * @param date the date, want to be formatted
     * @param dateFormat the format date pattern
     * @param timeZone timezone to use
     * @return formatted date
     */
    fun formatDate(
        date: Date,
        dateFormat: String,
        timeZone: TimeZone = DateTimeZone.getDefault().toTimeZone(),
        locale: Locale = Locale.getDefault()
    ): String {
        val formattedDate = SimpleDateFormat(dateFormat, locale)
        formattedDate.timeZone = timeZone
        return formattedDate.format(date)
    }

    /**
     * Format date string to another format.
     *
     * @param date the date, want to be formatted
     * @param fromFormat from format date pattern
     * @param toFormat to format date pattern
     * @param timeZoneTo timezone to use
     * @return formatted date
     */
    fun formatDate(
        date: String,
        fromFormat: String,
        toFormat: String,
        timeZoneFrom: TimeZone = DateTimeZone.getDefault().toTimeZone(),
        timeZoneTo: TimeZone = DateTimeZone.getDefault().toTimeZone()
    ): String {
        if (date.isBlank()) return ""
        val parseDate = SimpleDateFormat(fromFormat, Locale.getDefault())
        parseDate.timeZone = timeZoneTo
        val dateParsed = parseDate.parse(date)
        val formattedDate = SimpleDateFormat(toFormat, Locale.getDefault())
        formattedDate.timeZone = timeZoneFrom
        return formattedDate.format(dateParsed)
    }

    fun plusDateDays(date: Date, days: Int): Date {
        if (days < 0) return date

        return Date(date.time + (MILLIS_PER_DAY * days))
    }

    fun minusDateDays(date: Date, days: Int): Date {
        if (days < 0) return date

        return Date(date.time + (MILLIS_PER_DAY * -days))
    }

    /**
     * Checks if two date objects are on the same day ignoring time.
     *
     * @param date1 the first date, not altered, not null
     * @param date2 the second date, not altered, not null
     * @return true if they represent the same day
     */
    fun isSameDay(date1: Date, date2: Date): Boolean {
        val calendar1 = getCalendar(date1)
        val calendar2 = getCalendar(date2)

        return (calendar1.get(Calendar.ERA) == calendar2.get(Calendar.ERA)
            && calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
            && calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR))
    }

    /**
     * Returns a calendar object for the given date.
     * The calendar represents the date at 0 o'clock.
     *
     * @param date date to use for the calendar time
     * @return a calendar object
     */
    fun getCalendar(date: Date? = null): Calendar {
        var dateTime = DateTime()

        if (date != null) {
            dateTime = dateTime.withMillis(date.time)
        }
        dateTime = dateTime
            .withMillisOfDay(0)

        return Calendar.getInstance(DateTimeZone.getDefault().toTimeZone(), Locale.getDefault())
            .apply {
                date?.let {
                    time = dateTime.toDate()
                }
            }
    }

    /**
     * Creates a date.
     *
     * @param year  year
     * @param month month
     * @param date  date
     * @return date
     */
    fun getDate(year: Int, month: Int, date: Int): Date {
        val dateTime = DateTime(DateTimeZone.getDefault())
            .withYear(year)
            .withMonthOfYear(month)
            .withDayOfMonth(date)
            .withMillisOfDay(0)
        return dateTime.toDate()
    }

    fun getSystemDateInJakartaTimeZone(): String {
        val timeZone = TimeZone.getTimeZone(TIME_ZONE_JAKARTA)
        val dateFormat = SimpleDateFormat(DATE_TIME_FORMAT_COMPLETE_3_WITH_TIME_ZONE)
        dateFormat.timeZone = timeZone
        val dateTime = DateTime.now(DateTimeZone.forTimeZone(timeZone))
        return dateFormat.format(dateTime.toDate())
    }

    fun plusDateBy(time: Long, next: Int): Long {
        return DateTime(time, DateTimeZone.getDefault())
            .withMillisOfDay(0)
            .plusDays(next)
            .millis
    }

    fun getDateFromIsoResponse(
        initializedDate: Date,
        isoField13: String,
        isoField12: String
    ): Date {
        val initializedCalendar = Calendar.getInstance()
        initializedCalendar.time = initializedDate
        val isInitializedAtLastDecember =
            initializedCalendar.get(Calendar.DAY_OF_MONTH) == 31 && initializedCalendar.get(Calendar.MONTH) == Calendar.DECEMBER

        val responseCalendar = Calendar.getInstance()
        responseCalendar.time = initializedDate
        responseCalendar.set(
            Calendar.MONTH,
            getCalendarMonthFromMonthNumber(getMonthFromIsoField13(isoField13))
        )
        responseCalendar.set(Calendar.DAY_OF_MONTH, getDayFromIsoField13(isoField13))
        val isResponseAtJanuaryFirst =
            responseCalendar.get(Calendar.DAY_OF_MONTH) == 1 && responseCalendar.get(Calendar.MONTH) == Calendar.JANUARY

        if (isResponseAtJanuaryFirst && isInitializedAtLastDecember) {
            responseCalendar.add(Calendar.YEAR, 1)
        }
        return responseCalendar.time
    }

    private fun getCalendarMonthFromMonthNumber(monthNumber: Int): Int {
        return when (monthNumber) {
            1 -> Calendar.JANUARY
            2 -> Calendar.FEBRUARY
            3 -> Calendar.MARCH
            4 -> Calendar.APRIL
            5 -> Calendar.MAY
            6 -> Calendar.JUNE
            7 -> Calendar.JULY
            8 -> Calendar.AUGUST
            9 -> Calendar.SEPTEMBER
            10 -> Calendar.OCTOBER
            11 -> Calendar.NOVEMBER
            12 -> Calendar.DECEMBER
            else -> throw RuntimeException()
        }
    }

    private fun getDayFromIsoField13(isoField13: String) = isoField13.substring(2 until 4).toInt()

    private fun getMonthFromIsoField13(isoField13: String) = isoField13.substring(0 until 2).toInt()

    fun getCurrentMillis() = DateTime.now(DateTimeZone.getDefault()).millis

    fun updateTimeInCurrentMillisWithJakartaTimeZone(hour: Int, minute: Int): Long {
        return Calendar.getInstance().let { calendar ->
            calendar.timeZone = TimeZone.getTimeZone(TIME_ZONE_JAKARTA)
            calendar.timeInMillis = getCurrentMillis()
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            calendar.timeInMillis
        }
    }

    fun formatMillisToString(
        millis: Long,
        dateFormat: String,
        timeZone: TimeZone = DateTimeZone.getDefault().toTimeZone(),
        locale: Locale = Locale.getDefault()
    ): String {
        val date = Date(millis)
        val formattedDate = SimpleDateFormat(dateFormat, locale)
        formattedDate.timeZone = timeZone
        return formattedDate.format(date)
    }

    fun getDuration(millis: Long): Duration{
        return Duration.millis(millis)
    }

    fun getLocaleIndonesia() = indonesiaLocale

    override fun getDate(): Date {
        return DateTime.now().toDate()
    }

    override fun getTimeZone(): String {
        return DateTimeZone.getDefault().toTimeZone().displayName
    }
}