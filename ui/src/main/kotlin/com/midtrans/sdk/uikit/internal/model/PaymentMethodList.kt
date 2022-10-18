package com.midtrans.sdk.uikit.internal.model

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

data class PaymentMethodList(
    val paymentMethods: List<PaymentMethodItem>
)

@Parcelize
data class PaymentMethodItem(
    val type: String,
    @StringRes val titleId: Int,
    val methods: List<String> = listOf(),
    val icons: List<Int>
): Parcelable


