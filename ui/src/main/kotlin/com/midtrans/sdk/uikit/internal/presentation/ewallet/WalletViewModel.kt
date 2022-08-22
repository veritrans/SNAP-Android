package com.midtrans.sdk.uikit.internal.presentation.ewallet

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.exception.SnapError
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.api.requestbuilder.payment.EWalletPaymentRequestBuilder
import com.midtrans.sdk.uikit.internal.util.DateTimeUtil
import java.util.*
import javax.inject.Inject

internal class WalletViewModel @Inject constructor(
    private val snapCore: SnapCore,
    private val datetimeUtil: DateTimeUtil
) : ViewModel() {

    val qrCodeUrlLiveData = MutableLiveData<String>()
    val deepLinkUrlLiveData = MutableLiveData<String>()
    val billingNumberLiveData = MutableLiveData<String>()
    var expiredTime = datetimeUtil.plusDateBy(datetimeUtil.getCurrentMillis(), 1)

fun chargeQrPayment(
        snapToken: String,
        @PaymentType.Def paymentType: String
    ) {
        val requestBuilder = EWalletPaymentRequestBuilder().withPaymentType(paymentType)
        snapCore.pay(
            snapToken = snapToken,
            paymentRequestBuilder = requestBuilder,
            callback = object : Callback<TransactionResponse> {
                override fun onSuccess(result: TransactionResponse) {
                    result.run {
                        qrCodeUrl?.let { qrCodeUrlLiveData.value = it }
                        qrisUrl?.let { qrCodeUrlLiveData.value = it }
                        deeplinkUrl?.let { deepLinkUrlLiveData.value = it }
                       // gopayExpirationRaw?.let { expiredTime.value  }
                    }
                }

                override fun onError(error: SnapError) {
                    TODO("Not yet implemented") // TODO: error dialog etc
                }
            }
        )
    }

    private fun parseTime(dateString: String): Long {
        val expCalendar = Calendar.getInstance()
        expCalendar.time =
            datetimeUtil.getDate(
                date = dateString.replace(" WIB", ""),
                dateFormat = DATE_FORMAT,
                timeZone = timeZoneUtc
            )
        expCalendar.set(Calendar.YEAR, datetimeUtil.getCalendar().get(Calendar.YEAR))
        return expCalendar.timeInMillis
    }

    fun getExpiredHour(): String {
        val duration = datetimeUtil.getDuration(
            datetimeUtil.getTimeDiffInMillis(
                datetimeUtil.getCurrentMillis(),
                expiredTime
            )
        )
        return String.format(
            "%02d:%02d:%02d",
            duration.toHours(),
            duration.seconds % 3600 / 60,
            duration.seconds % 60
        )
    }

    companion object {
        private const val DATE_FORMAT = "dd MMMM hh:mm"
        private const val TIME_FORMAT = "hh:mm:ss"
        private val timeZoneWib = TimeZone.getTimeZone("Asia/Jakarta")
        private val timeZoneUtc = TimeZone.getTimeZone("UTC")
    }
}