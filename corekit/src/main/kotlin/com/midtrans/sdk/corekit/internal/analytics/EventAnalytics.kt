package com.midtrans.sdk.corekit.internal.analytics

import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_MERCHANT_ID
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_MERCHANT_NAME
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_PLATFORM
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_SDK_TYPE
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_SDK_VERSION
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_SERVICE_TYPE
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_SOURCE_TYPE

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

    fun trackSnapPageViewed() {}


    //TODO will be implemented separately
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
    fun trackSnapGetTokenRequest() {}
    fun trackSnapGetTokenResult() {}

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
                PROPERTY_SERVICE_TYPE to "snap"
            )
        )
    }

    fun registerCommonTransactionProperties(

    ) {
        
    }
}
