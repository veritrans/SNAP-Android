package com.midtrans.sdk.corekit.internal.analytics

import com.midtrans.sdk.corekit.BuildConfig
import com.midtrans.sdk.corekit.internal.analytics.EventName.EVENT_SNAP_3DS_RESULT
import com.midtrans.sdk.corekit.internal.analytics.EventName.EVENT_SNAP_ACCOUNT_NUMBER_COPIED
import com.midtrans.sdk.corekit.internal.analytics.EventName.EVENT_SNAP_CHARGE_REQUEST
import com.midtrans.sdk.corekit.internal.analytics.EventName.EVENT_SNAP_CHARGE_RESULTS
import com.midtrans.sdk.corekit.internal.analytics.EventName.EVENT_SNAP_CTA_CLICKED
import com.midtrans.sdk.corekit.internal.analytics.EventName.EVENT_SNAP_EXBIN_RESPONSE
import com.midtrans.sdk.corekit.internal.analytics.EventName.EVENT_SNAP_GET_TOKEN_REQUEST
import com.midtrans.sdk.corekit.internal.analytics.EventName.EVENT_SNAP_GET_TOKEN_RESULT
import com.midtrans.sdk.corekit.internal.analytics.EventName.EVENT_SNAP_HOW_TO_PAY_VIEWED
import com.midtrans.sdk.corekit.internal.analytics.EventName.EVENT_SNAP_OPEN_DEEPLINK
import com.midtrans.sdk.corekit.internal.analytics.EventName.EVENT_SNAP_ORDER_DETAILS_VIEWED
import com.midtrans.sdk.corekit.internal.analytics.EventName.EVENT_SNAP_PAGE_CLOSED
import com.midtrans.sdk.corekit.internal.analytics.EventName.EVENT_SNAP_PAYMENT_NUMBER_BUTTON_RETRIED
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_3DS_VERSION
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CARD_BANK
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CARD_BANK_CODE
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CARD_BIN
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CARD_BIN_CLASSS
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CARD_BIN_TYPE
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CARD_BRAND
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CARD_CHANNEL
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CARD_COUNTRY_CODE
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CARD_TYPE
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CHANNEL_RESPONSE_CODE
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CHANNEL_RESPONSE_MESSAGE
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CHARGE_RESPONSE_BANK
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CREDIT_CARD_POINT
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CTA_NAME
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CURRENCY
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_ECI
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CUSTOMER_CITY
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CUSTOMER_EMAIL
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CUSTOMER_NAME
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CUSTOMER_PHONE_NUMBER
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CUSTOMER_POST_CODE
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_FRAUD_STATUS
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_GROSS_AMOUNT
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_MERCHANT_ID
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_MERCHANT_NAME
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_NET_AMOUNT
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_ORDER_ID
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_PAGE_NAME
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_PAYMENT_METHOD_NAME
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_PLATFORM
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_PROMO_AMOUNT
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_PROMO_ID
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_PROMO_NAME
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_REGISTRATION_REQUIRED
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_RESPONSE_TIME
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_SDK_TYPE
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_SDK_VERSION
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_SERVICE_TYPE
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_SNAP_TOKEN
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_SNAP_TYPE
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_SOURCE_TYPE
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_STATUS_CODE
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_TOTAL_ITEMS
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_TOTAL_QUANTITY
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_TRANSACTION_ID
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_TRANSACTION_STATUS

class EventAnalytics(
    private val mixpanelTracker: MixpanelTracker
) {
    fun setUserIdentity(
        userId: String,
        userName: String,
        extras: Map<String, String> = mapOf()
    ) {
        mixpanelTracker.setUserIdentity(
            id = userId,
            name = userName,
            extras = extras
        )
    }

    fun registerCommonProperties(platform: String) {
        mixpanelTracker.registerCommonProperties(
            mapOf(
                PROPERTY_SDK_VERSION to BuildConfig.SDK_VERSION,
                PROPERTY_SDK_TYPE to "UI",
                PROPERTY_SOURCE_TYPE to "midtrans-mobile",
                PROPERTY_SERVICE_TYPE to "snap",
                PROPERTY_SNAP_TYPE to "Sdk",
                PROPERTY_PLATFORM to platform
            )
        )
    }

    fun registerCommonTransactionProperties(
        snapToken: String,
        orderId: String,
        grossAmount: String,
        merchantId: String,
        merchantName: String
    ) {
        mixpanelTracker.registerCommonProperties(
            mapOf(
                PROPERTY_SNAP_TOKEN to snapToken,
                PROPERTY_ORDER_ID to orderId,
                PROPERTY_GROSS_AMOUNT to grossAmount,
                PROPERTY_MERCHANT_ID to merchantId,
                PROPERTY_MERCHANT_NAME to merchantName
            )
        )
    }

    fun registerCommonCustomerProperties(
        customerName: String?,
        customerEmail: String?,
        customerPhoneNumber: String?,
        customerCity: String?,
        customerPostCode: String?,
        totalItems: String?,
        totalQuantity: String?,
    ) {
        val customerProperties = mutableMapOf<String, String>()
        customerName?.also { customerProperties[PROPERTY_CUSTOMER_NAME] = it }
        customerEmail?.also { customerProperties[PROPERTY_CUSTOMER_EMAIL] = it }
        customerPhoneNumber?.also { customerProperties[PROPERTY_CUSTOMER_PHONE_NUMBER] = it }
        customerCity?.also { customerProperties[PROPERTY_CUSTOMER_CITY] = it }
        customerPostCode?.also { customerProperties[PROPERTY_CUSTOMER_POST_CODE] = it }
        totalItems?.also { customerProperties[PROPERTY_TOTAL_ITEMS] = it }
        totalQuantity?.also { customerProperties[PROPERTY_TOTAL_QUANTITY] = it }

        mixpanelTracker.registerCommonProperties(customerProperties)
    }

    //TODO will be implemented separately
    fun trackSnapPageViewed() {}
    fun trackSnapCustomerDataInput() {}

    fun trackSnapAccountNumberCopied(
        pageName: String?,
        paymentMethodName: String?
    ) {
        val optionalProperties = mutableMapOf<String, String>()
        paymentMethodName?.also { optionalProperties[PROPERTY_PAYMENT_METHOD_NAME] = it }
        pageName?.also { optionalProperties[PROPERTY_PAGE_NAME] = it }

        mixpanelTracker.trackEvent(
            eventName = EVENT_SNAP_ACCOUNT_NUMBER_COPIED,
            properties = optionalProperties
        )
    }

    fun trackSnapPaymentNumberButtonRetried(
        pageName: String?,
        paymentMethodName: String?
    ) {
        val optionalProperties = mutableMapOf<String, String>()
        paymentMethodName?.also { optionalProperties[PROPERTY_PAYMENT_METHOD_NAME] = it }
        pageName?.also { optionalProperties[PROPERTY_PAGE_NAME] = it }

        mixpanelTracker.trackEvent(
            eventName = EVENT_SNAP_PAYMENT_NUMBER_BUTTON_RETRIED,
            properties = optionalProperties
        )
    }

    fun trackSnapError() {}
    fun trackSnapTokenizationResult() {}
    fun trackSnapCtaError() {}

    fun trackSnapOrderDetailsViewed(
        pageName: String?,
        paymentMethodName: String?,
        transactionId: String?,
        netAmount: String?
    ) {
        val optionalProperties = mutableMapOf<String, String>()
        pageName?.also { optionalProperties[PROPERTY_PAGE_NAME] = it }
        paymentMethodName?.also { optionalProperties[PROPERTY_PAYMENT_METHOD_NAME] = it }
        transactionId?.also { optionalProperties[PROPERTY_TRANSACTION_ID] = it }
        netAmount?.also { optionalProperties[PROPERTY_NET_AMOUNT] = it }

        mixpanelTracker.trackEvent(
            eventName = EVENT_SNAP_ORDER_DETAILS_VIEWED,
            properties = optionalProperties
        )
    }

    fun trackSnap3DsResult(
        transactionStatus: String?,
        cardType: String?,
        bank: String?,
        threeDsVersion: String?,
        channelResponseCode: String?,
        eci: String?,
        paymentMethodName: String
    ) {
        val optionalProperties = mutableMapOf<String,String>()
        transactionStatus?.also { optionalProperties[PROPERTY_TRANSACTION_STATUS] = it }
        cardType?.also { optionalProperties[PROPERTY_CARD_TYPE] = it }
        bank?.also { optionalProperties[PROPERTY_CARD_BANK] = it }
        threeDsVersion?.also { optionalProperties[PROPERTY_3DS_VERSION] = it }
        channelResponseCode?.also { optionalProperties[PROPERTY_CHANNEL_RESPONSE_CODE] = it }
        eci?.also { optionalProperties[PROPERTY_ECI] = it }

        mixpanelTracker.trackEvent(
            eventName = EVENT_SNAP_3DS_RESULT,
            properties = mapOf(
                PROPERTY_PAYMENT_METHOD_NAME to paymentMethodName
            ) + optionalProperties
        )
    }

    fun trackSnapOpenDeeplink(
        pageName: String,
        paymentMethodName: String
    ) {
        mixpanelTracker.trackEvent(
            eventName = EVENT_SNAP_OPEN_DEEPLINK,
            properties = mapOf(
                PROPERTY_PAGE_NAME to pageName,
                PROPERTY_PAYMENT_METHOD_NAME to paymentMethodName
            )
        )
    }

    fun trackSnapPageClosed(pageName: String) {
        mixpanelTracker.trackEvent(
            eventName = EVENT_SNAP_PAGE_CLOSED,
            properties = mapOf(PROPERTY_PAGE_NAME to pageName)
        )
    }

    fun trackSnapExbinResponse(
        pageName: String,
        paymentMethodName: String,
        registrationRequired: String?,
        countryCode: String?,
        channel: String?,
        brand: String?,
        binType: String?,
        binClass: String?,
        bin: String?,
        bankCode: String?
    ) {
        val optionalProperties = mutableMapOf<String, String>()
        registrationRequired?.also { optionalProperties[PROPERTY_REGISTRATION_REQUIRED] = it }
        countryCode?.also { optionalProperties[PROPERTY_CARD_COUNTRY_CODE] = it }
        channel?.also { optionalProperties[PROPERTY_CARD_CHANNEL] = it }
        brand?.also { optionalProperties[PROPERTY_CARD_BRAND] = it }
        binType?.also { optionalProperties[PROPERTY_CARD_BIN_TYPE] = it }
        binClass?.also { optionalProperties[PROPERTY_CARD_BIN_CLASSS] = it }
        bin?.also { optionalProperties[PROPERTY_CARD_BIN] = it }
        bankCode?.also { optionalProperties[PROPERTY_CARD_BANK_CODE] = it }

        mixpanelTracker.trackEvent(
            eventName = EVENT_SNAP_EXBIN_RESPONSE,
            properties = mapOf(
                PROPERTY_PAGE_NAME to pageName,
                PROPERTY_PAYMENT_METHOD_NAME to paymentMethodName
            ) + optionalProperties
        )
    }

    fun trackSnapHowToPayViewed(
        pageName: String,
        paymentMethodName: String
    ) {
        mixpanelTracker.trackEvent(
            eventName = EVENT_SNAP_HOW_TO_PAY_VIEWED,
            properties = mapOf(
                PROPERTY_PAGE_NAME to pageName,
                PROPERTY_PAYMENT_METHOD_NAME to paymentMethodName
            )
        )
    }

    fun trackSnapCtaClicked(
        ctaName: String,
        pageName: String,
        paymentMethodName: String
    ) {
        mixpanelTracker.trackEvent(
            eventName = EVENT_SNAP_CTA_CLICKED,
            properties = mapOf(
                PROPERTY_CTA_NAME to ctaName,
                PROPERTY_PAGE_NAME to pageName,
                PROPERTY_PAYMENT_METHOD_NAME to paymentMethodName
            )
        )
    }

    fun trackSnapChargeRequest(
        pageName: String,
        paymentMethodName: String,
        promoName: String? = null,
        promoAmount: String? = null,
        promoId: String? = null,
        creditCardPoint: String? = null
    ) {
        val optionalProperties = mutableMapOf<String, String>()
        promoName?.also { optionalProperties[PROPERTY_PROMO_NAME] = it }
        promoAmount?.also { optionalProperties[PROPERTY_PROMO_AMOUNT] = it }
        promoId?.also { optionalProperties[PROPERTY_PROMO_ID] = it }
        creditCardPoint?.also { optionalProperties[PROPERTY_CREDIT_CARD_POINT] = it }

        val properties = mapOf(
            PROPERTY_PAGE_NAME to pageName,
            PROPERTY_PAYMENT_METHOD_NAME to paymentMethodName
        ) + optionalProperties

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
        val optionalProperties = mutableMapOf<String, String>()
        bank?.also { optionalProperties[PROPERTY_CHARGE_RESPONSE_BANK] = it }
        channelResponseCode?.also { optionalProperties[PROPERTY_CHANNEL_RESPONSE_CODE] = it }
        channelResponseMessage?.also { optionalProperties[PROPERTY_CHANNEL_RESPONSE_MESSAGE] = it }
        cardType?.also { optionalProperties[PROPERTY_CARD_TYPE] = it }
        threeDsVersion?.also { optionalProperties[PROPERTY_3DS_VERSION] = it }

        val properties = mapOf(
            PROPERTY_TRANSACTION_STATUS to transactionStatus,
            PROPERTY_FRAUD_STATUS to fraudStatus,
            PROPERTY_CURRENCY to currency,
            PROPERTY_STATUS_CODE to statusCode,
            PROPERTY_TRANSACTION_ID to transactionId,
            PROPERTY_PAGE_NAME to pageName,
            PROPERTY_PAYMENT_METHOD_NAME to paymentMethodName,
            PROPERTY_RESPONSE_TIME to responseTime,
        ) + optionalProperties

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

}
