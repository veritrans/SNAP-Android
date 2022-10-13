package com.midtrans.sdk.uikit.internal.presentation.creditcard

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.text.input.TextFieldValue
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.model.*
import com.midtrans.sdk.corekit.internal.network.model.response.TransactionDetails
import com.midtrans.sdk.uikit.internal.getOrAwaitValue
import com.midtrans.sdk.uikit.internal.presentation.errorcard.ErrorCard
import com.midtrans.sdk.uikit.internal.util.DateTimeUtil
import com.midtrans.sdk.uikit.internal.util.SnapCreditCardUtil
import com.midtrans.sdk.uikit.internal.view.PromoData
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
    val time = 1609866000000L //"Wed Jan 6 2021 11:32:50 +0700"// (Asia/Jakarta)

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

        val cardTokenResponse = CardTokenResponse(
            statusCode = "200",
            statusMessage = "Credit card token is created as Token ID.",
            tokenId = "41002312-1236-2b142f51-1093-4c9e-88eb-5ae529fde1b9",
            hash = "41002312-1236-mami",
            bank = "bank amat",
            redirectUrl = null
        )

        val transactionResponse = TransactionResponse(
            statusCode = "200"
        )

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
        callback.onSuccess(transactionResponse)
        Assert.assertEquals(transactionResponse, creditCardViewModel.transactionResponseLiveData.getOrAwaitValue())
    }


    @Test
    fun getExpiredHourShouldReturnhhmmss() {
        val snapCore: SnapCore = mock()
        val dateTimeUtil: DateTimeUtil = mock()
        val snapCreditCardUtil: SnapCreditCardUtil = mock()
        val errorCard: ErrorCard = mock()
        `when`(
            dateTimeUtil.getDate(
                date = eq("2022-01-06 11:32:50 +0700"),
                dateFormat = eq("yyyy-MM-dd hh:mm:ss Z"),
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
        val errorCard: ErrorCard = mock()
        val snapCreditCardUtil: SnapCreditCardUtil = mock()
        val binNumber = "12345678"
        val bankIconResId = 1
        val bankCode = "009"
        val binResponse = BinResponse(
            data = BinData(null, null, null, null, null, null, bankCode, null)
        )
        `when`(snapCreditCardUtil.getBankIcon(bankCode)).thenReturn(bankIconResId)
        val creditCardViewModel =
            CreditCardViewModel(snapCore = snapCore, dateTimeUtil, snapCreditCardUtil, errorCard)

        creditCardViewModel.getBankIconImage(binNumber)
        val callbackCaptor: KArgumentCaptor<Callback<BinResponse>> = argumentCaptor()

        verify(snapCore).getBinData(
            eq(binNumber),
            callbackCaptor.capture()
        )

        val callback = callbackCaptor.firstValue
        callback.onSuccess(binResponse)
        Assert.assertEquals(bankIconResId, creditCardViewModel.bankIconId.getOrAwaitValue())
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
        `when`(snapCreditCardUtil.getCreditCardApplicablePromosData(any(), any())).thenReturn(promoData)

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
        creditCardViewModel.getPromosData("12121")
        verify(snapCreditCardUtil, times(2)).getCreditCardApplicablePromosData(any(), any())
        Assert.assertEquals(promoData, creditCardViewModel.promoDataLiveData.getOrAwaitValue())
    }
}