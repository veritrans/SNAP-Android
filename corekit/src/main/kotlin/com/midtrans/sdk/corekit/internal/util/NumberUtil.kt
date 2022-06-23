package com.midtrans.sdk.corekit.internal.util

import java.text.NumberFormat
import java.util.*

internal object NumberUtil {

    //TODO: check rounding/ truncate setting
   fun formatDoubleToString(value: Double): String {
       val numberFormat = NumberFormat.getInstance(Locale.US)
       numberFormat.isGroupingUsed = false
       return numberFormat.format(value)
   }
}
