package com.midtrans.sdk.uikit.internal.presentation.directdebit

import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.internal.analytics.PageName
import com.midtrans.sdk.uikit.internal.base.BaseViewModel
import com.midtrans.sdk.uikit.internal.util.DateTimeUtil
import java.util.*
import javax.inject.Inject

internal class UobSelectionViewModel @Inject constructor(
    snapCore: SnapCore,
    private val dateTimeUtil: DateTimeUtil
) : BaseViewModel() {

    init {
        eventAnalytics = snapCore.getEventAnalytics()
    }

    private var expireTimeInMillis = 0L

    private fun parseTime(dateString: String): Long {
        val date = dateTimeUtil.getDate(
            date = dateString,
            dateFormat = DATE_FORMAT,
            timeZone = timeZoneUtc
        )
        return date.time
    }

    fun setExpiryTime(expireTime: String?) {
        expireTime?.let {
            expireTimeInMillis = parseTime(it)
        }
    }

    fun getExpiredHour(): String {
        val duration = dateTimeUtil.getDuration(
            dateTimeUtil.getTimeDiffInMillis(
                dateTimeUtil.getCurrentMillis(),
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

    fun trackOrderDetailsViewed() {
        trackOrderDetailsViewed(
            pageName = PageName.UOB_SELECTION_PAGE,
            paymentMethodName = PaymentType.UOB_EZPAY
        )
    }

    fun trackPageViewed(stepNumber: Int) {
        trackPageViewed(
            pageName = PageName.UOB_SELECTION_PAGE,
            paymentMethodName = PaymentType.UOB_EZPAY,
            stepNumber = stepNumber.toString()
        )
    }

    companion object {
        private const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss Z"
        private const val TIME_FORMAT = "%02d:%02d:%02d"
        private val timeZoneUtc = TimeZone.getTimeZone("UTC")
    }
}
