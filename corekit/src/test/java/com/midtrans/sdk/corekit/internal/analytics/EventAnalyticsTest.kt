package com.midtrans.sdk.corekit.internal.analytics

import com.midtrans.sdk.corekit.internal.analytics.EventName.EVENT_SNAP_GET_TOKEN_REQUEST
import com.midtrans.sdk.corekit.internal.analytics.EventName.EVENT_SNAP_GET_TOKEN_RESULT
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_RESPONSE_TIME
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_SNAP_TOKEN
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
            EventName.PROPERTY_MERCHANT_ID to "id",
            EventName.PROPERTY_MERCHANT_NAME to "name",
            EventName.PROPERTY_SOURCE_TYPE to "midtrans-mobile",
            EventName.PROPERTY_SERVICE_TYPE to "snap",
            EventName.PROPERTY_SNAP_TYPE to "Mobile"
        )
        eventAnalytics.setUserIdentity("id", "name", mapOf())
        verify(mixpanelTracker).setUserIdentity("id", "name", mapOf())
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
        eventAnalytics.trackSnapGetTokenResult("token")
        verify(mixpanelTracker).trackEvent(
            eventName = EVENT_SNAP_GET_TOKEN_RESULT,
            properties = mapOf(
                PROPERTY_SNAP_TOKEN to "token",
                PROPERTY_RESPONSE_TIME to ""
            )
        )
    }
}