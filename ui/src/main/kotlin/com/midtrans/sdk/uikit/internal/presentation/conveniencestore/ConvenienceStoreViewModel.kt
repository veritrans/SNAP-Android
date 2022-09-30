package com.midtrans.sdk.uikit.internal.presentation.conveniencestore

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.zxing.BarcodeFormat
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.exception.SnapError
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.api.requestbuilder.payment.ConvenienceStorePaymentRequestBuilder
import com.midtrans.sdk.uikit.internal.presentation.errorcard.ErrorCard
import com.midtrans.sdk.uikit.internal.util.BarcodeEncoder
import com.midtrans.sdk.uikit.internal.util.DateTimeUtil
import com.midtrans.sdk.uikit.internal.util.DateTimeUtil.TIME_ZONE_UTC
import java.util.concurrent.TimeUnit
import javax.inject.Inject


internal class ConvenienceStoreViewModel @Inject constructor(
    private val snapCore: SnapCore,
    private val datetimeUtil: DateTimeUtil,
    private val errorCard: ErrorCard
) : ViewModel() {

    val barCodeBitmapLiveData = MutableLiveData<Bitmap>()
    val pdfUrlLiveData = MutableLiveData<String>()
    val paymentCodeLiveData = MutableLiveData<String>()
    var expiredTime = datetimeUtil.getCurrentMillis() + TimeUnit.MINUTES.toMillis(15)
    val errorLiveData = MutableLiveData<Int?>()

    fun chargeConvenienceStorePayment(
        snapToken: String,
        @PaymentType.Def paymentType: String
    ) {
        val requestBuilder = ConvenienceStorePaymentRequestBuilder().withPaymentType(paymentType)
        snapCore.pay(
            snapToken = snapToken,
            paymentRequestBuilder = requestBuilder,
            callback = object : Callback<TransactionResponse> {
                override fun onSuccess(result: TransactionResponse) {
                    result.run {
                        paymentCode?.let { generateBarcode(it) }
                        paymentCode?.let { paymentCodeLiveData.value = it }
                        indomaretExpirationRaw?.let { expiredTime = parseTime(it) }
                        alfamartExpirationRaw?.let { expiredTime = parseTime(it) }
                        pdfUrl.let { pdfUrlLiveData.value = it }
                    }
                }

                override fun onError(error: SnapError) {
                    errorLiveData.value = errorCard.getErrorCardType(error)
                }
            }
        )
    }

    private fun generateBarcode(barcode: String){
        try {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap: Bitmap =
                barcodeEncoder.encodeBitmap(barcode, BarcodeFormat.CODE_39, 1000, 100)
            barCodeBitmapLiveData.value = bitmap
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

    fun resetError(){
        errorLiveData.value = null
    }
    fun getExpiredHour(): String = datetimeUtil.getExpiredHour(expiredTime)

    companion object{
        const val DATE_FORMAT = "yyyy-MM-dd hh:mm Z"
    }
}