package com.midtrans.sdk.corekit.internal.network.restapi

import com.midtrans.sdk.corekit.api.model.BinResponse
import com.midtrans.sdk.corekit.api.model.CardTokenResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.QueryMap


internal interface CoreApi {

    @GET("v2/token?")
    fun getCardToken(
        @QueryMap param : Map<String, String>
    ): Single<CardTokenResponse>

    @GET("v1/bins/{bin_number}")
    fun getBinData(
        @Header("Authorization") clientKey: String?,
        @Path("bin_number") binNumber: String?
    ): Single<BinResponse>
}