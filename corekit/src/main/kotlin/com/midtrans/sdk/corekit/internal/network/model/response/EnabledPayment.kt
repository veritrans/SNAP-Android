package com.midtrans.sdk.corekit.internal.network.model.response

import java.io.Serializable

/**
 * Created by ziahaqi on 10/13/16.
 */
class EnabledPayment(var type: String, var category: String) : Serializable {
    var status: String? = null
    var acquirer: String? = null

    companion object {
        const val STATUS_UP = "up"
        const val STATUS_DOWN = "down"
    }
}
