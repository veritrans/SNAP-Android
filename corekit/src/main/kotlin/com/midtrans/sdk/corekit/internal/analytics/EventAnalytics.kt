package com.midtrans.sdk.corekit.internal.analytics

import com.midtrans.sdk.corekit.BuildConfig
import com.midtrans.sdk.corekit.internal.analytics.EventName.EVENT_SNAP_CHARGE_REQUEST
import com.midtrans.sdk.corekit.internal.analytics.EventName.EVENT_SNAP_CHARGE_RESULTS
import com.midtrans.sdk.corekit.internal.analytics.EventName.EVENT_SNAP_GET_TOKEN_REQUEST
import com.midtrans.sdk.corekit.internal.analytics.EventName.EVENT_SNAP_GET_TOKEN_RESULT
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_3DS_VERSION
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CARD_TYPE
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CHANNEL_RESPONSE_CODE
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CHANNEL_RESPONSE_MESSAGE
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CHARGE_RESPONSE_BANK
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CREDIT_CARD_POINT
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CURRENCY
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_FRAUD_STATUS
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_GROSS_AMOUNT
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_MERCHANT_ID
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_MERCHANT_NAME
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_ORDER_ID
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_PAGE_NAME
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_PAYMENT_METHOD_NAME
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_PLATFORM
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_PROMO_AMOUNT
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_PROMO_ID
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_PROMO_NAME
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
        userId: String,
        userName: String,
        merchantId: String,
        merchantName: String,
        extras: Map<String, String> = mapOf()
    ) {
        mixpanelTracker.setUserIdentity(
            id = userId,
            name = userName,
            extras = extras
        )
        registerCommonProperties(saudagarId = merchantId, merchantName = merchantName)
    }

    fun registerPropertyPlatform(isTablet: Boolean) {
        val platform = if (isTablet) "Tablet" else "Mobile"
        mixpanelTracker.registerCommonProperties(
            mapOf(PROPERTY_PLATFORM to platform)
        )
    }

    private fun registerCommonProperties(
        saudagarId: String,
        merchantName: String
    ) {
        mixpanelTracker.registerCommonProperties(
            mapOf(
                PROPERTY_SDK_VERSION to BuildConfig.SDK_VERSION,
                PROPERTY_SDK_TYPE to "UI",
                PROPERTY_MERCHANT_ID to saudagarId,
                PROPERTY_MERCHANT_NAME to merchantName,
                PROPERTY_SOURCE_TYPE to "midtrans-mobile",
                PROPERTY_SERVICE_TYPE to "snap",
                PROPERTY_SNAP_TYPE to "Sdk"
            )
        )
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
        paymentMethodName: String,
        promoName: String? = null,
        promoAmount: String? = null,
        promoId: String? = null,
        creditCardPoint: String? = null
    ) {
        val optional = mutableMapOf<String, String>()
        promoName?.also { optional[PROPERTY_PROMO_NAME] = it }
        promoAmount?.also { optional[PROPERTY_PROMO_AMOUNT] = it }
        promoId?.also { optional[PROPERTY_PROMO_ID] = it }
        creditCardPoint?.also { optional[PROPERTY_CREDIT_CARD_POINT] = it }

        val properties = mapOf(
            PROPERTY_PAGE_NAME to pageName,
            PROPERTY_PAYMENT_METHOD_NAME to paymentMethodName
        ) + optional

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
        transactionId: String,
        @PageName.Def pageName: String,
        paymentMethodName: String,
        responseTime: String,
        bank: String? = null,
        channelResponseCode: String? = null,
        channelResponseMessage: String? = null,
        cardType: String? = null,
        threeDsVersion: String? = null
    ) {
        val optional = mutableMapOf<String, String>()
        bank?.also { optional[PROPERTY_CHARGE_RESPONSE_BANK] = it }
        channelResponseCode?.also { optional[PROPERTY_CHANNEL_RESPONSE_CODE] = it }
        channelResponseMessage?.also { optional[PROPERTY_CHANNEL_RESPONSE_MESSAGE] = it }
        cardType?.also { optional[PROPERTY_CARD_TYPE] = it }
        threeDsVersion?.also { optional[PROPERTY_3DS_VERSION] = it }

        val properties = mapOf(
            PROPERTY_TRANSACTION_STATUS to transactionStatus,
            PROPERTY_FRAUD_STATUS to fraudStatus,
            PROPERTY_CURRENCY to currency,
            PROPERTY_STATUS_CODE to statusCode,
            PROPERTY_TRANSACTION_ID to transactionId,
            PROPERTY_PAGE_NAME to pageName,
            PROPERTY_PAYMENT_METHOD_NAME to paymentMethodName,
            PROPERTY_RESPONSE_TIME to responseTime,
        ) + optional

        mixpanelTracker.trackEvent(
            eventName = EVENT_SNAP_CHARGE_RESULTS,
            properties = properties
        )
    }

    fun trackSnapGetTokenRequest(snapToken: String) {
        mixpanelTracker.trackEvent(
            eventName = EVENT_SNAP_GET_TOKEN_REQUEST,
            properties = mapOf(PROPERTY_SNAP_TOKEN to snapToken)
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

    fun registerCommonTransactionProperties(
        snapToken: String,
        orderId: String,
        grossAmount: String
    ) {
        mixpanelTracker.registerCommonProperties(
            mapOf(
                PROPERTY_SNAP_TOKEN to snapToken,
                PROPERTY_ORDER_ID to orderId,
                PROPERTY_GROSS_AMOUNT to grossAmount
            )
        )
    }
}
