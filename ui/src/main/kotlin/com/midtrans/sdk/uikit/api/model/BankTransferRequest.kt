package com.midtrans.sdk.uikit.api.model

import com.midtrans.sdk.corekit.api.model.BankTransferRequest

open class BankTransferRequest(
    vaNumber: String,
    freeText: FreeText? = null,
    subCompanyCode: String? = null
) : BankTransferRequest(
    vaNumber = vaNumber,
    freeText = freeText,
    subCompanyCode = subCompanyCode
)
