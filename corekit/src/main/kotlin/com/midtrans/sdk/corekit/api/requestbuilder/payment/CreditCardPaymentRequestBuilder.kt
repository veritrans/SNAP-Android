package com.midtrans.sdk.corekit.api.requestbuilder.payment

import com.midtrans.sdk.corekit.api.exception.InvalidPaymentTypeException
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.internal.network.model.request.CustomerDetailRequest
import com.midtrans.sdk.corekit.internal.network.model.request.PaymentParam
import com.midtrans.sdk.corekit.internal.network.model.request.PaymentRequest
import com.midtrans.sdk.corekit.internal.network.model.request.PromoDetailRequest
import com.midtrans.sdk.corekit.internal.util.NumberUtil
import com.midtrans.sdk.corekit.internal.util.StringUtil.checkIfContentNotNull

class CreditCardPaymentRequestBuilder: PaymentRequestBuilder() {
    private lateinit var paymentType: String
    private lateinit var cardToken: String
    private var saveCard = false
    private var customerEmail: String? = null
    private var customerFullName: String? = null
    private var customerPhone: String? = null
    private var discountedGrossAmount: Double? = null
    private var promoId: String? = null
    private var bank: String? = null
    private var point: Double? = null
    private var installment: String? = null

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

    fun withDiscountedGrossAmount(value: Double): CreditCardPaymentRequestBuilder = apply {
        discountedGrossAmount = value
    }

    fun withPromoId(value: String): CreditCardPaymentRequestBuilder = apply {
        promoId = value
    }

    fun withBank(value: String): CreditCardPaymentRequestBuilder = apply {
        bank = value
    }

    fun withPoint(value: Double): CreditCardPaymentRequestBuilder = apply {
        point = value
    }

    fun withInstallment(value: String): CreditCardPaymentRequestBuilder = apply {
        installment = value
    }

    override fun build(): PaymentRequest {
        return when (paymentType) {
            PaymentType.CREDIT_CARD -> PaymentRequest(
                paymentType = paymentType,
                customerDetails = constructCustomerDetail(),
                paymentParams = PaymentParam(cardToken = cardToken, saveCard = saveCard, bank = bank, point = point, installment = installment),
                promoDetails = PromoDetailRequest(discountedGrossAmount = discountedGrossAmount?.let { NumberUtil.formatDoubleToString(it)},
                    promoId = promoId)
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