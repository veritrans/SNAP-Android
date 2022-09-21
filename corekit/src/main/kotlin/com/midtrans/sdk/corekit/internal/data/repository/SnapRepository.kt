package com.midtrans.sdk.corekit.internal.data.repository

import com.midtrans.sdk.corekit.api.model.BankPointResponse
import com.midtrans.sdk.corekit.api.model.DeleteSavedCardResponse
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.internal.network.model.request.PaymentRequest
import com.midtrans.sdk.corekit.internal.network.model.response.Transaction
import com.midtrans.sdk.corekit.internal.network.restapi.SnapApi
import io.reactivex.Single

internal class SnapRepository(private val snapApi: SnapApi): BaseRepository() {

    fun pay(snapToken: String, paymentRequest: PaymentRequest): Single<TransactionResponse> {
        return snapApi.pay(snapToken, paymentRequest)
    }
    fun deleteSavedCard(snapToken: String, maskedCard: String): Single<DeleteSavedCardResponse> {
        return  snapApi.deleteSavedCard(snapToken, maskedCard)
    }
    fun getBankPoint(
        snapToken: String,
        cardToken: String,
        grossAmount: Double
    ): Single<BankPointResponse> {
        return snapApi.getBankPoint(snapToken, cardToken, grossAmount)
    }
    fun getTransactionDetail(snapToken: String): Single<Transaction> {
        return snapApi.getPaymentOption(snapToken)
    }
    fun getTransactionStatus(snapToken: String): Single<TransactionResponse> {
        return snapApi.getTransactionStatus(snapToken)
    }
}