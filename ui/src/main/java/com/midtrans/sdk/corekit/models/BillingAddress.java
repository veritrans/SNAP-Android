package com.midtrans.sdk.corekit.models;

public class BillingAddress extends com.midtrans.sdk.uikit.api.model.Address {
    public BillingAddress(String firstName, String lastName, String address, String city, String postalCode, String phone, String countryCode) {
        super(firstName, lastName, address, city, postalCode, phone, countryCode);
    }
}
