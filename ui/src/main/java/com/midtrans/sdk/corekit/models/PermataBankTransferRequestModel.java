package com.midtrans.sdk.corekit.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.midtrans.sdk.corekit.models.snap.BankTransferRequestModel;
import com.midtrans.sdk.uikit.api.model.BankTransferRequest;

public class PermataBankTransferRequestModel extends BankTransferRequestModel {
    private String recipientName;

    public PermataBankTransferRequestModel(String vaNumber) {
        super(vaNumber);
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }
}
