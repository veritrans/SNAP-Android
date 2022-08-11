package com.midtrans.sdk.corekit.internal.data.repository

import com.midtrans.sdk.corekit.internal.network.model.request.SnapTokenRequest
import com.midtrans.sdk.corekit.internal.network.model.response.SnapTokenResponse
import com.midtrans.sdk.corekit.internal.network.restapi.MerchantApi
import io.reactivex.Single

internal class MerchantApiRepository(private val merchantApi: MerchantApi) {
    fun getSnapToken(request: SnapTokenRequest): Single<SnapTokenResponse> {
        return merchantApi.getSnapToken(request)
    }
}