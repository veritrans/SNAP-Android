package com.midtrans.sdk.corekit.internal.exception

internal class ApiError(private val error: Throwable) : Throwable(error) {

}
