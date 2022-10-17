package com.midtrans.sdk.corekit.api.model

data class BinData(
    val registrationRequired: String?,
    val countryName: String?,
    val countryCode: String?,
    val channel: String?,
    val brand: String?,
    val bin: String?,
    val bankCode: String?,
    val bank: String?,
    val binType: String?
)
