package com.midtrans.sdk.uikit.api.model

import android.graphics.Color
import java.util.*

data class CustomColorTheme(
    val colorPrimaryHex: String? = null,
    val colorPrimaryDarkHex: String? = null,
    val colorSecondaryHex: String? = null,
) {
    fun getPrimaryColor(): Int {
        return try {
            if (!colorPrimaryHex!!.startsWith("#")) {
                Color.parseColor("#" + colorPrimaryHex.lowercase(Locale.getDefault()))
            } else Color.parseColor(colorPrimaryHex.lowercase(Locale.getDefault()))
        } catch (exception: Exception) {
            Color.parseColor("#999999")
        }
    }

    fun getPrimaryDarkColor(): Int {
        return try {
            if (!colorPrimaryDarkHex!!.startsWith("#")) {
                Color.parseColor("#" + colorPrimaryDarkHex.lowercase(Locale.getDefault()))
            } else Color.parseColor(colorPrimaryDarkHex.lowercase(Locale.getDefault()))
        } catch (exception: Exception) {
            Color.parseColor("#737373")
        }
    }

    fun getSecondaryColor(): Int {
        return try {
            if (!colorSecondaryHex!!.startsWith("#")) {
                Color.parseColor("#" + colorSecondaryHex.lowercase(Locale.getDefault()))
            } else Color.parseColor(colorSecondaryHex.lowercase(Locale.getDefault()))
        } catch (exception: Exception) {
            Color.parseColor("#adadad")
        }
    }
}