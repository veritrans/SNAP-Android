package com.midtrans.sdk.corekit.internal.data.repository

import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.internal.network.model.request.PaymentRequest
import com.midtrans.sdk.corekit.internal.network.restapi.SnapApi
import io.reactivex.Single

internal class SnapRepository(private val snapApi: SnapApi): BaseRepository() {

    fun pay(snapToken: String, paymentRequest: PaymentRequest): Single<TransactionResponse> {
        return snapApi.pay(snapToken, paymentRequest)
    }
}