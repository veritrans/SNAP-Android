package com.midtrans.sdk.corekit.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.midtrans.sdk.uikit.api.model.BankTransferRequest;


public class BcaBankTransferRequestModel extends BankTransferRequest {
    public BcaBankTransferRequestModel(@NonNull String vaNumber, @NonNull FreeText freeText, @Nullable String subCompanyCode) {
        super(vaNumber, freeText, subCompanyCode, null);
    }
}
