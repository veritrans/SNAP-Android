package com.midtrans.sdk.uikit.internal.data.repository

import android.content.ContentValues
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.midtrans.sdk.uikit.internal.domain.repository.ImageRepository
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ImageRepositoryImpl @Inject constructor(
    private val context: Context
) : ImageRepository {

    override fun downloadAndSaveImage(
        imageUrl: String,
        fileName: String?
    ): Single<Boolean> {
        return Single.fromCallable {
            try {
                val url = URL(imageUrl)
                val bitmap = BitmapFactory.decodeStream(url.openStream())
                    ?: throw IllegalStateException("Failed to decode image from URL")
                
                val finalFileName = fileName ?: generateDefaultFileName()
                
                saveBitmapToGallery(bitmap, finalFileName)
            } catch (exception: Exception) {
                false
            }
        }.subscribeOn(Schedulers.io())
    }

    private fun saveBitmapToGallery(
        bitmap: android.graphics.Bitmap,
        fileName: String
    ): Boolean {
        return try {
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "$fileName.png")
                put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                    put(MediaStore.Images.Media.IS_PENDING, 1)
                }
            }

            val uri = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            
            uri?.let { imageUri ->
                context.contentResolver.openOutputStream(imageUri)?.use { outputStream ->
                    bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, outputStream)
                }
                
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    contentValues.clear()
                    contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                    context.contentResolver.update(imageUri, contentValues, null, null)
                }
                
                true
            } ?: false
        } catch (exception: Exception) {
            false
        }
    }

    private fun generateDefaultFileName(): String {
        return "QRIS_${System.currentTimeMillis()}"
    }
}