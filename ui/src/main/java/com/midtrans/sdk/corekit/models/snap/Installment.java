package com.midtrans.sdk.corekit.models.snap;

import java.util.List;
import java.util.Map;

public class Installment extends com.midtrans.sdk.uikit.api.model.Installment {

    public Installment(
            boolean isRequired,
            Map<String, List<Integer>> terms
    ) {
        super(isRequired, terms);
    }
}
