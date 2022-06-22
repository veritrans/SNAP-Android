package com.midtrans.sdk.corekit.internal.usecase

import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.internal.network.model.request.PaymentRequest
import com.midtrans.sdk.corekit.internal.network.model.request.CustomerDetailRequest
import com.midtrans.sdk.corekit.internal.data.repository.SnapRepository
import io.reactivex.Single

internal class BankTransferUsecase(
    private val snapRepository: SnapRepository
) {

    fun charge(snaptoken: String,
    @PaymentType.Def paymentType: String,
    email: String?): Single<TransactionResponse> {
        val bankTransferPaymentRequest = PaymentRequest(
            paymentType = paymentType,
            customerDetails = CustomerDetailRequest(email = email)
        )
        return snapRepository.chargeBankTransfer(snaptoken, bankTransferPaymentRequest)
    }

}