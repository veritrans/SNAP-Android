package com.midtrans.sdk.corekit.api.exception;

class CallbackUnimplementedException : SnapError() {
    override val message: String?
        get() = MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED

    companion object {
        const val MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED = "Callback Unimplemented"
    }
}
