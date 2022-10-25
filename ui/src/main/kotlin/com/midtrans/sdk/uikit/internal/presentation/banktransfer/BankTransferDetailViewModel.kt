package com.midtrans.sdk.uikit.internal.presentation.banktransfer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.exception.SnapError
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.api.model.TransactionResult
import com.midtrans.sdk.corekit.api.requestbuilder.payment.BankTransferPaymentRequestBuilder
import com.midtrans.sdk.uikit.internal.util.DateTimeUtil
import com.midtrans.sdk.uikit.internal.util.DateTimeUtil.DATE_FORMAT
import com.midtrans.sdk.uikit.internal.util.DateTimeUtil.TIME_ZONE_WIB
import java.util.*
import javax.inject.Inject

internal class BankTransferDetailViewModel @Inject constructor(
    private val snapCore: SnapCore,
    private val datetimeUtil: DateTimeUtil
) : ViewModel() {

    private val _vaNumberLiveData = MutableLiveData<String>()
    private val _companyCodeLiveData = MutableLiveData<String>()
    private val _billingNumberLiveData = MutableLiveData<String>()
    private val _bankCodeLiveData = MutableLiveData<String>()
    private val _transactionResult = MutableLiveData<TransactionResult>()
    val vaNumberLiveData: LiveData<String> = _vaNumberLiveData
    val companyCodeLiveData: LiveData<String> = _companyCodeLiveData
    val billingNumberLiveData: LiveData<String> = _billingNumberLiveData
    val bankCodeLiveData: LiveData<String> = _bankCodeLiveData
    val transactionResult: LiveData<TransactionResult> = _transactionResult

    var expiredTime = datetimeUtil.plusDateBy(datetimeUtil.getCurrentMillis(), 1)

    fun chargeBankTransfer(
        snapToken: String,
        @PaymentType.Def paymentType: String,
        customerEmail: String? = null
    ) {
        val requestBuilder = BankTransferPaymentRequestBuilder().withPaymentType(paymentType)
        customerEmail?.let {
            requestBuilder.withCustomerEmail(it)
        }
        snapCore.pay(
            snapToken = snapToken,
            paymentRequestBuilder = requestBuilder,
            callback = object : Callback<TransactionResponse> {
                override fun onSuccess(result: TransactionResponse) {
                    result.run {
                        bcaVaNumber?.let { _vaNumberLiveData.value = it }
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
                        bcaExpiration?.let { expiredTime = parseTime(it) }
                        bniExpiration?.let { expiredTime = parseTime(it) }
                        briExpiration?.let { expiredTime = parseTime(it) }
                        permataExpiration?.let { expiredTime = parseTime(it) }
                        _transactionResult.value = TransactionResult(status = transactionStatus.orEmpty(), transactionId = transactionId.orEmpty(), paymentType = paymentType)
                    }
                }

                override fun onError(error: SnapError) {
                    // TODO: error dialog etc
                }
            }
        )
    }

    private fun parseTime(dateString: String): Long {
        val expCalendar = Calendar.getInstance()
        expCalendar.time =
            datetimeUtil.getDate(
                date = dateString.replace("WIB", "+0700"),
                dateFormat = DATE_FORMAT,
                timeZone = TIME_ZONE_WIB,
                locale = Locale.US
            )
        expCalendar.set(Calendar.YEAR, datetimeUtil.getCalendar().get(Calendar.YEAR))
        return expCalendar.timeInMillis
    }

    fun getExpiredHour() = datetimeUtil.getExpiredHour(expiredTime)

    companion object{
        private const val BANK_CODE_BNI = "009 - BNI46"
        private const val BANK_CODE_BRI = "002 - BRI"
        private const val BANK_CODE_PERMATA = "013 - PERMATA"
    }
}