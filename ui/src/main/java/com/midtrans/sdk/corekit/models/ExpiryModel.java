package com.midtrans.sdk.corekit.models;

import androidx.annotation.NonNull;

import com.midtrans.sdk.uikit.api.model.Expiry;

public class ExpiryModel extends Expiry {
    public ExpiryModel(@NonNull String startTime, @NonNull String unit, int duration) {
        super(startTime, unit, duration);
    }
}
