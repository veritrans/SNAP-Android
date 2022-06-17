package com.midtrans.sdk.corekit.api.model

/**
 * It hold an information about purchased item, like id, price etc.
 *
 * Created by shivam on 10/29/15.
 */
data class ItemDetails(
    val id: String? = null,
    val price: Double? = null,
    val quantity: Int = 0,
    val name: String? = null,
)
 