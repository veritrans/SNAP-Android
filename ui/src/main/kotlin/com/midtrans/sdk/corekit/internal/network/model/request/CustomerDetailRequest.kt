package com.midtrans.sdk.corekit.internal.network.model.request

internal data class CustomerDetailRequest(
    val email: String? = null,
    val phone: String? = null,
    val fullName: String? = null
)

