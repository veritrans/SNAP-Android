package com.midtrans.sdk.corekit.api.model

import androidx.annotation.StringDef

class InstallmentBank {
    @StringDef(
        MANDIRI,
        CIMB,
        BCA,
        BNI,
        MAYBANK,
        BRI,
        OFFLINE
    )
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class Def
    companion object {
        const val MANDIRI = "mandiri"
        const val CIMB = "cimb"
        const val BCA = "bca"
        const val BNI = "bni"
        const val MAYBANK = "maybank"
        const val BRI = "bri"
        const val OFFLINE = "offline"
    }
}