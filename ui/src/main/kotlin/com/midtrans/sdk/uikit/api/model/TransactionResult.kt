package com.midtrans.sdk.uikit.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class TransactionResult internal constructor(
    val status: String,
    val transactionId: String,
    val paymentType: String
) : Parcelable
//TODO check if alias is still needed
typealias PublicTransactionResult = com.midtrans.sdk.uikit.api.model.TransactionResult