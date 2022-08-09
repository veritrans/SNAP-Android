package com.midtrans.sdk.uikit.internal.presentation.creditcard

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.exception.SnapError
import com.midtrans.sdk.corekit.api.model.BinResponse
import javax.inject.Inject

//class CreditCardViewModel(
//    val exbinRepository: ExbinRepository
//): ViewModel() {
//    val bankIconLiveData = MutableLiveData<String>()
//
//    @SuppressLint("CheckResult")
//    fun updateBankIcon(binNumber: String){
//        exbinRepository.getBankName(binNumber)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                bankIconLiveData.value = it
//            },
//                {}
//            )
//    }
//}

class CreditCardViewModel @Inject constructor(
    private val snapCore: SnapCore
) : ViewModel()  {
    val bankNameLiveData = MutableLiveData<String>()

    @SuppressLint("CheckResult")
    fun getBankName(binNumber: String,
                    clientKet: String) {
        snapCore.getBinData(
            binNumber = binNumber,
            clientKey = "VT-client-yrHf-c8Sxr-ck8tx",
            callback = object : Callback<BinResponse> {
                override fun onSuccess(result: BinResponse) {
                    result.run {
                        data?.bank.let { bankNameLiveData.value = it }
                    }
                }

                override fun onError(error: SnapError) {
                    TODO("Not yet implemented")
                }
            }
        )
    }
}
