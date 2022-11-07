package com.midtrans.sdk.uikit.internal.presentation.statusscreen

import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.internal.analytics.PageName
import com.midtrans.sdk.uikit.internal.base.BaseViewModel
import javax.inject.Inject

internal class SuccessScreenViewModel @Inject constructor(
    snapCore: SnapCore
): BaseViewModel() {

    init {
        eventAnalytics = snapCore.getEventAnalytics()
    }

    fun trackSnapButtonClicked(
        ctaName: String,
        paymentType: String
    ) {
        trackCtaClicked(
            ctaName = ctaName,
            paymentMethodName = paymentType,
            pageName = PageName.SUCCESS_PAGE
        )
    }
}