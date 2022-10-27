package com.midtrans.sdk.corekit.internal.analytics

import com.mixpanel.android.mpmetrics.MixpanelAPI
import org.json.JSONObject

class MixpanelTracker(private val mixpanelApi: MixpanelAPI) {
    fun setUserIdentity(
        id: String,
        name: String,
        extras: Map<String, String>
    ) {
        mixpanelApi.apply {
            identify(id)
            people.set(PROPERTY_NAME, name)
            people.setMap(extras)
        }
    }

    fun trackEvent(
        eventName: String,
        properties: Map<String, Any> = mapOf()
    ) {
        mixpanelApi.track(eventName, JSONObject(properties))
    }

    fun registerCommonProperties(
        properties: Map<String, Any>
    ) {
        mixpanelApi.registerSuperProperties(JSONObject(properties))
    }

    companion object {
        private const val PROPERTY_NAME = "\$name"
    }
}