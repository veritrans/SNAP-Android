package com.midtrans.sdk.corekit.internal.data.repository

import com.midtrans.sdk.corekit.api.model.CardTokenResponse
import com.midtrans.sdk.corekit.internal.network.restapi.CoreApi
import io.reactivex.Single

internal class CardTokenRepository(private val coreApi: CoreApi) {

    fun getCardToken(param: Map<String,String>): Single<CardTokenResponse> {
        return coreApi.getToken(param)
    }
}