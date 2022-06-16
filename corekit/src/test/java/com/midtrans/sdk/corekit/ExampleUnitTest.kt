package com.midtrans.sdk.corekit

import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.internal.data.repository.SnapRepository
import com.midtrans.sdk.corekit.internal.network.model.request.BankTransferPaymentRequest
import com.midtrans.sdk.corekit.internal.network.restapi.SnapApi
import com.midtrans.sdk.corekit.internal.scheduler.TestSdkScheduler
import com.midtrans.sdk.corekit.internal.usecase.BankTransferUsecase

import io.mockk.mockk
import io.reactivex.Single
import org.junit.Test

import org.junit.Assert.*

import org.mockito.Mockito.`when`
import org.mockito.kotlin.*


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */


class ExampleUnitTest {
    @Test
    fun testAdd() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun snapRE() {
        val snapApi = mock<SnapApi>()

        `when`(snapApi.paymentUsingVa(any(), any())).thenReturn(Single.just(mockk()))
        val snapRepository = SnapRepository(snapApi = snapApi)
        val snapToken = "token"
        val bankTransferPaymentRequest = mockk<BankTransferPaymentRequest>()
        val scheduler = TestSdkScheduler().io()
        snapRepository.chargeBankTransfer(
            snapToken = snapToken,
            request = bankTransferPaymentRequest
        )
            .subscribeOn(scheduler)
            .subscribe()

        scheduler.start()
        verify(snapApi).paymentUsingVa(snapToken, bankTransferPaymentRequest)
    }

    @Test
    fun bankTransferUsecase() {
        val snapRepository = mock<SnapRepository>()
        val scheduler = TestSdkScheduler().io()
        `when`(snapRepository.chargeBankTransfer(any(), any())).thenReturn(Single.just(mockk()))
        val bankTransferUsecase = BankTransferUsecase(snapRepository)
        val snapToken = "token"
        val email = "email"
        bankTransferUsecase.charge(
            snaptoken = snapToken,
            email = email,
            paymentType = PaymentType.BCA_VA
        ).subscribeOn(scheduler)
            .subscribe()
        scheduler.start()

        var capturedBankTfPaymentRequest: BankTransferPaymentRequest? = null
        verify(snapRepository).chargeBankTransfer(eq(snapToken), argThat {
            capturedBankTfPaymentRequest = this
            true
        })
        assertEquals(PaymentType.BCA_VA, capturedBankTfPaymentRequest?.paymentType)
        assertEquals(email, capturedBankTfPaymentRequest?.customerDetails?.email)
    }

}