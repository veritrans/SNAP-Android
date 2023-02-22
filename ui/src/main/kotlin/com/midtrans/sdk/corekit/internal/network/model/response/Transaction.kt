package com.midtrans.sdk.corekit.internal.network.model.response

import com.midtrans.sdk.corekit.api.model.*

internal data class Transaction(
    val token: String? = null,
    val transactionDetails: TransactionDetails? = null,
    val callbacks: Callbacks? = null,
    val enabledPayments: List<EnabledPayment>? = null,
    val merchant: Merchant? = null,
    val creditCard: CreditCard? = null,
    val promos: List<PromoResponse>? = null,
    val promoDetails: PromoDetails? = null,
    val itemDetails: List<ItemDetails>? = null,
    val customerDetails: CustomerDetails? = null,
    val gopay: PaymentCallback? = null,
    val expiryTime: String? = null,
    val result: TransactionResponse? = null
)
