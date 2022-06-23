package com.midtrans.sdk.corekit.api.requestbuilder

import com.midtrans.sdk.corekit.api.exception.InvalidPaymentTypeException
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.internal.network.model.request.CustomerDetailRequest
import com.midtrans.sdk.corekit.internal.network.model.request.PaymentParam
import com.midtrans.sdk.corekit.internal.network.model.request.PaymentRequest
import com.midtrans.sdk.corekit.internal.util.StringUtil.checkIfContentNotNull

class CreditCardPaymentRequestBuilder: PaymentRequestBuilder() {
    private lateinit var paymentType: String
    private lateinit var cardToken: String
    private var saveCard = false

    private var customerEmail: String? = null
    private var customerFullName: String? = null
    private var customerPhone: String? = null

    fun withPaymentType(@PaymentType.Def value: String): CreditCardPaymentRequestBuilder = apply {
        paymentType = value
    }

    fun withCustomerEmail(value: String): CreditCardPaymentRequestBuilder = apply {
        customerEmail = value
    }
    fun withCustomerFullName(value: String): CreditCardPaymentRequestBuilder = apply {
        customerFullName = value
    }
    fun withCustomerPhone(value: String): CreditCardPaymentRequestBuilder = apply {
        customerPhone = value
    }
    fun withCardToken(value: String): CreditCardPaymentRequestBuilder = apply {
        cardToken = value
    }
    fun withSaveCard(value: Boolean): CreditCardPaymentRequestBuilder = apply {
        saveCard = value
    }

    override fun build(): PaymentRequest {
        return when (paymentType) {
            PaymentType.CREDIT_CARD -> PaymentRequest(
                paymentType = paymentType,
                customerDetails = constructCustomerDetail(),
                paymentParams = PaymentParam(cardToken = cardToken, saveCard = saveCard)
            )
            else -> throw InvalidPaymentTypeException()
        }
    }
    private fun constructCustomerDetail(): CustomerDetailRequest?{
        return when{
            checkIfContentNotNull(customerEmail, customerFullName, customerPhone) -> CustomerDetailRequest(email = customerEmail, fullName = customerFullName, phone = customerPhone)
            else -> null
        }
    }
}