package com.midtrans.sdk.corekit.internal.analytics

internal object EventName {
    const val EVENT_SNAP_PAGE_VIEWED = "SNAP Page Viewed"
    const val EVENT_SNAP_ORDER_DETAILS_VIEWED = "SNAP Order Details Viewed"
    const val EVENT_SNAP_CHARGE_REQUEST = "SNAP Charge Request"
    const val EVENT_SNAP_CTA_CLICKED = "SNAP CTA Clicked"
    const val EVENT_SNAP_CHARGE_RESULTS = "SNAP Charge Results"
    const val EVENT_SNAP_HOW_TO_PAY_VIEWED = "SNAP How To Pay Viewed"
    const val EVENT_SNAP_ACCOUNT_NUMBER_COPIED = "SNAP Account Number copied"
    const val EVENT_SNAP_PAYMENT_NUMBER_BUTTON_RETRIED = "SNAP Payment Number Button retried"
    const val EVENT_SNAP_EXBIN_RESPONSE = "SNAP Exbin Response"
    const val EVENT_SNAP_PAGE_CLOSED = "SNAP Page Closed"
    const val EVENT_SNAP_OPEN_DEEPLINK = "SNAP Open Deeplink"
    const val EVENT_SNAP_ERROR = "SNAP Error"
    const val EVENT_SNAP_3DS_RESULT = "SNAP 3DS Result"
    const val EVENT_SNAP_TOKENIZATION_RESULT = "SNAP Tokenization Result"
    const val EVENT_SNAP_CTA_ERROR = "SNAP CTA Error"
    const val EVENT_SNAP_GET_TOKEN_REQUEST = "SNAP Get Token Request"
    const val EVENT_SNAP_GET_TOKEN_RESULT = "SNAP Get Token Result"
    const val EVENT_SNAP_CUSTOMER_DATA_INPUT = "SNAP Customer data input"

    const val PROPERTY_SDK_VERSION = "sdkVersion"
    const val PROPERTY_SDK_TYPE = "sdkType"
    const val PROPERTY_COLOUR_SCHEME = "colourScheme"
    const val PROPERTY_MERCHANT_ID = "merchantId"
    const val PROPERTY_MERCHANT_NAME = "merchantName"
    const val PROPERTY_TRANSACTION_ID = "transactionId"
    const val PROPERTY_ORDER_ID = "orderId"
    const val PROPERTY_GROSS_AMOUNT = "grossAmount"
    const val PROPERTY_NET_AMOUNT = "netAmount"
    const val PROPERTY_PAYMENTS_ENABLED = "paymentsEnabled"
    const val PROPERTY_PAYMENTS_ENABLED_LENGTH = "paymentsEnabledLength"
    const val PROPERTY_CUSTOMER_NAME = "customerName"
    const val PROPERTY_CUSTOMER_EMAIL = "customerEmail"
    const val PROPERTY_CUSTOMER_PHONE_NUMBER = "customerPhoneNumber"
    const val PROPERTY_CUSTOMER_CITY = "customerCity"
    const val PROPERTY_CUSTOMER_POST_CODE = "customerPostCode"
    const val PROPERTY_TOTAL_ITEMS = "totalItems"
    const val PROPERTY_TOTAL_QUANTITY = "totalQuantity"
    const val PROPERTY_SNAP_TYPE = "snapType"
    const val PROPERTY_SNAP_REDIRECT_URL = "snapRedirectUrl"
    const val PROPERTY_MERCHANT_URL = "merchantUrl"
    const val PROPERTY_PAGE_NAME = "pageName"
    const val PROPERTY_ONE_CLICK_TOKEN_AVAILABLE = "cardOneClickTokenAvailable"
    const val PROPERTY_TWO_CLICK_TOKEN_AVAILABLE = "cardTwoClickTokenAvailable"
    const val PROPERTY_PAYMENT_METHOD_NAME = "paymentMethodName"
    const val PROPERTY_OTHER_VA_PROCESSOR = "otherVaProcessor"
    const val PROPERTY_ALLOW_RETRY = "allowRetry"
    const val PROPERTY_PRIORITY_CARD_FEATURE = "priorityCardFeature"
    const val PROPERTY_SAVED_TOKENS = "savedTokens"
    const val PROPERTY_PROMO_ENABLED = "promoEnabled"
    const val PROPERTY_SECURE_ENABLED = "secure"
    const val PROPERTY_SAVE_CARD_ENABLED = "saveCard"
    const val PROPERTY_BLACKLISTED_BINS = "blacklistedBins"
    const val PROPERTY_WHITELISTED_BINS = "allowlistedBins"
    const val PROPERTY_INSTALLMENT_TERMS = "InstallmentTerms"
    const val PROPERTY_INSTALLMENT_BANK = "InstallmentBank"
    const val PROPERTY_INSTALLMENT_REQUIRED = "installmentRequired"
    const val PROPERTY_SOURCE_TYPE = "sourceType"
    const val PROPERTY_SERVICE_TYPE = "serviceType"
    const val PROPERTY_STEP_NUMBER = "stepNumber"
    const val PROPERTY_SNAP_TOKEN = "snapToken"
    const val PROPERTY_PROMO_NAME = "promoName"
    const val PROPERTY_PROMO_AMOUNT = "promoAmount"
    const val PROPERTY_PROMO_ID = "promoID"
    const val PROPERTY_CREDIT_CARD_POINT = "creditCardPoint"
    const val PROPERTY_CTA_NAME = "ctaName"
    const val PROPERTY_CHARGE_RESPONSE_BANK = "bank"
    const val PROPERTY_CHANNEL_RESPONSE_CODE = "channelResponseCode"
    const val PROPERTY_CHANNEL_RESPONSE_MESSAGE = "channelResponseMessage"
    const val PROPERTY_TRANSACTION_STATUS = "transactionStatus"
    const val PROPERTY_FRAUD_STATUS = "fraudStatus"
    const val PROPERTY_CURRENCY = "currency"
    const val PROPERTY_CARD_TYPE = "cardType"
    const val PROPERTY_3DS_VERSION = "3DSVersion"
    const val PROPERTY_STATUS_CODE = "statusCode"
    const val PROPERTY_STATUS_MESSAGE = "statusMessage"
    const val PROPERTY_REGISTRATION_REQUIRED = "registrationRequired"
    const val PROPERTY_EXBIN_MESSAGE = "message"
    const val PROPERTY_CARD_COUNTRY_CODE = "countryCode"
    const val PROPERTY_CARD_CHANNEL = "channel"
    const val PROPERTY_CARD_BRAND = "brand"
    const val PROPERTY_CARD_BANK_CODE = "bankCode"
    const val PROPERTY_CARD_BIN_CLASSS = "binClass"
    const val PROPERTY_CARD_BIN_TYPE = "binType"
    const val PROPERTY_CARD_BIN = "bin"
    const val PROPERTY_CARD_BANK = "bank"
    const val PROPERTY_ERROR_MESSAGE = "errorMessage"
    const val PROPERTY_ECI = "ECI"
    const val PROPERTY_NOTICE_MESSAGE = "noticeMessage"
    const val PROPERTY_STATUS_TEXT = "statusText"
    const val PROPERTY_TOKEN_ID = "tokenId"
    const val PROPERTY_RESPONSE_TIME = "responseTime"
    const val PROPERTY_PLATFORM = "Platform"
}