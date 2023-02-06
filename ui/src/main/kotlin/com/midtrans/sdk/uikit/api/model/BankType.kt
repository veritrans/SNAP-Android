package com.midtrans.sdk.uikit.api.model

import androidx.annotation.StringDef

class BankType {
    @StringDef(
        MANDIRI,
        CIMB,
        BCA,
        BNI,
        MAYBANK,
        BRI,
        DANAMON,
        MANDIRI_DEBIT,
        BNI_DEBIT_ONLINE,
        PERMATA,
        MEGA,
        OFFLINE
    )
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class Def
    companion object {
        const val CIMB = "cimb"
        const val BCA = "bca"
        const val MANDIRI = "mandiri"
        const val BNI = "bni"
        const val MAYBANK = "maybank"
        const val BRI = "bri"
        const val OFFLINE = "offline"
        const val DANAMON = "DANAMON"
        const val MANDIRI_DEBIT = "MANDIRI_DEBIT"
        const val BNI_DEBIT_ONLINE = "BNI_DEBIT_ONLINE"
        const val PERMATA = "PERMATA"
        const val MEGA = "MEGA"
    }
}