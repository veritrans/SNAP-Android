package com.midtrans.sdk.uikit.internal.util

import android.util.Patterns
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.midtrans.sdk.corekit.api.model.CreditCard
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.model.Promo
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.model.PromoData
import com.midtrans.sdk.uikit.internal.util.CurrencyFormat.currencyFormatRp
import kotlin.math.min

internal object SnapCreditCardUtil {

    private const val CARD_TYPE_VISA = "VISA"
    private const val CARD_TYPE_MASTERCARD = "MASTERCARD"
    private const val CARD_TYPE_AMEX = "AMEX"
    private const val CARD_TYPE_JCB = "JCB"
    const val DEFAULT_ONE_CLICK_CVV_VALUE = "123"
    private const val FORMATTED_MAX_CARD_NUMBER_LENGTH = 19
    const val FORMATTED_MAX_EXPIRY_LENGTH = 5
    const val FORMATTED_MAX_CVV_LENGTH = 6
    const val FORMATTED_MIN_CVV_LENGTH = 3
    const val SUPPORTED_MAX_BIN_NUMBER = 8
    const val NEW_CARD_FORM_IDENTIFIER = "newCardFormIdentifier"
    const val SAVED_CARD_IDENTIFIER = "savedCardIdentifier"
    const val INSTALLMENT_NOT_SUPPORTED = "installmentNotSupported"
    const val CARD_NOT_ELIGIBLE = "cardNotEligible"
    const val BANK_BNI = "bni"
    private const val NUMBER_GROUPING_4_DIGITS = "(\\d{4})(?=\\d)"

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

    fun isCardNumberInvalid(
        rawCardNumber: TextFieldValue,
        isBinBlocked: Boolean
    ): Boolean {
        val cardNumber = getCardNumberFromTextField(formatCreditCardNumber(rawCardNumber))
        val formattedCardNumberLength = formatCreditCardNumber(rawCardNumber).text.length

        return isBinBlocked
            || !isValidCardNumber(cardNumber)
            || formattedCardNumberLength != FORMATTED_MAX_CARD_NUMBER_LENGTH
    }

    fun isCvvInvalid(rawCvv: TextFieldValue): Boolean {
        val formattedCvvLength = formatCvv(rawCvv).text.length
        return formattedCvvLength < FORMATTED_MIN_CVV_LENGTH
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
        return value.text.substring(3, 5)
    }

    fun formatMaxPointDiscount(input:TextFieldValue, totalAmount: Long, pointBalanceAmount: Double) : Triple<TextFieldValue, String, Boolean> {
        val digit = input.text.filter {
            it.isDigit()
        }
        val length = min(digit.length, totalAmount.toString().length)
        val pointBalanceAvailable = pointBalanceAmount.toLong()

        var pointDiscount = TextFieldValue("")
        var amount : Long = 0
        var displayedAmount = "Rp0"
        var isError = false

        if (digit.isNotEmpty() && !digit.startsWith("0")){
            when {
                pointBalanceAvailable > totalAmount -> {
                    when {
                        digit.toLong() > totalAmount -> {
                            pointDiscount = input.copy(totalAmount.toString(), TextRange(length))
                            isError = false
                        }
                        else -> {
                            pointDiscount = input.copy(digit, TextRange(digit.length))
                            amount = totalAmount - digit.toLong()
                            isError = false

                        }
                    }
                }
                else -> {
                    pointDiscount = input.copy(digit, TextRange(digit.length))
                    amount = totalAmount - digit.toLong()
                    isError = digit.toLong() > pointBalanceAvailable
                }
            }
        } else {
            amount = totalAmount
        }

        if (amount > 0){
            displayedAmount = amount.toDouble().currencyFormatRp()
        }
        return  Triple(pointDiscount, displayedAmount, isError)
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
        var selectedTerm = installmentTerm.substringAfter("_")
        if (selectedTerm.isBlank()) selectedTerm = "0"
        return creditCardPromos?.map { promoResponse ->
            PromoData(
                identifier = promoResponse.id.toString(),
                promoName = promoResponse.name.orEmpty(),
                discountAmount = "-${promoResponse.calculatedDiscountAmount.currencyFormatRp()}",
                errorType = getPromoError(promoResponse, binNumber, selectedTerm),
                installmentTerm = promoResponse.installmentTerms,
                enabled = mutableStateOf(
                    (binNumber.isNotBlank().and(promoResponse.bins.isNullOrEmpty()))
                        .or(promoResponse.bins?.any { binNumber.startsWith(it) } ?: false)
                        .and(promoResponse.installmentTerms?.any { selectedTerm == it } ?: false)
                )
            )
        }?.sortedByDescending { it.enabled.value }
    }

    private fun getPromoError(promoResponse: Promo, binNumber: String, selectedTerm: String): String? {
        var output: String? = null

        promoResponse.bins?.let { promoBins ->
            promoResponse.installmentTerms?.let { installmentTerms ->
                if (promoBins.isNotEmpty() && binNumber.isNotBlank()) {
                    if (!installmentTerms.any { selectedTerm == it }) {
                        output = INSTALLMENT_NOT_SUPPORTED
                    } else if (!promoBins.any { binNumber.startsWith(it) }) {
                        output = CARD_NOT_ELIGIBLE
                    }
                }
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

    fun formatCreditCardNumber(input: TextFieldValue): TextFieldValue {
        val digit = input.text.filter {
            it.isDigit()
        }
        var processed: String = digit.replace("\\D", "").replace(" ", "")
        // insert a space after all groups of 4 digits that are followed by another digit
        processed = processed.replace(NUMBER_GROUPING_4_DIGITS.toRegex(), "$1 ")
        val length = min(processed.length, FORMATTED_MAX_CARD_NUMBER_LENGTH)
        return input.copy(text = processed.substring(0 until length), selection = TextRange(length))
    }

    fun formatCvv(input: TextFieldValue): TextFieldValue {
        val digit = input.text.filter {
            it.isDigit()
        }
        val length = min(digit.length, FORMATTED_MAX_CVV_LENGTH)
        return input.copy(digit.substring(0 until length), TextRange(length))
    }
}