package com.midtrans.sdk.corekit.models.snap;

import androidx.annotation.NonNull;
import com.midtrans.sdk.uikit.api.model.BankTransferRequest;

public class BankTransferRequestModel extends BankTransferRequest {
    public BankTransferRequestModel(@NonNull String vaNumber) {
        super(vaNumber, null, null);
    }
}
