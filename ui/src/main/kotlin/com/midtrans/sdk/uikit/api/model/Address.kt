package com.midtrans.sdk.uikit.api.model

import com.midtrans.sdk.corekit.api.model.Address

open class Address(
    firstName: String? = null,
    lastName: String? = null,
    address: String? = null,
    city: String? = null,
    postalCode: String? = null,
    phone: String? = null,
    countryCode: String? = null
) : Address(
    firstName = firstName,
    lastName = lastName,
    address = address,
    city = city,
    postalCode = postalCode,
    phone = phone,
    countryCode = countryCode
)

