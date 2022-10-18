package com.midtrans.sdk.corekit.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
open class CustomerDetails(
    val customerIdentifier: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val shippingAddress: Address? = null,
    val billingAddress: Address? = null
): Parcelable
