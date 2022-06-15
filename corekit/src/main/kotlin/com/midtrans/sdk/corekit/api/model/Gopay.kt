package com.midtrans.sdk.corekit.api.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by ziahaqi on 10/13/16.
 */
class Gopay(@field:SerializedName("callback_url") private var merchantGopayDeeplink: String) : Serializable{
    @SerializedName("enable_callback")
    private  var  enableCallback = false
    fun getMerchantGopayDeeplink(): String {
        return merchantGopayDeeplink
    }
    fun setMerchantGopayDeeplink(merchantGopayDeeplink: String){
        this.merchantGopayDeeplink = merchantGopayDeeplink
        enableCallback = true
    }
    init {
        enableCallback = true
    }
}