package com.midtrans.sdk.corekit.internal.network.restapi

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface MerchantApi {
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("charge")
    fun getSnapToken(@Body request: SnapTokenRequest): Single<SnapTokenResponse>
}