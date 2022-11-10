package com.midtrans.sdk.uikit.api.model

import com.midtrans.sdk.corekit.api.model.Expiry

open class Expiry(
    startTime: String,
    unit: String,
    duration: Int
) : Expiry(
    startTime = startTime,
    unit = unit,
    duration = duration
)