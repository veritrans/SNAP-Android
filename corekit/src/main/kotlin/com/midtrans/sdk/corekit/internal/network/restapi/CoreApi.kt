package com.midtrans.sdk.corekit.internal.network.restapi

import com.midtrans.sdk.corekit.api.model.CardTokenResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.QueryMap

internal interface CoreApi {

    @GET("token?x_source=android")
    fun getToken(
        @QueryMap param : Map<String, Any>
    ): Single<CardTokenResponse>
}