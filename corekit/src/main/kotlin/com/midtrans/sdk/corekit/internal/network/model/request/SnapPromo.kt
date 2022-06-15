package com.midtrans.sdk.corekit.internal.network.model.request

import com.google.gson.annotations.SerializedName

/**
 * Created by rakawm on 1/2/17.
 */
class SnapPromo {
    var isEnabled = false

    @SerializedName("allowed_promo_codes")
    var allowedPromoCodes: List<String>? = null
}