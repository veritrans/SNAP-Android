package com.midtrans.sdk.uikit.api.model

import com.midtrans.sdk.corekit.api.model.TransactionResult


class TransactionResult internal constructor(transactionResult: TransactionResult) //TODO temporary for direct debit only, revisit when updating callback for host app
{
    val status: String
    val transactionId: String
    val paymentType: String
    init {
        status = transactionResult.status
        transactionId = transactionResult.transactionId
        paymentType = transactionResult.paymentType
    }
}
typealias PublicTransactionResult = com.midtrans.sdk.uikit.api.model.TransactionResult