package com.midtrans.sdk.uikit.api.model

import com.midtrans.sdk.corekit.api.model.ItemDetails

open class ItemDetails(
    id: String? = null,
    price: Double? = null,
    quantity: Int = 0,
    name: String? = null,
) : ItemDetails(
    id = id,
    price = price,
    quantity = quantity,
    name = name
)