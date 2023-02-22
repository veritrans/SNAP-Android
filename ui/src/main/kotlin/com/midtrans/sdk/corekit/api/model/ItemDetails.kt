package com.midtrans.sdk.corekit.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
open class ItemDetails(
    val id: String? = null,
    val price: Double? = null,
    val quantity: Int = 0,
    val name: String? = null,
) : Parcelable
 