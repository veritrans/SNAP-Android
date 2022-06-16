package com.midtrans.sdk.corekit.internal.util

import android.util.Base64

internal fun ByteArray.toHexString() = this.joinToString("") { "%02x".format(it) }

internal fun ByteArray.toHexStringUpperCase() = this.toHexString().toUpperCase()

internal fun ByteArray.toAsciiString(): String {
    val stringBuilder = StringBuilder()

    for (byte in this)
        stringBuilder.append(byte.toChar())

    return stringBuilder.toString()
}

internal fun ByteArray.copyWithOffset(fromIndex: Int, offset: Int): ByteArray {
    return this.copyOfRange(fromIndex, fromIndex + offset)
}

internal fun ByteArray.toPanHexString() =
    this.toHexString()
        .toCharArray()
        .filter { it.isDigit() }
        .joinToString("")
        .trimStart('0')

internal fun ByteArray.toBase64EncodedString(): String {
    val encodedByte = Base64.encode(this, Base64.NO_WRAP)
    return String(encodedByte)
}

internal fun ByteArray?.orEmptyByteArray() =
    this ?: ByteArray(0)
