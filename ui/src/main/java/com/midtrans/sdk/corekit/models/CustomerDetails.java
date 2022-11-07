package com.midtrans.sdk.corekit.models;

/**
 * It holds an information about customer like name , email and phone.
 *
 * Created by shivam on 10/29/15.
 */
public class CustomerDetails extends com.midtrans.sdk.uikit.api.model.CustomerDetails {

    public CustomerDetails(String firstName, String lastName, String email, String phone) {
        super(null, firstName, lastName, email, phone, null, null);
    }

}
