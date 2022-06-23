package com.midtrans.sdk.corekit.internal.util

import org.junit.Assert.*
import org.junit.Test

internal class StringUtilTest{
    @Test
    internal fun testCheckIfContentNotNullShouldReturnFalse() {
        assertFalse(StringUtil.checkIfContentNotNull(null, null, null, ""))
    }
    @Test
    internal fun testCheckIfContentNotNullShouldReturnTrue() {
        assertTrue(StringUtil.checkIfContentNotNull(null, null, null, "sesuatu"))
    }
}