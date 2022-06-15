package com.midtrans.sdk.corekit.api.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by rakawm on 4/25/17.
 */
class BankTransferRequestModel : Serializable {
    @SerializedName("va_number")
    var vaNumber: String? = null

    constructor() {}
    constructor(vaNumber: String?) {
        this.vaNumber = vaNumber
    }
}