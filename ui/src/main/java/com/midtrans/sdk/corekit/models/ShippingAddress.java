package com.midtrans.sdk.corekit.models;

public class ShippingAddress extends com.midtrans.sdk.uikit.api.model.Address {
    public ShippingAddress(String firstName, String lastName, String address, String city, String postalCode, String phone, String countryCode) {
        super(firstName, lastName, address, city, postalCode, phone, countryCode);
    }
}
