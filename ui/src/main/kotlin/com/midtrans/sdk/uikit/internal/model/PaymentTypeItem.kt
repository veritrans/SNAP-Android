package com.midtrans.sdk.uikit.internal.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentTypeItem(
    val type: String,
    val methods: String? = null
): Parcelable