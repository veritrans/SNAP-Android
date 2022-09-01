package com.midtrans.sdk.corekit.internal.network.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MerchantPreferences(
    val otherVaProcessor: String? = null,
    val displayName: String? = null,
    val finishUrl: String? = null,
    val errorUrl: String? = null,
    val pendingUrl: String? = null,
    val logoUrl: String? = null,
    val colorScheme: String? = null,
    val colorSchemeUrl: String? = null,
    val locale: String? = null
): Parcelable
