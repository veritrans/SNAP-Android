package com.midtrans.sdk.corekit.internal.network.model.request

import com.google.gson.annotations.SerializedName

/**
 * Created by rakawm on 7/19/16.
 */
class SnapTransactionDetails(orderId: String?, grossAmount: Double?) {
    @SerializedName("order_id")
    var orderId: String? = null

    @SerializedName("gross_amount")
    var grossAmount: Double? = null
    private var currency: String? = null
    fun setCurrency(currency: String?) {
        this.currency = currency
    }

    init {
        this.orderId = orderId
        this.grossAmount = grossAmount
    }
}