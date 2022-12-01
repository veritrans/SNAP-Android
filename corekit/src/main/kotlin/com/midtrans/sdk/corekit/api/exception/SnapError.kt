package com.midtrans.sdk.corekit.api.exception

import retrofit2.HttpException

open class SnapError(
    cause: Throwable? = null,
    message: String? = null
): Throwable(cause = cause, message = message) {

    fun getErrorInformation(): String {
        return message ?: cause?.javaClass?.name ?: this.javaClass.name
    }

    fun getHttpStatusCode(): Int? {
        return if (cause is HttpException) {
            val exception = cause as HttpException
            exception.code()
        } else {
            null
        }
    }

    companion object{
        // messages
        const val MESSAGE_ERROR_EMPTY_RESPONSE = "Failed to retrieve response from server"
        const val MESSAGE_ERROR_EMPTY_MERCHANT_URL = "Merchant base url is empty. Please set merchant base url on SDK"
        const val MESSAGE_ERROR_INVALID_DATA_SUPPLIED = "Invalid data supplied to SDK."
        const val MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER = "Failed to connect to server."
        const val MESSAGE_ERROR_ALREADY_RUNNING = "Failed to connect to server."
    }
}
