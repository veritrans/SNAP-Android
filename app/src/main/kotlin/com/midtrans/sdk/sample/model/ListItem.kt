package com.midtrans.sdk.sample.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListItem(
    val title: String,
    val type: String,
    val isSelected: Boolean
) : Parcelable
