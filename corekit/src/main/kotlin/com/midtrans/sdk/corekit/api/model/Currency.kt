package com.midtrans.sdk.corekit.api.model

import androidx.annotation.StringDef

class Currency {
    @StringDef(IDR, SGD)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class Def
    companion object {
        const val IDR = "IDR"
        const val SGD = "SGD"
    }
}
