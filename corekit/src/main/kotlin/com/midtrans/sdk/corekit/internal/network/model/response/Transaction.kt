package com.midtrans.sdk.corekit.internal.network.model.response

import com.google.gson.annotations.SerializedName
import com.midtrans.sdk.corekit.api.model.CreditCard
import com.midtrans.sdk.corekit.api.model.CustomerDetails
import com.midtrans.sdk.corekit.api.model.Gopay
import com.midtrans.sdk.corekit.api.model.ItemDetails

internal class Transaction {
    var token: String? = null

    @SerializedName("transaction_details")
    var transactionDetails: TransactionDetails? = null
    var callbacks: Callbacks? = null

    @SerializedName("enabled_payments")
    var enabledPayments: List<EnabledPayment>? = null

    @SerializedName("merchant")
    var merchantData: MerchantData? = null

    @SerializedName("credit_card")
    var creditCard: CreditCard? = null

    @Deprecated("")
    var promos: List<PromoResponse>? = null

    @SerializedName("promo_details")
    var promoDetails: PromoDetails? = null

    @SerializedName("item_details")
    var itemDetails: List<ItemDetails>? = null

    @SerializedName("customer_details")
    var customerDetails: CustomerDetails? = null

    @SerializedName("gopay")
    var gopay: Gopay? = null
}
