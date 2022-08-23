package com.midtrans.sdk.uikit.internal.base

import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import com.midtrans.sdk.uikit.R
import kotlin.math.sqrt

open class BaseActivity : AppCompatActivity(){
    protected fun isTabletDevice(): Boolean {
        val metrics = DisplayMetrics()
        this.windowManager.defaultDisplay.getMetrics(metrics)

        val yInches = metrics.heightPixels / metrics.ydpi
        val xInches = metrics.widthPixels / metrics.xdpi
        val diagonalInches = sqrt((xInches * xInches + yInches * yInches).toDouble())
        val hasTabletAttribute = resources.getBoolean(R.bool.isTablet)

        return diagonalInches >= 6.5 && hasTabletAttribute
    }
}