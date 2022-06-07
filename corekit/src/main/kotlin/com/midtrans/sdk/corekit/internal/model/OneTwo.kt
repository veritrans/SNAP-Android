package com.midtrans.sdk.corekit.internal.model

import androidx.annotation.IntDef

internal class OneTwo {
    @IntDef(
        ONE,
        TWO
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class Def

    companion object {
        const val ONE = 1
        const val TWO = 2
    }
}
