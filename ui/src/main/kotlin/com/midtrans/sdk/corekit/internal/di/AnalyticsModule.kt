package com.midtrans.sdk.corekit.internal.di

import android.app.Application
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.util.Base64
import clickstream.*
import clickstream.config.*
import clickstream.eventvisualiser.CSEventVisualiserListener
import clickstream.eventvisualiser.ui.CSEventVisualiserUI
import clickstream.logger.CSLogLevel
import com.midtrans.sdk.corekit.internal.analytics.ClickstreamEventAnalytics
import com.midtrans.sdk.corekit.internal.analytics.ClickstreamTracker
import com.midtrans.sdk.uikit.BuildConfig
import com.midtrans.sdk.corekit.internal.analytics.EventAnalytics
import com.midtrans.sdk.corekit.internal.analytics.MixpanelTracker
import com.mixpanel.android.mpmetrics.MixpanelAPI
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class AnalyticsModule {
    @Provides
    @Singleton
    fun provideMixpanelTracker(
        context: Context
    ): MixpanelTracker {
        return MixpanelTracker(MixpanelAPI.getInstance(context, BuildConfig.MIXPANEL_TOKEN))
    }

    @Provides
    @Singleton
    fun provideConcreteCSDeviceInfo(): ConcreteCSDeviceInfo {
        return ConcreteCSDeviceInfo(
            deviceManufacturer = Build.MANUFACTURER,
            deviceModel = Build.MODEL,
            sdkVersion = "",
            operatingSystem = Build.VERSION.RELEASE,
            deviceHeight = "",
            deviceWidth = ""
        )
    }

    @Provides
    @Singleton
    fun provideClickStreamConfiguration(
        context: Context,
        concreteCSDeviceInfo: ConcreteCSDeviceInfo
    ): CSConfiguration {
        return CSConfiguration.Builder(
            context = context,
            info = CSInfo(
                appInfo = CSAppInfo(appVersion = "1"),
                locationInfo = CSLocationInfo(0.0, 0.0, mapOf()),
                deviceInfo = concreteCSDeviceInfo,
                sessionInfo = CSSessionInfo(sessionID = ""),
                userInfo = CSUserInfo(
                    currentCountry = "",
                    signedUpCountry = "",
                    identity = 1,
                    email = ""
                )
            ),
            config = CSConfig(
                eventProcessorConfiguration = CSEventProcessorConfig.default(),
                eventSchedulerConfig = CSEventSchedulerConfig.default(),
                healthEventConfig = CSHealthEventConfig.default(),
                networkConfig = CSNetworkConfig.default(url = "https://midtrans-raccoon-integration.gojekapi.com/api/v1/events", headers = getBasicAuthHeadersWithApiKey(context))
            )
        ).apply {
            setLogLevel(CSLogLevel.DEBUG)
//            addEventListener(CSEventVisualiserListener.getInstance())
        }.build()
    }

    @Provides
    @Singleton
    fun provideClickStream(clickStreamConfiguration: CSConfiguration, context: Context): ClickStream {
//        if (ClickStream.getInstance() != null){
//            ClickStream.initialize(clickStreamConfiguration)
////            CSEventVisualiserUI.initialise(context.applicationContext as Application)
////            CSEventVisualiserUI.getInstance().show()
//        }
        ClickStream.initialize(clickStreamConfiguration)
        return ClickStream.getInstance()
    }

    @Provides
    @Singleton
    fun provideClickstreamTracker(clickStream: ClickStream, context: Context): ClickstreamTracker {
        return ClickstreamTracker(clickStream, context)
    }

    @Provides
    @Singleton
    fun provideEventAnalytics(
        mixpanelTracker: MixpanelTracker
    ): EventAnalytics {
        return EventAnalytics(mixpanelTracker)
    }

    @Provides
    @Singleton
    fun provideClickstreamEventAnalytics(
        clickstreamTracker: ClickstreamTracker
    ): ClickstreamEventAnalytics {
        return ClickstreamEventAnalytics(clickstreamTracker)
    }


    private fun getBasicAuthHeadersWithApiKey(context: Context): Map<String, String> {
        val apiKey = "ce99f28c-20f4-a52e-5ccb-635f61371911"
        val encodedApiKey = String(Base64.encode(apiKey.toByteArray(), Base64.NO_WRAP))
        val uniqueId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        return mapOf(
            "Authorization" to "Basic $encodedApiKey",
            "X-UniqueId" to uniqueId
        )

    }
}


data class ConcreteCSDeviceInfo(
    private val deviceManufacturer: String,
    private val deviceModel: String,
    private val sdkVersion: String,
    private val operatingSystem: String,
    private val deviceHeight: String,
    private val deviceWidth: String
) : CSDeviceInfo {

    override fun getDeviceManufacturer(): String = deviceManufacturer

    override fun getDeviceModel(): String = deviceModel

    override fun getSDKVersion(): String = sdkVersion

    override fun getOperatingSystem(): String = operatingSystem

    override fun getDeviceHeight(): String = deviceHeight

    override fun getDeviceWidth(): String = deviceWidth
}