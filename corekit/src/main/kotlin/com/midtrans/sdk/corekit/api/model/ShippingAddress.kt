package com.midtrans.sdk.corekit.api.model

import com.google.gson.annotations.SerializedName
/**
 * It holds an information about shipping address of user.
 *
 * Created by shivam on 10/29/15.
 */
class ShippingAddress {
    var address: String? = null
    var city: String? = null
    var phone: String? = null

    @SerializedName("first_name")
    var firstName: String? = null

    @SerializedName("last_name")
    var lastName: String? = null

    @SerializedName("postal_code")
    var postalCode: String? = null

    @SerializedName("country_code")
    var countryCode: String? = null
}