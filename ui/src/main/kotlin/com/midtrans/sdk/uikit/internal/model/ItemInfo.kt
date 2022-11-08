package com.midtrans.sdk.uikit.internal.model

import android.os.Parcelable
import com.midtrans.sdk.corekit.api.model.ItemDetails
import kotlinx.parcelize.Parcelize

@Parcelize
data class ItemInfo(
    val itemDetails: List<ItemDetails>,
    val totalAmount: Double
): Parcelable
