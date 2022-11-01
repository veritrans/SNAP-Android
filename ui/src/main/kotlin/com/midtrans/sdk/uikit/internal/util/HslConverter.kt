package com.midtrans.sdk.uikit.internal.util

object HslConverter {
    fun rgbToHsv(rgbColor: Int): FloatArray{
        val hsvArray: FloatArray = floatArrayOf(0f, 0f, 0f)
        android.graphics.Color.colorToHSV(rgbColor, hsvArray)
        return hsvArray
    }

    fun hsvToRgb(hsvColor: FloatArray): Int{
        return android.graphics.Color.HSVToColor(hsvColor)
    }

    fun addBrightness(rgbColor: Int, brightness: Float): Int{
        val hsvColor = rgbToHsv(rgbColor)
        hsvColor[2] += brightness
        hsvColor[1] -= 0.5f
        return hsvToRgb(hsvColor)
    }
    fun addSaturation(rgbColor: Int, saturation: Float): Int{
        val hsvColor = rgbToHsv(rgbColor)
        hsvColor[1] += saturation
        return hsvToRgb(hsvColor)
    }
}