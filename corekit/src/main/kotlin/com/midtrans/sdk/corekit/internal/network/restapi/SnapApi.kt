package com.midtrans.sdk.corekit.internal.network.restapi

import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.internal.network.model.request.BankTransferPaymentRequest
import com.midtrans.sdk.corekit.internal.network.model.request.DirectDebitPaymentRequest
import com.midtrans.sdk.corekit.internal.network.model.response.Transaction
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

internal interface SnapApi {

    /**
     * Get snap transaction details using Snap Endpoint.
     *
     * @param snapToken snap token
     */
    @GET("v1/transactions/{snap_token}")
    fun getPaymentOption(@Path("snap_token") snapToken: String?): Single<Transaction>

    /**
     * Charge payment using bank transfer Virtual account.
     *
     * @param paymentRequest Payment Request Details.
     */
    //TODO: generalize all charge request for all payment type
    @POST("v1/transactions/{snap_token}/pay")
    fun paymentUsingVa(
        @Path("snap_token") snapToken: String,
        @Body paymentRequest: BankTransferPaymentRequest?
    ): Single<TransactionResponse>

    @POST("v1/transactions/{snap_token}/pay")
    fun paymentUsingDirectDebit(
        @Path("snap_token") snapToken: String,
        @Body paymentRequest: DirectDebitPaymentRequest
    ): Single<TransactionResponse>
}