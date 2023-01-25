package com.midtrans.sdk.corekit.models.snap;

import java.util.List;
import java.util.Map;

public class Installment {
    private boolean isRequired;
    private Map<String, List<Integer>> terms;

    public boolean isRequired() {
        return isRequired;
    }

    public void setRequired(boolean required) {
        isRequired = required;
    }

    public Map<String, List<Integer>> getTerms() {
        return terms;
    }

    public void setTerms(Map<String, List<Integer>> terms) {
        this.terms = terms;
    }
}
