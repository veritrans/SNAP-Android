package com.midtrans.sdk.corekit.internal.network.model.response

import com.google.gson.annotations.SerializedName

/**
 * Created by rakawm on 10/12/16.
 */
class MerchantPreferences {
    @SerializedName("other_va_processor")
    var otherVaProcessor: String? = null

    @SerializedName("display_name")
    var displayName: String? = null

    @SerializedName("finish_url")
    var finishUrl: String? = null

    @SerializedName("error_url")
    var errorUrl: String? = null

    @SerializedName("pending_url")
    var pendingUrl: String? = null

    @SerializedName("logo_url")
    var logoUrl: String? = null

    @SerializedName("color_scheme")
    var colorScheme: String? = null

    @SerializedName("color_scheme_url")
    var colorSchemeUrl: String? = null
    var locale: String? = null
}