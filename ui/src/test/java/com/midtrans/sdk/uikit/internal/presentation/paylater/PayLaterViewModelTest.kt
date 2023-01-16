package com.midtrans.sdk.uikit.internal.presentation.paylater

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.exception.InvalidPaymentTypeException
import com.midtrans.sdk.corekit.api.exception.SnapError
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.internal.analytics.EventAnalytics
import com.midtrans.sdk.corekit.internal.analytics.PageName
import com.midtrans.sdk.uikit.internal.getOrAwaitValue
import com.midtrans.sdk.uikit.internal.util.DateTimeUtil
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.*
import java.time.Duration
import java.util.*

internal class PayLaterViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val snapCore: SnapCore = mock()

    private val dateTimeUtil: DateTimeUtil = mock()

    private val eventAnalytics: EventAnalytics = mock()

    private lateinit var viewModel: PayLaterViewModel

    @Before
    fun setup() {
        whenever(snapCore.getEventAnalytics()) doReturn eventAnalytics
        viewModel = PayLaterViewModel(snapCore, dateTimeUtil)
    }

    @Test
    fun checkStatusWhenSucceedShouldCallbackOnSuccess() {
        val snapToken = "snap-token"
        val callbackCaptor: KArgumentCaptor<Callback<TransactionResponse>> = argumentCaptor()
        val transactionId = "transaction-id"
        val transactionStatus = UiKitConstants.STATUS_CANCELED

        viewModel.checkStatus(snapToken)

        Mockito.verify(snapCore).getTransactionStatus(
            snapToken = eq(snapToken),
            callback = callbackCaptor.capture()
        )

        val callback = callbackCaptor.firstValue
        callback.onSuccess(
            TransactionResponse(
                transactionStatus = transactionStatus,
                transactionId = transactionId
            )
        )

        assertEquals(
            transactionId,
            viewModel.transactionId.getOrAwaitValue()
        )

        verify(eventAnalytics).trackSnapError(
            pageName = eq(PageName.AKULAKU_PAGE),
            paymentMethodName = eq(PaymentType.AKULAKU),
            errorMessage = eq(""),
            statusCode = eq(""),
        )
    }

    @Test
    fun checkStatusWhenErrorShouldCallbackOnError() {
        val snapToken = "snap-token"
        val exception = SnapError()
        val callbackCaptor: KArgumentCaptor<Callback<TransactionResponse>> = argumentCaptor()

        viewModel.checkStatus(snapToken)

        Mockito.verify(snapCore).getTransactionStatus(
            snapToken = eq(snapToken),
            callback = callbackCaptor.capture()
        )

        val callback = callbackCaptor.firstValue
        callback.onError(exception)

        verify(eventAnalytics).trackSnapError(
            pageName = PageName.AKULAKU_PAGE,
            paymentMethodName = PaymentType.AKULAKU,
            statusCode = null,
            errorMessage = exception.message ?: exception.javaClass.name
        )
    }

    @Test
    fun payPayLaterWhenSucceedShouldInvokeSnapCorePayAndGetRedirectUrl() {
        val snapToken = "snapToken"
        val paymentType = "akulaku"
        val callbackCaptor: KArgumentCaptor<Callback<TransactionResponse>> = argumentCaptor()

        viewModel.payPayLater(snapToken, paymentType)

        verify(snapCore).pay(
            snapToken = eq(snapToken),
            paymentRequestBuilder = any(),
            callback = callbackCaptor.capture()
        )

        verify(eventAnalytics).trackSnapChargeRequest(
            pageName = PageName.AKULAKU_PAGE,
            paymentMethodName = paymentType,
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
        assertEquals("redirect-url", viewModel.transactionResponseLiveData.getOrAwaitValue().redirectUrl)
        verify(eventAnalytics).trackSnapChargeResult(
            transactionStatus = eq("transaction-status"),
            fraudStatus = eq("fraud-status"),
            currency = eq("currency"),
            statusCode = eq("status-code"),
            transactionId = eq("transaction-id"),
            pageName = eq(PageName.AKULAKU_PAGE),
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
    fun payPayLaterWhenErrorShouldTrackError() {
        val snapToken = "snapToken"
        val paymentType = "akulaku"
        val callbackCaptor: KArgumentCaptor<Callback<TransactionResponse>> = argumentCaptor()
        val exception = InvalidPaymentTypeException()

        viewModel.payPayLater(snapToken, paymentType)

        verify(snapCore).pay(
            snapToken = eq(snapToken),
            paymentRequestBuilder = any(),
            callback = callbackCaptor.capture()
        )

        val callback = callbackCaptor.firstValue
        callback.onError(exception)
        verify(eventAnalytics).trackSnapError(
            pageName = PageName.AKULAKU_PAGE,
            paymentMethodName = paymentType,
            errorMessage = exception.message ?: exception.javaClass.name,
            statusCode = null
        )
    }

    @Test
    fun verifyTrackSnapButtonClicked() {
        viewModel.trackSnapButtonClicked("cta-name", PaymentType.AKULAKU)
        verify(eventAnalytics).trackSnapCtaClicked(
            ctaName = "cta-name",
            pageName = PageName.AKULAKU_PAGE,
            paymentMethodName = PaymentType.AKULAKU
        )
    }

    @Test
    fun verifyTrackHowToPayClicked() {
        viewModel.trackHowToPayClicked(PaymentType.AKULAKU)
        verify(eventAnalytics).trackSnapHowToPayViewed(
            pageName = PageName.AKULAKU_PAGE,
            paymentMethodName = PaymentType.AKULAKU
        )
    }

    @Test
    fun verifyTrackOpenWebView() {
        viewModel.trackOpenWebView(PaymentType.AKULAKU)
        verify(eventAnalytics).trackSnapOpenDeeplink(
            pageName = PageName.AKULAKU_PAGE,
            paymentMethodName = PaymentType.AKULAKU
        )
    }

    @Test
    fun verifyTrackOrderDetailsViewed() {
        viewModel.trackOrderDetailsViewed(PaymentType.AKULAKU)
        verify(eventAnalytics).trackSnapOrderDetailsViewed(
            pageName = PageName.AKULAKU_PAGE,
            paymentMethodName = PaymentType.AKULAKU,
            transactionId = null,
            netAmount = null
        )
    }

    @Test
    fun verifyTrackPageViewed() {
        viewModel.trackPageViewed(PaymentType.AKULAKU, 2)
        verify(eventAnalytics).trackSnapPageViewed(
            pageName = PageName.AKULAKU_PAGE,
            paymentMethodName = PaymentType.AKULAKU,
            transactionId = null,
            stepNumber = "2"
        )
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
        val payLaterViewModel =
            PayLaterViewModel(snapCore = snapCore, dateTimeUtil)
        payLaterViewModel.setExpiryTime("2022-01-06 11:32:50 +0700")
        assertEquals("00:00:01", payLaterViewModel.getExpiredHour())
    }
}