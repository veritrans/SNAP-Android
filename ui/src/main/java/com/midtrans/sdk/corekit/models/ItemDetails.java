package com.midtrans.sdk.corekit.models;

/**
 * It hold an information about purchased item, </p>like id, price etc.
 *
 * Created by shivam on 10/29/15.
 */
public class ItemDetails extends com.midtrans.sdk.uikit.api.model.ItemDetails {
    public ItemDetails(String id, Double price, Integer quantity, String name) {
        super (id, price, quantity, name);
    }
}
