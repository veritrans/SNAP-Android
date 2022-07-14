package com.midtrans.sdk.uikit.internal.view

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.runtime.*

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun AnimatedIcon(
    resId: Int,
): AnimationToggle {
    var atEnd by remember { mutableStateOf(false) }
    val image = AnimatedImageVector.animatedVectorResource(id = resId)

    Image(
        painter = rememberAnimatedVectorPainter(image, atEnd = atEnd),
        contentDescription = "null"
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

interface AnimationToggle {
    fun start()
    fun stop()
}