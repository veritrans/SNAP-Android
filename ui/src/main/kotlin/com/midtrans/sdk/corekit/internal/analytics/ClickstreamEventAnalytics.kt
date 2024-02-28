package com.midtrans.sdk.corekit.internal.analytics

import android.os.Build
import android.util.Log
import com.midtrans.clickstream.meta.*
import com.midtrans.clickstream.products.common.*
import com.midtrans.clickstream.products.events.ui.Page
import com.midtrans.sdk.uikit.BuildConfig

class ClickstreamEventAnalytics(
    private val clickstreamTracker: ClickstreamTracker
) {

    var platform : String? = null
    var merchantUrl: String? = null
    var snaptoken: String? = null
    var orderId = ""
    var grossAmount = ""
    var merchantId = ""
    var merchantName = ""
    var colourSchema = ""
    var enabledPayments = ""
    var enabledPaymentsLength = ""
    var snapRedirectUrl : String? = null
    var allowRetry : String? = null
    var otherVaProcessor : String? = null

    //credit card
//    var cardOneClickTokenAvailable: String? = null
//    var cardTwoClickTokenAvailable: String? = null
//    var priorityCardFeature: String? = null
//    var savedTokens: String? = null
//    var promoEnabled: String? = null
//    var secure: String? = null
//    var saveCard: String? = null
//    var blacklistedBins: String? = null
//    var allowlistedBins: String? = null
//    var installmentTerms: String? = null
//    var installmentBank: String? = null
//    var installmentRequired: String? = null
    var creditCardCommonProperties: CreditCard? = null


    fun setUserIdentity(
        userId: String,
        userName: String,
        extras: Map<String, String> = mapOf()
    ) {
//        mixpanelTracker.setUserIdentity(
//            id = userId,
//            name = userName,
//            extras = extras
//        )
    }

    fun registerCommonProperties(
        platform: String,
        merchantUrl: String?
    ) {
        val optionalProperties = mutableMapOf<String, String>()
        merchantUrl?.also { optionalProperties[EventName.PROPERTY_MERCHANT_URL] = it }

        Log.d("hasilnya", "hasilnya common properties")

//        mixpanelTracker.registerCommonProperties(
//            mapOf(
//                EventName.PROPERTY_SDK_VERSION to BuildConfig.SDK_VERSION,
//                EventName.PROPERTY_SDK_TYPE to "UI",
//                EventName.PROPERTY_SOURCE_TYPE to "mobile-android",
//                EventName.PROPERTY_SERVICE_TYPE to "snap",
//                EventName.PROPERTY_SNAP_TYPE to "Sdk",
//                EventName.PROPERTY_PLATFORM to platform
//            ) + optionalProperties
//        )
        this.platform = platform
        this.merchantUrl = merchantUrl
    }

    fun registerCommonTransactionProperties(
        snapToken: String,
        orderId: String,
        grossAmount: String,
        merchantId: String,
        merchantName: String,
        colourSchema: String,
        enabledPayments: String,
        enabledPaymentsLength: String,
        snapRedirectUrl: String?,
        allowRetry: String?,
        otherVaProcessor: String?,
    ) {
        val optionalProperties = mutableMapOf<String, String>()
        snapRedirectUrl?.also { optionalProperties[EventName.PROPERTY_SNAP_REDIRECT_URL] = it }
        allowRetry?.also { optionalProperties[EventName.PROPERTY_ALLOW_RETRY] = it }
        otherVaProcessor?.also { optionalProperties[EventName.PROPERTY_OTHER_VA_PROCESSOR] = it }

//        mixpanelTracker.registerCommonProperties(
//            mapOf(
//                EventName.PROPERTY_SNAP_TOKEN to snapToken,
//                EventName.PROPERTY_ORDER_ID to orderId,
//                EventName.PROPERTY_GROSS_AMOUNT to grossAmount,
//                EventName.PROPERTY_MERCHANT_ID to merchantId,
//                EventName.PROPERTY_MERCHANT_NAME to merchantName,
//                EventName.PROPERTY_COLOUR_SCHEME to colourSchema,
//                EventName.PROPERTY_PAYMENTS_ENABLED to enabledPayments,
//                EventName.PROPERTY_PAYMENTS_ENABLED_LENGTH to enabledPaymentsLength
//            ) + optionalProperties
//        )

        this.snaptoken = snapToken
        this.orderId = orderId
        this.grossAmount = grossAmount
        this.merchantId = merchantId
        this.merchantName = merchantName
        this.colourSchema = colourSchema
        this.enabledPayments = enabledPayments
        this.enabledPaymentsLength = enabledPaymentsLength
        this.snapRedirectUrl = snapRedirectUrl
        this.allowRetry = allowRetry
        this.otherVaProcessor = otherVaProcessor
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
        customerName?.also { customerProperties[EventName.PROPERTY_CUSTOMER_NAME] = it }
        customerEmail?.also { customerProperties[EventName.PROPERTY_CUSTOMER_EMAIL] = it }
        customerPhoneNumber?.also { customerProperties[EventName.PROPERTY_CUSTOMER_PHONE_NUMBER] = it }
        customerCity?.also { customerProperties[EventName.PROPERTY_CUSTOMER_CITY] = it }
        customerPostCode?.also { customerProperties[EventName.PROPERTY_CUSTOMER_POST_CODE] = it }
        totalItems?.also { customerProperties[EventName.PROPERTY_TOTAL_ITEMS] = it }
        totalQuantity?.also { customerProperties[EventName.PROPERTY_TOTAL_QUANTITY] = it }

//        if (customerProperties.isNotEmpty()) {
//            mixpanelTracker.registerCommonProperties(customerProperties)
//        }
    }

    fun registerCommonCreditCardProperties(
        cardOneClickTokenAvailable: String?,
        cardTwoClickTokenAvailable: String?,
        priorityCardFeature: String?,
        savedTokens: String?,
        promoEnabled: String?,
        secure: String?,
        saveCard: String?,
        blacklistedBins: String?,
        allowlistedBins: String?,
        installmentTerms: String?,
        installmentBank: String?,
        installmentRequired: String?
    ) {
        val cardProperties = mutableMapOf<String, String>()
        cardOneClickTokenAvailable?.also { cardProperties[EventName.PROPERTY_ONE_CLICK_TOKEN_AVAILABLE] = it }
        cardTwoClickTokenAvailable?.also { cardProperties[EventName.PROPERTY_TWO_CLICK_TOKEN_AVAILABLE] = it }
        priorityCardFeature?.also { cardProperties[EventName.PROPERTY_PRIORITY_CARD_FEATURE] = it }
        savedTokens?.also { cardProperties[EventName.PROPERTY_SAVED_TOKENS] = it }
        promoEnabled?.also { cardProperties[EventName.PROPERTY_PROMO_ENABLED] = it }
        secure?.also { cardProperties[EventName.PROPERTY_SECURE_ENABLED] = it }
        saveCard?.also { cardProperties[EventName.PROPERTY_SAVE_CARD_ENABLED] = it }
        blacklistedBins?.also { cardProperties[EventName.PROPERTY_BLACKLISTED_BINS] = it }
        allowlistedBins?.also { cardProperties[EventName.PROPERTY_ALLOWLISTED_BINS] = it }
        installmentTerms?.also { cardProperties[EventName.PROPERTY_INSTALLMENT_TERMS] = it }
        installmentBank?.also { cardProperties[EventName.PROPERTY_INSTALLMENT_BANK] = it }
        installmentRequired?.also { cardProperties[EventName.PROPERTY_INSTALLMENT_REQUIRED] = it }

//        if (cardProperties.isNotEmpty()) {
//            mixpanelTracker.registerCommonProperties(cardProperties)
//        }
        if (cardProperties.isNotEmpty()) {
            creditCardCommonProperties = CreditCard.newBuilder()
                .setCardOneClickTokenAvailable(cardOneClickTokenAvailable.toBoolean())
                .setCardTwoClickTokenAvailable(cardTwoClickTokenAvailable.toBoolean())
//            .setPriorityCardFeature()
//            .setSavedTokensNumber(savedTokens?.toInt() ?: null)
//            .setPromoEnabled()
                .setSecure(secure.toBoolean())
                .setSaveCard(saveCard.toBoolean())
//            .setBlacklistedBins(blacklistedBins)
//            .setWhitelistedBins(allowlistedBins)
                .setInstallmentTerms(installmentTerms)
//            .setInstallmentBank(installmentBank)
                .setInstallmentRequired(installmentRequired.toBoolean())
                .build()

        }


    }

    //TODO will be implemented separately
    fun trackSnapCtaError() {}

    fun trackSnapNotice(
        pageName: String,
        paymentMethodName: String,
        statusText: String,
        noticeMessage: String?
    ) {
        val optionalProperties = mutableMapOf<String, String>()
        noticeMessage?.let { optionalProperties[EventName.PROPERTY_NOTICE_MESSAGE] = it }

//        mixpanelTracker.trackEvent(
//            eventName = EventName.EVENT_SNAP_NOTICE,
//            properties = mapOf(
//                EventName.PROPERTY_PAGE_NAME to pageName,
//                EventName.PROPERTY_PAYMENT_METHOD_NAME to paymentMethodName,
//                EventName.PROPERTY_STATUS_TEXT to statusText
//            ) + optionalProperties
//        )
    }

    fun trackSnapError(
        pageName: String,
        paymentMethodName: String,
        statusCode: String?,
        errorMessage: String
    ) {
        val optionalProperties = mutableMapOf<String, String>()
        statusCode?.let { optionalProperties[EventName.PROPERTY_STATUS_CODE] = it }

//        mixpanelTracker.trackEvent(
//            eventName = EventName.EVENT_SNAP_ERROR,
//            properties = mapOf(
//                EventName.PROPERTY_PAGE_NAME to pageName,
//                EventName.PROPERTY_PAYMENT_METHOD_NAME to paymentMethodName,
//                EventName.PROPERTY_ERROR_MESSAGE to errorMessage
//            ) + optionalProperties
//        )

        var exchange = Exchange.newBuilder()
            .setStatusCode(statusCode)
            .setErrorMessage(errorMessage)
            .build()
        var pageDetail = PageDetail.newBuilder()
            .setPageName(pageName)
            .setPaymentMethodName(paymentMethodName)
            .build()

        var page = Page.newBuilder()
            .setEventName(EventName.EVENT_SNAP_ACCOUNT_NUMBER_COPIED)
            .setExchange(exchange)
            .setPageDetail(pageDetail)
            .setMeta(getCommonMeta())
            .build()
        clickstreamTracker.trackEvent(page)
    }

    fun trackSnapAccountNumberCopied(
        pageName: String,
        paymentMethodName: String
    ) {
//        mixpanelTracker.trackEvent(
//            eventName = EventName.EVENT_SNAP_ACCOUNT_NUMBER_COPIED,
//            properties = mapOf(
//                EventName.PROPERTY_PAGE_NAME to pageName,
//                EventName.PROPERTY_PAYMENT_METHOD_NAME to paymentMethodName
//            )
//        )
        var pageDetail = PageDetail.newBuilder()
            .setPageName(pageName)
            .setPaymentMethodName(paymentMethodName)
            .build()

        var page = Page.newBuilder()
            .setEventName(EventName.EVENT_SNAP_ACCOUNT_NUMBER_COPIED)
            .setPageDetail(pageDetail)
            .setMeta(getCommonMeta())
            .build()
        clickstreamTracker.trackEvent(page)
    }

    fun trackSnapPaymentNumberButtonRetried(
        pageName: String,
        paymentMethodName: String
    ) {
//        mixpanelTracker.trackEvent(
//            eventName = EventName.EVENT_SNAP_PAYMENT_NUMBER_BUTTON_RETRIED,
//            properties = mapOf(
//                EventName.PROPERTY_PAGE_NAME to pageName,
//                EventName.PROPERTY_PAYMENT_METHOD_NAME to paymentMethodName
//            )
//        )

        var pageDetail = PageDetail.newBuilder()
            .setPageName(pageName)
            .setPaymentMethodName(paymentMethodName)
            .build()

        var page = Page.newBuilder()
            .setEventName(EventName.EVENT_SNAP_PAYMENT_NUMBER_BUTTON_RETRIED)
            .setPageDetail(pageDetail)
            .setMeta(getCommonMeta())
            .build()
        clickstreamTracker.trackEvent(page)

    }

    fun trackSnapCustomerDataInput(
        pageName: String,
        paymentMethodName: String,
        email: String?,
        phoneNumber: String?,
        displayField: String
    ) {
        val optionalProperties = mutableMapOf<String, String>()
        email?.also { optionalProperties[EventName.PROPERTY_CUSTOMER_EMAIL] = it }
        phoneNumber?.also { optionalProperties[EventName.PROPERTY_CUSTOMER_PHONE_NUMBER] = it }

//        mixpanelTracker.trackEvent(
//            eventName = EventName.EVENT_SNAP_CUSTOMER_DATA_INPUT,
//            properties = mapOf(
//                EventName.PROPERTY_PAGE_NAME to pageName,
//                EventName.PROPERTY_PAYMENT_METHOD_NAME to paymentMethodName,
//                EventName.PROPERTY_DISPLAY_FIELD to displayField
//            ) + optionalProperties
//        )
    }

    fun trackSnapTokenizationResult(
        pageName: String,
        paymentMethodName: String,
        statusCode: String,
        tokenId: String
    ) {
//        mixpanelTracker.trackEvent(
//            eventName = EventName.EVENT_SNAP_TOKENIZATION_RESULT,
//            properties = mapOf(
//                EventName.PROPERTY_PAGE_NAME to pageName,
//                EventName.PROPERTY_PAYMENT_METHOD_NAME to paymentMethodName,
//                EventName.PROPERTY_STATUS_CODE to statusCode,
//                EventName.PROPERTY_TOKEN_ID to tokenId
//            )
//        )
    }

    fun trackSnapPageViewed(
        transactionId: String?,
        pageName: String?,
        paymentMethodName: String?,
        stepNumber: String?
    ) {
        val optionalProperties = mutableMapOf<String, String>()
        pageName?.also { optionalProperties[EventName.PROPERTY_PAGE_NAME] = it }
        paymentMethodName?.also { optionalProperties[EventName.PROPERTY_PAYMENT_METHOD_NAME] = it }
        transactionId?.also { optionalProperties[EventName.PROPERTY_TRANSACTION_ID] = it }
        stepNumber?.also { optionalProperties[EventName.PROPERTY_STEP_NUMBER] = it }

//        mixpanelTracker.trackEvent(
//            eventName = EventName.EVENT_SNAP_PAGE_VIEWED,
//            properties = optionalProperties
//        )
    }

    fun trackSnapOrderDetailsViewed(
        pageName: String?,
        paymentMethodName: String?,
        transactionId: String?,
        netAmount: String?
    ) {
        val optionalProperties = mutableMapOf<String, String>()
        pageName?.also { optionalProperties[EventName.PROPERTY_PAGE_NAME] = it }
        paymentMethodName?.also { optionalProperties[EventName.PROPERTY_PAYMENT_METHOD_NAME] = it }
        transactionId?.also { optionalProperties[EventName.PROPERTY_TRANSACTION_ID] = it }
        netAmount?.also { optionalProperties[EventName.PROPERTY_NET_AMOUNT] = it }

//        mixpanelTracker.trackEvent(
//            eventName = EventName.EVENT_SNAP_ORDER_DETAILS_VIEWED,
//            properties = optionalProperties
//        )
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
        val optionalProperties = mutableMapOf<String, String>()
        transactionStatus?.also { optionalProperties[EventName.PROPERTY_TRANSACTION_STATUS] = it }
        cardType?.also { optionalProperties[EventName.PROPERTY_CARD_TYPE] = it }
        bank?.also { optionalProperties[EventName.PROPERTY_CARD_BANK] = it }
        threeDsVersion?.also { optionalProperties[EventName.PROPERTY_3DS_VERSION] = it }
        channelResponseCode?.also { optionalProperties[EventName.PROPERTY_CHANNEL_RESPONSE_CODE] = it }
        eci?.also { optionalProperties[EventName.PROPERTY_ECI] = it }

//        mixpanelTracker.trackEvent(
//            eventName = EventName.EVENT_SNAP_3DS_RESULT,
//            properties = mapOf(
//                EventName.PROPERTY_PAYMENT_METHOD_NAME to paymentMethodName
//            ) + optionalProperties
//        )
    }

    fun trackSnapOpenDeeplink(
        pageName: String,
        paymentMethodName: String
    ) {
//        mixpanelTracker.trackEvent(
//            eventName = EventName.EVENT_SNAP_OPEN_DEEPLINK,
//            properties = mapOf(
//                EventName.PROPERTY_PAGE_NAME to pageName,
//                EventName.PROPERTY_PAYMENT_METHOD_NAME to paymentMethodName
//            )
//        )
        var pageDetail = PageDetail.newBuilder()
            .setPageName(pageName)
            .setPaymentMethodName(paymentMethodName)
            .build()

        var page = Page.newBuilder()
            .setEventName(EventName.EVENT_SNAP_OPEN_DEEPLINK)
            .setPageDetail(pageDetail)
            .setMeta(getCommonMeta())
            .build()
        clickstreamTracker.trackEvent(page)

    }

    fun trackSnapPageClosed(pageName: String) {
//        mixpanelTracker.trackEvent(
//            eventName = EventName.EVENT_SNAP_PAGE_CLOSED,
//            properties = mapOf(EventName.PROPERTY_PAGE_NAME to pageName)
//        )

        var pageDetail = PageDetail.newBuilder()
            .setPageName(pageName)
            .build()

        var page = Page.newBuilder()
            .setEventName(EventName.EVENT_SNAP_PAGE_CLOSED)
            .setPageDetail(pageDetail)
            .setMeta(getCommonMeta())
            .build()
        clickstreamTracker.trackEvent(page)
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
        registrationRequired?.also { optionalProperties[EventName.PROPERTY_REGISTRATION_REQUIRED] = it }
        countryCode?.also { optionalProperties[EventName.PROPERTY_CARD_COUNTRY_CODE] = it }
        channel?.also { optionalProperties[EventName.PROPERTY_CARD_CHANNEL] = it }
        brand?.also { optionalProperties[EventName.PROPERTY_CARD_BRAND] = it }
        binType?.also { optionalProperties[EventName.PROPERTY_CARD_BIN_TYPE] = it }
        binClass?.also { optionalProperties[EventName.PROPERTY_CARD_BIN_CLASSS] = it }
        bin?.also { optionalProperties[EventName.PROPERTY_CARD_BIN] = it }
        bankCode?.also { optionalProperties[EventName.PROPERTY_CARD_BANK_CODE] = it }

//        mixpanelTracker.trackEvent(
//            eventName = EventName.EVENT_SNAP_EXBIN_RESPONSE,
//            properties = mapOf(
//                EventName.PROPERTY_PAGE_NAME to pageName,
//                EventName.PROPERTY_PAYMENT_METHOD_NAME to paymentMethodName
//            ) + optionalProperties
//        )
        var pageDetail = PageDetail.newBuilder()
            .setPageName(pageName)
            .setPaymentMethodName(paymentMethodName)
            .build()

        var exbinResponse = ExbinResponse.newBuilder()
            .setRegistrationRequired(registrationRequired)
            .setCountryCode(countryCode)
            .setChannel(channel)
            .setBrand(brand)
            .setBinType(binType)
            .setBinClass(binClass)
            .setBin(bin)
            .setBankCode(bankCode)
            .build()

        var page = Page.newBuilder()
            .setEventName(EventName.EVENT_SNAP_EXBIN_RESPONSE)
            .setExchange(Exchange.newBuilder().setExbinResponse(exbinResponse))
            .setPageDetail(pageDetail)
            .setMeta(getCommonMeta())
            .build()
        clickstreamTracker.trackEvent(page)

    }

    fun trackSnapHowToPayViewed(
        pageName: String,
        paymentMethodName: String
    ) {
//        mixpanelTracker.trackEvent(
//            eventName = EventName.EVENT_SNAP_HOW_TO_PAY_VIEWED,
//            properties = mapOf(
//                EventName.PROPERTY_PAGE_NAME to pageName,
//                EventName.PROPERTY_PAYMENT_METHOD_NAME to paymentMethodName
//            )
//        )

        var pageDetail = PageDetail.newBuilder()
            .setPageName(pageName)
            .setPaymentMethodName(paymentMethodName)
            .build()

        var page = Page.newBuilder()
            .setEventName(EventName.EVENT_SNAP_HOW_TO_PAY_VIEWED)
            .setPageDetail(pageDetail)
            .setMeta(getCommonMeta())
            .build()
        clickstreamTracker.trackEvent(page)
    }

    fun trackSnapCtaClicked(
        ctaName: String,
        pageName: String,
        paymentMethodName: String
    ) {
//        mixpanelTracker.trackEvent(
//            eventName = EventName.EVENT_SNAP_CTA_CLICKED,
//            properties = mapOf(
//                EventName.PROPERTY_CTA_NAME to ctaName,
//                EventName.PROPERTY_PAGE_NAME to pageName,
//                EventName.PROPERTY_PAYMENT_METHOD_NAME to paymentMethodName
//            )
//        )

        var action = Action.newBuilder()
            .setCtaName(ctaName)
            .build()
        var pageDetail = PageDetail.newBuilder()
            .setPageName(pageName)
            .setPaymentMethodName(paymentMethodName)
            .build()

        var page = Page.newBuilder()
            .setAction(action)
            .setEventName(EventName.EVENT_SNAP_CTA_CLICKED)
            .setPageDetail(pageDetail)
            .setMeta(getCommonMeta())
            .build()
        clickstreamTracker.trackEvent(page)
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
        promoName?.also { optionalProperties[EventName.PROPERTY_PROMO_NAME] = it }
        promoAmount?.also { optionalProperties[EventName.PROPERTY_PROMO_AMOUNT] = it }
        promoId?.also { optionalProperties[EventName.PROPERTY_PROMO_ID] = it }
        creditCardPoint?.also { optionalProperties[EventName.PROPERTY_CREDIT_CARD_POINT] = it }

        val properties = mapOf(
            EventName.PROPERTY_PAGE_NAME to pageName,
            EventName.PROPERTY_PAYMENT_METHOD_NAME to paymentMethodName
        ) + optionalProperties

//        mixpanelTracker.trackEvent(
//            eventName = EventName.EVENT_SNAP_CHARGE_REQUEST,
//            properties = properties
//        )

        var pageDetail = PageDetail.newBuilder()
            .setPageName(pageName)
            .setPaymentMethodName(paymentMethodName)
            .build()

        var chargeParams = ChargeParams.newBuilder()
            .setPromoName(promoName)
            .setPromoAmount(promoAmount)
            .setPromoId(promoId)
            .build()

        var page = Page.newBuilder()
            .setEventName(EventName.EVENT_SNAP_CHARGE_REQUEST)
            .setExchange(Exchange.newBuilder().setChargeParams(chargeParams))
            .setPageDetail(pageDetail)
            .setMeta(getCommonMeta())
            .build()



        clickstreamTracker.trackEvent(page = page)
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
        bank?.also { optionalProperties[EventName.PROPERTY_CHARGE_RESPONSE_BANK] = it }
        channelResponseCode?.also { optionalProperties[EventName.PROPERTY_CHANNEL_RESPONSE_CODE] = it }
        channelResponseMessage?.also { optionalProperties[EventName.PROPERTY_CHANNEL_RESPONSE_MESSAGE] = it }
        cardType?.also { optionalProperties[EventName.PROPERTY_CARD_TYPE] = it }
        threeDsVersion?.also { optionalProperties[EventName.PROPERTY_3DS_VERSION] = it }

        val properties = mapOf(
            EventName.PROPERTY_TRANSACTION_STATUS to transactionStatus,
            EventName.PROPERTY_FRAUD_STATUS to fraudStatus,
            EventName.PROPERTY_CURRENCY to currency,
            EventName.PROPERTY_STATUS_CODE to statusCode,
            EventName.PROPERTY_TRANSACTION_ID to transactionId,
            EventName.PROPERTY_PAGE_NAME to pageName,
            EventName.PROPERTY_PAYMENT_METHOD_NAME to paymentMethodName,
            EventName.PROPERTY_RESPONSE_TIME to responseTime,
        ) + optionalProperties

//        mixpanelTracker.trackEvent(
//            eventName = EventName.EVENT_SNAP_CHARGE_RESULTS,
//            properties = properties
//        )


        Exchange.newBuilder().setErrorHttpStatus("").build()


        var pageDetail = PageDetail.newBuilder()
            .setPageName(pageName)
//            .setPaymentMethodName()
            .build()
        var payment = Payment.newBuilder()
            .setPaymentMethodName(paymentMethodName)
            .build()
        var chargeResponse = ChargeResponse.newBuilder()
            .setTransactionId(transactionId)
            .setOrderId("")
            .setBank(bank)
            .setChannelResponseCode(channelResponseCode)
            .setChannelResponseMessage(channelResponseMessage)
            .setTransactionStatus(transactionStatus)
            .setFraudStatus(fraudStatus)
            .setCurrency(currency)
            .setCardType(cardType)
            .setVersion3Ds(threeDsVersion)
            .setPaymentType("")
            .setChargerType("")
            .build()




        var page = Page.newBuilder()
            .setPageDetail(pageDetail)
            .setExchange(Exchange.newBuilder().setChargeResponse(chargeResponse))
            .setEventName(EventName.EVENT_SNAP_CHARGE_RESULTS)
            .setPageDetail(pageDetail)
            .setPayment(payment)
            .setMeta(getCommonMeta())
            .build()

        clickstreamTracker.trackEvent(page)

    }

    fun trackSnapGetTokenRequest(snapToken: String) {
//        mixpanelTracker.trackEvent(
//            eventName = EventName.EVENT_SNAP_GET_TOKEN_REQUEST,
//            properties = mapOf(EventName.PROPERTY_SNAP_TOKEN to snapToken)
//        )

        var page = Page.newBuilder()
            .setEventName(EventName.EVENT_SNAP_GET_TOKEN_REQUEST)
            .setMeta(getCommonMeta())
            .build()

        clickstreamTracker.trackEvent(page)
    }

    fun trackSnapGetTokenResult(
        snapToken: String,
        responseTime: String
    ) {
//        mixpanelTracker.trackEvent(
//            eventName = EventName.EVENT_SNAP_GET_TOKEN_RESULT,
//            properties = mapOf(
//                EventName.PROPERTY_SNAP_TOKEN to snapToken,
//                EventName.PROPERTY_RESPONSE_TIME to responseTime
//            )
//        )
//        clickstreamTracker.trackEvent(EventName.EVENT_SNAP_GET_TOKEN_RESULT)
        this.snaptoken = snapToken
        var exchange = Exchange.newBuilder().setResponseTime(responseTime.toLong()).build()

        var page = Page.newBuilder()
            .setEventName(EventName.EVENT_SNAP_GET_TOKEN_RESULT)
            .setExchange(exchange)
            .setMeta(getCommonMeta())
            .build()
        clickstreamTracker.trackEvent(page)
    }
    
   private fun getCommonMeta(): EventMeta {

       //        mixpanelTracker.registerCommonProperties(
//            mapOf(
//                EventName.PROPERTY_SDK_VERSION to BuildConfig.SDK_VERSION,
//                EventName.PROPERTY_SDK_TYPE to "UI",
//                EventName.PROPERTY_SOURCE_TYPE to "mobile-android",
//                EventName.PROPERTY_SERVICE_TYPE to "snap",
//                EventName.PROPERTY_SNAP_TYPE to "Sdk",
//                EventName.PROPERTY_PLATFORM to platform
//            ) + optionalProperties
//        )
        return EventMeta.newBuilder()
            .setSnapToken(snaptoken)
            .setOrderId(orderId)
            .setApp(App.newBuilder()
                .setMerchantUrl(merchantUrl)
                .setAppName("snap sdk")
                .setSourceType("mobile-android")
                .setSdkVersion(BuildConfig.SDK_VERSION)
                .setAllowRetry(allowRetry.toBoolean())
                .setColourScheme(colourSchema)
//                .setServiceType("snap")
//                .setSdkType("UI")
            )
            .setDevice(
                Device.newBuilder()
                .setPlatform(platform)
                .setOsVersion(Build.VERSION.RELEASE)
                .setNetwork("")
                .setModel(Build.MODEL)
                .setManufacturer(Build.MANUFACTURER)
                .setBrand(Build.BRAND)
                .setScreenHeight("")
                .setScreenWidth("")
                .setDeviceId("")
                .setDeviceType(Build.DEVICE)
            )
            .setTransaction(TransactionDetail.newBuilder()
                .setCurrency("")
                .setGrossAmount(grossAmount)
                .setCreditCard(creditCardCommonProperties)
            )
            .build()
    }
}