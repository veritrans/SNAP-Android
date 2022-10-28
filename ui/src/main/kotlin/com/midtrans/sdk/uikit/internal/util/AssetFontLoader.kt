package com.midtrans.sdk.uikit.internal.util

import android.content.Context

import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily

object AssetFontLoader{

    private val fontCache: MutableMap<String, FontFamily> = mutableMapOf()

    @OptIn(ExperimentalTextApi::class)
    fun fontFamily(pathToAssetFont: String, context: Context): FontFamily {
        return fontCache[pathToAssetFont]
            ?: FontFamily(
                Font(pathToAssetFont, context.resources.assets)
            ).apply {
                fontCache[pathToAssetFont] = this
            }
    }

}
