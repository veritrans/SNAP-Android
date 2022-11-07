package com.midtrans.sdk.uikit.internal.presentation.ewallet

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.KArgumentCaptor
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class DeepLinkViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var snapCore: SnapCore

    private lateinit var testClass: DeepLinkViewModel

    private lateinit var closeable: AutoCloseable

    @Before
    fun setup() {
        closeable = MockitoAnnotations.openMocks(this)
        testClass = DeepLinkViewModel(snapCore)
    }

    @After
    fun teardown() {
        closeable.close()
    }

    @Test
    fun checkStatusShouldReturnResultViaLiveData() {
        val snapToken = "Snap Token"
        testClass.checkStatus(snapToken)

        val callbackCaptor: KArgumentCaptor<Callback<TransactionResponse>> = argumentCaptor()
        verify(snapCore).getTransactionStatus(
            snapToken = eq(snapToken),
            callback = callbackCaptor.capture()
        )
        val callback = callbackCaptor.firstValue
        val transactionId = "Transaction ID"
        val transactionStatus = "Transaction Status"
        val paymentType = "Payment Type"
        callback.onSuccess(
            TransactionResponse(
                transactionId = transactionId,
                transactionStatus = transactionStatus,
                paymentType = paymentType
            )
        )
        val result = testClass.checkStatusResultLiveData.getOrAwaitValue()
        assertEquals(transactionId, result.transactionId)
        assertEquals(transactionStatus, result.status)
        assertEquals(paymentType, result.paymentType)
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