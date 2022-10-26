package com.midtrans.sdk.corekit.internal.analytics

import com.midtrans.sdk.corekit.internal.analytics.EventName.EVENT_SNAP_GET_TOKEN_RESULT
import com.midtrans.sdk.corekit.internal.analytics.EventName.EVENT_SNAP_TOKENIZATION_RESULT
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_MERCHANT_ID
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_MERCHANT_NAME
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_PLATFORM
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_RESPONSE_TIME
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_SDK_TYPE
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_SDK_VERSION
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_SERVICE_TYPE
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_SNAP_TOKEN
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_SNAP_TYPE
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_SOURCE_TYPE
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_STATUS_CODE
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_STATUS_MESSAGE

class EventAnalytics(
    private val mixpanelTracker: MixpanelTracker
) {
    fun setUserIdentity(
        id: String,
        name: String,
        extras: Map<String, String>
    ) {
        mixpanelTracker.setUserIdentity(
            id = id,
            name = name,
            extras = extras
        )
        registerSuperProperties(saudagarId = id, merchantName = name)
    }

    fun testTracker() {
        mixpanelTracker.trackEvent("testEvent")
    }

    //TODO will be implemented separately
    fun trackSnapPageViewed() {}
    fun trackSnapCustomerDataInput() {}
    fun trackSnapOrderDetailsViewed() {}
    fun trackSnapChargeRequest() {}
    fun trackSnapCtaClicked() {}
    fun trackSnapChargeResult() {}
    fun trackSnapHowToPayViewed() {}
    fun trackSnapAccountNumberCopied() {}
    fun trackSnapPaymentNumberButtonRetried() {}
    fun trackSnapExbinResponse() {}
    fun trackSnapPageClosed() {}
    fun trackSnapOpenDeeplink() {}
    fun trackSnapError() {}
    fun trackSnap3dsResult() {}
    fun trackSnapTokenizationResult() {}
    fun trackSnapCtaError() {}

    fun trackSnapGetTokenRequest(snapToken: String) {
        mixpanelTracker.trackEvent(
            eventName = EVENT_SNAP_TOKENIZATION_RESULT,
            properties = mapOf(PROPERTY_SNAP_TOKEN to snapToken)
        )
    }

    fun trackSnapGetTokenResult(
        snapToken: String,
        statusCode: String?,
        statusMessage: String?,
    ) {
        mixpanelTracker.trackEvent(
            eventName = EVENT_SNAP_GET_TOKEN_RESULT,
            properties = mapOf(
                PROPERTY_SNAP_TOKEN to snapToken,
                PROPERTY_STATUS_CODE to statusCode.orEmpty(),
                PROPERTY_STATUS_MESSAGE to statusMessage.orEmpty(),
                PROPERTY_RESPONSE_TIME to "" //TODO ask how to track response time?
            )
        )
    }

    private fun registerSuperProperties(
        saudagarId: String,
        merchantName: String
    ) {
        mixpanelTracker.registerSuperProperties(
            mapOf(
                PROPERTY_PLATFORM to "Mobile",
                PROPERTY_SDK_VERSION to "1.1.1", //TODO get sdk version
                PROPERTY_SDK_TYPE to "UI",
                PROPERTY_MERCHANT_ID to saudagarId,
                PROPERTY_MERCHANT_NAME to merchantName,
                PROPERTY_SOURCE_TYPE to "midtrans-mobile",
                PROPERTY_SERVICE_TYPE to "snap",
                PROPERTY_SNAP_TYPE to "Mobile"
            )
        )
    }
}
