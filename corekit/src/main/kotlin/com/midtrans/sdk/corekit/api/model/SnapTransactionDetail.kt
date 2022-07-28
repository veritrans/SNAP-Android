package com.midtrans.sdk.corekit.api.model

data class SnapTransactionDetail(
    val orderId: String,
    val grossAmount: Double,
    val currency: String = "IDR"
)
