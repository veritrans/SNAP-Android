package com.midtrans.sdk.corekit.api.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Installment(
    val isRequired: Boolean = false,
    val terms: Map<String, List<Int>>? = null,
) : Parcelable
