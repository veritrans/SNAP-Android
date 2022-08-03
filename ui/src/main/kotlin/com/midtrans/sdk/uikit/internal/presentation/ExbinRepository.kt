package com.midtrans.sdk.uikit.internal.presentation

import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.exception.SnapError
import com.midtrans.sdk.corekit.api.model.BinResponse
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe

class ExbinRepository(
    val corekit: SnapCore,
    val binNumber: String,
    val clientKey: String
) {
    fun getBankName(bin: String): Single<String> {
        val result = Single.create( SingleOnSubscribe<String>

        //TODO: Fix when corekit available
        { emitter -> emitter.onSuccess("bri")
            corekit.getBinData(
                binNumber = binNumber,
                clientKey = clientKey,
                callback = object : Callback<BinResponse> {
                    override fun onSuccess(result: BinResponse) {
                        result.data?.bank?.let {
                            emitter.onSuccess(it)
                        }
                    }
                    override fun onError(error: SnapError) {
                        emitter.onError(error)
                    }
                }
            )
        })
        return result
    }
}