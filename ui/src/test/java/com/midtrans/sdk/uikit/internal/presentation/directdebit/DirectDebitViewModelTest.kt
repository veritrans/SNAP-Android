package com.midtrans.sdk.uikit.internal.presentation.directdebit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.uikit.internal.getOrAwaitValue
import com.midtrans.sdk.uikit.internal.util.DateTimeUtil
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*

internal class DirectDebitViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val snapCore: SnapCore = mock()

    private val dateTimeUtil: DateTimeUtil = mock()

    private lateinit var viewModel: DirectDebitViewModel

    @Before
    fun setup() {
        viewModel = DirectDebitViewModel(snapCore, dateTimeUtil)
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

        val callback = callbackCaptor.firstValue
        callback.onSuccess(TransactionResponse(redirectUrl = "redirect-url"))
        Assert.assertEquals("redirect-url", viewModel.getTransactionResponse().getOrAwaitValue().redirectUrl)
    }
}
