package com.midtrans.sdk.uikit.internal.util

import android.content.ContentValues
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ImageDownloadHelper @Inject constructor() {

    fun downloadAndSaveImage(
        context: Context,
        imageUrl: String,
        fileName: String = "QRIS_${System.currentTimeMillis()}"
    ): Single<Boolean> {
        return Single.fromCallable {
            val url = URL(imageUrl)
            val bitmap = BitmapFactory.decodeStream(url.openStream())
            saveBitmapToGallery(context, bitmap, fileName)
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
    }

    private fun saveBitmapToGallery(
        context: Context,
        bitmap: android.graphics.Bitmap,
        fileName: String
    ): Boolean {
        return try {
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "$fileName.png")
                put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
            }

            val uri = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            
            uri?.let {
                context.contentResolver.openOutputStream(it)?.use { outputStream ->
                    bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, outputStream)
                }
                true
            } ?: false
        } catch (e: Exception) {
            false
        }
    }
}