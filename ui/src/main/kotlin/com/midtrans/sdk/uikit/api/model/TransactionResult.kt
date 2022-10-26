package com.midtrans.sdk.uikit.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.midtrans.sdk.corekit.api.model.TransactionResult

@Parcelize
class TransactionResult internal constructor(
    val status: String,
    val transactionId: String,
    val paymentType: String
) : Parcelable {
    constructor(transactionResult: TransactionResult) : this(
        transactionId = transactionResult.transactionId,
        status = transactionResult.status,
        paymentType = transactionResult.paymentType
    )
}
//TODO check if alias is still needed
typealias PublicTransactionResult = com.midtrans.sdk.uikit.api.model.TransactionResult