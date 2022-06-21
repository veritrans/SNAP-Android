package com.midtrans.sdk.corekit.internal.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*
import kotlin.math.ceil

internal object NumberUtil {
    private const val ZERO_DOUBLE = 0.0
    private const val DEFAULT_ROUND_TO = 100L

    private val currencyFormat: NumberFormat by lazy {
        val numberFormat = NumberFormat.getCurrencyInstance(Locale("in"))
        val decimalFormatSymbols = (numberFormat as DecimalFormat).decimalFormatSymbols
        decimalFormatSymbols.currencySymbol = "Rp"
        decimalFormatSymbols.minusSign = '-'
        numberFormat.decimalFormatSymbols = decimalFormatSymbols
        numberFormat.maximumFractionDigits = 0
        numberFormat
    }

    fun getCurrencyNumber(number: Double): String {
        val formatted = currencyFormat.format(number)
        return formatted.replace(",00", "")
    }
    fun getCurrencyNumber(number: Long): String {
        val formatted = currencyFormat.format(number)
        return formatted.replace(",00", "")
    }

    fun getPaddedAmount(amount: Long): String {
        return String.format(Locale.getDefault(),"%012d",amount * 100)
    }

    fun getFormattedAmount(amount: String, currencySymbol: String): String {
        val formattedAmount = StringBuilder(currencySymbol)
        formattedAmount.append(getThousandSeparatedNumber(amount))

        return formattedAmount.toString()
    }

    fun getThousandSeparatedNumber(amount: String): String {
        return if (amount.isNotEmpty()) {
            val numberFormat = DecimalFormat("###,###")
            numberFormat.decimalFormatSymbols = DecimalFormatSymbols(Locale.ROOT).apply {
                groupingSeparator = ".".toCharArray()[0]
            }
            numberFormat.format(amount.toLong())
        } else "0"
    }

    fun calculateRounding(value: Long): Long {
        return calculateRounding(value, DEFAULT_ROUND_TO)
    }

    private fun calculateRounding(value: Long, roundTo: Long): Long {
        return (ceil(value.toDouble() / roundTo) * roundTo - value).toLong()
    }

    fun getCurrencyNumber(number: String): String {
        if (number == "-") return number

        val formatted = currencyFormat.format(number.toDouble())
        return formatted.replace(",00", "")
    }

    fun getStringFromCurrencyText(currencyText: String): String =
        getNumberFromCurrencyText(currencyText).toString()

    private fun getNumberFromCurrencyText(currencyText: String): Number =
        currencyFormat.parse(currencyText)
}
