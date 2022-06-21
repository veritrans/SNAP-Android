package com.midtrans.sdk.corekit.api.model

data class Installment(
    val isRequired: Boolean = false,
    val terms: Map<String, List<Int>>? = null,
)
