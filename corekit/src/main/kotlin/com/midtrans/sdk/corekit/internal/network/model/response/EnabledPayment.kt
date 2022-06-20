package com.midtrans.sdk.corekit.internal.network.model.response

internal data class EnabledPayment(var type: String, var category: String) {

    companion object {
        const val STATUS_UP = "up"
        const val STATUS_DOWN = "down"
    }
}
