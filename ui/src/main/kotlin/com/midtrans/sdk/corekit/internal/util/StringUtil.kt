package com.midtrans.sdk.corekit.internal.util

internal object StringUtil {
    fun checkIfContentNotNull(vararg value: String?): Boolean{
        return value.any() {
            !it.isNullOrEmpty()
        }
    }
}
