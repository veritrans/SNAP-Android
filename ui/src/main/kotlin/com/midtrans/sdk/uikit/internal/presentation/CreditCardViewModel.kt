package com.midtrans.sdk.uikit.internal.presentation

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CreditCardViewModel(
    val exbinRepository: ExbinRepository
): ViewModel() {
    val bankIconLiveData = MutableLiveData<String>()

    @SuppressLint("CheckResult")
    fun updateBankIcon(binNumber: String){
        exbinRepository.getBankName(binNumber)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                bankIconLiveData.value = it
            },
                {}
            )
    }
}
