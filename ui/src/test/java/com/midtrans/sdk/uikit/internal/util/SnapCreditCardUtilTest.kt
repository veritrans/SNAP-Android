package com.midtrans.sdk.uikit.internal.util

import androidx.compose.ui.text.input.TextFieldValue
import com.midtrans.sdk.corekit.api.model.CreditCard
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.model.Promo
import com.midtrans.sdk.uikit.R
import org.junit.Assert
import org.junit.Test

class SnapCreditCardUtilTest {

    @Test
    fun isValidCardNumberShouldPerformLuhnCheck() {
        val validCardNumber = "4123456789989884"
        val invalidCardNumber = "4123456789989880"
        Assert.assertTrue(SnapCreditCardUtil.isValidCardNumber(validCardNumber))
        Assert.assertFalse(SnapCreditCardUtil.isValidCardNumber(invalidCardNumber))
    }

    @Test
    fun getCardTypeShouldDecodeBasedOnNumberPrefix() { //TODO: actual unit test by Fauzy.

    }

    @Test
    fun getPrincipalIconShouldReturnCorrespondingResId() { //TODO: actual unit test by Fauzy.

    }

    @Test
    fun getCardNumberFromTextFieldShouldRemoveSpaces() {
        val validCardNumber = "4123 4567 8998 9884"
        val trimmedCardNumber = "4123456789989884"
        Assert.assertEquals(
            trimmedCardNumber, SnapCreditCardUtil.getCardNumberFromTextField(
                TextFieldValue(text = validCardNumber)
            )
        )
    }

    @Test
    fun getExpMonthFromTextFieldShouldReturnMonthPortion() {
        val expiryDate = "22/23"
        Assert.assertEquals(
            "22",
            SnapCreditCardUtil.getExpMonthFromTextField(TextFieldValue(text = expiryDate))
        )
    }

    @Test
    fun getExpYearFromTextFieldShouldReturnYearPortion() {
        val expiryDate = "22/23"
        Assert.assertEquals(
            "23",
            SnapCreditCardUtil.getExpYearFromTextField(TextFieldValue(text = expiryDate))
        )
    }

    @Test
    fun isBinBlockedShouldCalculateAccordingBlacklistAndWhiteList() {
        val blackList = listOf("123456", "234567")
        val whiteList = listOf("111111", "123456")

        //whitelist null blacklist null
        val creditCard0 = CreditCard()
        Assert.assertFalse(
            SnapCreditCardUtil.isBinBlocked(
                "4123456789989884",
                creditCard0
            )
        )

        //whitelist empty
        val creditCard1 = CreditCard(whitelistBins = listOf())
        Assert.assertFalse(
            SnapCreditCardUtil.isBinBlocked(
                "4123456789989884",
                creditCard1
            )
        )

        //blacklist empty
        val creditCard2 = CreditCard(blacklistBins = listOf())
        Assert.assertFalse(
            SnapCreditCardUtil.isBinBlocked(
                "4123456789989884",
                creditCard2
            )
        )

        //whitelist
        val creditCard3 = CreditCard(whitelistBins = whiteList)
        Assert.assertTrue(SnapCreditCardUtil.isBinBlocked("4123456789989884", creditCard3))
        Assert.assertFalse(SnapCreditCardUtil.isBinBlocked("11111123456789", creditCard3))

        //blacklist
        val creditCard4 = CreditCard(blacklistBins = blackList)
        Assert.assertFalse(SnapCreditCardUtil.isBinBlocked("4123456789989884", creditCard4))
        Assert.assertTrue(SnapCreditCardUtil.isBinBlocked("12345623456789", creditCard4))

        //blacklist + whitelist
        val creditCard5 =
            CreditCard(blacklistBins = blackList, whitelistBins = whiteList)
        Assert.assertTrue(SnapCreditCardUtil.isBinBlocked("4123456789989884", creditCard5))
        Assert.assertTrue(SnapCreditCardUtil.isBinBlocked("12345623456789", creditCard5))

        //whitelisted + blacklisted
        Assert.assertTrue(
            SnapCreditCardUtil.isBinBlocked(
                "123456789989884",
                creditCard5
            )
        )

        val whiteListBank = listOf("bca", "debit")
        val creditCard6 =
            CreditCard( whitelistBins = whiteListBank)
        Assert.assertFalse(SnapCreditCardUtil.isBinBlocked("4123456789989884", creditCard6, "bca", "debit"))
        Assert.assertTrue(SnapCreditCardUtil.isBinBlocked("4123456789989884", creditCard6, "bca", "credit"))
        Assert.assertTrue(SnapCreditCardUtil.isBinBlocked("4123456789989884", creditCard6, "mandiri", "debit"))

        val blacklistListBank = listOf("bca", "debit")
        val creditCard7 =
            CreditCard( blacklistBins = blacklistListBank)

        Assert.assertTrue(SnapCreditCardUtil.isBinBlocked("4123456789989884", creditCard7, "bca", "debit"))
        Assert.assertTrue(SnapCreditCardUtil.isBinBlocked("4123456789989884", creditCard7, "bca", "credit"))
        Assert.assertTrue(SnapCreditCardUtil.isBinBlocked("4123456789989884", creditCard7, "mandiri", "debit"))
    }

    @Test
    fun getBankIcon() {
        Assert.assertEquals(R.drawable.ic_outline_bri_24, SnapCreditCardUtil.getBankIcon("BRI"))
        Assert.assertEquals(R.drawable.ic_bank_bni_24, SnapCreditCardUtil.getBankIcon("BNI"))
        Assert.assertEquals(
            R.drawable.ic_bank_mandiri_24,
            SnapCreditCardUtil.getBankIcon("MANDIRI")
        )
        Assert.assertEquals(R.drawable.ic_bank_bca_24, SnapCreditCardUtil.getBankIcon("BCA"))
        Assert.assertEquals(R.drawable.ic_bank_cimb_24, SnapCreditCardUtil.getBankIcon("CIMB"))
        Assert.assertEquals(R.drawable.ic_bank_mega_24, SnapCreditCardUtil.getBankIcon("MEGA"))
        Assert.assertEquals(null, SnapCreditCardUtil.getBankIcon("Bank X"))
    }

    @Test
    fun getCreditCardApplicablePromosDataShouldQualifyBasedOnBinNumber(){
        val supportedInstallment = listOf("0", "3", "6", "12")
        val selectedTerm = "bni_6"
        val promo1 = Promo(
            id = 1L,
            name = "promo",
            bins = listOf("481111"),
            installmentTerms = supportedInstallment,
            calculatedDiscountAmount = 1000.0,
            paymentTypes = listOf(PaymentType.CREDIT_CARD)
        )

       //Matched promo
        val result1 = SnapCreditCardUtil.getCreditCardApplicablePromosData("4811111111111", listOf(promo1), selectedTerm)
        Assert.assertEquals("promo", result1?.get(0)?.promoName )
        Assert.assertTrue(result1?.get(0)?.enabled?.value!!)

        //promo not enabled
        val result12 = SnapCreditCardUtil.getCreditCardApplicablePromosData("44111111111111", listOf(promo1), selectedTerm)
        Assert.assertFalse(result12?.get(0)?.enabled?.value!!)

        //promo with empty bins
        val promo2 = Promo(
            id = 1L,
            name = "promo",
            bins = listOf(),
            installmentTerms = supportedInstallment,
            calculatedDiscountAmount = 1000.0,
            paymentTypes = listOf(PaymentType.CREDIT_CARD)
        )

        //match whatever bins
        val result2 = SnapCreditCardUtil.getCreditCardApplicablePromosData("44111111111111", listOf(promo2), selectedTerm)
        Assert.assertEquals("promo", result2?.get(0)?.promoName )
        Assert.assertTrue(result2?.get(0)?.enabled?.value!!)

        val result22 = SnapCreditCardUtil.getCreditCardApplicablePromosData("48111111111111", listOf(promo2), selectedTerm)
        Assert.assertEquals("promo", result22?.get(0)?.promoName )
        Assert.assertTrue(result22?.get(0)?.enabled?.value!!)

        //promo with no credit card payment type
        val promo3 = Promo(
            id = 1L,
            name = "promo",
            bins = listOf(),
            installmentTerms = supportedInstallment,
            calculatedDiscountAmount = 1000.0,
            paymentTypes = listOf(PaymentType.ALFAMART)
        )
        val result3 = SnapCreditCardUtil.getCreditCardApplicablePromosData("44111111111111", listOf(promo3), selectedTerm)
        Assert.assertNull(result3)
    }

    @Test
    fun getCreditCardApplicablePromosDataShouldQualifyBasedOnInstallmentTermCondition() {
        val supportedInstallment = listOf("3", "12")
        val promo = Promo(
            id = 1L,
            name = "promo",
            bins = listOf("481111"),
            installmentTerms = supportedInstallment,
            calculatedDiscountAmount = 1000.0,
            paymentTypes = listOf(PaymentType.CREDIT_CARD)
        )

        //Supported Condition
        val result1 = SnapCreditCardUtil.getCreditCardApplicablePromosData("4811111111111", listOf(promo), "bni_3")
        Assert.assertEquals("promo", result1?.get(0)?.promoName )
        Assert.assertTrue(result1?.get(0)?.enabled?.value!!)

        //Not Selected Installment
        val result2 = SnapCreditCardUtil.getCreditCardApplicablePromosData("4811111111111", listOf(promo), "")
        Assert.assertEquals("promo", result2?.get(0)?.promoName )
        Assert.assertFalse(result2?.get(0)?.enabled?.value!!)

        //Unsupported Condition
        val result3 = SnapCreditCardUtil.getCreditCardApplicablePromosData("4811111111111", listOf(promo), "bni_6")
        Assert.assertEquals("promo", result3?.get(0)?.promoName )
        Assert.assertFalse(result3?.get(0)?.enabled?.value!!)
    }

    @Test
    fun formatCreditCardShouldReturnNumberGroupedBy4Digits() {
        val expected = "4811 1111 1111 1114"
        val result1 = SnapCreditCardUtil.formatCreditCardNumber(TextFieldValue("4811111111111114"))
        Assert.assertEquals(expected, result1.text)

        val result2 = SnapCreditCardUtil.formatCreditCardNumber(TextFieldValue("4811-1111-1111-1114"))
        Assert.assertEquals(expected, result2.text)
    }

    @Test
    fun formatCvvShouldReturnCvvWithMaxLength3() {
        val expected = "123456"
        val result1 = SnapCreditCardUtil.formatCvv(TextFieldValue("123456789"))
        Assert.assertEquals(expected, result1.text)

        val result2 = SnapCreditCardUtil.formatCvv(TextFieldValue("-123456-"))
        Assert.assertEquals(expected, result2.text)
    }
}