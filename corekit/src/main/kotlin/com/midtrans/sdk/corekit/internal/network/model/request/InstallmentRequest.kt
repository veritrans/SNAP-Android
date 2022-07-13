package com.midtrans.sdk.corekit.internal.network.model.request

data class InstallmentRequest(
    val installment: Boolean,
    val bank: String,
    val installmentTerm : Int,
)
