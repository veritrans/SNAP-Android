package com.midtrans.sdk.uikit.api.model

import com.midtrans.sdk.corekit.api.model.CreditCard
import kotlinx.parcelize.Parcelize

class CreditCard(
    saveCard: Boolean = false,
    tokenId: String? = null,
    secure: Boolean = false,
    channel: String? = null,
    bank: String? = null,
    whitelistBins: List<String>? = null,
    blacklistBins: List<String>? = null,
    installment: Installment? = null,
    type: String? = null,
) : CreditCard(
    saveCard = saveCard,
    tokenId = tokenId,
    secure = secure,
    channel = channel,
    bank = bank,
    whitelistBins = whitelistBins,
    blacklistBins = blacklistBins,
    installment = installment,
    type = type
)