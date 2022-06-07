package com.midtrans.sdk.corekit.internal.util

import java.util.*

interface TimeProvider {
    fun getDate(): Date
    fun getTimeZone(): String
}