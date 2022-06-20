package com.midtrans.sdk.corekit.api.model

data class BillingAddress(
    val firstName: String? = null,
    val lastName: String? = null,
    val address: String? = null,
    val city: String? = null,
    val postalCode: String? = null,
    val phone: String? = null,
    val countryCode: String? = null
)
