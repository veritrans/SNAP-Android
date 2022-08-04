package com.midtrans.sdk.corekit.internal.usecase

import android.annotation.SuppressLint
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.exception.SnapError
import com.midtrans.sdk.corekit.api.model.*
import com.midtrans.sdk.corekit.api.requestbuilder.snaptoken.SnapTokenRequestBuilder
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
        builder: SnapTokenRequestBuilder,
        callback: Callback<String>
    ) {
        val request = builder.build()

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