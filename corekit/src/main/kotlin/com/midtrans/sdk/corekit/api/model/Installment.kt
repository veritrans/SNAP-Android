package com.midtrans.sdk.corekit.api.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
open class Installment(
    @SerializedName("required") val isRequired: Boolean = false,
    @SerializedName("terms") val terms: Map<String, List<Int>>? = null,
) : Parcelable
