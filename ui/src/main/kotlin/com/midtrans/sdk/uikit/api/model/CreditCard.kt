package com.midtrans.sdk.uikit.api.model

import com.midtrans.sdk.corekit.api.model.CreditCard
import com.midtrans.sdk.corekit.api.model.SavedToken
import com.midtrans.sdk.corekit.internal.constant.Authentication
import com.midtrans.sdk.corekit.internal.util.Utils.mappingToCreditCardAuthentication

open class CreditCard(
    saveCard: Boolean = false,
    tokenId: String? = null,
    authentication: String? = null,
    channel: String? = null,
    bank: String? = null,
    savedTokens: List<SavedToken>? = null,
    whitelistBins: List<String>? = null,
    blacklistBins: List<String>? = null,
    installment: Installment? = null,
    type: String? = null
) : CreditCard(
    saveCard = saveCard,
    tokenId = tokenId,
    secure = authentication != null && authentication == Authentication.AUTH_3DS,
    channel = channel,
    bank = bank,
    savedTokens = savedTokens,
    whitelistBins = whitelistBins,
    blacklistBins = blacklistBins,
    installment = installment,
    type = type,
    authentication = mappingToCreditCardAuthentication(authentication, authentication != null && authentication == Authentication.AUTH_3DS,)
)
