package com.midtrans.sdk.corekit.models;

/**
 * It holds an information about customer like name , email and phone.
 *
 * Created by shivam on 10/29/15.
 */
public class CustomerDetails extends com.midtrans.sdk.uikit.api.model.CustomerDetails {
    public CustomerDetails(String customerIdentifier, String firstName, String lastName, String email, String phone, ShippingAddress shippingAddress, BillingAddress billingAddress) {
        super(customerIdentifier, firstName, lastName, email, phone, shippingAddress, billingAddress);
    }
}
