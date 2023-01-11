package com.midtrans.sdk.corekit.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.midtrans.sdk.uikit.api.model.BankTransferRequest;

public class PermataBankTransferRequestModel extends BankTransferRequest {
    public PermataBankTransferRequestModel(@NonNull String vaNumber, @Nullable String recipientName) {
        super(vaNumber, null, null, recipientName);
    }
}

