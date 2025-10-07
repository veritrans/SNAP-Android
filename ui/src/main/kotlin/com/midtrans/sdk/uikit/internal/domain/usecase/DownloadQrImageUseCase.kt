package com.midtrans.sdk.uikit.internal.domain.usecase

import com.midtrans.sdk.uikit.internal.domain.repository.ImageRepository
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DownloadQrImageUseCase @Inject constructor(
    private val imageRepository: ImageRepository
) {

    operator fun invoke(
        imageUrl: String,
        fileName: String? = null
    ): Single<Boolean> {
        return Single.fromCallable {
            validateImageUrl(imageUrl)
        }.flatMap {
            imageRepository.downloadAndSaveImage(imageUrl, fileName)
        }.onErrorReturn {
            false
        }
    }

    private fun validateImageUrl(imageUrl: String) {
        when {
            imageUrl.isBlank() -> throw IllegalArgumentException("Image URL cannot be blank")
            !imageUrl.startsWith("http") -> throw IllegalArgumentException("Invalid image URL format")
        }
    }
}