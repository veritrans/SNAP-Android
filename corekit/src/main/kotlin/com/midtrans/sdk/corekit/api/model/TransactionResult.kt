package com.midtrans.sdk.corekit.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
open class TransactionResult( //TODO temporary for direct debit only, revisit when updating callback for host app
    val status: String,
    val transactionId: String,
    val paymentType: String
) : Parcelable
