package com.midtrans.sdk.corekit.models.snap;

import java.util.List;

public class CreditCard extends com.midtrans.sdk.uikit.api.model.CreditCard {

    public CreditCard(
            boolean saveCard,
            String tokenId,
            boolean secure,
            String channel,
            String bank,
            List<SavedToken> savedTokens,
            List<String> whitelistBins,
            List<String> blacklistBins,
            Installment installment,
            String type,
            String authentication
    ) {
        super(saveCard, tokenId, secure, channel, bank, savedTokens, whitelistBins, blacklistBins, installment, type, authentication);
    }
}
