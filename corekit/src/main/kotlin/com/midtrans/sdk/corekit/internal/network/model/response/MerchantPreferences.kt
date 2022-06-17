package com.midtrans.sdk.corekit.internal.network.model.response

import com.google.gson.annotations.SerializedName

/**
 * Created by rakawm on 10/12/16.
 */
internal data class MerchantPreferences (
    @SerializedName("other_va_processor")
    val otherVaProcessor: String? = null,

    @SerializedName("display_name")
    val displayName: String? = null,

    @SerializedName("finish_url")
    val finishUrl: String? = null,

    @SerializedName("error_url")
    val errorUrl: String? = null,

    @SerializedName("pending_url")
    val pendingUrl: String? = null,

    @SerializedName("logo_url")
    val logoUrl: String? = null,

    @SerializedName("color_scheme")
    val colorScheme: String? = null,

    @SerializedName("color_scheme_url")
    val colorSchemeUrl: String? = null,
    val locale: String? = null
)