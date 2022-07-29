package com.midtrans.sdk.corekit.internal.network.model.request

import com.google.gson.annotations.SerializedName
import com.midtrans.sdk.corekit.api.model.*

internal data class SnapTokenRequest(
    val costumerDetails: CustomerDetails? = null,
    val itemDetails: List<ItemDetails>? = null,
    val transactionDetails: SnapTransactionDetail? = null,
    val creditCard: CreditCard? = null,
    val userId: String? = null,
    val permataVa: BankTransferRequest? = null,
    val bcaVa: BankTransferRequest? = null,
    val bniVa: BankTransferRequest? = null,
    val briVa: BankTransferRequest? = null,
    val enabledPayments: List<String>? = null,
    val expiry: Expiry? = null,
    val promo: Promo? = null,
    @SerializedName("custom_field1") val customField1: String? = null,
    @SerializedName("custom_field2") val customField2: String? = null,
    @SerializedName("custom_field3") val customField3: String? = null,
    val gopay: PaymentCallback? = null,
    val shopeepay: PaymentCallback? = null,
    val uobEzpay: PaymentCallback? = null
)