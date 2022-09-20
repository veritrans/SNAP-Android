package com.midtrans.sdk.corekit.api.model

import android.os.Parcelable
import com.midtrans.sdk.corekit.internal.network.model.response.MerchantData
import com.midtrans.sdk.corekit.internal.network.model.response.TransactionDetails
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentOption(
    val token: String,
    val options: List<PaymentMethod>,
    val creditCard: CreditCard?,
    val promos: List<PromoResponse>?,
    val merchantData: MerchantData?,
    val customerDetails: CustomerDetails?,
    val transactionDetails: TransactionDetails?,
    val expiryTme: String?
) : Parcelable
