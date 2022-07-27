package com.midtrans.sdk.uikit.external.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PaymentList(
    val options: List<PaymentMethod>
) : Parcelable

@Parcelize
data class PaymentMethod(
    val type: String? = null,
    val category: String? = null,
    val mode: List<String>? = null,
    val acquirer: String? = null,
    val status: String
) : Parcelable
