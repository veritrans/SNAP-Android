package com.midtrans.sdk.corekit.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    val firstName: String? = null,
    val lastName: String? = null,
    val address: String? = null,
    val city: String? = null,
    val postalCode: String? = null,
    val phone: String? = null,
    val countryCode: String? = null
) : Parcelable
