package com.midtrans.sdk.uikit.internal.util

import java.util.*

interface TimeProvider {
    fun getDate(): Date
    fun getTimeZone(): String
}