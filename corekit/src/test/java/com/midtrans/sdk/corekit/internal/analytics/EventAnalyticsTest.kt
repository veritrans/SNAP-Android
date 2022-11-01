package com.midtrans.sdk.corekit.internal.analytics

import com.midtrans.sdk.corekit.api.model.PaymentMethod
import com.midtrans.sdk.corekit.internal.analytics.EventName.EVENT_SNAP_CHARGE_REQUEST
import com.midtrans.sdk.corekit.internal.analytics.EventName.EVENT_SNAP_CHARGE_RESULTS
import com.midtrans.sdk.corekit.internal.analytics.EventName.EVENT_SNAP_GET_TOKEN_REQUEST
import com.midtrans.sdk.corekit.internal.analytics.EventName.EVENT_SNAP_GET_TOKEN_RESULT
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CURRENCY
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_FRAUD_STATUS
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_PAGE_NAME
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_PAYMENT_METHOD_NAME
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_RESPONSE_TIME
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_SNAP_TOKEN
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_STATUS_CODE
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_TRANSACTION_ID
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_TRANSACTION_STATUS
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify

internal class EventAnalyticsTest {
    private lateinit var closeable: AutoCloseable
    private lateinit var eventAnalytics: EventAnalytics

    @Mock
    private lateinit var mixpanelTracker: MixpanelTracker

    @Before
    fun setUp() {
        closeable = MockitoAnnotations.openMocks(this)
        eventAnalytics = EventAnalytics(mixpanelTracker)
    }

    @After
    fun tearDown() {
        closeable.close()
    }

    @Test
    fun verifySetUserIdentity() {
        val commonProperties = mapOf(
            EventName.PROPERTY_PLATFORM to "Mobile",
            EventName.PROPERTY_SDK_VERSION to "2.0.0",
            EventName.PROPERTY_SDK_TYPE to "UI",
            EventName.PROPERTY_MERCHANT_ID to "merchant_id",
            EventName.PROPERTY_MERCHANT_NAME to "merchant_name",
            EventName.PROPERTY_SOURCE_TYPE to "midtrans-mobile",
            EventName.PROPERTY_SERVICE_TYPE to "snap",
            EventName.PROPERTY_SNAP_TYPE to "Mobile"
        )
        eventAnalytics.setUserIdentity("user_id", "user_name","merchant_id", "merchant_name", mapOf())
        verify(mixpanelTracker).setUserIdentity("user_id", "user_name", mapOf())
        verify(mixpanelTracker).registerCommonProperties(commonProperties)
    }

    @Test
    fun verifyTrackSnapGetTokenRequest() {
        eventAnalytics.trackSnapGetTokenRequest("token")
        verify(mixpanelTracker).trackEvent(
            eventName = EVENT_SNAP_GET_TOKEN_REQUEST,
            properties = mapOf(PROPERTY_SNAP_TOKEN to "token")
        )
    }

    @Test
    fun verifyTrackSnapGetTokenResult() {
        eventAnalytics.trackSnapGetTokenResult("token", "1000")
        verify(mixpanelTracker).trackEvent(
            eventName = EVENT_SNAP_GET_TOKEN_RESULT,
            properties = mapOf(
                PROPERTY_SNAP_TOKEN to "token",
                PROPERTY_RESPONSE_TIME to "1000"
            )
        )
    }

    @Test
    fun verifyTrackSnapChargeRequest() {
        eventAnalytics.trackSnapChargeRequest("page-name", "credit-card")
        verify(mixpanelTracker).trackEvent(
            eventName = EVENT_SNAP_CHARGE_REQUEST,
            properties = mapOf(
                PROPERTY_PAGE_NAME to "page-name",
                PROPERTY_PAYMENT_METHOD_NAME to "credit-card"
            )
        )
    }

    @Test
    fun verifyTrackSnapChargeResult() {
        eventAnalytics.trackSnapChargeResult(
            transactionStatus = "transaction-status",
            fraudStatus = "fraud-status",
            currency = "currency",
            statusCode = "status-code",
            transactionId = "transaction-id",
            pageName = PageName.CREDIT_DEBIT_CARD_PAGE,
            paymentMethodName = "payment-type",
            responseTime = "response-time"
        )
        verify(mixpanelTracker).trackEvent(
            eventName = EVENT_SNAP_CHARGE_RESULTS,
            properties = mapOf(
                PROPERTY_TRANSACTION_STATUS to "transaction-status",
                PROPERTY_FRAUD_STATUS to "fraud-status",
                PROPERTY_CURRENCY to "currency",
                PROPERTY_STATUS_CODE to "status-code",
                PROPERTY_TRANSACTION_ID to "transaction-id",
                PROPERTY_PAGE_NAME to PageName.CREDIT_DEBIT_CARD_PAGE,
                PROPERTY_PAYMENT_METHOD_NAME to "payment-type",
                PROPERTY_RESPONSE_TIME to "response-time"
            )
        )
    }
}