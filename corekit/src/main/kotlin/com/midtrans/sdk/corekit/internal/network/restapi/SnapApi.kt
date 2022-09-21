package com.midtrans.sdk.corekit.internal.network.restapi

import com.midtrans.sdk.corekit.api.model.BankPointResponse
import com.midtrans.sdk.corekit.api.model.DeleteSavedCardResponse
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.internal.network.model.request.PaymentRequest
import com.midtrans.sdk.corekit.internal.network.model.response.Transaction
import io.reactivex.Single
import retrofit2.http.*

internal interface SnapApi {

    @GET("v1/transactions/{snap_token}")
    fun getPaymentOption(@Path("snap_token") snapToken: String?): Single<Transaction>

    @POST("v1/transactions/{snap_token}/pay")
    fun pay(
        @Path("snap_token") snapToken: String,
        @Body paymentRequest: PaymentRequest
    ): Single<TransactionResponse>

    @DELETE("v1/transactions/{snap_token}/saved_tokens/{masked_card}")
    fun deleteSavedCard(
        @Path("snap_token") snapToken: String,
        @Path("masked_card") maskedCard: String
    ): Single<DeleteSavedCardResponse>

    @GET("v1/transactions/{snap_token}/point_inquiry/{card_token}")
    fun getBankPoint(
        @Path("snap_token") snapToken: String,
        @Path("card_token") cardToken: String,
        @Query("gross_amount") grossAmount: Double
    ): Single<BankPointResponse>

    @GET("v1/transactions/{snap_token}/status")
    fun getTransactionStatus(@Path("snap_token") snapToken: String?): Single<TransactionResponse>

}