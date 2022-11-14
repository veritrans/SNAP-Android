package com.midtrans.sdk.corekit.models;

import androidx.annotation.NonNull;
import java.util.List;

public class FreeText extends com.midtrans.sdk.uikit.api.model.FreeText {
    public FreeText(@NonNull List<? extends FreeTextLanguage> inquiry, @NonNull List<? extends FreeTextLanguage> payment) {
        super(inquiry, payment);
    }
}
