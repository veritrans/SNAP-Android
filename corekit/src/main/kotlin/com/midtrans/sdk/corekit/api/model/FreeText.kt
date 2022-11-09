package com.midtrans.sdk.corekit.api.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class FreeText(
    val inquiry: List<FreeTextLanguage>,
    val payment: List<FreeTextLanguage>
): Parcelable