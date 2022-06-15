package com.midtrans.sdk.corekit.api.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @author rakawm
 */
data class VaNumber(
    val bank: String? = null,

    @SerializedName("va_number")
    val accountNumber: String? = null
) : Serializable

