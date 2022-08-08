package com.midtrans.sdk.corekit.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Promo(
    val enabled: Boolean,
    val allowedPromoCodes: List<String>
) : Parcelable
