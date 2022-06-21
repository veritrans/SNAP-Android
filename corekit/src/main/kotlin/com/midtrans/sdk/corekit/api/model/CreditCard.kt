package com.midtrans.sdk.corekit.api.model

data class CreditCard(
    val saveCard: Boolean = false,
    val tokenId: String? = null,
    val isSecure: Boolean = false,
    val channel: String? = null,
    val bank: String? = null,
    val savedTokens: List<SavedToken>? = null,
    val whitelistBins: List<String>? = null,
    val blacklistBins: List<String>? = null,
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