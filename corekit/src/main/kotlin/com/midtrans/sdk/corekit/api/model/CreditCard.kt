package com.midtrans.sdk.corekit.api.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CreditCard(
    val saveCard: Boolean = false,
    val tokenId: String? = null,
    val secure: Boolean = false,
    val channel: String? = null,
    val bank: String? = null,
    val savedTokens: List<SavedToken>? = null,
    val whitelistBins: List<String>? = null,
    val blacklistBins: List<String>? = null,
    val installment: Installment? = null,
    val type: String? = null,
    val authentication: String? = null
) : Parcelable {
    companion object {
        const val MIGS = "migs"

        @Deprecated("")
        val RBA = "rba"
    }
}