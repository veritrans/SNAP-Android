package com.midtrans.sdk.corekit.models.snap;

public class BankTransferRequestModel {
    private String vaNumber;

    public BankTransferRequestModel() {}

    public BankTransferRequestModel(String vaNumber) {
        setVaNumber(vaNumber);
    }

    public String getVaNumber() {
        return vaNumber;
    }

    public void setVaNumber(String vaNumber) {
        this.vaNumber = vaNumber;
    }
}
