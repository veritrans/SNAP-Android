package com.midtrans.sdk.uikit.internal.domain.repository

import io.reactivex.Single

internal interface ImageRepository {
    
    fun downloadAndSaveImage(
        imageUrl: String,
        fileName: String? = null
    ): Single<Boolean>
}