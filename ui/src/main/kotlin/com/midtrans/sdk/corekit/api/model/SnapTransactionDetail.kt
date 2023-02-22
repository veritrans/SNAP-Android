package com.midtrans.sdk.corekit.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
open class SnapTransactionDetail(
    val orderId: String,
    val grossAmount: Double,
    val currency: String = "IDR"
) : Parcelable
