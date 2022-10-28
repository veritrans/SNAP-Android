package com.midtrans.sdk.uikit.internal.presentation.ewallet

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

class WalletViewModelTest {

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
        Locale.setDefault(Locale("en", "US"))
    }

    @Test
    fun chargeQrShouldInvokeSnapCorePay() {
        val snapCore: SnapCore = mock()
        val dateTimeUtil: DateTimeUtil = mock()
        val snapToken = "SnapToken"
        val paymentType = PaymentType.GOPAY
        val eventAnalytics: EventAnalytics = mock()

        whenever(snapCore.getEventAnalytics()) doReturn eventAnalytics

        val walletViewModel = WalletViewModel(snapCore = snapCore, dateTimeUtil)
        walletViewModel.chargeQrPayment(
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
            pageName = PageName.GOPAY_DEEPLINK_PAGE,
            paymentMethodName = PaymentType.GOPAY,
            promoName = null,
            promoAmount = null,
            promoId = null,
            creditCardPoint = null
        )
    }


    @Test
    fun getExpiredHourShouldReturnhhmmss() {
        val snapCore: SnapCore = mock()
        val dateTimeUtil: DateTimeUtil = mock()
        val snapToken = "SnapToken"
        val paymentType = PaymentType.GOPAY
        val millis = 1609907570066L //"Wed Jan 6 2021 11:32:50 +0700"// (Asia/Jakarta)

        `when`(dateTimeUtil.getCalendar(null)).thenReturn(
            Calendar.getInstance().apply { time = Date(millis) }
        )
        `when`(dateTimeUtil.getDate(any(), any(), any(), any())).thenReturn(Date(millis))
        `when`(dateTimeUtil.getExpiredHour(any())).thenReturn("00:00:01")

        val walletViewModel =
            WalletViewModel(snapCore = snapCore, dateTimeUtil)
        walletViewModel.chargeQrPayment(
            snapToken = snapToken,
            paymentType = paymentType
        )
        val callbackCaptor: KArgumentCaptor<Callback<TransactionResponse>> = argumentCaptor()
        verify(snapCore).pay(
            snapToken = eq(snapToken),
            paymentRequestBuilder = any(),
            callback = callbackCaptor.capture()
        )
        val qrCodeUrl = "http://qr"
        val callback = callbackCaptor.firstValue
        callback.onSuccess(
            TransactionResponse(
                qrCodeUrl = qrCodeUrl,
                gopayExpirationRaw = "2021-01-06 11:32 +0700"
            )
        )

        Assert.assertEquals(qrCodeUrl, walletViewModel.qrCodeUrlLiveData.getOrAwaitValue())
        Assert.assertEquals("00:00:01", walletViewModel.getExpiredHour())

    }


    @Test
    fun deeplinkShouldDeliveredViaLiveData() {
        val snapCore: SnapCore = mock()
        val dateTimeUtil: DateTimeUtil = mock()
        val snapToken = "SnapToken"
        val paymentType = PaymentType.BNI_VA
        val millis = 1609907570066L //"Wed Jan 6 2021 11:32:50 +0700"// (Asia/Jakarta)

        `when`(dateTimeUtil.getCalendar(null)).thenReturn(
            Calendar.getInstance().apply { time = Date(millis) }
        )
        `when`(dateTimeUtil.getDate(any(), any(), any(), any())).thenReturn(Date(millis))
        `when`(dateTimeUtil.getExpiredHour(any())).thenReturn("00:00:01")

        val walletViewModel =
            WalletViewModel(snapCore = snapCore, dateTimeUtil)
        walletViewModel.chargeQrPayment(
            snapToken = snapToken,
            paymentType = paymentType
        )
        val callbackCaptor: KArgumentCaptor<Callback<TransactionResponse>> = argumentCaptor()
        verify(snapCore).pay(
            snapToken = eq(snapToken),
            paymentRequestBuilder = any(),
            callback = callbackCaptor.capture()
        )
        val qrCodeUrl = "http://qr"
        val deepLinkUrl = "http://deeplink"
        val callback = callbackCaptor.firstValue
        callback.onSuccess(
            TransactionResponse(
                qrCodeUrl = qrCodeUrl,
                gopayExpirationRaw = "2021-01-06 11:32 +0700",
                deeplinkUrl = deepLinkUrl
            )
        )

        Assert.assertEquals(qrCodeUrl, walletViewModel.qrCodeUrlLiveData.getOrAwaitValue())
        Assert.assertEquals(deepLinkUrl, walletViewModel.deepLinkUrlLiveData.getOrAwaitValue())
        Assert.assertEquals("00:00:01", walletViewModel.getExpiredHour())

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