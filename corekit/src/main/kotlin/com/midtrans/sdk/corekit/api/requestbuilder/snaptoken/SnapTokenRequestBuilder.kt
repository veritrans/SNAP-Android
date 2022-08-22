package com.midtrans.sdk.corekit.api.requestbuilder.snaptoken

import com.midtrans.sdk.corekit.api.exception.MissingParameterException
import com.midtrans.sdk.corekit.api.model.CreditCard
import com.midtrans.sdk.corekit.api.model.CustomerDetails
import com.midtrans.sdk.corekit.api.model.Expiry
import com.midtrans.sdk.corekit.api.model.GopayPaymentCallback
import com.midtrans.sdk.corekit.api.model.ItemDetails
import com.midtrans.sdk.corekit.api.model.PaymentCallback
import com.midtrans.sdk.corekit.api.model.PromoRequest
import com.midtrans.sdk.corekit.api.model.SnapTransactionDetail
import com.midtrans.sdk.corekit.internal.network.model.request.BankTransferRequest
import com.midtrans.sdk.corekit.internal.network.model.request.SnapTokenRequest

class SnapTokenRequestBuilder {
    private var customerDetails: CustomerDetails? = null
    private var itemDetails: List<ItemDetails>? = null
    private lateinit var transactionDetails: SnapTransactionDetail
    private var creditCard: CreditCard? = null
    private var userId: String? = null
    private var permataVa: BankTransferRequest? = null
    private var bcaVa: BankTransferRequest? = null
    private var bniVa: BankTransferRequest? = null
    private var briVa: BankTransferRequest? = null
    private var enabledPayments: List<String>? = null
    private var expiry: Expiry? = null
    private var promoRequest: PromoRequest? = null
    private var customField1: String? = null
    private var customField2: String? = null
    private var customField3: String? = null
    private var gopayCallback: GopayPaymentCallback? = null
    private var shopeepayCallback: PaymentCallback? = null
    private var uobEzpayCallback: PaymentCallback? = null
    
    fun withCustomerDetails(value: CustomerDetails?) = apply { customerDetails = value }
    fun withItemDetails(value: List<ItemDetails>?) = apply { itemDetails = value }
    fun withTransactionDetails(value: SnapTransactionDetail) = apply { transactionDetails = value }
    fun withCreditCard(value: CreditCard?) = apply { creditCard = value }
    fun withUserId(value: String?) = apply { userId = value }
    fun withPermataVa(value: BankTransferRequest?) = apply { permataVa = value }
    fun withBcaVa(value: BankTransferRequest?) = apply { bcaVa = value }
    fun withBniVa(value: BankTransferRequest?) = apply { bniVa = value }
    fun withBriVa(value: BankTransferRequest?) = apply { briVa = value }
    fun withEnabledPayments(value: List<String>?) = apply { enabledPayments = value }
    fun withExpiry(value: Expiry?) = apply { expiry = value }
    fun withPromo(value: PromoRequest?) = apply { promoRequest = value }
    fun withCustomField1(value: String?) = apply { customField1 = value }
    fun withCustomField2(value: String?) = apply { customField2 = value }
    fun withCustomField3(value: String?) = apply { customField3 = value }
    fun withGopayCallback(value: GopayPaymentCallback?) = apply { gopayCallback = value }
    fun withShopeepayCallback(value: PaymentCallback?) = apply { shopeepayCallback = value }
    fun withUobEzpayCallback(value: PaymentCallback?) = apply { uobEzpayCallback = value }

    internal fun build(): SnapTokenRequest {
        if (!::transactionDetails.isInitialized) {
            throw MissingParameterException("Transaction detail is required")
        }

        return SnapTokenRequest(
            customerDetails = customerDetails,
            itemDetails = itemDetails,
            transactionDetails = transactionDetails,
            creditCard = creditCard,
            userId = userId,
            permataVa = permataVa,
            bcaVa = bcaVa,
            bniVa = bniVa,
            briVa = briVa,
            enabledPayments = enabledPayments,
            expiry = expiry,
            promoRequest = promoRequest,
            customField1 = customField1,
            customField2 = customField2,
            customField3 = customField3,
            gopay = gopayCallback,
            shopeepay = shopeepayCallback,
            uobEzpay = uobEzpayCallback
        )
    }
}