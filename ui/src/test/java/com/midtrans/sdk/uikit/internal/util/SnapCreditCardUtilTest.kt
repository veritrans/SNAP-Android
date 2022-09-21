package com.midtrans.sdk.uikit.internal.util

import androidx.compose.ui.text.input.TextFieldValue
import com.midtrans.sdk.corekit.api.model.CreditCard
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
    fun getCardTypeShouldDecodeBasedOnNumberPrefix() { //TODO: actual unit test :(

    }

    @Test
    fun getPrincipalIconShouldReturnCorrespondingResId() { //TODO: actual unit test :(

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
        val creditCard0 = CreditCard()
        Assert.assertFalse(
            SnapCreditCardUtil.isBinBlocked(
                "4123456789989884",
                creditCard0
            )
        ) //whitelist null blacklist null
        val creditCard1 = CreditCard(whitelistBins = listOf())
        Assert.assertFalse(
            SnapCreditCardUtil.isBinBlocked(
                "4123456789989884",
                creditCard1
            )
        ) //whitelist empty
        val creditCard2 = CreditCard(blacklistBins = listOf())
        Assert.assertFalse(
            SnapCreditCardUtil.isBinBlocked(
                "4123456789989884",
                creditCard2
            )
        ) //blacklist empty
        val creditCard3 = CreditCard(whitelistBins = whiteList) //whitelist
        Assert.assertTrue(SnapCreditCardUtil.isBinBlocked("4123456789989884", creditCard3))
        Assert.assertFalse(SnapCreditCardUtil.isBinBlocked("11111123456789", creditCard3))
        val creditCard4 = CreditCard(blacklistBins = blackList) //blacklist
        Assert.assertFalse(SnapCreditCardUtil.isBinBlocked("4123456789989884", creditCard4))
        Assert.assertTrue(SnapCreditCardUtil.isBinBlocked("12345623456789", creditCard4))

        val creditCard5 =
            CreditCard(blacklistBins = blackList, whitelistBins = whiteList) //blacklist + whitelist
        Assert.assertTrue(SnapCreditCardUtil.isBinBlocked("4123456789989884", creditCard5))
        Assert.assertTrue(SnapCreditCardUtil.isBinBlocked("12345623456789", creditCard5))
        Assert.assertTrue(
            SnapCreditCardUtil.isBinBlocked(
                "123456789989884",
                creditCard5
            )
        ) //whitelisted + blacklisted
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
}