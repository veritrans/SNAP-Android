package com.midtrans.sdk.corekit.api.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Shopeepay(@field:SerializedName("callback_url") private val callbackUrl: String) :
    Serializable