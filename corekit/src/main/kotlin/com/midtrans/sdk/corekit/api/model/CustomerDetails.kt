package com.midtrans.sdk.corekit.api.model

data class CustomerDetails(
    val customerIdentifier: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val shippingAddress: Address? = null,
    val billingAddress: Address? = null
)
