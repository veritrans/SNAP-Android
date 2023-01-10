package com.midtrans.sdk.corekit.models.snap;

import java.util.List;

public class CreditCard extends com.midtrans.sdk.uikit.api.model.CreditCard {

    public CreditCard(
            boolean saveCard,
            String tokenId,
            String authentication,
            boolean secure,
            String channel,
            String bank,
            List<SavedToken> savedTokens,
            List<String> whitelistBins,
            List<String> blacklistBins,
            Installment installment,
            String type
    ) {
        super(saveCard, tokenId, authentication, secure, channel, bank, savedTokens, whitelistBins, blacklistBins, installment, type);
    }
}
