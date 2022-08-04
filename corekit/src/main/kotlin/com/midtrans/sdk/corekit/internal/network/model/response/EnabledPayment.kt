package com.midtrans.sdk.corekit.internal.network.model.response

internal data class EnabledPayment(
    val type: String,
    val status: String,
    val category: String? = null,
    val acquirer: String? = null,
    val mode: List<String> = emptyList()
) {

    companion object {
        const val STATUS_UP = "up"
        const val STATUS_DOWN = "down"
    }
}
