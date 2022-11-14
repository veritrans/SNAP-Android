package com.midtrans.sdk.uikit.api.model

import com.midtrans.sdk.corekit.api.model.FreeText

open class FreeText(
    inquiry: List<FreeTextLanguage>,
    payment: List<FreeTextLanguage>
) : FreeText(
    inquiry = inquiry,
    payment = payment
)