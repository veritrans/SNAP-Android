package com.midtrans.sdk.corekit.internal.network.model.response

internal data class MerchantData(
    val preference: MerchantPreferences? = null,
    val clientKey: String? = null,
    val enabledPrinciples: List<String>? = null,
    val pointBanks: List<String>? = null,
    val merchantId: String? = null,
    val acquiringBanks: List<String>? = null,
    val priorityCardFeature: String? = null,
    val recurringMidIsActive: Boolean? = null
)
