package com.midtrans.sdk.uikit.internal.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BankTransfer2Presenter: ViewModel() {
    val timerLiveData: LiveData<String> = MutableLiveData()
    val companyCodeLiveData: LiveData<String> = MutableLiveData()
    val billingCodeLiveData: LiveData<String> = MutableLiveData()

    fun setup(

    ){}
}