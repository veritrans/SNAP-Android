package com.midtrans.sdk.uikit.internal.util

import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.time.Clock
import java.time.Instant

import java.util.*


class DateTimeUtilTest {
    val time = 1609907570000L //"Wed Jan 6 2021 11:32:50 +0700"// (Asia/Jakarta)
    val date = "06 Jan 2021"

    @Before
    fun setup() {
        val timeZone = TimeZone.getTimeZone("Asia/Jakarta")
        Instant.now(
            Clock.fixed(
            Instant.ofEpochMilli(time),
            timeZone.toZoneId()))
//        DateTimeZone.setDefault(DateTimeZone.forTimeZone(timeZone))
        Locale.setDefault(Locale("en", "US"))
    }

    @After
    fun cleanUp() {
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
    fun getCalendarShouldReturnCalendarOfDate() {
        val time = 1609907570066L //"Wed Jan 6 2021 11:32:50 +0700"// (Asia/Jakarta)
        val time2 = 1609866000000L ////"Wed Jan 6 2021 00:00:00 +0700"// (Asia/Jakarta)
        val result = DateTimeUtil.getCalendar(Date(time))
        Assert.assertEquals(Date(time2), result.time)

    }

    @Test
    fun plusDateByShouldAddingDateBy() {
        val time = 1609907570066L //"Wed Jan 6 2021 11:32:50 +0700"// (Asia/Jakarta)
        val time2 = 1609952400000L ////"Thu Jan 7 2021 00:00:00 +0700"// (Asia/Jakarta)
        val result = DateTimeUtil.plusDateBy(time, 1)
        Assert.assertEquals(time2, result)
    }

    @Test
    fun getCurrentMillis() {
        val result = DateTimeUtil.getCurrentMillis()
        Assert.assertEquals(time, result)
    }


}