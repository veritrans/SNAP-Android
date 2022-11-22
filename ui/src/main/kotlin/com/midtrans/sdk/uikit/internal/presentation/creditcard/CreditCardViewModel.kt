package com.midtrans.sdk.uikit.internal.presentation.creditcard

import android.util.Log
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.exception.SnapError
import com.midtrans.sdk.corekit.api.model.*
import com.midtrans.sdk.corekit.api.requestbuilder.cardtoken.NormalCardTokenRequestBuilder
import com.midtrans.sdk.corekit.api.requestbuilder.cardtoken.TwoClickCardTokenRequestBuilder
import com.midtrans.sdk.corekit.api.requestbuilder.payment.CreditCardPaymentRequestBuilder
import com.midtrans.sdk.corekit.api.requestbuilder.payment.OneClickCardPaymentRequestBuilder
import com.midtrans.sdk.corekit.internal.analytics.PageName
import com.midtrans.sdk.corekit.internal.network.model.response.TransactionDetails
import com.midtrans.sdk.uikit.internal.base.BaseViewModel
import com.midtrans.sdk.uikit.internal.model.PromoData
import com.midtrans.sdk.uikit.internal.presentation.errorcard.ErrorCard
import com.midtrans.sdk.uikit.internal.util.CurrencyFormat.currencyFormatRp
import com.midtrans.sdk.uikit.internal.util.DateTimeUtil
import com.midtrans.sdk.uikit.internal.util.SnapCreditCardUtil
import com.midtrans.sdk.uikit.internal.view.SavedCreditCardFormData
import java.util.*
import javax.inject.Inject

internal class CreditCardViewModel @Inject constructor(
    private val snapCore: SnapCore,
    private val datetimeUtil: DateTimeUtil,
    private val snapCreditCardUtil: SnapCreditCardUtil,
    private val errorCard: ErrorCard
) : BaseViewModel() {

    init {
        eventAnalytics = snapCore.getEventAnalytics()
    }

    private val _bankIconId = MutableLiveData<Int>()
    private val _binType = MutableLiveData<String>()
    private val _cardIssuerBank = MutableLiveData<String>()
    private val _transactionResponse = MutableLiveData<TransactionResponse>()
    private val _errorTypeLiveData = MutableLiveData<Pair<Int?, String>>()
    private val _promoDataLiveData = MutableLiveData<List<PromoData>>()
    private var _netAmountWithoutCurrencyLiveData = MutableLiveData<Double>()
    private val _netAmountLiveData = MutableLiveData<String>()
    private val _binBlockedLiveData = MutableLiveData<Boolean>()
    private val _errorLiveData = MutableLiveData<SnapError>()
    private val _isPointBankShown = MutableLiveData<Boolean>()
    private val _isPointBankEnabled = MutableLiveData<Boolean>()
    private val _pointBalanceAmount = MutableLiveData<String>()
    private val _cardToken = MutableLiveData<String>()
    private var expireTimeInMillis = 0L
    private var allowRetry = false
    private var promos: List<Promo>? = null
    private var transactionDetails: TransactionDetails? = null
    private var pointBanks: List<String?>? = null

    val promoDataLiveData: LiveData<List<PromoData>> = _promoDataLiveData
    val netAmountLiveData: LiveData<String> = _netAmountLiveData
    val netAmountWithoutCurrencyLiveData: LiveData<Double> = _netAmountWithoutCurrencyLiveData
    private var creditCard: CreditCard? = null
    private var _isInstallmentRequired = MutableLiveData<Boolean>()

    val bankIconId: LiveData<Int> = _bankIconId
    val binType: LiveData<String> = _binType
    val cardIssuerBank: LiveData<String> = _cardIssuerBank
    val transactionResponseLiveData: LiveData<TransactionResponse> = _transactionResponse
    val errorTypeLiveData: LiveData<Pair<Int?, String>> = _errorTypeLiveData
    val binBlockedLiveData: LiveData<Boolean> = _binBlockedLiveData
    val errorLiveData: LiveData<SnapError> = _errorLiveData
    val isPointBankShown: LiveData<Boolean> = _isPointBankShown
    private val isPointBankEnabled: LiveData<Boolean> = _isPointBankEnabled
    val pointBalanceAmount: LiveData<String> = _pointBalanceAmount
    val cardToken: LiveData<String> = _cardToken
    val isInstallmentRequired: LiveData<Boolean> = _isInstallmentRequired


    private var promoName: String? = null
    private var promoAmount: Double? = null
    private var finalAmount = 0.0

    fun setExpiryTime(expireTime: String?) {
        expireTime?.let {
            expireTimeInMillis = parseTime(it)
        }
    }

    fun setAllowRetry(allowRetry: Boolean) {
        this.allowRetry = allowRetry
    }

    private fun parseTime(dateString: String): Long {
        val date = datetimeUtil.getDate(
            date = dateString,
            dateFormat = DATE_FORMAT,
            timeZone = timeZoneUtc
        )
        return date.time
    }

    fun setPromos(promos: List<Promo>?) {
        this.promos = promos
        getPromosData("", "")
    }

    fun setTransactionDetails(transactionDetails: TransactionDetails?) {
        this.transactionDetails = transactionDetails
    }

    fun setPromoId(promoId: Long) {
        _netAmountLiveData.value = transactionDetails?.grossAmount?.currencyFormatRp()
        _netAmountWithoutCurrencyLiveData.value = transactionDetails?.grossAmount
        promos?.find { it.id == promoId }?.discountedGrossAmount?.let {
            _netAmountLiveData.value = it.currencyFormatRp()
            _netAmountWithoutCurrencyLiveData.value = it
        }
    }

    fun setCreditCardDetails(creditCard: CreditCard?) {
        this.creditCard = creditCard
        creditCard?.installment?.isRequired?.let {
            _isInstallmentRequired.value = it
        }
    }

    fun hidePointBank(installmentTerm: String) = when {
        installmentTerm.isNullOrBlank() -> {
            _isPointBankShown.value = true
        }
        else -> {
            _isPointBankShown.value = false
        }
    }

    fun setPointBanks(pointBanks: List<String>?) {
        this.pointBanks = pointBanks
    }

    private fun checkForPointBank(cardIssuerBank: String) {
        if (isInstallmentRequired.value != true) {
            pointBanks?.contains(cardIssuerBank)?.apply {
                _isPointBankEnabled.value = true
            }
            handleShowingPointBank(cardIssuerBank)
        }
    }

    private fun handleShowingPointBank(cardIssuerBank: String) {
        _isPointBankShown.value =
            isPointBankEnabled.value == true && checkForBniPoint(cardIssuerBank) == true
    }

    private fun checkForBniPoint(cardIssuerBank: String): Boolean {
        return cardIssuerBank.lowercase() == BANK_BNI
    }

    fun getPromosData(binNumber: String, installmentTerm: String) {
        _promoDataLiveData.value =
            snapCreditCardUtil.getCreditCardApplicablePromosData(binNumber, promos, installmentTerm)
    }

    fun getBinData(binNumber: String) {
        snapCore.getBinData(
            binNumber = binNumber,
            callback = object : Callback<BinResponse> {
                override fun onSuccess(result: BinResponse) {
                    result.run {
                        trackCreditDebitCardExbinResponse(data)
                        data?.bankCode?.let {
                            _bankIconId.value = snapCreditCardUtil.getBankIcon(it.lowercase())
                            _cardIssuerBank.value = it
                            checkForPointBank(it)
                        }
                        data?.binType?.let {
                            _binType.value = it
                        }
                        _binBlockedLiveData.value = snapCreditCardUtil
                            .isBinBlocked(
                                cardNumber = binNumber,
                                creditCard = creditCard,
                                bank = data?.bankCode.orEmpty(),
                                binType = data?.binType.orEmpty()
                            )
                    }
                }

                override fun onError(error: SnapError) {
                    _errorLiveData.value = error
                }
            }
        )
    }

    fun resetCardNumberAttribute() {
        _bankIconId.value = null
        _binBlockedLiveData.value = false
        _isPointBankShown.value = false
    }

    fun chargeUsingCreditCard(
        transactionDetails: TransactionDetails?,
        cardNumber: TextFieldValue,
        cardExpiry: TextFieldValue,
        cardCvv: TextFieldValue,
        isSavedCard: Boolean,
        customerEmail: String,
        customerPhone: String,
        promoId: Long?,
        installmentTerm: String,
        snapToken: String
    ) {
        val tokenRequest = NormalCardTokenRequestBuilder()
            .withCardNumber(snapCreditCardUtil.getCardNumberFromTextField(cardNumber))
            .withCardExpMonth(snapCreditCardUtil.getExpMonthFromTextField(cardExpiry))
            .withCardExpYear(snapCreditCardUtil.getExpYearFromTextField(cardExpiry))
            .withCardCvv(cardCvv.text)

        transactionDetails?.currency?.let {
            tokenRequest.withCurrency(it)
        }
        transactionDetails?.grossAmount?.let {
            tokenRequest.withGrossAmount(it)
        }
        transactionDetails?.orderId?.let {
            tokenRequest.withOrderId(it)
        }

        snapCore.getCardToken(
            cardTokenRequestBuilder = tokenRequest,
            callback = object : Callback<CardTokenResponse> {
                override fun onSuccess(result: CardTokenResponse) {

                    val ccRequestBuilder = CreditCardPaymentRequestBuilder()
                        .withSaveCard(isSavedCard)
                        .withPaymentType(PaymentType.CREDIT_CARD)
                        .withCustomerEmail(customerEmail)
                        .withCustomerPhone(customerPhone)
                        .withInstallment(installmentTerm)

                    promos
                        ?.find { it.id == promoId }
                        ?.also { promoName = it.name }
                        ?.discountedGrossAmount
                        ?.let {
                            promoAmount = it
                            ccRequestBuilder.withPromo(
                                discountedGrossAmount = it,
                                promoId = promoId.toString()
                            )
                        }

                    result.tokenId?.let {
                        ccRequestBuilder.withCardToken(it)
                    }

                    trackSnapChargeRequest(
                        pageName = PageName.CREDIT_DEBIT_CARD_PAGE,
                        paymentMethodName = PaymentType.CREDIT_CARD,
                        promoName = promoName,
                        promoAmount = promoAmount?.toString(),
                        promoId = promoId?.toString(),
                        creditCardPoint = null //TODO add this after point implemented
                    )

                    snapCore.pay(
                        snapToken = snapToken,
                        paymentRequestBuilder = ccRequestBuilder,
                        callback = object : Callback<TransactionResponse> {
                            override fun onSuccess(result: TransactionResponse) {
                                trackSnapChargeResult(result)
                                errorCard.getErrorCardType(result, allowRetry)
                                    ?.let {
                                        val transactionId = result.transactionId.orEmpty()
                                        _errorTypeLiveData.value = Pair(it, transactionId)
                                    }
                                    ?: run {
                                        _transactionResponse.value = result
                                        null
                                    }
                            }

                            override fun onError(error: SnapError) {
                                val errorType = errorCard.getErrorCardType(error, allowRetry)
                                _errorTypeLiveData.value = Pair(errorType, "")
                            }
                        }
                    )
                }

                override fun onError(error: SnapError) {
                    val errorType = errorCard.getErrorCardType(error, allowRetry)
                    _errorTypeLiveData.value = Pair(errorType, "")
                    _errorLiveData.value = error
                }
            }
        )
    }

    fun chargeUsingCreditCard(
        formData: SavedCreditCardFormData,
        snapToken: String,
        transactionDetails: TransactionDetails?,
        cardCvv: TextFieldValue,
        customerEmail: String,
        installmentTerm: String,
        promoId: Long?
    ) {
        if (formData.tokenType == SavedToken.ONE_CLICK) {
            val oneClickRequestBuilder = OneClickCardPaymentRequestBuilder()
                .withPaymentType(PaymentType.CREDIT_CARD)
                .withInstallment(installmentTerm)
                .withMaskedCard(formData.displayedMaskedCard)

            promos
                ?.find { it.id == promoId }
                ?.also { promoName = it.name }
                ?.discountedGrossAmount
                ?.let {
                    promoAmount = it
                    oneClickRequestBuilder.withPromo(
                        discountedGrossAmount = it,
                        promoId = promoId.toString()
                    )
                }

            trackSnapChargeRequest(
                pageName = PageName.CREDIT_DEBIT_CARD_PAGE,
                paymentMethodName = PaymentType.CREDIT_CARD,
                promoName = promoName,
                promoAmount = promoAmount?.toString(),
                promoId = promoId?.toString(),
                creditCardPoint = null //TODO add this after point implemented
            )

            snapCore.pay(
                snapToken = snapToken,
                paymentRequestBuilder = oneClickRequestBuilder,
                callback = object : Callback<TransactionResponse> {
                    override fun onSuccess(result: TransactionResponse) {
                        _transactionResponse.value = result
                        trackSnapChargeResult(result)
                    }

                    override fun onError(error: SnapError) {
                        val errorType = errorCard.getErrorCardType(error, allowRetry)
                        _errorTypeLiveData.value = Pair(errorType, "")
                        _errorLiveData.value = error
                    }
                }
            )
        } else {
            val tokenRequest = TwoClickCardTokenRequestBuilder()
                .withCardCvv(cardCvv.text)
                .withTokenId(formData.tokenId)

            transactionDetails?.currency?.let {
                tokenRequest.withCurrency(it)
            }
            transactionDetails?.grossAmount?.let {
                tokenRequest.withGrossAmount(it)
            }
            transactionDetails?.orderId?.let {
                tokenRequest.withOrderId(it)
            }
            snapCore.getCardToken(cardTokenRequestBuilder = tokenRequest,
                callback = object : Callback<CardTokenResponse> {
                    override fun onSuccess(result: CardTokenResponse) {
                        var promoName: String? = null
                        var promoAmount: Double? = null

                        val ccRequestBuilder = CreditCardPaymentRequestBuilder()
                            .withPaymentType(PaymentType.CREDIT_CARD)
                            .withInstallment(installmentTerm)
                            .withCustomerEmail(customerEmail).apply {
                                promos
                                    ?.find { it.id == promoId }
                                    ?.also { promoName = it.name }
                                    ?.discountedGrossAmount
                                    ?.let {
                                        promoAmount = it
                                        withPromo(
                                            discountedGrossAmount = it,
                                            promoId = promoId.toString()
                                        )
                                    }
                            }

                        result.tokenId?.let {
                            ccRequestBuilder.withCardToken(it)
                        }

                        trackSnapChargeRequest(
                            pageName = PageName.CREDIT_DEBIT_CARD_PAGE,
                            paymentMethodName = PaymentType.CREDIT_CARD,
                            promoName = promoName,
                            promoAmount = promoAmount?.toString(),
                            promoId = promoId?.toString(),
                            creditCardPoint = null
                        )

                        snapCore.pay(
                            snapToken = snapToken,
                            paymentRequestBuilder = ccRequestBuilder,
                            callback = object : Callback<TransactionResponse> {
                                override fun onSuccess(result: TransactionResponse) {
                                    _transactionResponse.value = result
                                    trackSnapChargeResult(result)
                                }

                                override fun onError(error: SnapError) {
                                    val errorType = errorCard.getErrorCardType(error, allowRetry)
                                    _errorTypeLiveData.value = Pair(errorType, "")
                                    _errorLiveData.value = error
                                }
                            }
                        )
                    }

                    override fun onError(error: SnapError) {
                        //TODO: Need to confirm how to handle get token error on UI
                        Log.e("error get 2click token", "error, error, error")
                        _errorLiveData.value = error
                    }
                }
            )
        }
    }

    fun getBankPoint(
        snapToken: String,
        transactionDetails: TransactionDetails?,
        cardNumber: TextFieldValue,
        cardExpiry: TextFieldValue,
        cardCvv: TextFieldValue,
        promoId: Long?
    ) {
        val tokenRequest = NormalCardTokenRequestBuilder()
            .withCardNumber(snapCreditCardUtil.getCardNumberFromTextField(cardNumber))
            .withCardExpMonth(snapCreditCardUtil.getExpMonthFromTextField(cardExpiry))
            .withCardExpYear(snapCreditCardUtil.getExpYearFromTextField(cardExpiry))
            .withCardCvv(cardCvv.text)

        transactionDetails?.currency?.let {
            tokenRequest.withCurrency(it)
        }
        transactionDetails?.grossAmount?.let {
            finalAmount = it
        }
        transactionDetails?.orderId?.let {
            tokenRequest.withOrderId(it)
        }

        promos
            ?.find { it.id == promoId }
            ?.also { promoName = it.name }
            ?.discountedGrossAmount
            ?.let {
                finalAmount = it
            }

        tokenRequest.withGrossAmount(finalAmount)

        snapCore.getCardToken(
            cardTokenRequestBuilder = tokenRequest,
            callback = object : Callback<CardTokenResponse> {
                override fun onSuccess(result: CardTokenResponse) {

                    result.tokenId?.let { tokenId ->
                        _cardToken.value = tokenId
                        snapCore.getBankPoint(
                            snapToken = snapToken,
                            cardToken = tokenId,
                            grossAmount = finalAmount,
                            callback = object : Callback<BankPointResponse> {
                                override fun onSuccess(result: BankPointResponse) {
                                    _pointBalanceAmount.value = result.pointBalanceAmount
                                }

                                override fun onError(error: SnapError) {
                                    val errorType = errorCard.getErrorCardType(error, allowRetry)
                                    _errorTypeLiveData.value = Pair(errorType, "")
                                    _errorLiveData.value = error
                                }
                            }
                        )
                    }
                }

                override fun onError(error: SnapError) {
                    val errorType = errorCard.getErrorCardType(error, allowRetry)
                    _errorTypeLiveData.value = Pair(errorType, "")
                    _errorLiveData.value = error
                }
            }
        )
    }

    fun chargeWithPoint(
        transactionDetails: TransactionDetails?,
        cardNumber: TextFieldValue,
        cardExpiry: TextFieldValue,
        cardCvv: TextFieldValue,
        isSavedCard: Boolean,
        customerEmail: String,
        customerPhone: String,
        promoId: Long?,
        pointAmount: Double,
        snapToken: String
    ) {

        val ccRequestBuilder = CreditCardPaymentRequestBuilder()
            .withSaveCard(isSavedCard)
            .withPaymentType(PaymentType.CREDIT_CARD)
            .withCustomerEmail(customerEmail)
            .withCustomerPhone(customerPhone)
            .withPoint(pointAmount)

        cardToken.value?.let {
            ccRequestBuilder.withCardToken(it)
        }
        cardIssuerBank.value?.let {
            ccRequestBuilder.withBank(it)
        }

        snapCore.pay(
            snapToken = snapToken,
            paymentRequestBuilder = ccRequestBuilder,
            callback = object : Callback<TransactionResponse> {
                override fun onSuccess(result: TransactionResponse) {
                    _transactionResponse.value = result
                    trackSnapChargeResult(result)
                }

                override fun onError(error: SnapError) {
                    val errorType = errorCard.getErrorCardType(error, allowRetry)
                    _errorTypeLiveData.value = Pair(errorType, "")
                    _errorLiveData.value = error
                }
            }
        )
    }

    fun resetError() {
        _errorTypeLiveData.value = null
    }

    fun getTransactionStatus(snapToken: String) {
        snapCore.getTransactionStatus(
            snapToken = snapToken,
            callback = object : Callback<TransactionResponse> {
                override fun onSuccess(result: TransactionResponse) {
                    trackCreditCard3DsResult(result)
                    errorCard.getErrorCardType(result, allowRetry)?.let {
                        val transactionId = result.transactionId.orEmpty()
                        _errorTypeLiveData.value = Pair(it, transactionId)
                    } ?: run {
                        _transactionResponse.value = result
                        null
                    }
                }

                override fun onError(error: SnapError) {
                    val errorType = errorCard.getErrorCardType(error, allowRetry)
                    _errorTypeLiveData.value = Pair(errorType, "")
                    _errorLiveData.value = error
                }
            }
        )
    }

    fun deleteSavedCard(snapToken: String, maskedCard: String) {
        snapCore.deleteSavedCard(
            snapToken = snapToken,
            maskedCard = maskedCard,
            callback = object : Callback<DeleteSavedCardResponse> {
                override fun onSuccess(result: DeleteSavedCardResponse) {
                    Log.e("Delete Card Success", "Delete Card Success")
                }

                override fun onError(error: SnapError) {
                    Log.e("Delete Card Error", "Delete Card Error")
                    _errorLiveData.value = error
                }
            }
        )
    }

    fun trackSnapButtonClicked(ctaName: String) {
        trackCtaClicked(
            ctaName = ctaName,
            paymentMethodName = PaymentType.CREDIT_CARD,
            pageName = PageName.CREDIT_DEBIT_CARD_PAGE
        )
    }

    private fun trackSnapChargeResult(response: TransactionResponse) {
        trackSnapChargeResult(
            response = response,
            paymentMethodName = PaymentType.CREDIT_CARD,
            pageName = PageName.CREDIT_DEBIT_CARD_PAGE
        )
    }

    private fun trackCreditCard3DsResult(result: TransactionResponse) {
        trackCreditCard3DsResult(
            transactionStatus = result.transactionStatus,
            cardType = result.cardType,
            bank = result.bank,
            threeDsVersion = result.threeDsVersion,
            channelResponseCode = result.channelResponseCode,
            eci = result.eci
        )
    }

    fun trackOrderDetailsViewed(netAmount: String) {
        trackOrderDetailsViewed(
            pageName = PageName.CREDIT_DEBIT_CARD_PAGE,
            paymentMethodName = PaymentType.CREDIT_CARD,
            netAmount = netAmount
        )
    }

    fun trackPageViewed(stepNumber: Int) {
        trackPageViewed(
            pageName = PageName.CREDIT_DEBIT_CARD_PAGE,
            paymentMethodName = PaymentType.CREDIT_CARD,
            stepNumber = stepNumber.toString()
        )
    }

    fun getExpiredHour(): String {
        val duration = datetimeUtil.getDuration(
            datetimeUtil.getTimeDiffInMillis(
                datetimeUtil.getCurrentMillis(),
                expireTimeInMillis
            )
        )
        return String.format(
            TIME_FORMAT,
            duration.toHours(),
            duration.seconds % 3600 / 60,
            duration.seconds % 60
        )
    }

    companion object {
        private const val DATE_FORMAT = "yyyy-MM-dd hh:mm:ss Z"
        private const val TIME_FORMAT = "%02d:%02d:%02d"
        private val timeZoneUtc = TimeZone.getTimeZone("UTC")
        private const val BANK_BNI = "bni"
    }
}
