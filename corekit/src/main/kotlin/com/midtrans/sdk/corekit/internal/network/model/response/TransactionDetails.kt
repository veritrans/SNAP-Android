package com.midtrans.sdk.corekit.internal.network.model.response

import com.google.gson.annotations.SerializedName

/**
 * @author rakawm
 */
internal data class TransactionDetails(
    @SerializedName("order_id")
    var orderId: String? = null,

    @SerializedName("gross_amount")
    var amount: Double = 0.0,
    val currency: String? = null

)