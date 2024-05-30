package com.midtrans.sdk.corekit.internal.usecase

import android.annotation.SuppressLint
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.exception.SnapError
import com.midtrans.sdk.corekit.api.exception.SnapError.Companion.MESSAGE_ERROR_HTTP_404
import com.midtrans.sdk.corekit.api.exception.SnapError.Companion.MESSAGE_ERROR_HTTP_406
import com.midtrans.sdk.corekit.api.exception.SnapError.Companion.MESSAGE_ERROR_HTTP_407
import com.midtrans.sdk.corekit.api.exception.SnapError.Companion.MESSAGE_ERROR_HTTP_408
import com.midtrans.sdk.corekit.api.exception.SnapError.Companion.MESSAGE_ERROR_HTTP_409
import com.midtrans.sdk.corekit.api.model.*
import com.midtrans.sdk.corekit.api.model.SavedToken.Companion.ONE_CLICK
import com.midtrans.sdk.corekit.api.model.SavedToken.Companion.TWO_CLICKS
import com.midtrans.sdk.corekit.api.requestbuilder.cardtoken.CreditCardTokenRequestBuilder
import com.midtrans.sdk.corekit.api.requestbuilder.payment.PaymentRequestBuilder
import com.midtrans.sdk.corekit.api.requestbuilder.snaptoken.SnapTokenRequestBuilder
import com.midtrans.sdk.corekit.core.Logger
import com.midtrans.sdk.corekit.internal.analytics.EventAnalytics
import com.midtrans.sdk.corekit.internal.constant.CommonConstant
import com.midtrans.sdk.corekit.internal.data.repository.CoreApiRepository
import com.midtrans.sdk.corekit.internal.data.repository.MerchantApiRepository
import com.midtrans.sdk.corekit.internal.data.repository.SnapRepository
import com.midtrans.sdk.corekit.internal.network.model.request.SnapTokenRequest
import com.midtrans.sdk.corekit.internal.network.model.response.EnabledPayment
import com.midtrans.sdk.corekit.internal.network.model.response.Transaction
import com.midtrans.sdk.corekit.internal.scheduler.BaseSdkScheduler
import io.reactivex.Single
import retrofit2.HttpException

internal class PaymentUsecase(
    private val scheduler: BaseSdkScheduler,
    private val snapRepository: SnapRepository,
    private val coreApiRepository: CoreApiRepository,
    private val merchantApiRepository: MerchantApiRepository,
    private val clientKey: String,
    private val eventAnalytics: EventAnalytics
) {
    private var promoDetails = PromoDetails()
    @SuppressLint("CheckResult")
    fun getPaymentOption(
        snapToken: String?,
        requestBuilder: SnapTokenRequestBuilder,
        callback: Callback<PaymentOption>
    ) {
        val snapTokenRequest = requestBuilder.build()

        val isUserSet = setAnalyticsUserIdentityWithCustomerDetail(snapToken, snapTokenRequest)
        if (snapToken.isNullOrBlank()) {
            val requestTime = System.currentTimeMillis()

            merchantApiRepository
                .also { eventAnalytics.trackSnapGetTokenRequest("") }
                .getSnapToken(snapTokenRequest)
                .onErrorResumeNext {
                    Single.error(
                        SnapError(
                            cause = it,
                            message = "Failed on getting snap token"
                        )
                    )
                }
                .flatMap { response ->
                    snapRepository
                        .getTransactionDetail(response.token.orEmpty())
                        .map (setAnalyticsUserIdentityWithSnapToken(isUserSet))
                        .map (trackCommonTransactionProperties(response.redirectUrl))
                        .map (trackCommonCustomerProperties())
                        .map (trackCommonCreditCardProperties())
                        .map { Pair(response.token, it) }
                }
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(
                    { data ->
                        val token = data.first
                        val responseData = data.second
                        val methods = mutableListOf<PaymentMethod>()
                        responseData.enabledPayments?.forEach {
                            addPaymentMethod(it, methods)
                        }
                        val responseTime = System.currentTimeMillis() - requestTime
                        eventAnalytics.trackSnapGetTokenResult(token.orEmpty(), responseTime.toString())
                        if (responseData?.featureTypes?.contains(CommonConstant.PROMO) == true) {
                            snapRepository.getPromo(token.orEmpty()).subscribeOn(scheduler.io())
                                .observeOn(scheduler.ui())
                                .doFinally{
                                    callback.onSuccess(
                                        PaymentOption(
                                            token = token.orEmpty(),
                                            options = methods,
                                            creditCard = responseData.creditCard,
                                            promos = promoDetails.promos,
                                            merchantData = responseData.merchant,
                                            customerDetails = responseData.customerDetails,
                                            transactionDetails = responseData.transactionDetails,
                                            expiryTme = responseData.expiryTime,
                                            enabledPayment = responseData.enabledPayments,
                                            result = responseData.result
                                        )
                                    )
                                }
                                .subscribe(
                                    {
                                        promoDetails = it
                                    },
                                    {
                                        Logger.d("Failed on getting promo")
                                    })
                        } else {
                            callback.onSuccess(
                                PaymentOption(
                                    token = token.orEmpty(),
                                    options = methods,
                                    creditCard = responseData.creditCard,
                                    promos = promoDetails.promos,
                                    merchantData = responseData.merchant,
                                    customerDetails = responseData.customerDetails,
                                    transactionDetails = responseData.transactionDetails,
                                    expiryTme = responseData.expiryTime,
                                    enabledPayment = responseData.enabledPayments,
                                    result = responseData.result
                                )
                            )
                        }
                    },
                    {
                        deliverError(it, callback)
                    }
                )
        } else {
            snapRepository.getTransactionDetail(snapToken)
                .map (setAnalyticsUserIdentityWithSnapToken(isUserSet))
                .map (trackCommonTransactionProperties(null))
                .map (trackCommonCustomerProperties())
                .map (trackCommonCreditCardProperties())
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(
                    { responseData ->
                        val methods = mutableListOf<PaymentMethod>()
                        responseData.enabledPayments?.forEach {
                            addPaymentMethod(it, methods)
                        }
                        if (responseData?.featureTypes?.contains(CommonConstant.PROMO) == true) {
                            snapRepository.getPromo(snapToken).subscribeOn(scheduler.io())
                                .observeOn(scheduler.ui())
                                .doFinally {
                                    callback.onSuccess(
                                        PaymentOption(
                                            token = snapToken,
                                            options = methods,
                                            creditCard = responseData.creditCard,
                                            promos = promoDetails.promos,
                                            merchantData = responseData.merchant,
                                            customerDetails = responseData.customerDetails,
                                            transactionDetails = responseData.transactionDetails,
                                            expiryTme = responseData.expiryTime,
                                            enabledPayment = responseData.enabledPayments,
                                            result = responseData.result
                                        )
                                    )
                                }
                                .subscribe(
                                    {
                                        promoDetails = it
                                    },
                                    {
                                        Logger.d("Failed on getting promo")
                                    })
                        } else {
                            callback.onSuccess(
                                PaymentOption(
                                    token = snapToken,
                                    options = methods,
                                    creditCard = responseData.creditCard,
                                    promos = promoDetails.promos,
                                    merchantData = responseData.merchant,
                                    customerDetails = responseData.customerDetails,
                                    transactionDetails = responseData.transactionDetails,
                                    expiryTme = responseData.expiryTime,
                                    enabledPayment = responseData.enabledPayments,
                                    result = responseData.result
                                )
                            )
                        }
                    },
                    {
                        deliverError(it, callback)
                    }
                )
        }
    }

    private fun setAnalyticsUserIdentityWithCustomerDetail(snapToken: String?, request: SnapTokenRequest): Boolean {
        var isUserSet = false
        val customerName = request.customerDetails?.let { "${it.firstName} ${it.lastName}" }
        val customerPhone = request.customerDetails?.phone
        val customerEmail = request.customerDetails?.email
        val id = when {
            !customerEmail.isNullOrEmpty() -> customerEmail
            !customerPhone.isNullOrEmpty() -> customerPhone
            else -> snapToken
        }
        id?.let {
            eventAnalytics.setUserIdentity(
                userId = it,
                userName = customerName.orEmpty()
            )
            isUserSet = true
        }
        return isUserSet
    }

    private fun setAnalyticsUserIdentityWithSnapToken(isUserSet: Boolean): (Transaction) -> Transaction {
        return { transaction ->
            if (!isUserSet) {
                val snapToken = transaction.token.orEmpty()
                val customerName = transaction.customerDetails?.let { "${it.firstName} ${it.lastName}" }
                eventAnalytics.setUserIdentity(
                    userId = snapToken,
                    userName = customerName.orEmpty(),
                )
            }
            transaction
        }
    }

    private fun trackCommonTransactionProperties(snapRedirectUrl: String?): (Transaction) -> Transaction {
        return { transaction ->
            transaction.apply {
                eventAnalytics.registerCommonTransactionProperties(
                    snapToken = token.orEmpty(),
                    orderId = transactionDetails?.orderId.orEmpty(),
                    grossAmount = transactionDetails?.grossAmount.toString(),
                    merchantId = merchant?.merchantId.orEmpty(),
                    merchantName = merchant?.preference?.displayName.orEmpty(),
                    colourSchema = merchant?.preference?.colorScheme.orEmpty(),
                    enabledPayments = enabledPayments?.map { it.type }?.toString().orEmpty(),
                    enabledPaymentsLength = enabledPayments?.size?.toString().orEmpty(),
                    snapRedirectUrl = snapRedirectUrl,
                    allowRetry = merchant?.allowRetry?.toString(),
                    otherVaProcessor = merchant?.preference?.otherVaProcessor
                )
            }
        }
    }

    private fun trackCommonCustomerProperties(): (Transaction) -> Transaction {
        return { transaction ->
            transaction.apply {
                val totalItems = itemDetails?.size
                val totalQuantity = itemDetails?.sumOf { it.quantity }
                val name = customerDetails?.let { "${it.firstName} ${it.lastName}" }
                val cityAndPostCode = getCustomerCityAndPostCode(
                    billingAddress = customerDetails?.billingAddress,
                    shippingAddress = customerDetails?.shippingAddress
                )

                eventAnalytics.registerCommonCustomerProperties(
                    customerName = name,
                    customerEmail = customerDetails?.email,
                    customerPhoneNumber = customerDetails?.phone,
                    customerCity = cityAndPostCode.first,
                    customerPostCode = cityAndPostCode.second,
                    totalItems = totalItems?.toString(),
                    totalQuantity = totalQuantity?.toString()
                )
            }
        }
    }

    private fun trackCommonCreditCardProperties(): (Transaction) -> Transaction {
        return { transaction ->
            transaction.apply {
                val isOneClickTokenAvailable =
                    creditCard?.savedTokens?.any { it.containsType(ONE_CLICK) }
                val isTwoClickTokenAvailable =
                    creditCard?.savedTokens?.any { it.containsType(TWO_CLICKS) }

                eventAnalytics.registerCommonCreditCardProperties(
                    cardOneClickTokenAvailable = isOneClickTokenAvailable?.toString(),
                    cardTwoClickTokenAvailable = isTwoClickTokenAvailable?.toString(),
                    priorityCardFeature = merchant?.priorityCardFeature,
                    savedTokens = creditCard?.savedTokens?.size?.toString(),
                    promoEnabled = promoDetails?.promos?.isNotEmpty()?.toString(),
                    secure = creditCard?.secure?.toString(),
                    saveCard = creditCard?.saveCard?.toString(),
                    blacklistedBins = creditCard?.blacklistBins?.toString(),
                    allowlistedBins = creditCard?.whitelistBins?.toString(),
                    installmentTerms = creditCard?.installment?.terms?.values?.toString(),
                    installmentBank = creditCard?.installment?.terms?.keys?.toString(),
                    installmentRequired = creditCard?.installment?.isRequired?.toString()
                )
            }
        }
    }

    private fun getCustomerCityAndPostCode(
        billingAddress: Address?,
        shippingAddress: Address?
    ): Pair<String?, String?> {
        val billingCity = billingAddress?.city
        val billingPostCode = billingAddress?.postalCode

        return if (billingCity.isNullOrEmpty() && billingPostCode.isNullOrEmpty()) {
            Pair(shippingAddress?.city, shippingAddress?.postalCode)
        } else {
            Pair(billingCity, billingPostCode)
        }
    }

    private fun addPaymentMethod(payment: EnabledPayment, methods: MutableList<PaymentMethod>) {
        if (payment.status == EnabledPayment.STATUS_DOWN)
            return

        if (payment.category == PaymentType.BANK_TRANSFER) {
            val index = methods.indexOfFirst { method ->
                method.type == PaymentType.BANK_TRANSFER
            }

            if (index == -1) {
                methods.add(
                    PaymentMethod(
                        type = PaymentType.BANK_TRANSFER,
                        channels = listOf(payment.type)
                    )
                )
            } else {
                methods[index] = PaymentMethod(
                    type = PaymentType.BANK_TRANSFER,
                    channels = methods[index]
                        .channels
                        .toMutableList()
                        .apply { add(payment.type) }
                )
            }
        } else if (payment.category == PaymentType.CSTORE) {
            methods.add(
                PaymentMethod(
                    type = payment.type,
                    channels = emptyList()
                )
            )
        } else if (payment.type == PaymentType.QRIS) {
            if (payment.acquirer == PaymentType.SHOPEEPAY) {
                methods.add(
                    PaymentMethod(
                        type = PaymentType.SHOPEEPAY_QRIS,
                        channels = emptyList()
                    )
                )
            } else if (payment.acquirer == PaymentType.GOPAY) {
                methods.add(
                    PaymentMethod(
                        type = PaymentType.GOPAY_QRIS,
                        channels = emptyList()
                    )
                )
            }
        } else {
            methods.add(
                PaymentMethod(
                    type = payment.type,
                    channels = (if (payment.type == PaymentType.UOB_EZPAY) {
                        payment.mode
                    } else {
                        emptyList()
                    }) as List<String>
                )
            )
        }
    }

    @SuppressLint("CheckResult")
    fun pay(
        snapToken: String,
        paymentRequestBuilder: PaymentRequestBuilder,
        callback: Callback<TransactionResponse>
    ) {
        try {
            val paymentRequest = paymentRequestBuilder.build()
            snapRepository.pay(snapToken, paymentRequest)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(
                    {
                        callback.onSuccess(it)
                    },
                    {
                        deliverError(it, callback)
                    }
                )
        } catch (error: Throwable) {
            deliverError(error, callback)
        }
    }

    private fun <T> deliverError(error: Throwable, callback: Callback<T>) {
        val snapError = when (error) {
            is SnapError -> error
            is HttpException -> {
                val message = when (error.code()) {
                    CommonConstant.HTTP_RESPONSE_404 -> MESSAGE_ERROR_HTTP_404
                    CommonConstant.HTTP_RESPONSE_406 -> MESSAGE_ERROR_HTTP_406
                    CommonConstant.HTTP_RESPONSE_407 -> MESSAGE_ERROR_HTTP_407
                    CommonConstant.HTTP_RESPONSE_408 -> MESSAGE_ERROR_HTTP_408
                    CommonConstant.HTTP_RESPONSE_409 -> MESSAGE_ERROR_HTTP_409
                    else -> null
                }
                SnapError(cause = error, message = message)
            }
            else -> SnapError(cause = error)

        }
        callback.onError(snapError)
    }

    @SuppressLint("CheckResult")
    fun getCardToken(
        cardTokenRequestBuilder: CreditCardTokenRequestBuilder,
        callback: Callback<CardTokenResponse>
    ) {
        try {
            cardTokenRequestBuilder.withClientKey(clientKey)
            val cardTokenRequest = cardTokenRequestBuilder.build()
            coreApiRepository.getCardToken(cardTokenRequest)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(
                    {
                        callback.onSuccess(it)
                    },
                    {
                        deliverError(it, callback)
                    }
                )
        } catch (error: Throwable) {
            deliverError(error, callback)
        }

    }

    @SuppressLint("CheckResult")
    fun getBinData(
        binNumber: String,
        callback: Callback<BinResponse>
    ) {
        try {
            coreApiRepository.getBinData(binNumber, clientKey)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(
                    {
                        callback.onSuccess(it)
                    },
                    {
                        deliverError(it, callback)
                    }
                )
        } catch (error: Throwable) {
            deliverError(error, callback)
        }
    }

    @SuppressLint("CheckResult")
    fun deleteSavedCard(
        snapToken: String,
        maskedCard: String,
        callback: Callback<DeleteSavedCardResponse>
    ) {
        try {
            snapRepository.deleteSavedCard(snapToken, maskedCard)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(
                    {
                        callback.onSuccess(it)
                    },
                    {
                        deliverError(it, callback)
                    }
                )
        } catch (error: Throwable) {
            deliverError(error, callback)
        }
    }

    @SuppressLint("CheckResult")
    fun getBankPoint(
        snapToken: String,
        cardToken: String,
        grossAmount: Double,
        callback: Callback<BankPointResponse>
    ) {
        try {
            snapRepository.getBankPoint(snapToken, cardToken, grossAmount)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(
                    {
                        callback.onSuccess(it)
                    },
                    {
                        deliverError(it, callback)
                    }
                )
        } catch (error: Throwable) {
            deliverError(error, callback)
        }
    }

    @SuppressLint("CheckResult")
    fun getTransactionStatus(
        snapToken: String,
        callback: Callback<TransactionResponse>
    ){
        try {
            snapRepository.getTransactionStatus(snapToken)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(
                    {
                        callback.onSuccess(it)
                    },
                    {
                        deliverError(it, callback)
                    }
                )
        } catch (error: Throwable) {
            deliverError(error, callback)
        }
    }
}