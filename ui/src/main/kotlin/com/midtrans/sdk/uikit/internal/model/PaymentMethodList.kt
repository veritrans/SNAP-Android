package com.midtrans.sdk.uikit.internal.model

import androidx.annotation.StringRes

data class PaymentMethodList(
    val paymentMethods: List<PaymentMethodItem>
)

data class PaymentMethodItem(
    val type: String,
    @StringRes val titleId: Int,
    val methods: List<String> = listOf(),
    val icons: List<Int>
)


