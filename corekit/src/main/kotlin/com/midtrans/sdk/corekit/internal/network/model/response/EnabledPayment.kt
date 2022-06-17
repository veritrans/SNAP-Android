package com.midtrans.sdk.corekit.internal.network.model.response

/**
 * Created by ziahaqi on 10/13/16.
 */
internal data class EnabledPayment(var type: String, var category: String) {

    companion object {
        const val STATUS_UP = "up"
        const val STATUS_DOWN = "down"
    }
}
