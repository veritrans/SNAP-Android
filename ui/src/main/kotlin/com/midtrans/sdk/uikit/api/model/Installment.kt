package com.midtrans.sdk.uikit.api.model

import com.midtrans.sdk.corekit.api.model.Installment


open class Installment(
    isRequired: Boolean = false,
    terms: Map<String, List<Int>>? = null,
) : Installment(
    isRequired = isRequired,
    terms = terms
)
