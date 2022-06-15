package com.midtrans.sdk.corekit.api.model

/**
 * It hold an information about purchased item, like id, price etc.
 *
 * Created by shivam on 10/29/15.
 */
class ItemDetails {
    var id: String? = null
    var price: Double? = null
    var quantity = 0
    var name: String? = null

    constructor() {}

    /**
     * @param id       unique id of the item.
     * @param price    price of the item.
     * @param quantity number of items that is purchased.
     * @param name     name of the item.
     */
    constructor(id: String?, price: Double, quantity: Int, name: String?) {
        this.id = id
        this.price = price
        this.quantity = quantity
        this.name = name
    }
}
