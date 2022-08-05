package com.midtrans.sdk.uikit.internal.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CustomerInfo(
    val name: String,
    val phone: String,
    val addressLines: List<String>
) : Parcelable
