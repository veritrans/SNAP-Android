package com.midtrans.sdk.corekit.api.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class FreeTextLanguage(
    val id: String,
    val en: String
): Parcelable