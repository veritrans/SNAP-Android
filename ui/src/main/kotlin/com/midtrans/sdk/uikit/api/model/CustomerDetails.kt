package com.midtrans.sdk.uikit.api.model

import com.midtrans.sdk.corekit.api.model.CustomerDetails


class CustomerDetails(
    customerIdentifier: String? = null,
    firstName: String? = null,
    lastName: String? = null,
    email: String? = null,
    phone: String? = null,
    shippingAddress: Address? = null,
    billingAddress: Address? = null
) : CustomerDetails(
    customerIdentifier = customerIdentifier,
    firstName = firstName,
    lastName = lastName,
    email = email,
    phone = phone,
    shippingAddress = shippingAddress,
    billingAddress = billingAddress
)
