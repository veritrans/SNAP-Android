package com.midtrans.sdk.uikit.api.model

import com.midtrans.sdk.corekit.api.model.SnapTransactionDetail


class SnapTransactionDetail(
    orderId: String,
    grossAmount: Double,
    currency: String = "IDR"
) : SnapTransactionDetail(
    orderId = orderId,
    grossAmount = grossAmount,
    currency = currency
)
