package com.midtrans.sdk.corekit.internal.network.model.request

import com.google.gson.annotations.SerializedName

/**
 * @author rakawm
 */


internal data class BankTransferPaymentRequest(
    @SerializedName("payment_type") val paymentType: String,
    @SerializedName("customer_details") val customerDetails: CustomerDetailRequest
)