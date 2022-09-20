package com.midtrans.sdk.uikit.internal.presentation.banktransfer

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.uikit.internal.util.DateTimeUtil
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.`when`
import org.mockito.kotlin.*
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

internal class BankTransferDetailViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    val time = 1609866000000L //"Wed Jan 6 2021 11:32:50 +0700"// (Asia/Jakarta)

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

    @Test
    fun chargeBankTransferShouldInvokeSnapCorePay() {
        val snapCore: SnapCore = mock()
        val dateTimeUtil: DateTimeUtil = mock()
        val snapToken = "SnapToken"
        val paymentType = PaymentType.BNI_VA
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