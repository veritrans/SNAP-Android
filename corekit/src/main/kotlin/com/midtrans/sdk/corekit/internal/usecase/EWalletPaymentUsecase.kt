package com.midtrans.sdk.corekit.internal.usecase

import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.internal.data.repository.SnapRepository
import com.midtrans.sdk.corekit.internal.network.model.request.PaymentParam
import com.midtrans.sdk.corekit.internal.network.model.request.PaymentRequest
import io.reactivex.Single

internal class EWalletPaymentUsecase(
    val snapRepository: SnapRepository
) {
    fun charge(
        snaptoken: String,
        @PaymentType.Def paymentType: String
    ): Single<TransactionResponse> {
        val paymentRequest = when (paymentType) {
            PaymentType.SHOPEEPAY_QRIS -> {
                PaymentRequest(
                    paymentType = PaymentType.QRIS,
                    paymentParams = PaymentParam(listOf(PaymentType.SHOPEEPAY))
                )
            }
            else -> {
                PaymentRequest(paymentType = paymentType)
            }
        }
        return snapRepository.chargeBankTransfer(snaptoken, paymentRequest)
    }
}