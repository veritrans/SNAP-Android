package com.midtrans.sdk.corekit.internal.network.model.response

data class SnapTokenResponse(
    val tokenId: String,
    val errorMessages: List<String>
)
