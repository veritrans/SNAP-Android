package com.midtrans.sdk.corekit.internal.network.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by ziahaqi on 8/5/16.
 */
class Installment {
    var isRequired = false

    @SerializedName("terms")
    @Expose
    var terms: Map<String, ArrayList<Int>>? = null
}
