package com.midtrans.sdk.uikit.internal.presentation.creditcard

import android.util.Log
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.exception.SnapError
import com.midtrans.sdk.corekit.api.model.*
import com.midtrans.sdk.corekit.api.requestbuilder.cardtoken.NormalCardTokenRequestBuilder
import com.midtrans.sdk.corekit.api.requestbuilder.cardtoken.TwoClickCardTokenRequestBuilder
import com.midtrans.sdk.corekit.api.requestbuilder.payment.CreditCardPaymentRequestBuilder
import com.midtrans.sdk.corekit.api.requestbuilder.payment.OneClickCardPaymentRequestBuilder
import com.midtrans.sdk.corekit.internal.network.model.response.TransactionDetails
import com.midtrans.sdk.uikit.internal.presentation.errorcard.ErrorCard
import com.midtrans.sdk.uikit.internal.util.CurrencyFormat.currencyFormatRp
import com.midtrans.sdk.uikit.internal.util.DateTimeUtil
import com.midtrans.sdk.uikit.internal.util.SnapCreditCardUtil
import com.midtrans.sdk.uikit.internal.view.PromoData
import com.midtrans.sdk.uikit.internal.view.SavedCreditCardFormData
import java.util.*
import javax.inject.Inject

internal class CreditCardViewModel @Inject constructor(
    private val snapCore: SnapCore,
    private val datetimeUtil: DateTimeUtil,
    private val snapCreditCardUtil: SnapCreditCardUtil,
    private val errorCard: ErrorCard
) : ViewModel() {

    private val bankIconId = MutableLiveData<Int>()
    private val binType = MutableLiveData<String>()
    private val cardIssuerBank = MutableLiveData<String>()
    private val _transactionResponse = MutableLiveData<TransactionResponse>()
    private val _transactionStatus = MutableLiveData<TransactionResponse>()
    private val _error = MutableLiveData<Int>()
    private val _promoDataLiveData = MutableLiveData<List<PromoData>>()
    private val _netAmountLiveData = MutableLiveData<String>()
    private var expireTimeInMillis = 0L
    private var allowRetry = false
    private var promos: List<Promo>? = null
    private var transactionDetails: TransactionDetails? = null

    val promoDataLiveData: LiveData<List<PromoData>> = _promoDataLiveData
    val netAmountLiveData: LiveData<String> = _netAmountLiveData
    var creditCard: CreditCard? = null

    fun getBankIconId(): LiveData<Int> = bankIconId
    fun getBinType(): LiveData<String> = binType
    fun getCardIssuerBank(): LiveData<String> = cardIssuerBank
    val transactionResponseLiveData: LiveData<TransactionResponse> = _transactionResponse
    val transactionStatusLiveData: LiveData<TransactionResponse> = _transactionStatus
    val errorLiveData: LiveData<Int> = _error
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

    fun setPromos(promos: List<Promo>?){
        this.promos = promos
        getPromosData("")
    }

    fun setTransactionDetails(transactionDetails: TransactionDetails?){
        this.transactionDetails = transactionDetails
    }

    fun setPromoId(promoId: Long){
        _netAmountLiveData.value = transactionDetails?.grossAmount?.currencyFormatRp()
        promos?.find { it.id == promoId }?.discountedGrossAmount?.let {
            _netAmountLiveData.value = it.currencyFormatRp()
        }
    }

    fun getPromosData(binNumber: String) {
        _promoDataLiveData.value = snapCreditCardUtil.getCreditCardApplicablePromosData(binNumber, promos)
    }

    fun getBinData(binNumber: String) {
        snapCore.getBinData(
            binNumber = binNumber,
            callback = object : Callback<BinResponse> {
                override fun onSuccess(result: BinResponse) {
                    result.run {
                        data?.bankCode?.let {
                            bankIconId.value = snapCreditCardUtil.getBankIcon(it.lowercase())
                            cardIssuerBank.value = it
                        }
                        data?.binType?.let {
                            binType.value = it
                        }
                    }
                }

                override fun onError(error: SnapError) {
                }
            }
        )
    }

    fun setBankIconToNull() {
        bankIconId.value = null
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

                    promos?.find { it.id == promoId }?.discountedGrossAmount?.let {
                        ccRequestBuilder.withPromo(discountedGrossAmount = it, promoId = promoId.toString())
                    }

                    result.tokenId?.let {
                        ccRequestBuilder.withCardToken(it)
                    }
                    snapCore.pay(
                        snapToken = snapToken,
                        paymentRequestBuilder = ccRequestBuilder,
                        callback = object : Callback<TransactionResponse> {
                            override fun onSuccess(result: TransactionResponse) {
                                errorCard.getErrorCardType(result, allowRetry)?.let {
                                    _error.value = it
                                } ?: run {
                                    _transactionResponse.value = result
                                    null
                                }
                            }

                            override fun onError(error: SnapError) {
                                _error.value = errorCard.getErrorCardType(error, allowRetry)
                            }
                        }
                    )
                }

                override fun onError(error: SnapError) {
                    _error.value = errorCard.getErrorCardType(error, allowRetry)
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
        promoId: Long?
        ){
        if (formData.tokenType == SavedToken.ONE_CLICK){
            snapCore.pay(
                snapToken = snapToken,
                paymentRequestBuilder = OneClickCardPaymentRequestBuilder()
                    .withPaymentType(PaymentType.CREDIT_CARD)
                    .withMaskedCard(formData.displayedMaskedCard).apply {
                        promos?.find { it.id == promoId }?.discountedGrossAmount?.let {
                            withPromo(discountedGrossAmount = it, promoId = promoId.toString())
                        }
                    },
                callback = object : Callback<TransactionResponse> {
                    override fun onSuccess(result: TransactionResponse) {
                        _transactionResponse.value = result
                    }
                    override fun onError(error: SnapError) {
                        _error.value = errorCard.getErrorCardType(error, allowRetry)
                    }
                }
            )
        } else {

            var tokenRequest = TwoClickCardTokenRequestBuilder()
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
            snapCore.getCardToken(cardTokenRequestBuilder = tokenRequest ,
                callback = object : Callback<CardTokenResponse> {
                    override fun onSuccess(result: CardTokenResponse) {
                        var ccRequestBuilder = CreditCardPaymentRequestBuilder()
                            .withPaymentType(PaymentType.CREDIT_CARD)
                            .withCustomerEmail(customerEmail).apply {
                                promos?.find { it.id == promoId }?.discountedGrossAmount?.let {
                                    withPromo(discountedGrossAmount = it, promoId = promoId.toString())
                                }
                            }

                        result?.tokenId?.let {
                            ccRequestBuilder.withCardToken(it)
                        }

                        snapCore.pay(
                            snapToken = snapToken,
                            paymentRequestBuilder = ccRequestBuilder,
                            callback = object : Callback<TransactionResponse> {
                                override fun onSuccess(result: TransactionResponse) {
                                    _transactionResponse.value = result
                                }
                                override fun onError(error: SnapError) {
                                    _error.value = errorCard.getErrorCardType(error, allowRetry)
                                }
                            }
                        )
                    }
                    override fun onError(error: SnapError) {
                        //TODO: Need to confirm how to handle get token error on UI
                        Log.e("error get 2click token", "error, error, error")
                    }
                }
            )
        }
    }

    fun resetError() {
        _error.value = null
    }

    fun getTransactionStatus(snapToken: String) {
        snapCore.getTransactionStatus(
            snapToken = snapToken,
            callback = object : Callback<TransactionResponse> {
                override fun onSuccess(result: TransactionResponse) {
                    errorCard.getErrorCardType(result, allowRetry)?.let {
                        _error.value = it
                    } ?: run {
                        _transactionResponse.value = result
                        null
                    }
                }

                override fun onError(error: SnapError) {
                    _error.value = errorCard.getErrorCardType(error, allowRetry)
                }
            }
        )
    }

    fun deleteSavedCard(snapToken: String, maskedCard: String){
        snapCore.deleteSavedCard(
            snapToken = snapToken,
            maskedCard = maskedCard,
            callback = object : Callback<DeleteSavedCardResponse> {
                override fun onSuccess(result: DeleteSavedCardResponse) {
                    Log.e("Delete Card Success", "Delete Card Success")
                }
                override fun onError(error: SnapError) {
                    Log.e("Delete Card Error", "Delete Card Error")
                }
            }
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
    }
}
