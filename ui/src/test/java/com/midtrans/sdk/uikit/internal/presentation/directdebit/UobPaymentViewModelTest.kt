package com.midtrans.sdk.uikit.internal.presentation.directdebit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.uikit.internal.getOrAwaitValue
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
    private lateinit var viewModel: UobPaymentViewModel

    @Before
    fun setup() {
        viewModel = UobPaymentViewModel(snapCore)
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

        val callback = callbackCaptor.firstValue
        callback.onSuccess(
            TransactionResponse(
                uobEzpayWebUrl = "uob-web-url",
                uobEzpayDeeplinkUrl = "uob-deeplink-url"
            )
        )
        Assert.assertEquals(
            TransactionResponse(
                uobEzpayWebUrl = "uob-web-url",
                uobEzpayDeeplinkUrl = "uob-deeplink-url"
            ),
            viewModel.getTransactionResponse().getOrAwaitValue()
        )
    }

    @Test
    fun checkStatusWhenSucceedShouldCallbackOnSuccess() {
        val snapToken = "snap-token"
        val callbackCaptor: KArgumentCaptor<Callback<TransactionResponse>> = argumentCaptor()

        viewModel.checkStatus(snapToken)

        verify(snapCore).checkStatus(
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