package com.midtrans.sdk.corekit.api.model

import java.io.Serializable

open class GopayPaymentCallback(
    val callbackUrl: String
) : Serializable {
    val enableCallback: Boolean = true
}