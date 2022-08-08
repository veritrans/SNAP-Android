package com.midtrans.sdk.corekit.api.model

import java.io.Serializable

data class GopayPaymentCallback(
    val callbackUrl: String
) : Serializable {
    val enableCallback: Boolean = true
}