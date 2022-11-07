package com.midtrans.sdk.corekit.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BankTransferRequest(
    val vaNumber: String,
    val freeText: String? = null,
    val subCompanyCode: String? = null
) : Parcelable