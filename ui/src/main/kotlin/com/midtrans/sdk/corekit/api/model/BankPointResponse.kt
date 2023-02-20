package com.midtrans.sdk.corekit.api.model

class BankPointResponse(
    val statusCode: String?,
    val statusMessage: String?,
    val validationMessages: List<String>?,
    val pointBalance: Double?,
    val pointBalanceAmount: String?,
    val transactionTime: String?
)