package com.midtrans.sdk.corekit.internal.analytics

import clickstream.*
import com.google.protobuf.*
import com.midtrans.clickstream.products.events.ui.Page
import java.util.*

//class ClickstreamTracker(private val clickstream: ClickStream) {
class ClickstreamTracker {
    fun trackEvent(
        page: Page
    ) {

//        val csEvent = CSEvent(
//            guid = UUID.randomUUID().toString(),
//            timestamp = Timestamp.getDefaultInstance(),
//            message = page
//        )
//        clickstream.trackEvent(event = csEvent, expedited = false)
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