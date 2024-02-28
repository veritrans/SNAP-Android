package com.midtrans.sdk.corekit.internal.analytics

import android.content.Context
import android.os.Build
import clickstream.*
import clickstream.config.*
import clickstream.eventvisualiser.CSEventVisualiserListener
import clickstream.logger.CSLogLevel
import com.google.protobuf.*
import com.midtrans.clickstream.products.events.ui.Page
import com.midtrans.sdk.corekit.internal.di.ConcreteCSDeviceInfo
import java.util.*

class ClickstreamTracker(private val clickstream: ClickStream, val context: Context) {
//class ClickstreamTracker {
//class ClickstreamTracker(val context: Context) {

//    init {
//        var concreteCSDeviceInfo = ConcreteCSDeviceInfo(
//            deviceManufacturer = Build.MANUFACTURER,
//            deviceModel = Build.MODEL,
//            sdkVersion = "",
//            operatingSystem = Build.VERSION.RELEASE,
//            deviceHeight = "",
//            deviceWidth = ""
//        )
//
//       var csConfig =  CSConfiguration.Builder(
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
//                networkConfig = CSNetworkConfig.default(url = "https://midtrans-raccoon-integration.gojekapi.com/api/v1/events", headers = mutableMapOf("satu" to "dua"))
//            )
//        ).apply {
//            setLogLevel(CSLogLevel.DEBUG)
//            addEventListener(CSEventVisualiserListener.getInstance())
//        }.build()
//
//        ClickStream.initialize(csConfig)
//    }
    fun trackEvent(
        page: Page
    ) {

        val csEvent = CSEvent(
            guid = UUID.randomUUID().toString(),
            timestamp = Timestamp.getDefaultInstance(),
            message = page
        )
        clickstream.trackEvent(event = csEvent, expedited = false)
    }


//    fun buildCommonEvent(){
//        Page.newBuilder()
//            .setMeta(
//            EventMeta.newBuilder()
//                .setApp(App.newBuilder()
//                    .setAppName("snap sdk")
//                    .setSdkVersion("")
//                    .setSdkFlowType("")
//                    .setSourceType("android")
//                    .setMerchantUrl("")
//                    .setColourScheme("")
//                    .setStepNumber("")
//                    .setAllowRetry(true))
//                .setDevice(Device.newBuilder().setDeviceType(""))
//                .setMerchant(Merchant.newBuilder().setMerchantName(""))
//                .setTransaction(TransactionDetail.newBuilder().setGrossAmount(""))
//        )
//            .build()
//    }

//    fun buildCommonEventMeta(): EventMeta {
//        return EventMeta.newBuilder()
//            .setApp(App.newBuilder()
//                .setAppName("snap sdk")
//                .setSdkVersion("")
//                .setSdkFlowType("")
//                .setSourceType("android")
//                .setMerchantUrl("")
//                .setColourScheme("")
//                .setStepNumber("")
//                .setAllowRetry(true))
//            .setDevice(Device.newBuilder().setDeviceType(""))
//            .setMerchant(Merchant.newBuilder().setMerchantName(""))
//            .setTransaction(TransactionDetail.newBuilder().setGrossAmount("")).build()
//    }
}