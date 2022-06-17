package com.midtrans.sdk.corekit.api.model

import com.google.gson.annotations.SerializedName

/**
 * It holds an information about billing address of user.
 *
 * Created by shivam on 10/29/15.
 */
data class BillingAddress(
    @SerializedName("first_name")
    val firstName: String? = null,

    @SerializedName("last_name")
    val lastName: String? = null,
    val address: String? = null,
    val city: String? = null,

    @SerializedName("postal_code")
    val postalCode: String? = null,
    val phone: String? = null,

    @SerializedName("country_code")
    val countryCode: String? = null,

    )