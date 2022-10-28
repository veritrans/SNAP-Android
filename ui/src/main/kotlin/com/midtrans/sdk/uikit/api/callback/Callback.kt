package com.midtrans.sdk.uikit.api.callback

import com.midtrans.sdk.uikit.api.exception.SnapError

interface Callback<T> {
    fun onSuccess(result: T)
    fun onError(error: SnapError)
}