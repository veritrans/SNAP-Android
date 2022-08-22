package com.midtrans.sdk.corekit.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class BinDetail (
    val registrationRequired: String?,
    val countryName: String?,
    val countryCode: String?,
    val channel: String?,
    val brand: String?,
    val bin: String?,
    val bankCode: String?,
    val bank: String?
): Parcelable