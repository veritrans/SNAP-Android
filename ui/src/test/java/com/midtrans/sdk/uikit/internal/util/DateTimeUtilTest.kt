package com.midtrans.sdk.uikit.internal.util

import org.joda.time.DateTimeUtils
import org.joda.time.DateTimeZone
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.*


class DateTimeUtilTest {
    val time = 1609907570000L //"Wed Jan 6 2021 11:32:50 +0700"// (Asia/Jakarta)
    val date = "06 Jan 2021"

    @Before
    fun setup() {
        val timeZone = TimeZone.getTimeZone("Asia/Jakarta")
        DateTimeUtils.setCurrentMillisFixed(time)
        DateTimeZone.setDefault(DateTimeZone.forTimeZone(timeZone))
        Locale.setDefault(Locale("en", "US"))
    }

    @After
    fun cleanUp() {
    }

    @Test
    fun getFormattedDate() {
        val date = "2021-01-07T07:52:28+00:00"
        val expectedDate = "07 Jan 2021"
        val result = DateTimeUtil.getFormattedDate(date)
        Assert.assertEquals(expectedDate, result)
    }

    @Test
    fun generateCurrentDate() {
        val result = DateTimeUtil.generateCurrentDate()
        Assert.assertEquals(date, result)
    }

    @Test
    fun getCurrentDayTime() {
        val expected = "06Jan2021"
        val result = DateTimeUtil.getCurrentDayTime()
        Assert.assertEquals(expected, result)
    }

    @Test
    fun getCurrentTimeOnly() {
        val expected = "11:32"
        val result = DateTimeUtil.getCurrentTimeOnly()
        Assert.assertEquals(expected, result)
    }

    @Test
    fun getIso8601DateFormatted() {
        val expected = "2021-01-06T11:32:50.000+07:00"
        val result = DateTimeUtil.getIso8601DateFormatted(Date(time))
        Assert.assertEquals(expected, result)
    }

    @Test
    fun generateCurrentTimeInIso8601() {
        val expected = "2021-01-06T04:32:50.000Z"
        val result = DateTimeUtil.generateCurrentTimeInIso8601()
        Assert.assertEquals(expected, result)
    }

    @Test
    fun getFormattedDateTime() {
        val expected = "06 Jan 2021 11:32:50"
        val dateTime = "2021-01-06T11:32:50.066+07:00"
        val result = DateTimeUtil.getFormattedDateTime(dateTime)
        Assert.assertEquals(expected, result)
    }

    @Test
    fun getTimeDiffReturnTimeDifferenceInSecond() {
        val startTime = 2000L
        val stopTime = 3000L
        val expectedTimeDiff = 1L //in second

        val result = DateTimeUtil.getTimeDiff(startTime, stopTime)
        Assert.assertEquals(expectedTimeDiff, result)
    }

    @Test
    fun getTimeDiffInMillisReturnDifferenceInMs() {
        val startTime = 2000L
        val stopTime = 3000L
        val expectedTimeDiff = 1000L //in ms

        val result = DateTimeUtil.getTimeDiffInMillis(startTime, stopTime)
        Assert.assertEquals(expectedTimeDiff, result)
    }

    @Test
    fun formatDateShouldFormattingDateCorrectly() {
        val expected = "Wed Jan 6 2021 11:32:50 +0700"// (Asia/Jakarta)
        val time = 1609907570066L
        val date = Date(time)
        val timeZone = TimeZone.getTimeZone("Asia/Jakarta")
        val result = DateTimeUtil.formatDate(date, "EE MMM d yyyy hh:mm:ss Z")
        val result2 = DateTimeUtil.formatDate(date, "EE MMM d yyyy hh:mm:ss Z", timeZone)
        Assert.assertEquals(expected, result)
        Assert.assertEquals(expected, result2)

        val result3 = DateTimeUtil.formatDate(
            expected,
            "EE MMM d yyyy hh:mm:ss Z",
            "EE MMM d yyyy hh:mm:ss Z",
            timeZone,
            timeZone
        )
        Assert.assertEquals(expected, result3)
        val result4 = DateTimeUtil.formatDate(
            expected,
            "EE MMM d yyyy hh:mm:ss Z",
            "EE MMM d yyyy hh:mm:ss Z"
        )
        Assert.assertEquals(expected, result4)
    }

    @Test
    fun formatDateFromMillisShouldFormattingDateCorrectly() {
        val expected = "Wed Jan 6 2021 11:32:50 +0700"// (Asia/Jakarta)
        val time = 1609907570066L
        val timeZone = TimeZone.getTimeZone("Asia/Jakarta")
        val result = DateTimeUtil.formatDateFromMillis(time, "EE MMM d yyyy hh:mm:ss Z")
        val result2 = DateTimeUtil.formatDateFromMillis(time, "EE MMM d yyyy hh:mm:ss Z", timeZone)
        Assert.assertEquals(expected, result)
        Assert.assertEquals(expected, result2)
    }

    @Test
    fun plusDateDaysShouldAddingDays() {
        val time = 1609907570066L //"Wed Jan 6 2021 11:32:50 +0700"// (Asia/Jakarta)
        val date = Date(time)
        val time2 = 1609993970066 ////"Thu Jan 7 2021 11:32:50 +0700"// (Asia/Jakarta)
        val date2 = Date(time2)
        val result1 = DateTimeUtil.plusDateDays(date, -1)
        Assert.assertEquals(date, result1)
        val result2 = DateTimeUtil.plusDateDays(date, 1)
        Assert.assertEquals(date2, result2)
    }

    @Test
    fun minusDateDaysShouldSubtractingDays() {
        val time = 1609907570066L //"Wed Jan 6 2021 11:32:50 +0700"// (Asia/Jakarta)
        val date = Date(time)
        val time2 = 1609993970066 ////"Thu Jan 7 2021 11:32:50 +0700"// (Asia/Jakarta)
        val date2 = Date(time2)
        val result1 = DateTimeUtil.minusDateDays(date2, -1)
        Assert.assertEquals(date2, result1)
        val result2 = DateTimeUtil.minusDateDays(date2, 1)
        Assert.assertEquals(date, result2)
    }

    @Test
    fun isSameDayShouldReturnTrueIfDateIsSameDay() {

        val time = 1609915813802L  //Wed Jan 6 2021 13:50:13 +0700// (Asia/Jakarta)
        val time2 = 1609993970066 ////"Thu Jan 7 2021 11:32:50 +0700"// (Asia/Jakarta)
        val time3 = 1609907570066L //"Wed Jan 6 2021 11:32:50 +0700"// (Asia/Jakarta)

        Assert.assertTrue(DateTimeUtil.isSameDay(Date(time), Date(time3)))
        Assert.assertFalse(DateTimeUtil.isSameDay(Date(time), Date(time2)))
    }

    @Test
    fun getCalendarShouldReturnCalendarOfDate() {
        val time = 1609907570066L //"Wed Jan 6 2021 11:32:50 +0700"// (Asia/Jakarta)
        val time2 = 1609866000000L ////"Wed Jan 6 2021 00:00:00 +0700"// (Asia/Jakarta)
        val result = DateTimeUtil.getCalendar(Date(time))
        Assert.assertEquals(Date(time2), result.time)

    }

    @Test
    fun getDateShouldReturnDateSpecified() {
        val time = 1609866000000L ////"Wed Jan 6 2021 00:00:00 +0700"// (Asia/Jakarta)

        val result = DateTimeUtil.getDate(2021, 1, 6)
        Assert.assertEquals(Date(time), result)
    }

    @Test
    fun plusDateByShouldAddingDateBy() {
        val time = 1609907570066L //"Wed Jan 6 2021 11:32:50 +0700"// (Asia/Jakarta)
        val time2 = 1609952400000L ////"Thu Jan 7 2021 00:00:00 +0700"// (Asia/Jakarta)
        val result = DateTimeUtil.plusDateBy(time, 1)
        Assert.assertEquals(time2, result)
    }

    @Test
    fun getSystemDateInJakartaTimeZone() {
        val expected = "2021-01-06T11:32:50+0700"
        val result = DateTimeUtil.getSystemDateInJakartaTimeZone()
        Assert.assertEquals(expected, result)
    }

    @Test
    fun getCurrentMillis() {
        val result = DateTimeUtil.getCurrentMillis()
        Assert.assertEquals(time, result)
    }

    @Test
    fun updateTimeInCurrentMillis() {
        val result = DateTimeUtil.updateTimeInCurrentMillisWithJakartaTimeZone(22,0)
        val expected = 1609945200000L
        Assert.assertEquals(expected, result)
    }

    @Test
    fun getDate(){
        val dateformat = "yyyy-MM-dd'T'HH:mm:ssZ"
        val date = "2021-01-06T11:32:50+0700"
        val dateDate = Date(time)
        val timeZone = TimeZone.getTimeZone("Asia/Jakarta")

        val result = DateTimeUtil.getDate(date, dateformat, timeZone)
        Assert.assertEquals(dateDate, result)
        val result2 = DateTimeUtil.getDate(date, dateformat)
        Assert.assertEquals(dateDate, result2)

        val result3 = DateTimeUtil.getDate()
        Assert.assertEquals(dateDate, result3)
    }

    @Test
    fun getCurrentDateAndTime() {
        val expected = "06-01-2021 11:32:50"
        val result = DateTimeUtil.getCurrentDateAndTime()
        Assert.assertEquals(expected, result)
    }

    @Test
    fun getTimeZone() {
        Assert.assertEquals(
            DateTimeZone.getDefault().toTimeZone().displayName,
            DateTimeUtil.getTimeZone()
        )
    }

    @Test
    fun getDateTimeMillisWhenDateMoveFromLastDecemberToFirstJanuaryReturnDateWithCorrectYear() {
        val lastDecember1989InMillis = 631080671000
        val firstJanuaryInMmDd = "0101"
        val timeElevenElevenEleven = "111111"

        val result = DateTimeUtil.getDateFromIsoResponse(
            Date(lastDecember1989InMillis),
            firstJanuaryInMmDd,
            timeElevenElevenEleven
        )

        val cal = Calendar.getInstance()
        cal.setTime(result)
        Assert.assertEquals(1990, cal.get(Calendar.YEAR))
    }

    @Test
    fun getDateTimeMillisWhenDateIsNotChangingReturnSameDate() {
        val firstNovember1990InMillis = 657432671000
        val firstNovemberInMmDd = "1101"
        val timeElevenElevenEleven = "111111"

        val result = DateTimeUtil.getDateFromIsoResponse(Date(firstNovember1990InMillis), firstNovemberInMmDd, timeElevenElevenEleven)
        val cal = Calendar.getInstance()
        cal.setTime(result)
        Assert.assertEquals(1990, cal.get(Calendar.YEAR))
    }
}