package com.midtrans.sdk.corekit.internal.data.repository

import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.internal.network.model.request.BankTransferPaymentRequest
import com.midtrans.sdk.corekit.internal.network.restapi.SnapApi
import io.reactivex.Single

internal class SnapRepository(private val snapApi: SnapApi): BaseRepository() {

    fun chargeBankTransfer(
        snapToken: String,
        request: BankTransferPaymentRequest
    ): Single<TransactionResponse> {
       return snapApi.paymentUsingVa(snapToken, request)
    }
}