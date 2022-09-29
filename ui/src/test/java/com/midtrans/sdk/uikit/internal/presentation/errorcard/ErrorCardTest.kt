package com.midtrans.sdk.uikit.internal.presentation.errorcard

import com.midtrans.sdk.corekit.api.exception.SnapError
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.HttpException

class ErrorCardTest {

    @Test
    fun getErrorCardTypeTimeoutFromBank() {
        val transactionResponse = TransactionResponse(statusCode = "503")
        Assert.assertEquals(
            ErrorCard.TIMEOUT_ERROR_DIALOG_FROM_BANK,
            ErrorCard.getErrorCardType(transactionResponse)
        )
    }

    @Test
    fun getErrorCardTypeTidMidError() {
        val transactionResponse = TransactionResponse(statusCode = "402")
        Assert.assertEquals(
            ErrorCard.TID_MID_ERROR_OTHER_PAY_METHOD_AVAILABLE,
            ErrorCard.getErrorCardType(transactionResponse)
        )
    }

    @Test
    fun getErrorCardTypeSystemError() {
        val transactionResponse = TransactionResponse(statusCode = "500")
        Assert.assertEquals(
            ErrorCard.SYSTEM_ERROR_DIALOG_ALLOW_RETRY,
            ErrorCard.getErrorCardType(transactionResponse, true)
        )
        Assert.assertEquals(
            ErrorCard.SYSTEM_ERROR_DIALOG_DISALLOW_RETRY,
            ErrorCard.getErrorCardType(transactionResponse, false)
        )
        val httpException: HttpException = mock()
        whenever(httpException.code()).thenReturn(500)
        Assert.assertEquals(
            ErrorCard.SYSTEM_ERROR_DIALOG_ALLOW_RETRY, ErrorCard.getErrorCardType(
                SnapError(httpException), true
            )
        )
        Assert.assertEquals(
            ErrorCard.SYSTEM_ERROR_DIALOG_DISALLOW_RETRY,
            ErrorCard.getErrorCardType(SnapError(httpException), false)
        )
    }

    @Test
    fun getErrorCardTypeDenied() {
        val transactionResponse =
            TransactionResponse(statusCode = "200", transactionStatus = "deny")
        Assert.assertEquals(
            ErrorCard.CARD_ERROR_DECLINED_DISALLOW_RETRY,
            ErrorCard.getErrorCardType(transactionResponse, true)
        )
    }
}