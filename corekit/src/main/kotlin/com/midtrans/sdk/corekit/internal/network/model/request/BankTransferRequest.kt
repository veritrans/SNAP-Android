package com.midtrans.sdk.corekit.internal.network.model.request

data class BankTransferRequest(
    val vaNumber: String,
    val freeText: String? = null,
    val subCompanyCode: String? = null
)
