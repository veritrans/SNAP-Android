package com.midtrans.sdk.corekit.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
open class BankTransferRequest(
    val vaNumber: String,
    val freeText: FreeText? = null,
    val subCompanyCode: String? = null
) : Parcelable
