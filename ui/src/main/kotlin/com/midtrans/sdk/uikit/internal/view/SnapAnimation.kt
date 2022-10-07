package com.midtrans.sdk.uikit.internal.view

import android.os.Build.VERSION.SDK_INT
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size.Companion.ORIGINAL

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun AnimatedIcon(
    resId: Int,
    modifier: Modifier = Modifier
): AnimationToggle {
    var atEnd by remember { mutableStateOf(false) }
    val image = AnimatedImageVector.animatedVectorResource(id = resId)

    Image(
        painter = rememberAnimatedVectorPainter(image, atEnd = atEnd),
        contentDescription = "null",
        modifier = modifier
    )
    return object : AnimationToggle {
        override fun start() {
            atEnd = true
        }

        override fun stop() {
            atEnd = false
        }

    }
}

@Composable
fun GifImage(
    gifResId: Int,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    Image(
        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(context).data(data = gifResId).apply(block = {
                size(ORIGINAL)
            }).build(), imageLoader = imageLoader
        ),
        contentDescription = null,
        modifier = modifier.fillMaxWidth(),
    )
}

interface AnimationToggle {
    fun start()
    fun stop()
}