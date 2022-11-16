package com.midtrans.sdk.uikit.internal.presentation.conveniencestore

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.zxing.BarcodeFormat
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.exception.SnapError
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.api.model.TransactionResult
import com.midtrans.sdk.corekit.api.requestbuilder.payment.ConvenienceStorePaymentRequestBuilder
import com.midtrans.sdk.corekit.internal.analytics.PageName
import com.midtrans.sdk.uikit.internal.base.BaseViewModel
import com.midtrans.sdk.uikit.internal.presentation.errorcard.ErrorCard
import com.midtrans.sdk.uikit.internal.util.BarcodeEncoder
import com.midtrans.sdk.uikit.internal.util.DateTimeUtil
import com.midtrans.sdk.uikit.internal.util.DateTimeUtil.TIME_ZONE_UTC
import java.util.concurrent.TimeUnit
import javax.inject.Inject


internal class ConvenienceStoreViewModel @Inject constructor(
    private val snapCore: SnapCore,
    private val datetimeUtil: DateTimeUtil,
    private val errorCard: ErrorCard,
    private val barcodeEncoder: BarcodeEncoder
) : BaseViewModel() {

    init {
        eventAnalytics = snapCore.getEventAnalytics()
    }

    private val _barCodeBitmapLiveData = MutableLiveData<Bitmap>()
    private val _pdfUrlLiveData = MutableLiveData<String>()
    private val _paymentCodeLiveData = MutableLiveData<String>()
    private val _transactionResultLiveData = MutableLiveData<TransactionResult>()
    private var expiredTime = datetimeUtil.getCurrentMillis() + TimeUnit.MINUTES.toMillis(15)
    private val _errorLiveData = MutableLiveData<Int?>()
    private var _transactionId: String? = null
    val barCodeBitmapLiveData: LiveData<Bitmap> = _barCodeBitmapLiveData
    val pdfUrlLiveData: MutableLiveData<String> = _pdfUrlLiveData
    val paymentCodeLiveData: MutableLiveData<String> = _paymentCodeLiveData
    val errorLiveData: LiveData<Int?> = _errorLiveData
    val transactionResultLiveData: LiveData<TransactionResult> = _transactionResultLiveData

    fun chargeConvenienceStorePayment(
        snapToken: String,
        @PaymentType.Def paymentType: String
    ) {
        val requestBuilder = ConvenienceStorePaymentRequestBuilder().withPaymentType(paymentType)
        trackSnapChargeRequest(
            pageName = getPageName(paymentType),
            paymentMethodName = paymentType
        )
        snapCore.pay(
            snapToken = snapToken,
            paymentRequestBuilder = requestBuilder,
            callback = object : Callback<TransactionResponse> {
                override fun onSuccess(result: TransactionResponse) {
                    result.run {
                        _transactionId = transactionId
                        paymentCode?.let { generateBarcode(it) }
                        paymentCode?.let { _paymentCodeLiveData.value = it }
                        indomaretExpirationRaw?.let { expiredTime = parseTime(it) }
                        alfamartExpirationRaw?.let { expiredTime = parseTime(it) }
                        pdfUrl.let { _pdfUrlLiveData.value = it }
                        _transactionResultLiveData.value = TransactionResult(
                            status = statusCode.orEmpty(),
                            transactionId = transactionId.orEmpty(),
                            paymentType = paymentType
                        )
                    }
                    trackSnapChargeResult(
                        response = result,
                        pageName = getPageName(paymentType),
                        paymentMethodName = paymentType
                    )
                }

                override fun onError(error: SnapError) {
                    _errorLiveData.value = errorCard.getErrorCardType(error)
                }
            }
        )
    }

    private fun generateBarcode(barcode: String){
        try {
            val bitmap: Bitmap =
                barcodeEncoder.encodeBitmap(barcode, BarcodeFormat.CODE_39, 1000, 100)
            _barCodeBitmapLiveData.value = bitmap
        } catch (e: Exception) {
        }
    }

    private fun parseTime(dateString: String): Long {
        val date = datetimeUtil.getDate(
            date = dateString,
            dateFormat = DATE_FORMAT,
            timeZone = TIME_ZONE_UTC
        )
        return date.time
    }

    private fun getPageName(paymentType: String): String {
        return when (paymentType) {
            PaymentType.ALFAMART -> PageName.ALFAMART_PAGE
            PaymentType.INDOMARET -> PageName.INDOMARET_PAGE
            else -> ""
        }
    }

    fun trackSnapButtonClicked(
        ctaName: String,
        paymentType: String
    ) {
        trackCtaClicked(
            ctaName = ctaName,
            paymentMethodName = paymentType,
            pageName = getPageName(paymentType)
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
            paymentMethodName = paymentType,
            transactionId = _transactionId
        )
    }

    fun trackPageViewed(
        paymentType: String,
        stepNumber: Int
    ) {
        trackPageViewed(
            pageName = getPageName(paymentType),
            paymentMethodName = paymentType,
            transactionId = _transactionId,
            stepNumber = stepNumber.toString()
        )
    }

    fun resetError(){
        _errorLiveData.value = null
    }
    fun getExpiredHour(): String = datetimeUtil.getExpiredHour(expiredTime)

    companion object{
        const val DATE_FORMAT = "yyyy-MM-dd hh:mm Z"
    }
}