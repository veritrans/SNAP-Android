package com.midtrans.sdk.uikit.internal.presentation.directdebit

import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.internal.analytics.PageName
import com.midtrans.sdk.uikit.internal.base.BaseViewModel
import com.midtrans.sdk.uikit.internal.util.DateTimeUtil
import javax.inject.Inject

internal class UobSelectionViewModel @Inject constructor(
    snapCore: SnapCore,
    private val dateTimeUtil: DateTimeUtil
): BaseViewModel() {

    init {
        eventAnalytics = snapCore.getEventAnalytics()
    }

    private var expiredTime = dateTimeUtil.plusDateBy(dateTimeUtil.getCurrentMillis(), 1) //TODO temporary is 24H, later get value from request snap if set

    fun getExpiredTime() = expiredTime

    fun getExpiredHour() = dateTimeUtil.getExpiredHour(expiredTime)

    fun trackOrderDetailsViewed() {
        trackOrderDetailsViewed(
            pageName = PageName.UOB_SELECTION_PAGE,
            paymentMethodName = PaymentType.UOB_EZPAY
        )
    }

    fun trackPageViewed() {
        trackPageViewed(
            pageName = PageName.UOB_SELECTION_PAGE,
            paymentMethodName = PaymentType.UOB_EZPAY
        )
    }
}