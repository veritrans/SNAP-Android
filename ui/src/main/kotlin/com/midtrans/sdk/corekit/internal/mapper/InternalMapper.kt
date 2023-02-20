package com.midtrans.sdk.corekit.internal.mapper

internal interface InternalMapper<in R> {
    fun mapInt(id: R): Int
}
