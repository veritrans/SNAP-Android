package com.midtrans.sdk.corekit.internal.network.model.response

import com.google.gson.annotations.SerializedName
import com.midtrans.sdk.corekit.api.model.CreditCard
import com.midtrans.sdk.corekit.api.model.CustomerDetails
import com.midtrans.sdk.corekit.api.model.Gopay
import com.midtrans.sdk.corekit.api.model.ItemDetails

internal data class Transaction(
    val token: String? = null,

    @SerializedName("transaction_details")
    val transactionDetails: TransactionDetails? = null,
    val callbacks: Callbacks? = null,

    @SerializedName("enabled_payments")
    val enabledPayments: List<EnabledPayment>? = null,

    @SerializedName("merchant")
    val merchantData: MerchantData? = null,

    @SerializedName("credit_card")
    val creditCard: CreditCard? = null,

    @Deprecated("")
    val promos: List<PromoResponse>? = null,

    @SerializedName("promo_details")
    val promoDetails: PromoDetails? = null,

    @SerializedName("item_details")
    val itemDetails: List<ItemDetails>? = null,

    @SerializedName("customer_details")
    val customerDetails: CustomerDetails? = null,

    @SerializedName("gopay")
    val gopay: Gopay? = null
)
