package com.midtrans.sdk.corekit.internal.analytics

import com.midtrans.sdk.corekit.BuildConfig
import com.midtrans.sdk.corekit.api.model.PaymentType
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
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CTA_NAME
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CARD_BANK_CODE
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CARD_BIN
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CARD_BIN_CLASSS
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CARD_BIN_TYPE
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CARD_BRAND
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CARD_CHANNEL
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CARD_COUNTRY_CODE
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CARD_TYPE
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CHANNEL_RESPONSE_CODE
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CURRENCY
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CUSTOMER_CITY
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CUSTOMER_EMAIL
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CUSTOMER_NAME
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CUSTOMER_PHONE_NUMBER
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_CUSTOMER_POST_CODE
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_ECI
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_FRAUD_STATUS
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_NET_AMOUNT
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_PAGE_NAME
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_PAYMENT_METHOD_NAME
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_PLATFORM
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_REGISTRATION_REQUIRED
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_RESPONSE_TIME
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_SNAP_TOKEN
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_STATUS_CODE
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_TOTAL_ITEMS
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_TOTAL_QUANTITY
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_TRANSACTION_ID
import com.midtrans.sdk.corekit.internal.analytics.EventName.PROPERTY_TRANSACTION_STATUS
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify

internal class EventAnalyticsTest {
    private lateinit var closeable: AutoCloseable
    private lateinit var eventAnalytics: EventAnalytics

    @Mock
    private lateinit var mixpanelTracker: MixpanelTracker

    @Before
    fun setUp() {
        closeable = MockitoAnnotations.openMocks(this)
        eventAnalytics = EventAnalytics(mixpanelTracker)
    }

    @After
    fun tearDown() {
        closeable.close()
    }

    @Test
    fun verifySetUserIdentity() {
        eventAnalytics.setUserIdentity("user_id", "user_name", mapOf())
        verify(mixpanelTracker).setUserIdentity("user_id", "user_name", mapOf())
    }

    @Test
    fun verifyRegisterCommonProperties() {
        eventAnalytics.registerCommonProperties("tablet")
        verify(mixpanelTracker).registerCommonProperties(
            mapOf(
                EventName.PROPERTY_SDK_VERSION to BuildConfig.SDK_VERSION,
                EventName.PROPERTY_SDK_TYPE to "UI",
                EventName.PROPERTY_SOURCE_TYPE to "midtrans-mobile",
                EventName.PROPERTY_SERVICE_TYPE to "snap",
                EventName.PROPERTY_SNAP_TYPE to "Sdk",
                PROPERTY_PLATFORM to "tablet"
            )
        )
    }

    @Test
    fun verifyRegisterCommonTransactionProperties() {
        eventAnalytics.registerCommonTransactionProperties(
            snapToken = "snap-token",
            orderId = "order-id",
            grossAmount = "gross-amount",
            merchantId = "merchant-id",
            merchantName = "merchant-name"
        )
        verify(mixpanelTracker).registerCommonProperties(
            mapOf(
                PROPERTY_SNAP_TOKEN to "snap-token",
                EventName.PROPERTY_ORDER_ID to "order-id",
                EventName.PROPERTY_GROSS_AMOUNT to "gross-amount",
                EventName.PROPERTY_MERCHANT_ID to "merchant-id",
                EventName.PROPERTY_MERCHANT_NAME to "merchant-name"
            )
        )
    }

    @Test
    fun verifyRegisterCommonCustomerProperties() {
        eventAnalytics.registerCommonCustomerProperties(
            customerName = "name",
            customerEmail = "email",
            customerPhoneNumber = "phone",
            customerCity = "city",
            customerPostCode = "post-code",
            totalItems = "total-items",
            totalQuantity = "total-quantity"
        )
        verify(mixpanelTracker).registerCommonProperties(
            mapOf(
                PROPERTY_CUSTOMER_NAME to "name",
                PROPERTY_CUSTOMER_EMAIL to "email",
                PROPERTY_CUSTOMER_PHONE_NUMBER to "phone",
                PROPERTY_CUSTOMER_CITY to "city",
                PROPERTY_CUSTOMER_POST_CODE to "post-code",
                PROPERTY_TOTAL_ITEMS to "total-items",
                PROPERTY_TOTAL_QUANTITY to "total-quantity"
            )
        )
    }

    @Test
    fun verifyTrackSnapGetTokenRequest() {
        eventAnalytics.trackSnapGetTokenRequest("token")
        verify(mixpanelTracker).trackEvent(
            eventName = EVENT_SNAP_GET_TOKEN_REQUEST,
            properties = mapOf(PROPERTY_SNAP_TOKEN to "token")
        )
    }

    @Test
    fun verifyTrackSnapGetTokenResult() {
        eventAnalytics.trackSnapGetTokenResult("token", "1000")
        verify(mixpanelTracker).trackEvent(
            eventName = EVENT_SNAP_GET_TOKEN_RESULT,
            properties = mapOf(
                PROPERTY_SNAP_TOKEN to "token",
                PROPERTY_RESPONSE_TIME to "1000"
            )
        )
    }

    @Test
    fun verifyTrackSnapAccountNumberCopied() {
        eventAnalytics.trackSnapAccountNumberCopied(PageName.BRI_VA_PAGE, PaymentType.BRI_VA)
        verify(mixpanelTracker).trackEvent(
            eventName = EVENT_SNAP_ACCOUNT_NUMBER_COPIED,
            properties = mapOf(
                PROPERTY_PAGE_NAME to PageName.BRI_VA_PAGE,
                PROPERTY_PAYMENT_METHOD_NAME to PaymentType.BRI_VA
            )
        )
    }

    @Test
    fun verifyTrackSnapPaymentNumberButtonRetried() {
        eventAnalytics.trackSnapPaymentNumberButtonRetried(PageName.GOPAY_QR_PAGE, PaymentType.GOPAY)
        verify(mixpanelTracker).trackEvent(
            eventName = EVENT_SNAP_PAYMENT_NUMBER_BUTTON_RETRIED,
            properties = mapOf(
                PROPERTY_PAGE_NAME to PageName.GOPAY_QR_PAGE,
                PROPERTY_PAYMENT_METHOD_NAME to PaymentType.GOPAY
            )
        )
    }

    @Test
    fun verifyTrackSnapChargeRequest() {
        eventAnalytics.trackSnapChargeRequest("page-name", "credit-card")
        verify(mixpanelTracker).trackEvent(
            eventName = EVENT_SNAP_CHARGE_REQUEST,
            properties = mapOf(
                PROPERTY_PAGE_NAME to "page-name",
                PROPERTY_PAYMENT_METHOD_NAME to "credit-card"
            )
        )
    }

    @Test
    fun verifyTrackSnapChargeResult() {
        eventAnalytics.trackSnapChargeResult(
            transactionStatus = "transaction-status",
            fraudStatus = "fraud-status",
            currency = "currency",
            statusCode = "status-code",
            transactionId = "transaction-id",
            pageName = PageName.CREDIT_DEBIT_CARD_PAGE,
            paymentMethodName = "payment-type",
            responseTime = "response-time"
        )
        verify(mixpanelTracker).trackEvent(
            eventName = EVENT_SNAP_CHARGE_RESULTS,
            properties = mapOf(
                PROPERTY_TRANSACTION_STATUS to "transaction-status",
                PROPERTY_FRAUD_STATUS to "fraud-status",
                PROPERTY_CURRENCY to "currency",
                PROPERTY_STATUS_CODE to "status-code",
                PROPERTY_TRANSACTION_ID to "transaction-id",
                PROPERTY_PAGE_NAME to PageName.CREDIT_DEBIT_CARD_PAGE,
                PROPERTY_PAYMENT_METHOD_NAME to "payment-type",
                PROPERTY_RESPONSE_TIME to "response-time"
            )
        )
    }

    @Test
    fun verifyTrackCtaClicked() {
        eventAnalytics.trackSnapCtaClicked(
            ctaName = "cta-name",
            pageName = "page-name",
            paymentMethodName = "payment-type"
        )
        verify(mixpanelTracker).trackEvent(
            eventName = EVENT_SNAP_CTA_CLICKED,
            properties = mapOf(
                PROPERTY_CTA_NAME to "cta-name",
                PROPERTY_PAGE_NAME to "page-name",
                PROPERTY_PAYMENT_METHOD_NAME to "payment-type"
            )
        )
    }

    @Test
    fun verifyTrackHowToPayViewed() {
        eventAnalytics.trackSnapHowToPayViewed(
            pageName = "page-name",
            paymentMethodName = "payment-type"
        )
        verify(mixpanelTracker).trackEvent(
            eventName = EVENT_SNAP_HOW_TO_PAY_VIEWED,
            properties = mapOf(
                PROPERTY_PAGE_NAME to "page-name",
                PROPERTY_PAYMENT_METHOD_NAME to "payment-type"
            )
        )
    }

    @Test
    fun verifyTrackSnapExbinResponse() {
        eventAnalytics.trackSnapExbinResponse(
            pageName = "page-name",
            paymentMethodName = "payment-type",
            registrationRequired = "false",
            countryCode = "country-code",
            channel = "channel",
            brand = "brand",
            binType = "bin-type",
            binClass = "bin-class",
            bin = "bin",
            bankCode = "bank-code"
        )
        verify(mixpanelTracker).trackEvent(
            eventName = EVENT_SNAP_EXBIN_RESPONSE,
            properties = mapOf(
                PROPERTY_PAGE_NAME to "page-name",
                PROPERTY_PAYMENT_METHOD_NAME to "payment-type",
                PROPERTY_REGISTRATION_REQUIRED to "false",
                PROPERTY_CARD_COUNTRY_CODE to "country-code",
                PROPERTY_CARD_CHANNEL to "channel",
                PROPERTY_CARD_BRAND to "brand",
                PROPERTY_CARD_BIN_TYPE to "bin-type",
                PROPERTY_CARD_BIN_CLASSS to "bin-class",
                PROPERTY_CARD_BIN to "bin",
                PROPERTY_CARD_BANK_CODE to "bank-code"
            )
        )
    }

    @Test
    fun verifyTrackSnapPageClosed() {
        eventAnalytics.trackSnapPageClosed(
            pageName = "page-name"
        )
        verify(mixpanelTracker).trackEvent(
            eventName = EVENT_SNAP_PAGE_CLOSED,
            properties = mapOf(PROPERTY_PAGE_NAME to "page-name")
        )
    }

    @Test
    fun verifyTrackSnapOpenDeeplink() {
        eventAnalytics.trackSnapOpenDeeplink(
            pageName = "page-name",
            paymentMethodName = "payment-type"
        )
        verify(mixpanelTracker).trackEvent(
            eventName = EVENT_SNAP_OPEN_DEEPLINK,
            properties = mapOf(
                PROPERTY_PAGE_NAME to "page-name",
                PROPERTY_PAYMENT_METHOD_NAME to "payment-type"
            )
        )
    }

    @Test
    fun verifyTrack3DsResult() {
        eventAnalytics.trackSnap3DsResult(
            transactionStatus = "transaction-status",
            cardType = "card-type",
            bank = "bank",
            threeDsVersion = "3ds-version",
            channelResponseCode = "response-code",
            eci = "eci",
            paymentMethodName = "payment-type"
        )
        verify(mixpanelTracker).trackEvent(
            eventName = EVENT_SNAP_3DS_RESULT,
            properties = mapOf(
                PROPERTY_TRANSACTION_STATUS to "transaction-status",
                PROPERTY_CARD_TYPE to "card-type",
                PROPERTY_CARD_BANK to "bank",
                PROPERTY_3DS_VERSION to "3ds-version",
                PROPERTY_CHANNEL_RESPONSE_CODE to "response-code",
                PROPERTY_ECI to "eci",
                PROPERTY_PAYMENT_METHOD_NAME to "payment-type"
            )
        )
    }

    @Test
    fun verifyTrackSnapOrderDetailsViewed() {
        eventAnalytics.trackSnapOrderDetailsViewed(
            pageName = "page-name",
            paymentMethodName = "payment-type",
            transactionId = "transaction-id",
            netAmount = "net-amount"
        )
        verify(mixpanelTracker).trackEvent(
            eventName = EVENT_SNAP_ORDER_DETAILS_VIEWED,
            properties = mapOf(
                PROPERTY_PAGE_NAME to "page-name",
                PROPERTY_PAYMENT_METHOD_NAME to "payment-type",
                PROPERTY_TRANSACTION_ID to "transaction-id",
                PROPERTY_NET_AMOUNT to "net-amount"
            )
        )
    }
}