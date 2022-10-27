package com.midtrans.sdk.corekit.internal.analytics

import com.midtrans.sdk.corekit.BuildConfig
import com.midtrans.sdk.corekit.internal.analytics.EventName.EVENT_SNAP_CHARGE_REQUEST
import com.midtrans.sdk.corekit.internal.analytics.EventName.EVENT_SNAP_GET_TOKEN_REQUEST
import com.midtrans.sdk.corekit.internal.analytics.EventName.EVENT_SNAP_GET_TOKEN_RESULT
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CURRENCY
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_FRAUD_STATUS
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_GROSS_AMOUNT
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_MERCHANT_ID
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_MERCHANT_NAME
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_ORDER_ID
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_PAGE_NAME
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_PAYMENT_METHOD_NAME
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_PLATFORM
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_RESPONSE_TIME
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_SDK_TYPE
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_SDK_VERSION
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_SERVICE_TYPE
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_SNAP_TOKEN
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_SNAP_TYPE
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_SOURCE_TYPE
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_STATUS_CODE
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_TRANSACTION_ID
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_TRANSACTION_STATUS

class EventAnalytics(
    private val mixpanelTracker: MixpanelTracker
) {
    fun setUserIdentity(
        id: String,
        name: String,
        extras: Map<String, String> = mapOf()
    ) {
        mixpanelTracker.setUserIdentity(
            id = id,
            name = name,
            extras = extras
        )
        registerCommonProperties(saudagarId = id, merchantName = name)
    }

    fun testTracker() {
        mixpanelTracker.trackEvent("testEvent")
    }

    //TODO will be implemented separately
    fun trackSnapPageViewed() {}
    fun trackSnapCustomerDataInput() {}
    fun trackSnapOrderDetailsViewed() {}
    fun trackSnapCtaClicked() {}
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

    fun trackSnapChargeRequest(
        pageName: String,
        promoInfo: Map<String, String> = mapOf()
    ) {
        val properties = mapOf(
            PROPERTY_PAGE_NAME to pageName
        ) + promoInfo
        mixpanelTracker.trackEvent(
            eventName = EVENT_SNAP_CHARGE_REQUEST,
            properties = properties
        )
    }

    fun trackSnapChargeResult(
        transactionStatus: String,
        fraudStatus: String,
        currency: String,
        statusCode: String,
        pageName: String,
        creditCardInfo: Map<String, String> = mapOf()
    ) {
        val properties = mapOf(
            PROPERTY_TRANSACTION_STATUS to transactionStatus,
            PROPERTY_FRAUD_STATUS to fraudStatus,
            PROPERTY_CURRENCY to currency,
            PROPERTY_STATUS_CODE to statusCode,
            PROPERTY_PAGE_NAME to pageName
        ) + creditCardInfo
        mixpanelTracker.trackEvent(
            eventName = EVENT_SNAP_CHARGE_REQUEST,
            properties = properties
        )
    }

    fun trackSnapGetTokenRequest(snapToken: String) {
        mixpanelTracker.trackEvent(
            eventName = EVENT_SNAP_GET_TOKEN_REQUEST,
            properties = mapOf(PROPERTY_SNAP_TOKEN to snapToken) //TODO currently snap token is empty because generate token happened inside sdk, check if it is still needed
        )
    }

    fun trackSnapGetTokenResult(
        snapToken: String,
        responseTime: String
    ) {
        mixpanelTracker.trackEvent(
            eventName = EVENT_SNAP_GET_TOKEN_RESULT,
            properties = mapOf(
                PROPERTY_SNAP_TOKEN to snapToken,
                PROPERTY_RESPONSE_TIME to responseTime
            )
        )
    }

    private fun registerCommonProperties(
        saudagarId: String,
        merchantName: String
    ) {
        mixpanelTracker.registerCommonProperties(
            mapOf(
                PROPERTY_PLATFORM to "Mobile",
                PROPERTY_SDK_VERSION to BuildConfig.SDK_VERSION,
                PROPERTY_SDK_TYPE to "UI",
                PROPERTY_MERCHANT_ID to saudagarId,
                PROPERTY_MERCHANT_NAME to merchantName,
                PROPERTY_SOURCE_TYPE to "midtrans-mobile",
                PROPERTY_SERVICE_TYPE to "snap",
                PROPERTY_SNAP_TYPE to "Mobile"
            )
        )
    }

    fun registerCommonTransactionProperties(
        snapToken: String,
        orderId: String,
        transactionId: String,
        grossAmount: String,
        paymentMethodName: String
    ) {
        mixpanelTracker.registerCommonProperties(
            mapOf(
                PROPERTY_SNAP_TOKEN to snapToken,
                PROPERTY_ORDER_ID to orderId,
                PROPERTY_TRANSACTION_ID to transactionId,
                PROPERTY_GROSS_AMOUNT to grossAmount,
                PROPERTY_PAYMENT_METHOD_NAME to paymentMethodName
            )
        )
    }

    fun unregisterCommonTransactionProperties() {
        val propertyNames = listOf(
            PROPERTY_SNAP_TOKEN,
            PROPERTY_ORDER_ID,
            PROPERTY_TRANSACTION_ID,
            PROPERTY_GROSS_AMOUNT,
            PROPERTY_PAYMENT_METHOD_NAME
        )
        mixpanelTracker.unregisterCommonProperties(propertyNames)
    }
}
