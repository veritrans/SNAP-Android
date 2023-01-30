package com.midtrans.sdk.corekit.models;

import com.midtrans.sdk.corekit.models.snap.BankTransferRequestModel;


public class BcaBankTransferRequestModel extends BankTransferRequestModel {

    private FreeText freeText;
    private String subCompanyCode;

    public BcaBankTransferRequestModel(String vaNumber) {
        super(vaNumber);
    }

    public BcaBankTransferRequestModel(String vaNumber, FreeText freeText) {
        super(vaNumber);
        this.freeText = freeText;
    }

    public String getSubCompanyCode() {
        return subCompanyCode;
    }

    public void setSubCompanyCode(String subCompanyCode) {
        this.subCompanyCode = subCompanyCode;
    }

    public FreeText getFreeText() {
        return freeText;
    }

    public void setFreeText(FreeText freeText) {
        this.freeText = freeText;
    }
}
