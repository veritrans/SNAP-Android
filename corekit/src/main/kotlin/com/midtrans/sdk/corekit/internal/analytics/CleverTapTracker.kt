package com.midtrans.sdk.corekit.internal.analytics

import com.clevertap.android.sdk.CleverTapAPI

internal class CleverTapTracker(
    private val sdkTracker: CleverTapAPI,
    private val hostTracker: CleverTapAPI?
) {
    private var commonHostEventProperty: Map<String, Any> = emptyMap()

    fun setCommonHostEventProperty(property: Map<String, Any>) {
        commonHostEventProperty = property
    }

    fun pushSdkEvent(name: String, property: Map<String, Any>?) {
        property?.let { sdkTracker.pushEvent(name, it) } ?: sdkTracker.pushEvent(name)
    }

    fun pushHostEvent(name: String, property: Map<String, Any>?, customHostEventProperty: Map<String, Any>?) {
        val eventProperty = property.orEmpty() + commonHostEventProperty + customHostEventProperty.orEmpty()
        if (eventProperty.isEmpty()) {
            hostTracker?.pushEvent(name)
        } else {
            hostTracker?.pushEvent(name, eventProperty)
        }
    }

    fun pushUserProfile(profile: Map<String, Any>) {
        sdkTracker.onUserLogin(profile)
    }

    fun updateUserProfile(profile: Map<String, String>) {
        sdkTracker.pushProfile(profile)
    }

    fun getSdkTracker(): CleverTapAPI = sdkTracker
    fun getHostTracker(): CleverTapAPI? = hostTracker
}