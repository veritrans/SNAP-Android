package com.midtrans.sdk.corekit.internal.usecase

import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.internal.data.repository.SnapRepository
import com.midtrans.sdk.corekit.internal.network.model.request.DirectDebitPaymentRequest
import com.midtrans.sdk.corekit.internal.network.model.request.PaymentRequestParam
import io.reactivex.Single

/**
 * currently handles KlikBCA, BCA Klikpay, Mandiri Clickpay, BRI Epay, CIMB Clicks, Danamon Online
 */
internal class DirectDebitUsecase(
    private val snapRepository: SnapRepository
) {
    fun charge(
        snaptoken: String,
        @PaymentType.Def paymentType: String,
        input3: String?,
        token: String?,
        tokenId: String?,
        userId: String?
    ): Single<TransactionResponse> {
        val paymentRequestParam = PaymentRequestParam(input3, token, tokenId, userId)
        val directDebitPaymentRequest = DirectDebitPaymentRequest(paymentType, paymentRequestParam)
        return snapRepository.chargeDirectDebit(snaptoken, directDebitPaymentRequest)
    }
}