package com.midtrans.sdk.corekit.internal.network.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransactionDetails(
    var orderId: String? = null,
    var grossAmount: Double = 0.0,
    val currency: String? = null
) : Parcelable