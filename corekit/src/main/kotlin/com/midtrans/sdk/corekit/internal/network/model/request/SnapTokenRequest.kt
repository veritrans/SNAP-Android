package com.midtrans.sdk.corekit.internal.network.model.request

import com.google.gson.annotations.SerializedName
import com.midtrans.sdk.corekit.api.model.CustomerDetails
import com.midtrans.sdk.corekit.api.model.ItemDetails
import com.midtrans.sdk.corekit.api.model.SnapTransactionDetail

internal data class SnapTokenRequest(
    val costumerDetails: CustomerDetails? = null,
    val itemDetails: List<ItemDetails>? = null,
    val transactionDetails: SnapTransactionDetail? = null,
    val creditCard: CreditCard? = null,
    val userId: String? = null,
    val permataVa: BankTransferRequestModel? = null,
    val bcaVa: BcaBankTransferRequestModel? = null,
    val bniVa: BankTransferRequestModel? = null,
    val briVa: BankTransferRequestModel? = null,
    val enabledPayments: List<String>? = null,
    val expiry: ExpiryModel? = null,
    val promo: SnapPromo? = null,
    @SerializedName("custom_field1") val customField1: String? = null,
    @SerializedName("custom_field2") val customField2: String? = null,
    @SerializedName("custom_field3") val customField3: String? = null,
    val gopay: Gopay? = null,
    val shopeepay: Shopeepay? = null,
    val uobEzpay: UobEzpay? = null,
)