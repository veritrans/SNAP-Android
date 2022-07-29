package com.midtrans.sdk.corekit.internal.usecase

import android.annotation.SuppressLint
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.exception.SnapError
import com.midtrans.sdk.corekit.api.model.*
import com.midtrans.sdk.corekit.internal.data.repository.MerchantApiRepository
import com.midtrans.sdk.corekit.internal.network.model.request.BankTransferRequest
import com.midtrans.sdk.corekit.internal.network.model.request.SnapTokenRequest
import com.midtrans.sdk.corekit.internal.scheduler.SdkScheduler

internal class GetSnapToken(
    private val scheduler: SdkScheduler,
    private val merchantApiRepository: MerchantApiRepository
) {
    @SuppressLint("CheckResult")
    fun getSnapToken(
        customerDetails: CustomerDetails? = null,
        itemDetails: List<ItemDetails>? = null,
        transactionDetails: SnapTransactionDetail? = null,
        creditCard: CreditCard? = null,
        userId: String? = null,
        permataVa: BankTransferRequest? = null,
        bcaVa: BankTransferRequest? = null,
        bniVa: BankTransferRequest? = null,
        briVa: BankTransferRequest? = null,
        enabledPayments: List<String>? = null,
        expiry: Expiry? = null,
        promo: Promo? = null,
        customField1: String? = null,
        customField2: String? = null,
        customField3: String? = null,
        gopay: PaymentCallback? = null,
        shopeepay: PaymentCallback? = null,
        uobEzpay: PaymentCallback? = null,
        callback: Callback<String>
    ) {
        val request = SnapTokenRequest(
            customerDetails,
            itemDetails,
            transactionDetails,
            creditCard,
            userId,
            permataVa,
            bcaVa,
            bniVa,
            briVa,
            enabledPayments,
            expiry,
            promo,
            customField1,
            customField2,
            customField3,
            gopay,
            shopeepay,
            uobEzpay
        )

        merchantApiRepository
            .getSnapToken(request)
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())
            .subscribe(
                {
                    callback.onSuccess(it.tokenId)
                },
                {
                    if (it is SnapError) {
                        callback.onError(it)
                    } else {
                        callback.onError(
                            SnapError(
                                cause = it,
                                message = "Failed on getting snap token"
                            )
                        )
                    }
                }
            )
    }
}