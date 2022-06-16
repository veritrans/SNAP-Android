package com.midtrans.sdk.corekit.internal.network.model.response

import com.google.gson.annotations.SerializedName

/**
 * @author rakawm
 */
internal class TransactionDetails {
    @SerializedName("order_id")
    var orderId: String? = null

    @SerializedName("gross_amount")
    var amount = 0.0
    val currency: String? = null

    constructor() {}
    constructor(orderId: String?, amount: Double) {
        this.orderId = orderId
        this.amount = amount
    }
}