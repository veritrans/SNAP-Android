package com.midtrans.sdk.uikit.internal.base

import android.content.res.Configuration
import android.util.DisplayMetrics
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.external.UiKitApi
import java.util.*
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
    protected fun isShowPaymentStatusPage() : Boolean {
        return getUikitSetting().showPaymentStatus
    }

    protected fun getUikitSetting() = UiKitApi.getDefaultInstance().uiKitSetting

    fun getStringResourceInEnglish(id: Int): String {
        val config = Configuration(resources.configuration)
        config.setLocale(Locale("en"))
        val localizedContext = createConfigurationContext(config)
        return localizedContext.getString(id)
    }

    fun setSecure(window: Window) {
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
    }
}