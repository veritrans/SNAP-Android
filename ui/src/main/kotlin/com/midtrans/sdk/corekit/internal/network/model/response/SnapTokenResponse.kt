package com.midtrans.sdk.corekit.internal.network.model.response

data class SnapTokenResponse(
    val token: String? = null,
    val redirectUrl: String? = null,    //TODO revisit this parameter on how to use it
    val errorMessages: List<String>? = null
)
