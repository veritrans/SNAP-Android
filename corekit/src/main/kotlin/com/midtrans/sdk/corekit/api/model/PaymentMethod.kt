package com.midtrans.sdk.corekit.api.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PaymentMethod(
    val type: String,
    val channels: List<String>
) : Parcelable