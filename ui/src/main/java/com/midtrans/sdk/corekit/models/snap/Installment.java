package com.midtrans.sdk.corekit.models.snap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Installment {
    private boolean isRequired;
    private Map<String, ArrayList<Integer>> terms;

    public boolean isRequired() {
        return isRequired;
    }

    public void setRequired(boolean required) {
        isRequired = required;
    }

    public Map<String, ArrayList<Integer>> getTerms() {
        return terms;
    }

    public void setTerms(Map<String, ArrayList<Integer>> terms) {
        this.terms = terms;
    }
}
