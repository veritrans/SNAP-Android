package com.midtrans.sdk.corekit.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
open class TransactionResult(
    val status: String,
    val transactionId: String? = null,
    val paymentType: String,
    val message: String? = null
) : Parcelable
