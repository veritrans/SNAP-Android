package com.midtrans.sdk.corekit.internal.analytics

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
    }

    fun testTracker() {
        mixpanelTracker.trackEvent("testEvent")
    }

    //TODO will be implemented separately
    fun trackSnapPageViewed() {}
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
    fun trackSnapCustomerDataInput() {}
}