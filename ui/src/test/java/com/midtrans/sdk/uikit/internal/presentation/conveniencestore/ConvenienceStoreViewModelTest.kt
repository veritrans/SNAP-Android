package com.midtrans.sdk.uikit.internal.presentation.conveniencestore

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.internal.analytics.EventAnalytics
import com.midtrans.sdk.corekit.internal.analytics.PageName
import com.midtrans.sdk.uikit.internal.getOrAwaitValue
import com.midtrans.sdk.uikit.internal.presentation.errorcard.ErrorCard
import com.midtrans.sdk.uikit.internal.util.BarcodeEncoder
import com.midtrans.sdk.uikit.internal.util.DateTimeUtil
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito
import org.mockito.kotlin.*
import java.time.Clock
import java.time.Instant
import java.util.*


class ConvenienceStoreViewModelTest {
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
    fun chargeConvenienceStoreShouldInvokeSnapCorePay() {
        val snapCore: SnapCore = mock()
        val errorCard: ErrorCard = mock()
        val barcodeEncoder: BarcodeEncoder = mock()
        val dateTimeUtil: DateTimeUtil = mock()
        val snapToken = "SnapToken"
        val paymentType = PaymentType.INDOMARET
        val eventAnalytics: EventAnalytics = mock()

        whenever(snapCore.getEventAnalytics()) doReturn eventAnalytics

        val convenienceStoreViewModel = ConvenienceStoreViewModel(snapCore = snapCore, dateTimeUtil, errorCard, barcodeEncoder)
        convenienceStoreViewModel.chargeConvenienceStorePayment(
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
            pageName = PageName.INDOMARET_PAGE,
            paymentMethodName = PaymentType.INDOMARET,
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
            pageName = eq(PageName.INDOMARET_PAGE),
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
        val errorCard: ErrorCard = mock()
        val barcodeEncoder: BarcodeEncoder = mock()
        val dateTimeUtil: DateTimeUtil = mock()
        val snapToken = "SnapToken"
        val paymentType = PaymentType.GOPAY
        val millis = 1609907570066L //"Wed Jan 6 2021 11:32:50 +0700"// (Asia/Jakarta)

        Mockito.`when`(dateTimeUtil.getCalendar(null)).thenReturn(
            Calendar.getInstance().apply { time = Date(millis) }
        )
        Mockito.`when`(dateTimeUtil.getDate(any(), any(), any(), any())).thenReturn(Date(millis))
        Mockito.`when`(dateTimeUtil.getExpiredHour(any())).thenReturn("00:00:01")

        val convenienceStoreViewModel =
            ConvenienceStoreViewModel(snapCore = snapCore, dateTimeUtil, errorCard, barcodeEncoder)
        convenienceStoreViewModel.chargeConvenienceStorePayment(
            snapToken = snapToken,
            paymentType = paymentType
        )
        val callbackCaptor: KArgumentCaptor<Callback<TransactionResponse>> = argumentCaptor()
        verify(snapCore).pay(
            snapToken = eq(snapToken),
            paymentRequestBuilder = any(),
            callback = callbackCaptor.capture()
        )
        val paymentCode = "123"
        val callback = callbackCaptor.firstValue
        callback.onSuccess(
            TransactionResponse(
                paymentCode = paymentCode,
                gopayExpirationRaw = "2021-01-06 11:32 +0700"
            )
        )

        Assert.assertEquals(paymentCode, convenienceStoreViewModel.paymentCodeLiveData.getOrAwaitValue())
        Assert.assertEquals("00:00:01", convenienceStoreViewModel.getExpiredHour())

    }


    @Test
    fun pdfUrlShouldDeliveredViaLiveData() {
        val snapCore: SnapCore = mock()
        val errorCard: ErrorCard = mock()
        val dateTimeUtil: DateTimeUtil = mock()
        val barcodeEncoder: BarcodeEncoder = mock()
        val snapToken = "SnapToken"
        val paymentType = PaymentType.BNI_VA
        val millis = 1609907570066L //"Wed Jan 6 2021 11:32:50 +0700"// (Asia/Jakarta)

        Mockito.`when`(dateTimeUtil.getCalendar(null)).thenReturn(
            Calendar.getInstance().apply { time = Date(millis) }
        )
        Mockito.`when`(dateTimeUtil.getDate(any(), any(), any(), any())).thenReturn(Date(millis))
        Mockito.`when`(dateTimeUtil.getExpiredHour(any())).thenReturn("00:00:01")

        val convenienceStoreViewModel =
            ConvenienceStoreViewModel(snapCore = snapCore, dateTimeUtil, errorCard, barcodeEncoder)
        convenienceStoreViewModel.chargeConvenienceStorePayment(
            snapToken = snapToken,
            paymentType = paymentType
        )
        val callbackCaptor: KArgumentCaptor<Callback<TransactionResponse>> = argumentCaptor()
        verify(snapCore).pay(
            snapToken = eq(snapToken),
            paymentRequestBuilder = any(),
            callback = callbackCaptor.capture()
        )
        val paymentCode = "123"
        val pdfUrl = "http://deeplink"
        val callback = callbackCaptor.firstValue
        callback.onSuccess(
            TransactionResponse(
                paymentCode = paymentCode,
                gopayExpirationRaw = "2021-01-06 11:32 +0700",
                pdfUrl = pdfUrl
            )
        )

        Assert.assertEquals(paymentCode, convenienceStoreViewModel.paymentCodeLiveData.getOrAwaitValue())
        Assert.assertEquals(pdfUrl, convenienceStoreViewModel.pdfUrlLiveData.getOrAwaitValue())
        Assert.assertEquals("00:00:01", convenienceStoreViewModel.getExpiredHour())

    }
}