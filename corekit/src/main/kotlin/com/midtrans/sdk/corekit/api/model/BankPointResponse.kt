package com.midtrans.sdk.corekit.api.model

class BankPointResponse(
    val statusCode: String?,
    val statusMessage: String?,
    val validationMessages: ArrayList<String>?,
    val pointBalance: Long?,
    val pointBalanceAmount: String?,
    val transactionTime: String?
)