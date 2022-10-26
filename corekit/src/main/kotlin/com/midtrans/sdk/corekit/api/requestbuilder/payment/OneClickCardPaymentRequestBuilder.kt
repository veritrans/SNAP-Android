package com.midtrans.sdk.corekit.api.requestbuilder.payment

import com.midtrans.sdk.corekit.api.exception.InvalidPaymentTypeException
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.internal.network.model.request.CustomerDetailRequest
import com.midtrans.sdk.corekit.internal.network.model.request.PaymentParam
import com.midtrans.sdk.corekit.internal.network.model.request.PaymentRequest
import com.midtrans.sdk.corekit.internal.network.model.request.PromoDetailRequest
import com.midtrans.sdk.corekit.internal.util.NumberUtil
import com.midtrans.sdk.corekit.internal.util.StringUtil

class OneClickCardPaymentRequestBuilder: PaymentRequestBuilder() {
    private lateinit var paymentType: String
    private lateinit var maskedCard: String
    private var customerEmail: String? = null
    private var customerFullName: String? = null
    private var customerPhone: String? = null
    private var promoDetailRequest: PromoDetailRequest? = null
    private var installment: String? = null

    fun withPaymentType(@PaymentType.Def value: String): OneClickCardPaymentRequestBuilder = apply {
        paymentType = value
    }

    fun withCustomerEmail(value: String): OneClickCardPaymentRequestBuilder = apply {
        customerEmail = value
    }

    fun withCustomerFullName(value: String): OneClickCardPaymentRequestBuilder = apply {
        customerFullName = value
    }

    fun withCustomerPhone(value: String): OneClickCardPaymentRequestBuilder = apply {
        customerPhone = value
    }

    fun withPromo(discountedGrossAmount: Double, promoId: String): OneClickCardPaymentRequestBuilder = apply {
        promoDetailRequest = PromoDetailRequest(
            discountedGrossAmount = discountedGrossAmount?.let { NumberUtil.formatDoubleToString(it) },
            promoId = promoId)
    }

    fun withInstallment(value: String): OneClickCardPaymentRequestBuilder = apply {
        if (value.isNotEmpty()) {
            installment = value
        }
    }

    fun withMaskedCard(value: String): OneClickCardPaymentRequestBuilder = apply {
        maskedCard = value
    }

    override fun build(): PaymentRequest {
        return when (paymentType) {
            PaymentType.CREDIT_CARD -> PaymentRequest(
                paymentType = paymentType,
                customerDetails = constructCustomerDetail(),
                paymentParams = PaymentParam(maskedCard = maskedCard, installment = installment),
                promoDetails = promoDetailRequest
            )
            else -> throw InvalidPaymentTypeException()
        }
    }
    private fun constructCustomerDetail(): CustomerDetailRequest?{
        return when{
            StringUtil.checkIfContentNotNull(customerEmail, customerFullName, customerPhone) -> CustomerDetailRequest(email = customerEmail, fullName = customerFullName, phone = customerPhone)
            else -> null
        }
    }
}