package com.midtrans.sdk.corekit.internal.data.repository

import android.util.Base64
import com.midtrans.sdk.corekit.api.model.BinResponse
import com.midtrans.sdk.corekit.api.model.CardTokenResponse
import com.midtrans.sdk.corekit.internal.network.restapi.CoreApi
import io.reactivex.Single

internal class CoreApiRepository(private val coreApi: CoreApi) {

    fun getCardToken(param: Map<String,String>): Single<CardTokenResponse> {
        return coreApi.getCardToken(param)
    }

    fun getBinData(binNumber: String, clientKey: String): Single<BinResponse> {
        return coreApi.getBinData("Basic " + Base64.encodeToString(clientKey.toByteArray(), Base64.NO_WRAP), binNumber)
    }
}