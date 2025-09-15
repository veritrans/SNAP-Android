package com.midtrans.sdk.uikit.internal.model

internal data class DownloadResult(
    val success: Boolean,
    val requiresPermission: Boolean = false,
    val imageUrl: String? = null
)