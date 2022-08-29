package com.midtrans.sdk.uikit.internal.util

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

object CurrencyFormat {
    private val currencyFormat: NumberFormat by lazy {
        val numberFormat = NumberFormat.getCurrencyInstance(Locale("in"))
        val decimalFormatSymbols = (numberFormat as DecimalFormat).decimalFormatSymbols
        decimalFormatSymbols.currencySymbol = "Rp"
        decimalFormatSymbols.minusSign = '-'
        numberFormat.decimalFormatSymbols = decimalFormatSymbols
        numberFormat.maximumFractionDigits = 0
        numberFormat
    }

    fun Double.currencyFormatRp(): String {
        val formatted = currencyFormat.format(this)
        return formatted.replace(",00", "")
    }
}