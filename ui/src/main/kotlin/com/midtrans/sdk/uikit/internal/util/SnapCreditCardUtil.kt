package com.midtrans.sdk.uikit.internal.util

import android.util.Patterns
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import com.midtrans.sdk.corekit.api.model.CreditCard
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.model.Promo
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.util.CurrencyFormat.currencyFormatRp
import com.midtrans.sdk.uikit.internal.view.PromoData

internal object SnapCreditCardUtil {

    const val CARD_TYPE_VISA = "VISA"
    const val CARD_TYPE_MASTERCARD = "MASTERCARD"
    const val CARD_TYPE_AMEX = "AMEX"
    const val CARD_TYPE_JCB = "JCB"
    const val DEFAULT_ONE_CLICK_CVV_VALUE = "123"
    const val FORMATTED_MAX_CARD_NUMBER_LENGTH = 19
    const val FORMATTED_MAX_EXPIRY_LENGTH = 5
    const val FORMATTED_MAX_CVV_LENGTH = 6
    const val FORMATTED_MIN_CVV_LENGTH = 3
    const val SUPPORTED_MAX_BIN_NUMBER = 8
    const val NEW_CARD_FORM_IDENTIFIER = "newCardFormIdentifier"
    const val SAVED_CARD_IDENTIFIER = "savedCardIdentifier"

    /**
     * Return validation of a given card number.
     *
     * @param cardNumber credit card number
     * @return true if given card number is valid else returns false.
     */
    fun isValidCardNumber(cardNumber: String): Boolean {
        var sum = 0
        var alternate = false
        for (i in cardNumber.length - 1 downTo 0) {
            var n = cardNumber.substring(i, i + 1).toInt()
            if (alternate) {
                n *= 2
                if (n > 9) {
                    n = n % 10 + 1
                }
            }
            sum += n
            alternate = !alternate
        }
        return sum % 10 == 0
    }

    //TODO: Need to find better solution about principal icon
    fun getCardType(cardNumber: String): String {
        return try {
            if (cardNumber.isEmpty()) {
                ""
            } else {
                if (cardNumber[0] == '4') {
                    CARD_TYPE_VISA
                } else if (cardNumber[0] == '5' && (cardNumber[1] == '1' || cardNumber[1] == '2'
                            || cardNumber[1] == '3' || cardNumber[1] == '4' || cardNumber[1] == '5')
                ) {
                    CARD_TYPE_MASTERCARD
                } else if (cardNumber[0] == '3' && (cardNumber[1] == '4' || cardNumber[1] == '7')) {
                    CARD_TYPE_AMEX
                } else if (cardNumber.startsWith("35") || cardNumber.startsWith("2131") || cardNumber.startsWith(
                        "1800")
                ) {
                    CARD_TYPE_JCB
                } else {
                    ""
                }
            }
        } catch (e: RuntimeException) {
            ""
        }
    }

    fun getPrincipalIcon(cardType: String): Int? {
        return when (cardType) {
            CARD_TYPE_VISA -> R.drawable.ic_outline_visa_24
            CARD_TYPE_MASTERCARD -> R.drawable.ic_outline_mastercard_24
            CARD_TYPE_AMEX -> R.drawable.ic_outline_amex_24
            CARD_TYPE_JCB -> R.drawable.ic_outline_jcb_24
            else -> null
        }
    }

    fun getCardNumberFromTextField(value: TextFieldValue) : String{
        return value.text.replace(" ", "")
    }
    fun getExpMonthFromTextField(value: TextFieldValue) : String{
        return value.text.substring(0, 2)
    }
    fun getExpYearFromTextField(value: TextFieldValue) : String{
        return return value.text.substring(3, 5)
    }

    private fun isWhiteListedByCreditDebit(
        creditCard: CreditCard?,
        binType: String = ""
    ): Boolean {
        return creditCard?.whitelistBins
            ?.map { it.lowercase() }
            ?.filter { whiteList -> whiteList == "debit" || whiteList == "credit" }
            ?.any { whiteListedCreditDebit -> whiteListedCreditDebit == binType.lowercase() } ?:false
    }

    private fun isBWhiteListedByBank(
        creditCard: CreditCard?,
        bank: String = ""
    ): Boolean {
        return creditCard?.whitelistBins
            ?.map { it.lowercase() }
            ?.filter { whiteList -> whiteList.toIntOrNull() == null }
            ?.filter { whiteList -> whiteList != "debit" && whiteList != "credit" }
            ?.any { whiteListedBank -> whiteListedBank == bank.lowercase() } ?:false
    }

    private fun isBlockedByBlackListedByCreditDebit(
        creditCard: CreditCard?,
        binType: String = ""
    ): Boolean {
        var blackListCreditDebitAvailable = false
        return (
                !creditCard?.blacklistBins
                    ?.map { it.lowercase() }
                    ?.filter { blackList -> blackList == "debit" || blackList == "credit" }
                    ?.map { blackListCreditDebitAvailable = true; it }
                    ?.filter { blackListedCreditDebit -> blackListedCreditDebit == binType.lowercase() }
                    .isNullOrEmpty())
            .and(blackListCreditDebitAvailable)
            .and(binType.isNotBlank())
    }

    private fun isBlockedByBlackListedByBank(
        creditCard: CreditCard?,
        bank: String = ""
    ): Boolean {
        var blackListBankAvailable = false
        return (
                !creditCard?.blacklistBins
                    ?.map { it.lowercase() }
                    ?.filter { blackList -> blackList.toIntOrNull() == null }
                    ?.filter { blackList -> blackList != "debit" && blackList != "credit" }
                    ?.map { blackListBankAvailable = true; it }
                    ?.filter { blackListedBank -> blackListedBank == bank.lowercase() }
                    .isNullOrEmpty())
            .and(blackListBankAvailable)
            .and(bank.isNotBlank())
    }

    private fun isWhiteListedByBinNumber(
        cardNumber: String,
        creditCard: CreditCard?
    ): Boolean {
        return creditCard?.whitelistBins
            ?.filter { whiteList -> whiteList.toIntOrNull() != null }
            ?.any { whiteListedBin -> cardNumber.startsWith(whiteListedBin) } ?:false
    }

    private fun isBlackListedByBinNumber(
        cardNumber: String,
        creditCard: CreditCard?,
    ): Boolean {
        var blackListBinAvailable = false
        return (
                !creditCard?.blacklistBins
                    ?.filter { whiteList -> whiteList.toIntOrNull() != null }
                    ?.map { blackListBinAvailable = true; it }
                    ?.filter { blacklistedBin -> cardNumber.startsWith(blacklistedBin) }
                    .isNullOrEmpty())
            .and(blackListBinAvailable)
    }

    fun isBinBlocked(
        cardNumber: String,
        creditCard: CreditCard?,
        bank: String = "",
        binType: String = ""
    ): Boolean {

        val isWhiteListedByCreditDebit =
            isWhiteListedByCreditDebit(creditCard = creditCard, binType = binType)

        val isWhiteListedByBank =
            isBWhiteListedByBank(creditCard = creditCard, bank = bank)

        val isWhiteListedByBinNumber =
            isWhiteListedByBinNumber(cardNumber = cardNumber, creditCard = creditCard)

        val cardIsWhitelisted = isWhiteListedByBank || isWhiteListedByCreditDebit || isWhiteListedByBinNumber
        val whitelistAvailable = creditCard?.whitelistBins?.isNotEmpty()?: false
        val isBlockedByWhitelist = whitelistAvailable && !cardIsWhitelisted
        val blockedByBlackListedByCreditDebit =
            isBlockedByBlackListedByCreditDebit(creditCard = creditCard, binType = binType)

        val blockedByBlackListedByBank =
            isBlockedByBlackListedByBank(creditCard = creditCard, bank = bank)

        val blackListedByBinNumber =
            isBlackListedByBinNumber(cardNumber = cardNumber, creditCard = creditCard)

        return isBlockedByWhitelist
                || blockedByBlackListedByCreditDebit
                || blockedByBlackListedByBank
                || blackListedByBinNumber
    }

    fun getBankIcon(bank: String): Int? {
        return when (bank.lowercase()) {
            "bri" -> R.drawable.ic_outline_bri_24
            "bni" -> R.drawable.ic_bank_bni_24
            "mandiri" -> R.drawable.ic_bank_mandiri_24
            "bca" -> R.drawable.ic_bank_bca_24
            "cimb" -> R.drawable.ic_bank_cimb_24
            "mega" -> R.drawable.ic_bank_mega_24
            else -> null
        }
    }

    fun getCreditCardApplicablePromosData(binNumber: String, promos: List<Promo>?, installmentTerm: String): List<PromoData>?{
        val creditCardPromos = promos?.filter { promo -> promo.paymentTypes?.contains(PaymentType.CREDIT_CARD)?: false }?.ifEmpty{ null }
        val selectedTerm = installmentTerm.substringAfter("_")
        return creditCardPromos?.map { promoResponse ->
            PromoData(
                identifier = promoResponse.id.toString(),
                leftText = promoResponse.name.orEmpty(),
                rightText = "-${promoResponse.calculatedDiscountAmount.currencyFormatRp()}",
                subLeftText = promoResponse.bins?.let { subLeftText(it, binNumber) },
                enabled = mutableStateOf(
                    (binNumber.isNotBlank().and(promoResponse.bins.isNullOrEmpty()))
                        .or(promoResponse.bins?.any { binNumber.startsWith(it)}?: false )
                        .and(promoResponse.installmentTerms?.any { selectedTerm == it }?: false)
                )
            )
        }?.sortedByDescending { it.enabled.value }
    }

    private fun subLeftText(promoBins: List<String>, binNumber: String) : String?{
        var output: String? = null
        val cardNotEligibleMessage = "Can't use this promo with selected card"

        if(promoBins.isNotEmpty() && binNumber.isNotBlank()){
            if (!promoBins.any { binNumber.startsWith(it) }) {
                output = cardNotEligibleMessage
            }
        }
        return output
    }

    fun isValidEmail(target: String): Boolean {
        return target.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    fun formatMaskedCard(maskedCard: String): String {
        val lastFourDigit = maskedCard.substring(startIndex = maskedCard.length - 4, endIndex = maskedCard.length)
        return "**** **** **** $lastFourDigit"
    }
}