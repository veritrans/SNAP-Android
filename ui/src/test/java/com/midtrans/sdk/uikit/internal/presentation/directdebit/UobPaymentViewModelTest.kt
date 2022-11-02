package com.midtrans.sdk.uikit.internal.presentation.directdebit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.internal.analytics.EventAnalytics
import com.midtrans.sdk.corekit.internal.analytics.PageName
import com.midtrans.sdk.uikit.internal.getOrAwaitValue
import com.midtrans.sdk.uikit.internal.util.DateTimeUtil
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.verify
import org.mockito.kotlin.*

internal class UobPaymentViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val snapCore: SnapCore = mock()
    private val dateTimeUtil: DateTimeUtil = mock()
    private val eventAnalytics: EventAnalytics = mock()
    private lateinit var viewModel: UobPaymentViewModel

    @Before
    fun setup() {
        whenever(snapCore.getEventAnalytics()) doReturn eventAnalytics
        viewModel = UobPaymentViewModel(snapCore, dateTimeUtil)
    }

    @Test
    fun payUobWhenSucceedShouldCallbackOnSuccess() {
        val snapToken = "snap-token"
        val callbackCaptor: KArgumentCaptor<Callback<TransactionResponse>> = argumentCaptor()

        viewModel.payUob(snapToken)

        verify(snapCore).pay(
            snapToken = eq(snapToken),
            paymentRequestBuilder = any(),
            callback = callbackCaptor.capture()
        )
        verify(eventAnalytics).trackSnapChargeRequest(
            pageName = PageName.UOB_PAGE,
            paymentMethodName = "uob_ezpay",
            promoName = null,
            promoAmount = null,
            promoId = null,
            creditCardPoint = null
        )
        val callback = callbackCaptor.firstValue
        callback.onSuccess(
            TransactionResponse(
                uobEzpayWebUrl = "uob-web-url",
                uobEzpayDeeplinkUrl = "uob-deeplink-url",
                paymentType = "uob_ezpay",
                transactionStatus = "transaction-status",
                fraudStatus = "fraud-status",
                currency = "currency",
                statusCode = "status-code",
                transactionId = "transaction-id"
            )
        )
        Assert.assertEquals(
            TransactionResponse(
                uobEzpayWebUrl = "uob-web-url",
                uobEzpayDeeplinkUrl = "uob-deeplink-url",
                paymentType = "uob_ezpay",
                transactionStatus = "transaction-status",
                fraudStatus = "fraud-status",
                currency = "currency",
                statusCode = "status-code",
                transactionId = "transaction-id"
            ),
            viewModel.getTransactionResponse().getOrAwaitValue()
        )
        verify(eventAnalytics).trackSnapChargeResult(
            transactionStatus = eq("transaction-status"),
            fraudStatus = eq("fraud-status"),
            currency = eq("currency"),
            statusCode = eq("status-code"),
            transactionId = eq("transaction-id"),
            pageName = eq(PageName.UOB_PAGE),
            paymentMethodName = eq("uob_ezpay"),
            responseTime = any(),
            bank = eq(null),
            channelResponseCode = eq(null),
            channelResponseMessage = eq(null),
            cardType = eq(null),
            threeDsVersion = eq(null)
        )
    }

    @Test
    fun checkStatusWhenSucceedShouldCallbackOnSuccess() {
        val snapToken = "snap-token"
        val callbackCaptor: KArgumentCaptor<Callback<TransactionResponse>> = argumentCaptor()

        viewModel.checkStatus(snapToken)

        verify(snapCore).getTransactionStatus(
            snapToken = eq(snapToken),
            callback = callbackCaptor.capture()
        )

        val callback = callbackCaptor.firstValue
        callback.onSuccess(
            TransactionResponse(
                transactionStatus = "settlement",
                transactionId = "transaction-id"
            )
        )
        Assert.assertEquals(
            Pair(
                "settlement",
                "transaction-id"
            ),
            viewModel.getTransactionResult().getOrAwaitValue()
        )
    }

}