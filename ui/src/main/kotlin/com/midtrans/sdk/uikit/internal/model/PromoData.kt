package com.midtrans.sdk.uikit.internal.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class PromoData(
    val identifier: String? = null,
    val promoName: String,
    val discountAmount: String,
    val errorType: String? = null,
    val installmentTerm: List<String>? = null,
    var enabled: MutableState<Boolean> = mutableStateOf(true)
)
