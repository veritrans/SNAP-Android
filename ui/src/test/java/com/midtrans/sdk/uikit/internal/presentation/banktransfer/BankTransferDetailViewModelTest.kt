package com.midtrans.sdk.uikit.internal.presentation.banktransfer

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.internal.analytics.EventAnalytics
import com.midtrans.sdk.corekit.internal.analytics.PageName
import com.midtrans.sdk.uikit.internal.util.DateTimeUtil
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito.`when`
import org.mockito.kotlin.*
import java.time.Clock
import java.time.Instant
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

internal class BankTransferDetailViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private val time = 1609866000000L //"Wed Jan 6 2021 11:32:50 +0700"// (Asia/Jakarta)

    @Before
    fun setup() {
        val timeZone = TimeZone.getTimeZone("Asia/Jakarta")
        Instant.now(
            Clock.fixed(
                Instant.ofEpochMilli(time),
                timeZone.toZoneId()))
        Locale.setDefault(Locale("en", "US"))
    }

    @Test
    fun chargeBankTransferShouldInvokeSnapCorePay() {
        val snapCore: SnapCore = mock()
        val dateTimeUtil: DateTimeUtil = mock()
        val snapToken = "SnapToken"
        val paymentType = PaymentType.BNI_VA
        val eventAnalytics: EventAnalytics = mock()

        whenever(snapCore.getEventAnalytics()) doReturn eventAnalytics

        val bankTransferDetailViewModel = BankTransferDetailViewModel(snapCore = snapCore, dateTimeUtil)
        bankTransferDetailViewModel.chargeBankTransfer(
            snapToken = snapToken,
            paymentType = paymentType
        )
        val callbackCaptor: KArgumentCaptor<Callback<TransactionResponse>> = argumentCaptor()
        verify(snapCore).pay(
            snapToken = eq(snapToken),
            paymentRequestBuilder = any(),
            callback = callbackCaptor.capture()
        )
        verify(eventAnalytics).trackSnapChargeRequest(
            pageName = PageName.BANK_TRANSFER_DETAIL_PAGE,
            paymentMethodName = PaymentType.BNI_VA,
            promoName = null,
            promoAmount = null,
            promoId = null,
            creditCardPoint = null
        )
        val callback = callbackCaptor.firstValue
        callback.onSuccess(
            TransactionResponse(
                redirectUrl = "redirect-url",
                paymentType = paymentType,
                transactionStatus = "transaction-status",
                fraudStatus = "fraud-status",
                currency = "currency",
                statusCode = "status-code",
                transactionId = "transaction-id"
            )
        )
        verify(eventAnalytics).trackSnapChargeResult(
            transactionStatus = eq("transaction-status"),
            fraudStatus = eq("fraud-status"),
            currency = eq("currency"),
            statusCode = eq("status-code"),
            transactionId = eq("transaction-id"),
            pageName = eq(PageName.BNI_VA_PAGE),
            paymentMethodName = eq(paymentType),
            responseTime = any(),
            bank = eq(null),
            channelResponseCode = eq(null),
            channelResponseMessage = eq(null),
            cardType = eq(null),
            threeDsVersion = eq(null)
        )
    }


    @Test
    fun getExpiredHourShouldReturnhhmmss() {
        val snapCore: SnapCore = mock()
        val dateTimeUtil: DateTimeUtil = mock()
        val snapToken = "SnapToken"
        val paymentType = PaymentType.BNI_VA
        `when`(
            dateTimeUtil.getDate(
                date = eq("06 January 11:32:50 +0700"),
                dateFormat = eq("dd MMMM hh:mm Z"),
                timeZone = any(),//argThat { timezone -> timezone.id == "Asia/Jakarta" },
                locale = any()
            )
        ).thenReturn(
            Date(1609907570066L)//"Wed Jan 6 2021 11:32:50 +0700"// (Asia/Jakarta)
        )
        `when`(dateTimeUtil.getCalendar(null)).thenReturn(
            Calendar.getInstance().apply { time = Date(1609907570066L) }
        )
        `when`(dateTimeUtil.getExpiredHour(any())).thenReturn("00:00:01")

        val bankTransferDetailViewModel =
            BankTransferDetailViewModel(snapCore = snapCore, dateTimeUtil)
        bankTransferDetailViewModel.chargeBankTransfer(
            snapToken = snapToken,
            paymentType = paymentType
        )
        val callbackCaptor: KArgumentCaptor<Callback<TransactionResponse>> = argumentCaptor()
        verify(snapCore).pay(
            snapToken = eq(snapToken),
            paymentRequestBuilder = any(),
            callback = callbackCaptor.capture()
        )
        val bniVa = "123456"
        val callback = callbackCaptor.firstValue
        callback.onSuccess(
            TransactionResponse(
                bniVaNumber = bniVa,
                bniExpiration = "06 January 11:32:50 WIB"
            )
        )

        Assert.assertEquals(bniVa, bankTransferDetailViewModel.vaNumberLiveData.getOrAwaitValue())
        Assert.assertEquals("00:00:01", bankTransferDetailViewModel.getExpiredHour())

    }

    @Test
    fun bankCodeLiveShouldCorrespondentWithBank() {
        val snapCore: SnapCore = mock()
        val dateTimeUtil: DateTimeUtil = mock()
        val snapToken = "SnapToken"
        val paymentType = PaymentType.BNI_VA
        val BANK_CODE_BNI = "009 - BNI"

        `when`(
            dateTimeUtil.getDate(
                date = eq("06 January 11:32:50 +0700"),
                dateFormat = eq("dd MMMM hh:mm Z"),
                timeZone = any(),//argThat { timezone -> timezone.id == "Asia/Jakarta" },
                locale = any()
            )
        ).thenReturn(
            Date(1609907570066L)//"Wed Jan 6 2021 11:32:50 +0700"// (Asia/Jakarta)
        )
        `when`(dateTimeUtil.getCalendar(null)).thenReturn(
            Calendar.getInstance().apply { time = Date(1609907570066L) }
        )
        `when`(dateTimeUtil.getExpiredHour(any())).thenReturn("00:00:01")

        val bankTransferDetailViewModel =
            BankTransferDetailViewModel(snapCore = snapCore, dateTimeUtil)
        bankTransferDetailViewModel.chargeBankTransfer(
            snapToken = snapToken,
            paymentType = paymentType
        )
        val callbackCaptor: KArgumentCaptor<Callback<TransactionResponse>> = argumentCaptor()
        verify(snapCore).pay(
            snapToken = eq(snapToken),
            paymentRequestBuilder = any(),
            callback = callbackCaptor.capture()
        )
        val bniVa = "123456"
        val callback = callbackCaptor.firstValue
        callback.onSuccess(
            TransactionResponse(
                bniVaNumber = bniVa,
                bniExpiration = "06 January 11:32:50 WIB"
            )
        )

        Assert.assertEquals(bniVa, bankTransferDetailViewModel.vaNumberLiveData.getOrAwaitValue())
        Assert.assertEquals(BANK_CODE_BNI, bankTransferDetailViewModel.bankCodeLiveData.getOrAwaitValue())
        Assert.assertEquals("00:00:01", bankTransferDetailViewModel.getExpiredHour())
    }

    @Test
    fun verifyTrackSnapButtonClicked() {
        val snapCore: SnapCore = mock()
        val dateTimeUtil: DateTimeUtil = mock()
        val eventAnalytics: EventAnalytics = mock()

        whenever(snapCore.getEventAnalytics()) doReturn eventAnalytics
        val bankTransferDetailViewModel =
            BankTransferDetailViewModel(snapCore = snapCore, dateTimeUtil)

        bankTransferDetailViewModel.trackSnapButtonClicked(
            ctaName = "cta-name",
            paymentType = PaymentType.BRI_VA
        )
        verify(eventAnalytics).trackSnapCtaClicked(
            ctaName = "cta-name",
            pageName = PageName.BRI_VA_PAGE,
            paymentMethodName = PaymentType.BRI_VA
        )
    }

    @Test
    fun verifyTrackHowToPayClicked() {
        val snapCore: SnapCore = mock()
        val dateTimeUtil: DateTimeUtil = mock()
        val eventAnalytics: EventAnalytics = mock()

        whenever(snapCore.getEventAnalytics()) doReturn eventAnalytics
        val bankTransferDetailViewModel =
            BankTransferDetailViewModel(snapCore = snapCore, dateTimeUtil)

        bankTransferDetailViewModel.trackHowToPayClicked(paymentType = PaymentType.BRI_VA)
        verify(eventAnalytics).trackSnapHowToPayViewed(
            pageName = PageName.BRI_VA_PAGE,
            paymentMethodName = PaymentType.BRI_VA
        )
    }

    @Test
    fun verifyTrackOrderDetailsViewed() {
        val snapCore: SnapCore = mock()
        val dateTimeUtil: DateTimeUtil = mock()
        val eventAnalytics: EventAnalytics = mock()

        whenever(snapCore.getEventAnalytics()) doReturn eventAnalytics
        val bankTransferDetailViewModel =
            BankTransferDetailViewModel(snapCore = snapCore, dateTimeUtil)

        bankTransferDetailViewModel.trackOrderDetailsViewed(paymentType = PaymentType.BRI_VA)
        verify(eventAnalytics).trackSnapOrderDetailsViewed(
            pageName = PageName.BRI_VA_PAGE,
            paymentMethodName = PaymentType.BRI_VA,
            transactionId = null,
            netAmount = null
        )
    }

    @Test
    fun verifyTrackPageViewed() {
        val snapCore: SnapCore = mock()
        val dateTimeUtil: DateTimeUtil = mock()
        val eventAnalytics: EventAnalytics = mock()

        whenever(snapCore.getEventAnalytics()) doReturn eventAnalytics
        val bankTransferDetailViewModel =
            BankTransferDetailViewModel(snapCore = snapCore, dateTimeUtil)

        bankTransferDetailViewModel.trackPageViewed(PaymentType.BRI_VA, 2)
        verify(eventAnalytics).trackSnapPageViewed(
            pageName = PageName.BRI_VA_PAGE,
            paymentMethodName = PaymentType.BRI_VA,
            transactionId = null,
            stepNumber = "2"
        )
    }

    fun <T> LiveData<T>.getOrAwaitValue(
        time: Long = 2,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ): T {
        var data: T? = null
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(o: T?) {
                data = o
                latch.countDown()
                this@getOrAwaitValue.removeObserver(this)
            }
        }

        this.observeForever(observer)

        // Don't wait indefinitely if the LiveData is not set.
        if (!latch.await(time, timeUnit)) {
            throw TimeoutException("LiveData value was never set.")
        }

        @Suppress("UNCHECKED_CAST")
        return data as T
    }
}