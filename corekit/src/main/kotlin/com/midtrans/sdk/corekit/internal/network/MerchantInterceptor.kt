package com.midtrans.sdk.corekit.internal.network

import android.util.Base64
import okhttp3.Interceptor
import okhttp3.Response

internal class MerchantInterceptor(private val merchantClientKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val encodedClientKey =
            String(Base64.encode(merchantClientKey.toByteArray(Charsets.UTF_8), Base64.NO_WRAP))
        val headerInterceptedRequest = request.newBuilder()
            .addHeader("Authorization", "Basic $encodedClientKey")
            .build()

        return chain.proceed(headerInterceptedRequest)
    }
}