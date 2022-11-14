package com.midtrans.sdk.corekit.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
open class FreeTextLanguage(
    val id: String,
    val en: String
): Parcelable
