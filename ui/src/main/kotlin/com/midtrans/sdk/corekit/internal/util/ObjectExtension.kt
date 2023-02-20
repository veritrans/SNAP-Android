package com.gojek.offline.payment.sdk.common.util

import com.google.gson.Gson

fun Any.toJson(): String {
    return Gson().toJson(this)
}
