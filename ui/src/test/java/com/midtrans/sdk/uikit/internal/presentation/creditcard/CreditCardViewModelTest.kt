package com.midtrans.sdk.uikit.internal.presentation.creditcard

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.text.input.TextFieldValue
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.exception.InvalidPaymentTypeException
import com.midtrans.sdk.corekit.api.model.*
import com.midtrans.sdk.corekit.internal.analytics.EventAnalytics
import com.midtrans.sdk.corekit.internal.analytics.PageName
import com.midtrans.sdk.corekit.internal.network.model.response.TransactionDetails
import com.midtrans.sdk.uikit.internal.getOrAwaitValue
import com.midtrans.sdk.uikit.internal.model.PromoData
import com.midtrans.sdk.uikit.internal.presentation.errorcard.ErrorCard
import com.midtrans.sdk.uikit.internal.util.DateTimeUtil
import com.midtrans.sdk.uikit.internal.util.SnapCreditCardUtil
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.kotlin.*
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.util.*

class CreditCardViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private val time = 1609866000000L //"Wed Jan 6 2021 11:32:50 +0700"// (Asia/Jakarta)

    @Before
    fun setup() {
        val timeZone = TimeZone.getTimeZone("Asia/Jakarta")
        Instant.now(
            Clock.fixed(
                Instant.ofEpochMilli(time),
                timeZone.toZoneId()))
        Locale.setDefault(Locale("en", "US"))
    }

    @Test
    fun chargeCreditCardShouldInvokeSnapCorePay() {
        val snapCore: SnapCore = mock()
        val errorCard: ErrorCard = mock()
        val snapCreditCardUtil: SnapCreditCardUtil = mock()
        val dateTimeUtil: DateTimeUtil = mock()
        val eventAnalytics: EventAnalytics = mock()
        val snapToken = "SnapToken"
        val paymentType = PaymentType.CREDIT_CARD
        val cardNumber = TextFieldValue(text = "1111 1111 1111")
        val expiry = TextFieldValue(text = "22/23")
        val cvv = TextFieldValue(text = "123")
        val customerEmail = "email@email.com"
        val customerPhone = "123456789"
        `when`(snapCreditCardUtil.getCardNumberFromTextField(any())).thenReturn("")
        `when`(snapCreditCardUtil.getExpMonthFromTextField(any())).thenReturn("")
        `when`(snapCreditCardUtil.getExpYearFromTextField(any())).thenReturn("")
        `when`(snapCore.getEventAnalytics()).thenReturn(eventAnalytics)
        `when`(errorCard.getErrorCardType(transactionResponse = any(), allowRetry = any())).thenReturn(null)

        val cardTokenResponse = CardTokenResponse(
            statusCode = "200",
            statusMessage = "Credit card token is created as Token ID.",
            tokenId = "41002312-1236-2b142f51-1093-4c9e-88eb-5ae529fde1b9",
            hash = "41002312-1236-mami",
            bank = "bank bni",
            redirectUrl = null
        )

        val transactionResponse = TransactionResponse(
            statusCode = "200",
            paymentType = paymentType,
            transactionStatus = "transaction-status",
            fraudStatus = "fraud-status",
            currency = "currency",
            transactionId = "transaction-id"
        )
        whenever(errorCard.getErrorCardType(transactionResponse, false)).thenReturn(null)

        val transactionDetail = TransactionDetails(
            orderId = "orderId",
            grossAmount = 5000.0,
            currency = "IDR"
        )

        val creditCardViewModel =
            CreditCardViewModel(snapCore = snapCore, dateTimeUtil, snapCreditCardUtil, errorCard)

        creditCardViewModel.chargeUsingCreditCard(
            transactionDetails = transactionDetail,
            cardNumber = cardNumber,
            cardExpiry = expiry,
            cardCvv = cvv,
            isSavedCard = false,
            customerEmail = customerEmail,
            customerPhone = customerPhone,
            snapToken = snapToken,
            installmentTerm = "",
            promoId = null
        )
        val callbackCaptor: KArgumentCaptor<Callback<TransactionResponse>> = argumentCaptor()
        val cardTokenCallbackCaptor: KArgumentCaptor<Callback<CardTokenResponse>> = argumentCaptor()

        verify(snapCore).getCardToken(
            any(),
            cardTokenCallbackCaptor.capture()
        )
        val cardTokenCallback = cardTokenCallbackCaptor.firstValue
        cardTokenCallback.onSuccess(cardTokenResponse)
        verify(snapCore).pay(
            snapToken = eq(snapToken),
            paymentRequestBuilder = any(),
            callback = callbackCaptor.capture()
        )
        verify(eventAnalytics).trackSnapChargeRequest(
            pageName = PageName.CREDIT_DEBIT_CARD_PAGE,
            paymentMethodName = paymentType,
            promoName = null,
            promoAmount = null,
            promoId = null,
            creditCardPoint = null
        )

        val callback = callbackCaptor.firstValue
        callback.onSuccess(transactionResponse)
        Assert.assertEquals(transactionResponse, creditCardViewModel.transactionResponseLiveData.getOrAwaitValue())
        verify(eventAnalytics).trackSnapChargeResult(
            transactionStatus = eq("transaction-status"),
            fraudStatus = eq("fraud-status"),
            currency = eq("currency"),
            statusCode = eq("200"),
            transactionId = eq("transaction-id"),
            pageName = eq(PageName.CREDIT_DEBIT_CARD_PAGE),
            paymentMethodName = eq(paymentType),
            responseTime = any(),
            bank = eq(null),
            channelResponseCode = eq(null),
            channelResponseMessage = eq(null),
            cardType = eq(null),
            threeDsVersion = eq(null)
        )
    }

    @Test
    fun payCreditCardWhenErrorShouldTrackError() {
        val snapCore: SnapCore = mock()
        val errorCard: ErrorCard = mock()
        val snapCreditCardUtil: SnapCreditCardUtil = mock()
        val dateTimeUtil: DateTimeUtil = mock()
        val eventAnalytics: EventAnalytics = mock()
        val snapToken = "SnapToken"
        val paymentType = PaymentType.CREDIT_CARD
        val cardNumber = TextFieldValue(text = "1111 1111 1111")
        val expiry = TextFieldValue(text = "22/23")
        val cvv = TextFieldValue(text = "123")
        val customerEmail = "email@email.com"
        val customerPhone = "123456789"
        val exception = InvalidPaymentTypeException()

        `when`(snapCreditCardUtil.getCardNumberFromTextField(any())).thenReturn("")
        `when`(snapCreditCardUtil.getExpMonthFromTextField(any())).thenReturn("")
        `when`(snapCreditCardUtil.getExpYearFromTextField(any())).thenReturn("")
        `when`(snapCore.getEventAnalytics()).thenReturn(eventAnalytics)
        `when`(errorCard.getErrorCardType(transactionResponse = any(), allowRetry = any())).thenReturn(null)

        val transactionDetail = TransactionDetails(
            orderId = "orderId",
            grossAmount = 5000.0,
            currency = "IDR"
        )
        val cardTokenResponse = CardTokenResponse(
            statusCode = "200",
            statusMessage = "Credit card token is created as Token ID.",
            tokenId = "41002312-1236-2b142f51-1093-4c9e-88eb-5ae529fde1b9",
            hash = "41002312-1236-mami",
            bank = "bank bni",
            redirectUrl = null
        )

        val creditCardViewModel = CreditCardViewModel(snapCore, dateTimeUtil, snapCreditCardUtil, errorCard)

        creditCardViewModel.chargeUsingCreditCard(
            transactionDetails = transactionDetail,
            cardNumber = cardNumber,
            cardExpiry = expiry,
            cardCvv = cvv,
            isSavedCard = false,
            customerEmail = customerEmail,
            customerPhone = customerPhone,
            snapToken = snapToken,
            installmentTerm = "",
            promoId = null
        )
        val callbackCaptor: KArgumentCaptor<Callback<TransactionResponse>> = argumentCaptor()
        val cardTokenCallbackCaptor: KArgumentCaptor<Callback<CardTokenResponse>> = argumentCaptor()

        verify(snapCore).getCardToken(
            any(),
            cardTokenCallbackCaptor.capture()
        )
        val cardTokenCallback = cardTokenCallbackCaptor.firstValue
        cardTokenCallback.onSuccess(cardTokenResponse)
        verify(snapCore).pay(
            snapToken = eq(snapToken),
            paymentRequestBuilder = any(),
            callback = callbackCaptor.capture()
        )
        val callback = callbackCaptor.firstValue
        callback.onError(exception)
        eventAnalytics.trackSnapError(
            pageName = PageName.CREDIT_DEBIT_CARD_PAGE,
            paymentMethodName = paymentType,
            statusCode = null,
            errorMessage = exception.message ?: exception.javaClass.name
        )
    }


    @Test
    fun getExpiredHourShouldReturnHHMMSS() {
        val snapCore: SnapCore = mock()
        val dateTimeUtil: DateTimeUtil = mock()
        val snapCreditCardUtil: SnapCreditCardUtil = mock()
        val errorCard: ErrorCard = mock()
        `when`(
            dateTimeUtil.getDate(
                date = eq("2022-01-06 11:32:50 +0700"),
                dateFormat = eq("yyyy-MM-dd HH:mm:ss Z"),
                timeZone = any(),
                locale = any()
            )
        ).thenReturn(
            Date(1609907570066L)//"Wed Jan 6 2021 11:32:50 +0700"// (Asia/Jakarta)
        )
        `when`(dateTimeUtil.getCalendar(null)).thenReturn(
            Calendar.getInstance().apply { time = Date(1609907570066L) }
        )
        `when`(dateTimeUtil.getDuration(any())).thenReturn(Duration.ofMillis(1000L)) //only this matter for final result
        `when`(dateTimeUtil.getTimeDiffInMillis(any(), any())).thenReturn(100000L)
        val creditCardViewModel =
            CreditCardViewModel(snapCore = snapCore, dateTimeUtil, snapCreditCardUtil, errorCard)
        creditCardViewModel.setExpiryTime("2022-01-06 11:32:50 +0700")
        Assert.assertEquals("00:00:01", creditCardViewModel.getExpiredHour())
    }

    @Test
    fun getBankIconShouldReturnBankIconResId(){
        val snapCore: SnapCore = mock()
        val dateTimeUtil: DateTimeUtil = mock()
        val eventAnalytics: EventAnalytics = mock()
        val errorCard: ErrorCard = mock()
        val snapCreditCardUtil: SnapCreditCardUtil = mock()
        val binNumber = "12345678"
        val bankIconResId = 1
        val bankCode = "009"
        val binResponse = BinResponse(
            data = BinData(null, null, null, null, null, null, null, bankCode, null, null)
        )
        `when`(snapCreditCardUtil.getBankIcon(bankCode)).thenReturn(bankIconResId)
        `when`(snapCore.getEventAnalytics()).thenReturn(eventAnalytics)
        val creditCardViewModel =
            CreditCardViewModel(snapCore = snapCore, dateTimeUtil, snapCreditCardUtil, errorCard)

        creditCardViewModel.getBinData(binNumber)
        val callbackCaptor: KArgumentCaptor<Callback<BinResponse>> = argumentCaptor()

        verify(snapCore).getBinData(
            eq(binNumber),
            callbackCaptor.capture()
        )
        val callback = callbackCaptor.firstValue
        callback.onSuccess(binResponse)
        Assert.assertEquals(bankIconResId, creditCardViewModel.bankIconId.getOrAwaitValue())
        verify(eventAnalytics).trackSnapExbinResponse(
            pageName = PageName.CREDIT_DEBIT_CARD_PAGE,
            paymentMethodName = PaymentType.CREDIT_CARD,
            registrationRequired = null,
            countryCode = null,
            channel = null,
            brand = null,
            binType = null,
            binClass = null,
            bin = null,
            bankCode = bankCode,
        )
    }

    @Test
    fun setPromoIdShouldUpdateTheNetAmount(){
        val snapCore: SnapCore = mock()
        val dateTimeUtil: DateTimeUtil = mock()
        val errorCard: ErrorCard = mock()
        val snapCreditCardUtil: SnapCreditCardUtil = mock()

        val promo = Promo(
            id = 1L,
            name = "promo",
            bins = listOf("481111"),
            calculatedDiscountAmount = 1000.0,
            paymentTypes = listOf(PaymentType.CREDIT_CARD),
            discountedGrossAmount = 9000.0
        )
        val creditCardViewModel =
            CreditCardViewModel(snapCore = snapCore, dateTimeUtil, snapCreditCardUtil, errorCard)

        creditCardViewModel.setPromos(listOf(promo))
        creditCardViewModel.setPromoId(1L)
        Assert.assertEquals("Rp9.000", creditCardViewModel.netAmountLiveData.getOrAwaitValue())
    }

    @Test
    fun getPromoDataShouldInvokeGetCreditCardApplicablePromosData(){
        val snapCore: SnapCore = mock()
        val dateTimeUtil: DateTimeUtil = mock()
        val errorCard: ErrorCard = mock()
        val snapCreditCardUtil: SnapCreditCardUtil = mock()
        val promoData: List<PromoData> = mock()
        `when`(snapCreditCardUtil.getCreditCardApplicablePromosData(any(), any(), any())).thenReturn(promoData)

        val promo = Promo(
            id = 1L,
            name = "promo",
            bins = listOf("481111"),
            installmentTerms = listOf("0", "3", "6", "12"),
            calculatedDiscountAmount = 1000.0,
            paymentTypes = listOf(PaymentType.CREDIT_CARD),
            discountedGrossAmount = 9000.0
        )
        val creditCardViewModel =
            CreditCardViewModel(snapCore = snapCore, dateTimeUtil, snapCreditCardUtil, errorCard)

        creditCardViewModel.setPromos(listOf(promo))
        creditCardViewModel.getPromosData("12121", "bni_6")
        verify(snapCreditCardUtil, times(2)).getCreditCardApplicablePromosData(any(), any(), any())
        Assert.assertEquals(promoData, creditCardViewModel.promoDataLiveData.getOrAwaitValue())
    }

    @Test
    fun verifySnapButtonClicked() {
        val snapCore: SnapCore = mock()
        val dateTimeUtil: DateTimeUtil = mock()
        val errorCard: ErrorCard = mock()
        val snapCreditCardUtil: SnapCreditCardUtil = mock()
        val eventAnalytics: EventAnalytics = mock()

        whenever(snapCore.getEventAnalytics()) doReturn eventAnalytics
        val creditCardViewModel =
            CreditCardViewModel(snapCore = snapCore, dateTimeUtil, snapCreditCardUtil, errorCard)
        creditCardViewModel.trackSnapButtonClicked("cta-name")
        verify(eventAnalytics).trackSnapCtaClicked(
            ctaName = "cta-name",
            pageName = PageName.CREDIT_DEBIT_CARD_PAGE,
            paymentMethodName = PaymentType.CREDIT_CARD
        )
    }

    @Test
    fun verifyTrackOrderDetailsViewed() {
        val snapCore: SnapCore = mock()
        val dateTimeUtil: DateTimeUtil = mock()
        val errorCard: ErrorCard = mock()
        val snapCreditCardUtil: SnapCreditCardUtil = mock()
        val eventAnalytics: EventAnalytics = mock()

        whenever(snapCore.getEventAnalytics()) doReturn eventAnalytics
        val creditCardViewModel =
            CreditCardViewModel(snapCore = snapCore, dateTimeUtil, snapCreditCardUtil, errorCard)
        creditCardViewModel.trackOrderDetailsViewed("5000")
        verify(eventAnalytics).trackSnapOrderDetailsViewed(
            pageName = PageName.CREDIT_DEBIT_CARD_PAGE,
            paymentMethodName = PaymentType.CREDIT_CARD,
            transactionId = null,
            netAmount = "5000"
        )
    }

    @Test
    fun verifyTrackPageViewed() {
        val snapCore: SnapCore = mock()
        val dateTimeUtil: DateTimeUtil = mock()
        val errorCard: ErrorCard = mock()
        val snapCreditCardUtil: SnapCreditCardUtil = mock()
        val eventAnalytics: EventAnalytics = mock()

        whenever(snapCore.getEventAnalytics()) doReturn eventAnalytics
        val creditCardViewModel =
            CreditCardViewModel(snapCore = snapCore, dateTimeUtil, snapCreditCardUtil, errorCard)
        creditCardViewModel.trackPageViewed(2)
        verify(eventAnalytics).trackSnapPageViewed(
            pageName = PageName.CREDIT_DEBIT_CARD_PAGE,
            paymentMethodName = PaymentType.CREDIT_CARD,
            transactionId = null,
            stepNumber = "2"
        )
    }

    @Test
    fun verifyTrackCustomerDataInput() {
        val snapCore: SnapCore = mock()
        val dateTimeUtil: DateTimeUtil = mock()
        val errorCard: ErrorCard = mock()
        val snapCreditCardUtil: SnapCreditCardUtil = mock()
        val eventAnalytics: EventAnalytics = mock()

        whenever(snapCore.getEventAnalytics()) doReturn eventAnalytics
        val creditCardViewModel =
            CreditCardViewModel(snapCore = snapCore, dateTimeUtil, snapCreditCardUtil, errorCard)
        creditCardViewModel.trackCustomerDataInput(
            email = "email",
            phoneNumber = "phone",
            displayField = true
        )
        verify(eventAnalytics).trackSnapCustomerDataInput(
            pageName = PageName.CREDIT_DEBIT_CARD_PAGE,
            paymentMethodName = PaymentType.CREDIT_CARD,
            email = "email",
            phoneNumber = "phone",
            displayField = "true"
        )
    }

    @Test
    fun verifyTrackSnapNotice() {
        val snapCore: SnapCore = mock()
        val dateTimeUtil: DateTimeUtil = mock()
        val errorCard: ErrorCard = mock()
        val snapCreditCardUtil: SnapCreditCardUtil = mock()
        val eventAnalytics: EventAnalytics = mock()

        whenever(snapCore.getEventAnalytics()) doReturn eventAnalytics
        val creditCardViewModel =
            CreditCardViewModel(snapCore = snapCore, dateTimeUtil, snapCreditCardUtil, errorCard)
        creditCardViewModel.trackSnapNotice(
            statusText = "text",
            noticeMessage = "message"
        )
        verify(eventAnalytics).trackSnapNotice(
            pageName = PageName.CREDIT_DEBIT_CARD_PAGE,
            paymentMethodName = PaymentType.CREDIT_CARD,
            statusText = "text",
            noticeMessage = "message"
        )
    }
}