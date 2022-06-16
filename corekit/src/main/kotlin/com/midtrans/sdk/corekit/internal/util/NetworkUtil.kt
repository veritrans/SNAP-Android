package com.midtrans.sdk.corekit.internal.util

import android.content.Context
import android.net.ConnectivityManager

internal object NetworkUtil {
    fun isNetworkAvailable(context: Context): Boolean {
        try {
            val connManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connManager.activeNetworkInfo != null && connManager.activeNetworkInfo!!
                    .isAvailable && connManager.activeNetworkInfo!!.isConnected
            ) {
                return true
            }
        } catch (ex: Exception) {
            return false
        }
        return false
    }
}