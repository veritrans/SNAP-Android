package com.midtrans.sdk.corekit.internal.util

import java.nio.ByteBuffer
import java.nio.ByteOrder

fun Int.toSignedByteArray(): ByteArray {
    val signedFourthByte = ((this shr 24) and 0x7F).toByte()
    val thirdByte = ((this shr 16)).toByte()
    val secondByte = ((this shr 8)).toByte()
    val firstByte = this.toByte()
    return byteArrayOf(signedFourthByte, thirdByte, secondByte, firstByte)
}

fun Int.toBytesLittleEndian(): ByteArray {
    return ByteBuffer
        .allocate(Integer.BYTES)
        .order(ByteOrder.LITTLE_ENDIAN)
        .putInt(this).array()
}

fun Int.toStringWithLeftZeroPadding(digit: Int): String{
    val numberString = this.toString()
    val strBuilder = StringBuilder()
    for(i in numberString.length until digit){
        strBuilder.append("0")
    }
    strBuilder.append(numberString)
    return strBuilder.toString()
}