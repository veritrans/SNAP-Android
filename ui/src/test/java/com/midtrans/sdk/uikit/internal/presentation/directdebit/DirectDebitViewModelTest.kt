package com.midtrans.sdk.uikit.internal.presentation.directdebit

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
import com.midtrans.sdk.uikit.internal.util.UiKitConstants.STATUS_CANCELED
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.*
import java.time.Duration
import java.util.*

internal class DirectDebitViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val snapCore: SnapCore = mock()

    private val dateTimeUtil: DateTimeUtil = mock()

    private val eventAnalytics: EventAnalytics = mock()

    private lateinit var viewModel: DirectDebitViewModel

    @Before
    fun setup() {
        whenever(snapCore.getEventAnalytics()) doReturn eventAnalytics
        viewModel = DirectDebitViewModel(snapCore, dateTimeUtil)
    }

    @Test
    fun checkStatusWhenSucceedShouldCallbackOnSuccess() {
        val snapToken = "snap-token"
        val callbackCaptor: KArgumentCaptor<Callback<TransactionResponse>> = argumentCaptor()
        val transactionId = "transaction-id"
        val transactionStatus = STATUS_CANCELED

        viewModel.checkStatus(snapToken, PaymentType.KLIK_BCA)

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
        Assert.assertEquals(
            transactionId,
            viewModel.transactionId.getOrAwaitValue()
        )

        verify(eventAnalytics).trackSnapError(
            pageName = eq(PageName.KLIK_BCA_PAGE),
            paymentMethodName = eq(PaymentType.KLIK_BCA),
            errorMessage = eq(""),
            statusCode = eq(""),
        )
    }

    @Test
    fun checkStatusWhenErrorShouldCallbackOnError() {
        val snapToken = "snap-token"
        val exception = SnapError()
        val callbackCaptor: KArgumentCaptor<Callback<TransactionResponse>> = argumentCaptor()

        viewModel.checkStatus(snapToken, PaymentType.KLIK_BCA)

        Mockito.verify(snapCore).getTransactionStatus(
            snapToken = eq(snapToken),
            callback = callbackCaptor.capture()
        )

        val callback = callbackCaptor.firstValue
        callback.onError(exception)

        verify(eventAnalytics).trackSnapError(
            pageName = PageName.KLIK_BCA_PAGE,
            paymentMethodName = PaymentType.KLIK_BCA,
            statusCode = null,
            errorMessage = exception.message ?: exception.javaClass.name
        )
    }

    @Test
    fun payDirectDebitWhenSucceedShouldInvokeSnapCorePayAndGetRedirectUrl() {
        val snapToken = "snapToken"
        val paymentType = "bca_klikpay"
        val callbackCaptor: KArgumentCaptor<Callback<TransactionResponse>> = argumentCaptor()

        viewModel.payDirectDebit(snapToken, paymentType, null)

        verify(snapCore).pay(
            snapToken = eq(snapToken),
            paymentRequestBuilder = any(),
            callback = callbackCaptor.capture()
        )
        verify(eventAnalytics).trackSnapChargeRequest(
            pageName = PageName.BCA_KLIK_PAY_PAGE,
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
        Assert.assertEquals(
            "redirect-url",
            viewModel.getTransactionResponse().getOrAwaitValue().redirectUrl
        )
        verify(eventAnalytics).trackSnapChargeResult(
            transactionStatus = eq("transaction-status"),
            fraudStatus = eq("fraud-status"),
            currency = eq("currency"),
            statusCode = eq("status-code"),
            transactionId = eq("transaction-id"),
            pageName = eq(PageName.BCA_KLIK_PAY_PAGE),
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
    fun payDirectDebitWhenErrorShouldTrackError() {
        val snapToken = "snapToken"
        val paymentType = "bca_klikpay"
        val callbackCaptor: KArgumentCaptor<Callback<TransactionResponse>> = argumentCaptor()
        val exception = InvalidPaymentTypeException()

        viewModel.payDirectDebit(snapToken, paymentType, null)

        verify(snapCore).pay(
            snapToken = eq(snapToken),
            paymentRequestBuilder = any(),
            callback = callbackCaptor.capture()
        )
        val callback = callbackCaptor.firstValue
        callback.onError(exception)
        verify(eventAnalytics).trackSnapError(
            pageName = PageName.BCA_KLIK_PAY_PAGE,
            paymentMethodName = PaymentType.BCA_KLIKPAY,
            statusCode = null,
            errorMessage = exception.message ?: exception.javaClass.name
        )
    }

    @Test
    fun verifyTrackSnapButtonClicked() {
        viewModel.trackSnapButtonClicked("cta-name", PaymentType.DANAMON_ONLINE)
        verify(eventAnalytics).trackSnapCtaClicked(
            ctaName = "cta-name",
            pageName = PageName.DANAMON_ONLINE_PAGE,
            paymentMethodName = PaymentType.DANAMON_ONLINE
        )
    }

    @Test
    fun verifyTrackHowToPayClicked() {
        viewModel.trackHowToPayClicked(PaymentType.DANAMON_ONLINE)
        verify(eventAnalytics).trackSnapHowToPayViewed(
            pageName = PageName.DANAMON_ONLINE_PAGE,
            paymentMethodName = PaymentType.DANAMON_ONLINE
        )
    }

    @Test
    fun verifyTrackOpenWebView() {
        viewModel.trackOpenRedirectionUrl(PaymentType.BCA_KLIKPAY)
        verify(eventAnalytics).trackSnapOpenDeeplink(
            pageName = PageName.BCA_KLIK_PAY_PAGE,
            paymentMethodName = PaymentType.BCA_KLIKPAY
        )
    }

    @Test
    fun verifyTrackOrderDetailsViewed() {
        viewModel.trackOrderDetailsViewed(PaymentType.BCA_KLIKPAY)
        verify(eventAnalytics).trackSnapOrderDetailsViewed(
            pageName = PageName.BCA_KLIK_PAY_PAGE,
            paymentMethodName = PaymentType.BCA_KLIKPAY,
            transactionId = null,
            netAmount = null
        )
    }

    @Test
    fun verifyTrackPageViewed() {
        viewModel.trackPageViewed(PaymentType.BCA_KLIKPAY, 2)
        verify(eventAnalytics).trackSnapPageViewed(
            pageName = PageName.BCA_KLIK_PAY_PAGE,
            paymentMethodName = PaymentType.BCA_KLIKPAY,
            transactionId = null,
            stepNumber = "2"
        )
    }

    @Test
    fun verifyTrackSnapNotice() {
        viewModel.trackSnapNotice(PaymentType.BCA_KLIKPAY, "text")
        verify(eventAnalytics).trackSnapNotice(
            pageName = PageName.BCA_KLIK_PAY_PAGE,
            paymentMethodName = PaymentType.BCA_KLIKPAY,
            statusText = "text",
            noticeMessage = null
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
        val directDebitViewModel =
            DirectDebitViewModel(snapCore = snapCore, dateTimeUtil)
        directDebitViewModel.setExpiryTime("2022-01-06 11:32:50 +0700")
        Assert.assertEquals("00:00:01", directDebitViewModel.getExpiredHour())
    }
}
