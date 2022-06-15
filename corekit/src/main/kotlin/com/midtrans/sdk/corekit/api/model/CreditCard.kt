package com.midtrans.sdk.corekit.api.model

import com.google.gson.annotations.SerializedName
import com.midtrans.sdk.corekit.internal.constant.Authentication
import com.midtrans.sdk.corekit.internal.network.model.request.Installment
import com.midtrans.sdk.corekit.internal.network.model.request.SavedToken
import com.midtrans.sdk.corekit.internal.util.Utils.mappingToCreditCardAuthentication

/**
 * Created by ziahaqi on 8/5/16.
 */
class CreditCard {
    @SerializedName("save_card")
    var isSaveCard = false

    @SerializedName("token_id")
    var tokenId: String? = null
    var isSecure = false
        private set
    var channel: String? = null
    var bank: String? = null

    @SerializedName("saved_tokens")
    var savedTokens: List<SavedToken>? = null

    @SerializedName("whitelist_bins")
    var whitelistBins: ArrayList<String>? = null
        private set

    @SerializedName("blacklist_bins")
    var blacklistBins: List<String>? = null

    @SerializedName("installment")
    var installment: Installment? = null
    var type: String? = null
    var authentication: String? = null
        set(cardAuthentication) {
            isSecure = cardAuthentication != null && cardAuthentication == Authentication.AUTH_3DS
            field = mappingToCreditCardAuthentication(cardAuthentication, isSecure)
        }

    fun setWhiteListBins(whiteListBins: ArrayList<String>?) {
        whitelistBins = whiteListBins
    }

    companion object {
        const val MIGS = "migs"

        @Deprecated("")
        val RBA = "rba"
    }
}