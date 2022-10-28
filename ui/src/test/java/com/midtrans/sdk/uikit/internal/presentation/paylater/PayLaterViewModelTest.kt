package com.midtrans.sdk.uikit.internal.presentation.paylater

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.internal.analytics.EventAnalytics
import com.midtrans.sdk.corekit.internal.analytics.PageName
import com.midtrans.sdk.uikit.internal.getOrAwaitValue
import com.midtrans.sdk.uikit.internal.util.DateTimeUtil
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*

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
            paymentMethodName = "akulaku",
            promoName = null,
            promoAmount = null,
            promoId = null,
            creditCardPoint = null
        )

        val callback = callbackCaptor.firstValue
        callback.onSuccess(TransactionResponse(redirectUrl = "redirect-url"))
        assertEquals("redirect-url", viewModel.getTransactionResponse().getOrAwaitValue().redirectUrl)
    }
}