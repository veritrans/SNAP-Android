package com.midtrans.sdk.corekit.internal.di

import android.app.Application
import android.content.Context
//import android.os.Build
//import clickstream.*
//import clickstream.config.*
//import clickstream.eventvisualiser.CSEventVisualiserListener
//import clickstream.eventvisualiser.ui.CSEventVisualiserUI
//import clickstream.logger.CSLogLevel
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

//    @Provides
//    @Singleton
//    fun provideConcreteCSDeviceInfo(): ConcreteCSDeviceInfo {
//        return ConcreteCSDeviceInfo(
//            deviceManufacturer = Build.MANUFACTURER,
//            deviceModel = Build.MODEL,
//            sdkVersion = "",
//            operatingSystem = Build.VERSION.RELEASE,
//            deviceHeight = "",
//            deviceWidth = ""
//        )
//    }
//
//    @Provides
//    @Singleton
//    fun provideClickStreamConfiguration(
//        context: Context,
//        concreteCSDeviceInfo: ConcreteCSDeviceInfo
//    ): CSConfiguration {
//        return CSConfiguration.Builder(
//            context = context,
//            info = CSInfo(
//                appInfo = CSAppInfo(appVersion = "1"),
//                locationInfo = CSLocationInfo(20.2, 10.2, mapOf("satu" to "dua")),
//                deviceInfo = concreteCSDeviceInfo,
//                sessionInfo = CSSessionInfo(sessionID = "sessionID"),
//                userInfo = CSUserInfo(
//                    currentCountry = "",
//                    signedUpCountry = "",
//                    identity = 1,
//                    email = ""
//                )
//            ),
//            config = CSConfig(
//                eventProcessorConfiguration = CSEventProcessorConfig.default(),
//                eventSchedulerConfig = CSEventSchedulerConfig.default(),
//                healthEventConfig = CSHealthEventConfig.default(),
//                networkConfig = CSNetworkConfig.default(url = "", headers = mutableMapOf("satu" to "dua"))
//            )
//        ).apply {
//            setLogLevel(CSLogLevel.DEBUG)
//            addEventListener(CSEventVisualiserListener.getInstance())
//        }.build()
//    }
//
//    @Provides
//    @Singleton
//    fun provideClickStream(clickStreamConfiguration: CSConfiguration, context: Context): ClickStream {
//        if (ClickStream.getInstance() != null){
//            ClickStream.initialize(clickStreamConfiguration)
//            CSEventVisualiserUI.initialise(context.applicationContext as Application)
//        }
//        return ClickStream.getInstance()
//    }
//
//    @Provides
//    @Singleton
//    fun provideClickstreamTracker(clickStream: ClickStream): ClickstreamTracker {
//        return ClickstreamTracker(clickStream)
//    }

    @Provides
    @Singleton
    fun provideEventAnalytics(
        mixpanelTracker: MixpanelTracker
    ): EventAnalytics {
        return EventAnalytics(mixpanelTracker)
    }

//    @Provides
//    @Singleton
//    fun provideClickstreamEventAnalytics(
//        clickstreamTracker: ClickstreamTracker
//    ): ClickstreamEventAnalytics {
//        return ClickstreamEventAnalytics(clickstreamTracker)
//    }
}


//data class ConcreteCSDeviceInfo(
//    private val deviceManufacturer: String,
//    private val deviceModel: String,
//    private val sdkVersion: String,
//    private val operatingSystem: String,
//    private val deviceHeight: String,
//    private val deviceWidth: String
//) : CSDeviceInfo {
//
//    override fun getDeviceManufacturer(): String = deviceManufacturer
//
//    override fun getDeviceModel(): String = deviceModel
//
//    override fun getSDKVersion(): String = sdkVersion
//
//    override fun getOperatingSystem(): String = operatingSystem
//
//    override fun getDeviceHeight(): String = deviceHeight
//
//    override fun getDeviceWidth(): String = deviceWidth
//}