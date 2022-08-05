package com.midtrans.sdk.corekit.api.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PaymentOption(
    val token: String,
    val options: List<PaymentMethod>
) : Parcelable
