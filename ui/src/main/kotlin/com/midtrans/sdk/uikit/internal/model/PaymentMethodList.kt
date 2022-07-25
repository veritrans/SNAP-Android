package com.midtrans.sdk.uikit.internal.model

data class PaymentMethodList(
    val paymentMethods: List<PaymentMethodItem>
)

data class PaymentMethodItem(
    val type: String,
    val methods: List<String> = listOf(),
    val icons: List<Int>
)


