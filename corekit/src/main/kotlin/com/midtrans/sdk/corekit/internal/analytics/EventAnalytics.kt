package com.midtrans.sdk.corekit.internal.analytics

import androidx.annotation.Keep
import com.midtrans.sdk.corekit.internal.config.Config

@Keep
internal class EventAnalytics(builder: Builder) {
    @Keep
    class Builder {
        private lateinit var cleverTapTracker: CleverTapTracker
        private lateinit var config: Config


        fun getCleverTapTracker(): CleverTapTracker = cleverTapTracker
        fun getConfig(): Config = config

        fun withCleverTapTracker(tracker: CleverTapTracker) = apply { cleverTapTracker = tracker }


        fun build() = EventAnalytics(this)
    }

    private val cleverTapTracker: CleverTapTracker
    private val config: Config

    init {
        cleverTapTracker = builder.getCleverTapTracker()
        config = builder.getConfig()

        instance = this
    }

    private fun trackEvent(
        eventName: String,
        values: Map<String, Any>? = null,
        customHostEventProperties: Map<String, Any>? = null
    ) {
        cleverTapTracker.pushSdkEvent(eventName, values)
        cleverTapTracker.pushHostEvent(
            eventName,
            values,
            customHostEventProperty = customHostEventProperties
        )
    }

    companion object {
        private var instance: EventAnalytics? = null
    }

}