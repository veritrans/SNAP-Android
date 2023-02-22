package com.midtrans.sdk.corekit.internal.network

import com.midtrans.sdk.uikit.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

internal class SnapRequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        return chain.proceed(
            request.newBuilder()
                .addHeader("X-Source", "mobile-android")
                .addHeader("X-Source-Version", "android-${BuildConfig.SDK_VERSION}")
                .addHeader("X-Service", "snap")
                .build()
        )
    }
}