package com.midtrans.sdk.corekit.internal.util

import android.util.Base64
import java.nio.charset.Charset

fun String.hexString2Bytes(): ByteArray {
    var data = this
    val result = ByteArray((data.length + 1) / 2)
    if (data.length and 0x1 == 1) {
        data = "0$data"
    }
    for (i in result.indices) {
        result[i] = (hex2byte(data[i * 2 + 1]) or (hex2byte(data[i * 2]) shl 4)).toByte()
    }
    return result
}

fun hex2byte(hex: Char): Int {
    if (hex in 'a'..'f') {
        return (hex - 'a' + 10)
    }

    if (hex in 'A'..'F') {
        return (hex - 'A' + 10)
    }

    return if (hex in '0'..'9') {
        (hex - '0')
    }
    else {
        0
    }
}

fun String.hexString2Byte() : Byte {
    var resultByte : Byte = 0
    forEach {currentChar ->
        resultByte = (resultByte * 10 + hex2byte(currentChar)).toByte()
    }

    return resultByte
}

fun String.toBase64DecodedByteArray(): ByteArray {
    return Base64.decode(this, Base64.DEFAULT)
}

fun String.toAsciiByteArray(): ByteArray = this.toByteArray(Charsets.ISO_8859_1)

fun String.toGbkByteArray(): ByteArray = toByteArray(Charset.forName("GBK"))
