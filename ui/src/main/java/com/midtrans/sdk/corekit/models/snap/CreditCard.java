package com.midtrans.sdk.corekit.models.snap;

import com.midtrans.sdk.corekit.internal.util.Utils;
import com.midtrans.sdk.uikit.api.model.Authentication;

import java.util.ArrayList;
import java.util.List;

public class CreditCard {

    public static final String MIGS = "migs";

    @Deprecated
    public static final String RBA = "rba";

    private boolean saveCard;
    private String tokenId;
    private boolean secure;
    private String channel;
    private String bank;
    private List<SavedToken> savedTokens;
    private ArrayList<String> whitelistBins;
    private List<String> blacklistBins;
    private Installment installment;
    private String type;

    public boolean isSaveCard() {
        return saveCard;
    }

    public void setSaveCard(boolean saveCard) {
        this.saveCard = saveCard;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public List<SavedToken> getSavedTokens() {
        return savedTokens;
    }

    public void setSavedTokens(List<SavedToken> savedTokens) {
        this.savedTokens = savedTokens;
    }

    public ArrayList<String> getWhitelistBins() {
        return whitelistBins;
    }

    public void setWhitelistBins(ArrayList<String> whitelistBins) {
        this.whitelistBins = whitelistBins;
    }

    public List<String> getBlacklistBins() {
        return blacklistBins;
    }

    public void setBlacklistBins(List<String> blacklistBins) {
        this.blacklistBins = blacklistBins;
    }

    public Installment getInstallment() {
        return installment;
    }

    public void setInstallment(Installment installment) {
        this.installment = installment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAuthentication() {
        return authentication;
    }

    private String authentication;


    public void setAuthentication(String cardAuthentication) {
        this.secure = cardAuthentication != null && cardAuthentication.equals(Authentication.AUTH_3DS);
        this.authentication = Utils.INSTANCE.mappingToCreditCardAuthentication(cardAuthentication, this.secure);
    }

}
