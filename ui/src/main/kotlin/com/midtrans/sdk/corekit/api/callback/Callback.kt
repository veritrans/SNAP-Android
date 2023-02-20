package com.midtrans.sdk.corekit.api.callback

import com.midtrans.sdk.corekit.api.exception.SnapError

open interface Callback<T> {
    fun onSuccess(result: T)

    fun onError(error: SnapError)
}
