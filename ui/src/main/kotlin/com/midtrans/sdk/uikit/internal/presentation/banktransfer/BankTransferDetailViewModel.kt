package com.midtrans.sdk.uikit.internal.presentation.banktransfer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.exception.SnapError
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.api.model.TransactionResult
import com.midtrans.sdk.corekit.api.requestbuilder.payment.BankTransferPaymentRequestBuilder
import com.midtrans.sdk.corekit.core.Logger
import com.midtrans.sdk.corekit.internal.analytics.PageName
import com.midtrans.sdk.corekit.internal.network.model.response.Merchant
import com.midtrans.sdk.uikit.internal.base.BaseViewModel
import com.midtrans.sdk.uikit.internal.util.DateTimeUtil
import com.midtrans.sdk.uikit.internal.util.DateTimeUtil.DATE_FORMAT
import javax.inject.Inject

internal class BankTransferDetailViewModel @Inject constructor(
    private val snapCore: SnapCore,
    private val datetimeUtil: DateTimeUtil
) : BaseViewModel() {

    init {
        eventAnalytics = snapCore.getEventAnalytics()
    }

    private val _vaNumberLiveData = MutableLiveData<String>()
    private val _companyCodeLiveData = MutableLiveData<String>()
    private val _billingNumberLiveData = MutableLiveData<String>()
    private val _bankCodeLiveData = MutableLiveData<String>()
    private val _transactionResult = MutableLiveData<TransactionResult>()
    private val _errorLiveData = MutableLiveData<SnapError>()
    private val _isBankTransferChargeErrorLiveData = MutableLiveData<Boolean>()
    private val _isExpired = MutableLiveData<Boolean>()
    val vaNumberLiveData: LiveData<String> = _vaNumberLiveData
    val companyCodeLiveData: LiveData<String> = _companyCodeLiveData
    val billingNumberLiveData: LiveData<String> = _billingNumberLiveData
    val bankCodeLiveData: LiveData<String> = _bankCodeLiveData
    val transactionResult: LiveData<TransactionResult> = _transactionResult
    val errorLiveData: LiveData<SnapError> = _errorLiveData
    val isBankTransferChargeErrorLiveData: LiveData<Boolean> = _isBankTransferChargeErrorLiveData
    val isExpired: LiveData<Boolean> = _isExpired
    var expiredTime = datetimeUtil.plusDateBy(datetimeUtil.getCurrentMillis(), 1)
    var merchant: Merchant? = null

    fun chargeBankTransfer(
        snapToken: String,
        @PaymentType.Def paymentType: String,
        customerEmail: String? = null
    ) {
        val requestBuilder = BankTransferPaymentRequestBuilder().withPaymentType(paymentType)
        customerEmail?.let {
            requestBuilder.withCustomerEmail(it)
        }
        trackSnapChargeRequest(
            pageName = PageName.BANK_TRANSFER_DETAIL_PAGE,
            paymentMethodName = paymentType
        )

        getBankCode()
        snapCore.pay(
            snapToken = snapToken,
            paymentRequestBuilder = requestBuilder,
            callback = object : Callback<TransactionResponse> {
                override fun onSuccess(result: TransactionResponse) {
                    _isExpired.value = result.validationMessages?.get(0)?.contains("expired") == true
                    trackSnapChargeResult(
                        response = result,
                        pageName = getPageName(paymentType),
                        paymentMethodName = paymentType
                    )
                    trackErrorStatusCode(
                        pageName = getPageName(paymentType),
                        paymentMethodName = paymentType,
                        errorMessage = result.statusMessage.orEmpty(),
                        statusCode = result.statusCode.orEmpty()
                    )
                    result.run {
                        bcaVaNumber?.let { _vaNumberLiveData.value = it }
                        cimbVaNumber?.let { _vaNumberLiveData.value = it }
                        bniVaNumber?.let {
                            _vaNumberLiveData.value = it
                            _bankCodeLiveData.value = BANK_CODE_BNI
                        }
                        briVaNumber?.let {
                            _vaNumberLiveData.value = it
                            _bankCodeLiveData.value = BANK_CODE_BRI
                        }
                        permataVaNumber?.let {
                            _vaNumberLiveData.value = it
                            _bankCodeLiveData.value = BANK_CODE_PERMATA
                        }
                        billerCode?.let { _companyCodeLiveData.value = it }
                        billKey?.let { _billingNumberLiveData.value = it }
                        bcaExpirationRaw?.let { expiredTime = parseTime(it) }
                        bniExpirationRaw?.let { expiredTime = parseTime(it) }
                        briExpirationRaw?.let { expiredTime = parseTime(it) }
                        permataExpirationRaw?.let { expiredTime = parseTime(it) }
                        mandiriBillExpirationRaw?.let { expiredTime = parseTime(it) }
                        cimbExpirationRaw?.let { expiredTime = parseTime(it) }
                        _transactionResult.value = TransactionResult(
                            status = transactionStatus.orEmpty(),
                            transactionId = transactionId.orEmpty(),
                            paymentType = paymentType
                        )
                    }
                    _isBankTransferChargeErrorLiveData.value = false
                    Logger.d("Bank Transfer Pay Success")
                }

                override fun onError(error: SnapError) {
                    Logger.e("Bank Transfer Pay Error")
                    trackSnapError(
                        pageName = getPageName(paymentType),
                        paymentMethodName = paymentType,
                        error = error
                    )
                    _errorLiveData.value = error
                    _isBankTransferChargeErrorLiveData.value = true
                }
            }
        )
    }

    private fun parseTime(dateString: String): Long {
        val date = datetimeUtil.getDate(
            date = dateString,
            dateFormat = DATE_FORMAT,
            timeZone = DateTimeUtil.TIME_ZONE_UTC
        )
        return date.time
    }

    private fun getPageName(paymentType: String): String {
        return when (paymentType) {
            PaymentType.BCA_VA -> PageName.BCA_VA_PAGE
            PaymentType.BNI_VA -> PageName.BNI_VA_PAGE
            PaymentType.BRI_VA -> PageName.BRI_VA_PAGE
            PaymentType.CIMB_VA -> PageName.CIMB_VA_PAGE
            PaymentType.PERMATA_VA -> PageName.PERMATA_VA_PAGE
            PaymentType.E_CHANNEL -> PageName.MANDIRI_E_CHANNEL_PAGE
            else -> PageName.OTHER_VA_PAGE
        }
    }

    fun trackSnapButtonClicked(
        ctaName: String,
        paymentType: String
    ) {
        trackCtaClicked(
            ctaName = ctaName,
            pageName = getPageName(paymentType),
            paymentMethodName = paymentType
        )
    }

    fun trackHowToPayClicked(paymentType: String) {
        trackHowToPayViewed(
            paymentMethodName = paymentType,
            pageName = getPageName(paymentType)
        )
    }

    fun trackOrderDetailsViewed(paymentType: String) {
        trackOrderDetailsViewed(
            pageName = getPageName(paymentType),
            paymentMethodName = paymentType
        )
    }

    fun trackAccountNumberCopied(paymentType: String) {
        eventAnalytics?.trackSnapAccountNumberCopied(
            paymentMethodName = paymentType,
            pageName = getPageName(paymentType)
        )
    }

    fun trackPageViewed(
        paymentType: String,
        stepNumber: Int
    ) {
        trackPageViewed(
            pageName = getPageName(paymentType),
            paymentMethodName = paymentType,
            stepNumber = stepNumber.toString()
        )
    }

    fun trackReloadClicked(paymentType: String) {
        eventAnalytics?.trackSnapPaymentNumberButtonRetried(
            paymentMethodName = paymentType,
            pageName = getPageName(paymentType)
        )
    }

    private fun getBankCode() {
        when (merchant?.preference?.otherVaProcessor) {
            PaymentType.BNI_VA -> BANK_CODE_BNI
            PaymentType.BRI_VA -> BANK_CODE_BRI
            PaymentType.PERMATA_VA -> BANK_CODE_PERMATA
            else -> null
        }?.let {
            _bankCodeLiveData.value = it
        }
    }

    fun getExpiredHour() = datetimeUtil.getExpiredHour(expiredTime)

    fun setDefaultExpiryTime(expiryTime: String?) {
        expiryTime?.let {
            expiredTime = parseTime(it)
        }
    }

    companion object{
        private const val BANK_CODE_BNI = "009 - BNI"
        private const val BANK_CODE_BRI = "002 - BRI"
        private const val BANK_CODE_PERMATA = "013 - PERMATA"
    }
}