package com.midtrans.sdk.corekit.api.model

import com.google.gson.annotations.SerializedName

/**
 * It holds an information about customer like name , email and phone.
 *
 * Created by shivam on 10/29/15.
 */
class CustomerDetails {
    @SerializedName("customer_identifier")
    var customerIdentifier: String? = null

    @SerializedName("first_name")
    var firstName: String? = null

    @SerializedName("last_name")
    var lastName: String? = null
    var email: String? = null
    var phone: String? = null

    @SerializedName("shipping_address")
    var shippingAddress: ShippingAddress? = null

    @SerializedName("billing_address")
    var billingAddress: BillingAddress? = null

    constructor() {}
    constructor(firstName: String?, lastName: String?, email: String?, phone: String?) {
        this.firstName = firstName
        this.lastName = lastName
        this.email = email
        this.phone = phone
    }
}