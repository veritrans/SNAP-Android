package com.midtrans.sdk.corekit.api.requestbuilder.payment

import com.midtrans.sdk.corekit.internal.network.model.request.PaymentRequest

abstract class PaymentRequestBuilder {
    internal abstract fun build(): PaymentRequest
}
