package com.midtrans.sdk.corekit.api.model

import com.google.gson.annotations.SerializedName

/**
 * It holds an information about customer like name , email and phone.
 *
 * Created by shivam on 10/29/15.
 */
data class CustomerDetails(
    @SerializedName("customer_identifier")
    val customerIdentifier: String? = null,

    @SerializedName("first_name")
    val firstName: String? = null,

    @SerializedName("last_name")
    val lastName: String? = null,
    val email: String? = null,
    val phone: String? = null,

    @SerializedName("shipping_address")
    val shippingAddress: ShippingAddress? = null,

    @SerializedName("billing_address")
    val billingAddress: BillingAddress? = null
)