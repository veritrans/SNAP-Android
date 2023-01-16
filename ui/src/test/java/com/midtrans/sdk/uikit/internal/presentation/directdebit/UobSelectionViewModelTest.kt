package com.midtrans.sdk.uikit.internal.presentation.directdebit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.internal.analytics.EventAnalytics
import com.midtrans.sdk.corekit.internal.analytics.PageName
import com.midtrans.sdk.uikit.internal.util.DateTimeUtil
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.*
import java.time.Duration
import java.util.*

internal class UobSelectionViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val snapCore: SnapCore = mock()

    private val dateTimeUtil: DateTimeUtil = mock()

    private val eventAnalytics: EventAnalytics = mock()

    private lateinit var viewModel: UobSelectionViewModel

    @Before
    fun setup() {
        whenever(snapCore.getEventAnalytics()) doReturn eventAnalytics
        viewModel = UobSelectionViewModel(snapCore, dateTimeUtil)
    }

    @Test
    fun getExpiredHourShouldReturnHHMMSS() {
        val snapCore: SnapCore = mock()
        val dateTimeUtil: DateTimeUtil = mock()
        Mockito.`when`(
            dateTimeUtil.getDate(
                date = eq("2022-01-06 11:32:50 +0700"),
                dateFormat = eq("yyyy-MM-dd HH:mm:ss Z"),
                timeZone = any(),
                locale = any()
            )
        ).thenReturn(
            Date(1609907570066L)//"Wed Jan 6 2021 11:32:50 +0700"// (Asia/Jakarta)
        )
        Mockito.`when`(dateTimeUtil.getCalendar(null)).thenReturn(
            Calendar.getInstance().apply { time = Date(1609907570066L) }
        )
        Mockito.`when`(dateTimeUtil.getDuration(any()))
            .thenReturn(Duration.ofMillis(1000L)) //only this matter for final result
        Mockito.`when`(dateTimeUtil.getTimeDiffInMillis(any(), any())).thenReturn(100000L)
        val uobSelectionViewModel =
            UobSelectionViewModel(snapCore = snapCore, dateTimeUtil)
        uobSelectionViewModel.setExpiryTime("2022-01-06 11:32:50 +0700")
        Assert.assertEquals("00:00:01", uobSelectionViewModel.getExpiredHour())
    }

    @Test
    fun verifyTrackOrderDetailsViewed() {
        viewModel.trackOrderDetailsViewed()
        verify(eventAnalytics).trackSnapOrderDetailsViewed(
            pageName = PageName.UOB_SELECTION_PAGE,
            paymentMethodName = PaymentType.UOB_EZPAY,
            transactionId = null,
            netAmount = null
        )
    }

    @Test
    fun verifyTrackPageViewed() {
        viewModel.trackPageViewed(2)
        verify(eventAnalytics).trackSnapPageViewed(
            pageName = PageName.UOB_SELECTION_PAGE,
            paymentMethodName = PaymentType.UOB_EZPAY,
            transactionId = null,
            stepNumber = "2"
        )
    }
}
