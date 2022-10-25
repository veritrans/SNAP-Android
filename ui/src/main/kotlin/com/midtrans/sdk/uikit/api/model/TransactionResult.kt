package com.midtrans.sdk.uikit.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

//TODO temporary for direct debit only, revisit when updating callback for host app
@Parcelize
class TransactionResult internal constructor(
    val status: String,
    val transactionId: String,
    val paymentType: String
) : Parcelable
typealias PublicTransactionResult = com.midtrans.sdk.uikit.api.model.TransactionResult