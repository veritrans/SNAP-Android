package com.midtrans.sdk.corekit.internal.usecase

import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.exception.SnapError
import com.midtrans.sdk.corekit.api.model.PaymentMethod
import com.midtrans.sdk.corekit.api.model.PaymentOption
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.ALFAMART
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.BANK_TRANSFER
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.SHOPEEPAY_QRIS
import com.midtrans.sdk.corekit.api.model.PaymentType.Companion.UOB_EZPAY
import com.midtrans.sdk.corekit.api.model.SnapTransactionDetail
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.api.requestbuilder.snaptoken.SnapTokenRequestBuilder
import com.midtrans.sdk.corekit.internal.analytics.EventAnalytics
import com.midtrans.sdk.corekit.internal.data.repository.CoreApiRepository
import com.midtrans.sdk.corekit.internal.data.repository.MerchantApiRepository
import com.midtrans.sdk.corekit.internal.data.repository.SnapRepository
import com.midtrans.sdk.corekit.internal.network.model.request.SnapTokenRequest
import com.midtrans.sdk.corekit.internal.network.model.response.*
import com.midtrans.sdk.corekit.internal.scheduler.BaseSdkScheduler
import com.midtrans.sdk.corekit.internal.scheduler.TestSdkScheduler
import io.reactivex.Single
import org.hamcrest.CoreMatchers.hasItems
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.*

class PaymentUsecaseTest {
    private lateinit var closeable: AutoCloseable
    private lateinit var scheduler: BaseSdkScheduler
    private lateinit var usecase: PaymentUsecase

    @Mock
    private lateinit var mockSnapRepository: SnapRepository

    @Mock
    private lateinit var mockCoreApiRepository: CoreApiRepository

    @Mock
    private lateinit var mockMerchantApiRepository: MerchantApiRepository

    @Mock
    private lateinit var eventAnalytics: EventAnalytics

    @Before
    fun setUp() {
        closeable = MockitoAnnotations.openMocks(this)
        scheduler = TestSdkScheduler()
        usecase = PaymentUsecase(
            scheduler, mockSnapRepository, mockCoreApiRepository, mockMerchantApiRepository, "clientKey", eventAnalytics
        )
    }

    @After
    fun tearDown() {
        closeable.close()
    }

    @Test
    fun getPaymentOptionWhenSnapTokenProvidedShouldNotGenerateNewSnapToken() {
        val paymentOptionCaptor = argumentCaptor<PaymentOption>()
        val mockCallback: Callback<PaymentOption> = mock()
        whenever(mockSnapRepository.getTransactionDetail("snap-token")) doReturn Single.just(
            Transaction(
                enabledPayments = provideEnabledPayments(),
                merchant = Merchant(
                    merchantId = "merchant-id",
                    preference = MerchantPreferences(
                        displayName = "merchant-name"
                    )
                )
            )
        )
        usecase.getPaymentOption(
            "snap-token",
            provideRequestBuilder(),
            mockCallback
        )
        verify(mockSnapRepository).getTransactionDetail("snap-token")
        verify(mockMerchantApiRepository, never()).getSnapToken(any())
        verify(eventAnalytics).setUserIdentity("merchant-id", "merchant-name", mapOf())
        verify(mockCallback).onSuccess(paymentOptionCaptor.capture())
        val result = paymentOptionCaptor.firstValue
        assertPaymentOption(result, "snap-token")
    }

    @Test
    fun getPaymentOptionWhenSnapTokenNotProvidedShouldGenerateNewSnapToken() {
        val paymentOptionCaptor = argumentCaptor<PaymentOption>()
        val mockCallback: Callback<PaymentOption> = mock()
        whenever(mockMerchantApiRepository.getSnapToken(any())) doReturn Single.just(
            SnapTokenResponse(
                token = "snap-token-generated",
                redirectUrl = "redirect-url-generated"
            )
        )
        whenever(mockSnapRepository.getTransactionDetail("snap-token-generated")) doReturn Single.just(
            Transaction(
                enabledPayments = provideEnabledPayments(),
                merchant = Merchant(
                    merchantId = "merchant-id",
                    preference = MerchantPreferences(
                        displayName = "merchant-name"
                    )
                )
            )
        )
        usecase.getPaymentOption(
            null,
            provideRequestBuilder(),
            mockCallback
        )
        verify(mockSnapRepository).getTransactionDetail("snap-token-generated")
        verify(mockMerchantApiRepository).getSnapToken(
            eq(
                SnapTokenRequest(
                    transactionDetails = SnapTransactionDetail(
                        orderId = "order-id",
                        grossAmount = 1234.00,
                        currency = "currency"
                    )
                )
            )
        )
        verify(eventAnalytics).setUserIdentity("merchant-id", "merchant-name", mapOf())
        verify(eventAnalytics).trackSnapGetTokenRequest(any())
        verify(eventAnalytics).trackSnapGetTokenResult(eq("snap-token-generated"), any())
        verify(mockCallback).onSuccess(paymentOptionCaptor.capture())
        val result = paymentOptionCaptor.firstValue
        assertPaymentOption(result, "snap-token-generated")
    }

    @Test
    fun getPaymentOptionWhenSnapFailedToBeGeneratedShouldCreateSnapError() {
        val errorCaptor = argumentCaptor<SnapError>()
        val mockCallback: Callback<PaymentOption> = mock()
        whenever(mockMerchantApiRepository.getSnapToken(any())) doReturn Single.error(RuntimeException("test snap token error"))
        usecase.getPaymentOption(
            null,
            provideRequestBuilder(),
            mockCallback
        )
        verify(mockSnapRepository, never()).getTransactionDetail(any())
        verify(mockMerchantApiRepository).getSnapToken(
            eq(
                SnapTokenRequest(
                    transactionDetails = SnapTransactionDetail(
                        orderId = "order-id",
                        grossAmount = 1234.00,
                        currency = "currency"
                    )
                )
            )
        )
        verify(mockCallback).onError(errorCaptor.capture())
        val error = errorCaptor.firstValue
        assertTrue(error.cause is RuntimeException)
        assertEquals("test snap token error", error.cause?.message)
        assertEquals("Failed on getting snap token", error.message)
    }

    @Test
    fun getPaymentOptionWhenSnapProvidedAndTransactionDetailFailedShouldCreateSnapError() {
        val errorCaptor = argumentCaptor<SnapError>()
        val mockCallback: Callback<PaymentOption> = mock()
        whenever(mockSnapRepository.getTransactionDetail("snap-token")) doReturn Single.error(RuntimeException("test get transaction detail error"))
        usecase.getPaymentOption(
            "snap-token",
            provideRequestBuilder(),
            mockCallback
        )
        verify(mockSnapRepository).getTransactionDetail("snap-token")
        verify(mockMerchantApiRepository, never()).getSnapToken(any())
        verify(mockCallback).onError(errorCaptor.capture())
        val error = errorCaptor.firstValue
        assertTrue(error.cause is RuntimeException)
        assertEquals("test get transaction detail error", error.cause?.message)
    }

    @Test
    fun getPaymentOptionWhenSnapIsNotProvidedAndTransactionDetailFailedShouldCreateSnapError() {
        val errorCaptor = argumentCaptor<SnapError>()
        val mockCallback: Callback<PaymentOption> = mock()
        whenever(mockMerchantApiRepository.getSnapToken(any())) doReturn Single.just(
            SnapTokenResponse(
                token = "snap-token-generated",
                redirectUrl = "redirect-url-generated"
            )
        )
        whenever(mockSnapRepository.getTransactionDetail("snap-token-generated")) doReturn Single.error(RuntimeException("test get transaction detail error"))
        usecase.getPaymentOption(
            null,
            provideRequestBuilder(),
            mockCallback
        )
        verify(mockSnapRepository).getTransactionDetail("snap-token-generated")
        verify(mockMerchantApiRepository).getSnapToken(
            eq(
                SnapTokenRequest(
                    transactionDetails = SnapTransactionDetail(
                        orderId = "order-id",
                        grossAmount = 1234.00,
                        currency = "currency"
                    )
                )
            ))
        verify(mockCallback).onError(errorCaptor.capture())
        val error = errorCaptor.firstValue
        assertTrue(error.cause is RuntimeException)
        assertEquals("test get transaction detail error", error.cause?.message)
    }

    @Test
    fun getTransactionStatusWhenSucceedShouldCallbackOnSuccess() {
        val mockCallback: Callback<TransactionResponse> = mock()
        val single = Single.just(
            TransactionResponse(
                transactionId = "transactionId",
                transactionStatus = "pending"
            )
        )

        whenever(mockSnapRepository.getTransactionStatus(any())) doReturn single
        usecase.getTransactionStatus("snap-token", mockCallback)
        single.test()
            .assertComplete()

        verify(mockCallback).onSuccess(any())
    }

    private fun assertPaymentOption(result: PaymentOption, snapToken: String) {
        assertThat(
            result.options,
            hasItems(
                PaymentMethod(
                    type = BANK_TRANSFER,
                    channels = listOf("bni_va", "permata_va")
                ),
                PaymentMethod(
                    type = SHOPEEPAY_QRIS,
                    channels = emptyList()
                ),
                PaymentMethod(
                    type = ALFAMART,
                    channels = emptyList()
                ),
                PaymentMethod(
                    type = UOB_EZPAY,
                    channels = listOf("uob_1", "uob_2")
                )
            )
        )
        assertEquals(4, result.options.size)
        assertEquals(snapToken, result.token)
    }

    private fun provideEnabledPayments(): List<EnabledPayment> {
        return listOf(
            EnabledPayment(
                type = "bni_va",
                category = "bank_transfer",
                status = "up",
            ),
            EnabledPayment(
                type = "bca_va",
                category = "bank_transfer",
                status = "down",
            ),
            EnabledPayment(
                type = "permata_va",
                category = "bank_transfer",
                status = "up",
            ),
            EnabledPayment(
                type = "qris",
                acquirer = "shopeepay",
                status = "up"
            ),
            EnabledPayment(
                type = "gopay",
                status = "down"
            ),
            EnabledPayment(
                type = "alfamart",
                category = "cstore",
                status = "up"
            ),
            EnabledPayment(
                type = "uob_ezpay",
                mode = listOf("uob_1", "uob_2"),
                status = "up"
            ),
        )
    }

    private fun provideRequestBuilder(): SnapTokenRequestBuilder {
        return SnapTokenRequestBuilder()
            .withTransactionDetails(
                value = SnapTransactionDetail(
                    orderId = "order-id",
                    grossAmount = 1234.00,
                    currency = "currency"
                )
            )
    }
}