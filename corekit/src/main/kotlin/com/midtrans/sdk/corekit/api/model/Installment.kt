package com.midtrans.sdk.corekit.api.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by ziahaqi on 8/5/16.
 */
data class Installment(
    val isRequired: Boolean = false,

    @SerializedName("terms")
    @Expose
    val terms: Map<String, ArrayList<Int>>? = null,
)
