package com.midtrans.sdk.corekit.api.model

data class BinData(
    val statusCode: String?,
    val statusMessage: String?,
    val bank: String?,
    val tokenId: String?,
    val redirectUrl: String?,
    val hash: String?
)
