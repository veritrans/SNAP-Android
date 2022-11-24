package com.midtrans.sdk.corekit.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VaNumber(
    val bank: String? = null,
    val vaNumber: String? = null
) : Parcelable

