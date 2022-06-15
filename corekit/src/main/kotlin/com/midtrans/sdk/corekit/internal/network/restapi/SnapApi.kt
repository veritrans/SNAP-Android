package com.midtrans.sdk.corekit.internal.network.restapi

import com.midtrans.sdk.corekit.internal.network.model.request.BankTransferPaymentRequest
import com.midtrans.sdk.corekit.internal.network.model.response.Transaction
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import io.reactivex.Single
import retrofit2.Call
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
        @POST("v1/transactions/{snap_token}/pay")
        fun paymentUsingVa(
                @Path("snap_token") snapToken: String,
                @Body paymentRequest: BankTransferPaymentRequest?
        ): Single<TransactionResponse>

}