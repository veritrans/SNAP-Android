package com.midtrans.sdk.uikit.internal.presentation.directdebit

import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

internal class DirectDebitViewModelTest {

    private val snapCore: SnapCore = mock()

    private lateinit var viewModel: DirectDebitViewModel

    @Before
    fun setup() {
        viewModel = DirectDebitViewModel(snapCore)
    }

    @Test
    fun payDirectDebitShouldInvokeSnapCorePay() {
        val snapToken = "snapToken"
        val paymentType = "bca_klikpay"
        val callback: KArgumentCaptor<Callback<TransactionResponse>> = argumentCaptor()

        viewModel.payDirectDebit(snapToken, paymentType, null)

        verify(snapCore).pay(
            snapToken = eq(snapToken),
            paymentRequestBuilder = any(),
            callback = callback.capture()
        )
    }
}