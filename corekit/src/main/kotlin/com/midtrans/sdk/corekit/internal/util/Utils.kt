package com.midtrans.sdk.corekit.internal.util

import com.midtrans.sdk.corekit.internal.constant.Authentication

object Utils {
    fun mappingToCreditCardAuthentication(type: String?, secure: Boolean): String? {
        return if (type.equals(Authentication.AUTH_3DS, ignoreCase = true) && secure) {
            Authentication.AUTH_3DS
        } else if (type.equals(Authentication.AUTH_RBA, ignoreCase = true) && !secure) {
            Authentication.AUTH_RBA
        } else {
            Authentication.AUTH_NONE
        }
    }
}