package com.midtrans.sdk.corekit.internal.network.model.response

import com.midtrans.sdk.corekit.api.model.CreditCard
import com.midtrans.sdk.corekit.api.model.CustomerDetails
import com.midtrans.sdk.corekit.api.model.Gopay
import com.midtrans.sdk.corekit.api.model.ItemDetails

internal data class Transaction(
    val token: String? = null,
    val transactionDetails: TransactionDetails? = null,
    val callbacks: Callbacks? = null,
    val enabledPayments: List<EnabledPayment>? = null,
    val merchantData: MerchantData? = null,
    val creditCard: CreditCard? = null,
    val promos: List<PromoResponse>? = null,
    val promoDetails: PromoDetails? = null,
    val itemDetails: List<ItemDetails>? = null,
    val customerDetails: CustomerDetails? = null,
    val gopay: Gopay? = null
)
