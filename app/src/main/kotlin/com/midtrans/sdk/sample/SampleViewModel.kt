package com.midtrans.sdk.sample

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.midtrans.sdk.corekit.SnapCore

class SampleViewModel: ViewModel() {
    var helloLiveData = MutableLiveData<String>()
    private val coreKit: SnapCore = SnapCore.getInstance()!!

    fun getHelloFromSnap(){
        helloLiveData.value = coreKit.hello()
    }
}