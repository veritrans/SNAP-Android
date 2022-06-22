package com.midtrans.sdk.corekit.internal.network.restapi

import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.internal.network.model.request.PaymentRequest
import com.midtrans.sdk.corekit.internal.network.model.response.Transaction
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

internal interface SnapApi {

    @GET("v1/transactions/{snap_token}")
    fun getPaymentOption(@Path("snap_token") snapToken: String?): Single<Transaction>

    @POST("v1/transactions/{snap_token}/pay")
    fun pay(
        @Path("snap_token") snapToken: String,
        @Body paymentRequest: PaymentRequest
    ): Single<TransactionResponse>
}