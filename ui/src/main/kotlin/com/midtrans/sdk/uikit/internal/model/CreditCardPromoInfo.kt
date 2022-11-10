package com.midtrans.sdk.uikit.internal.model

/**
 * promoAmount and pointAmount should be in negative
*/
data class CreditCardPromoInfo(
    val promoName: String?,
    val promoAmount: Double?,
    val pointName: String?,
    val pointAmount: Double?,
    val discountedAmount: Double?
)
