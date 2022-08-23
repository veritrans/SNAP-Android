package com.midtrans.sdk.corekit.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransactionResult( //TODO temporary for direct debit only
    val status: String,
    val transactionId: String,
    val paymentType: String
) : Parcelable
