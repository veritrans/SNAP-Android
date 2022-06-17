package com.midtrans.sdk.corekit.api.model

import com.google.gson.annotations.SerializedName

/**
 * Created by ziahaqi on 8/5/16.
 */
data class CreditCard(
    @SerializedName("save_card")
    val isSaveCard: Boolean = false,

    @SerializedName("token_id")
    val tokenId: String? = null,
    val isSecure: Boolean = false,
    val channel: String? = null,
    val bank: String? = null,

    @SerializedName("saved_tokens")
    val savedTokens: List<SavedToken>? = null,

    @SerializedName("whitelist_bins")
    val whitelistBins: ArrayList<String>? = null,

    @SerializedName("blacklist_bins")
    val blacklistBins: List<String>? = null,

    @SerializedName("installment")
    val installment: Installment? = null,
    val type: String? = null,
    val authentication: String? = null,

    ) {
    companion object {
        const val MIGS = "migs"

        @Deprecated("")
        val RBA = "rba"
    }
}