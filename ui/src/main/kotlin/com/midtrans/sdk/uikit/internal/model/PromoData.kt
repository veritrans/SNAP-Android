package com.midtrans.sdk.uikit.internal.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class PromoData(
    val identifier: String? = null,
    val leftText: String,
    val rightText: String,
    val subLeftText: Int? = null,
    val installmentTerm: List<String>? = null,
    var enabled: MutableState<Boolean> = mutableStateOf(true)
)
