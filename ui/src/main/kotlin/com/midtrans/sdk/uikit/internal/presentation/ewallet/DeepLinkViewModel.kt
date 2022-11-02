package com.midtrans.sdk.uikit.internal.presentation.ewallet

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.exception.SnapError
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.api.model.TransactionResult
import com.midtrans.sdk.uikit.internal.base.BaseViewModel
import javax.inject.Inject

internal class DeepLinkViewModel @Inject constructor(
    private val snapCore: SnapCore
) : BaseViewModel() {
    private val _checkStatusResultLiveData = MutableLiveData<TransactionResult>()
    val checkStatusResultLiveData: LiveData<TransactionResult> = _checkStatusResultLiveData

    fun checkStatus(snapToken: String) {
        snapCore.getTransactionStatus(
            snapToken = snapToken,
            callback = object : Callback<TransactionResponse> {
                override fun onSuccess(result: TransactionResponse) {
                    result.run {
                        _checkStatusResultLiveData.value =  TransactionResult(
                            status = transactionStatus.orEmpty(),
                            transactionId = transactionId.orEmpty(),
                            paymentType = paymentType.orEmpty()
                        )
                    }
                }

                override fun onError(error: SnapError) {
                    Log.e("Wallet payment status", error.javaClass.name)
                }
            }
        )
    }
}