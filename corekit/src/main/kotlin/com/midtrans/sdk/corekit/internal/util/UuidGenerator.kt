package com.midtrans.sdk.corekit.internal.util

import com.fasterxml.uuid.Generators
import java.util.*

internal class UuidGenerator {
    fun generateTimeBasedUuid(): String {
        return Generators.timeBasedGenerator().generate().toString()
    }

    fun generateRandomUuid(): String {
        return UUID.randomUUID().toString()
    }
}