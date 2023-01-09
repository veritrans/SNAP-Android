package com.midtrans.sdk.uikit.api.model

import androidx.annotation.StringDef

class Authentication {
    @StringDef(AUTH_3DS, AUTH_NONE, AUTH_RBA)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Def
    companion object {
        const val AUTH_3DS = "3ds"
        const val AUTH_NONE = "none"
        const val AUTH_RBA = "rba"
    }
}
