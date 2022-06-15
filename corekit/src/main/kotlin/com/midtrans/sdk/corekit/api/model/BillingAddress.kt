package com.midtrans.sdk.corekit.api.model

import com.google.gson.annotations.SerializedName

/**
 * It holds an information about billing address of user.
 *
 * Created by shivam on 10/29/15.
 */
class BillingAddress {
    @SerializedName("first_name")
    var firstName: String? = null

    @SerializedName("last_name")
    var lastName: String? = null
    var address: String? = null
    var city: String? = null

    @SerializedName("postal_code")
    var postalCode: String? = null
    var phone: String? = null

    @SerializedName("country_code")
    var countryCode: String? = null

    constructor() {}
    constructor(
        firstName: String?,
        lastName: String?,
        address: String?,
        city: String?,
        postalCode: String?,
        phone: String?,
        countryCode: String?
    ) {
        this.firstName = firstName
        this.lastName = lastName
        this.address = address
        this.city = city
        this.postalCode = postalCode
        this.phone = phone
        this.countryCode = countryCode
    }
}