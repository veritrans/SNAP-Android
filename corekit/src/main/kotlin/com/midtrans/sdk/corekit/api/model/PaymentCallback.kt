package com.midtrans.sdk.corekit.api.model

import java.io.Serializable

//TODO revisit this
//TODO check usage for Gopay since gopay should always have enableCallback true while others don't have that field
data class PaymentCallback(
    val callbackUrl: String,
    val enableCallback: Boolean? = null
) : Serializable