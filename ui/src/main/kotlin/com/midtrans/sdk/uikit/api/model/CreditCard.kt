package com.midtrans.sdk.uikit.api.model

import com.midtrans.sdk.corekit.api.model.CreditCard
import com.midtrans.sdk.corekit.api.model.SavedToken
import kotlinx.parcelize.Parcelize

open class CreditCard(
    saveCard: Boolean = false,
    tokenId: String? = null,
    secure: Boolean = false,
    channel: String? = null,
    bank: String? = null,
    savedTokens: List<SavedToken>? = null,
    whitelistBins: List<String>? = null,
    blacklistBins: List<String>? = null,
    installment: Installment? = null,
    type: String? = null,
    authentication: String? = null
) : CreditCard(
    saveCard = saveCard,
    tokenId = tokenId,
    secure = secure,
    channel = channel,
    bank = bank,
    savedTokens = savedTokens,
    whitelistBins = whitelistBins,
    blacklistBins = blacklistBins,
    installment = installment,
    type = type,
    authentication = authentication
)
