package com.midtrans.sdk.uikit.internal.presentation.banktransfer

import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.uikit.internal.base.BaseViewModel
import javax.inject.Inject

internal class BankTransferListViewModel @Inject constructor(
    snapCore: SnapCore
) : BaseViewModel() {

    init {
        eventAnalytics = snapCore.getEventAnalytics()
    }

    fun trackOrderDetailsViewed() {
        trackOrderDetailsViewed(
            paymentMethodName = PaymentType.BANK_TRANSFER
        )
    }

    fun trackPageViewed() {
        trackPageViewed(
            paymentMethodName = PaymentType.BANK_TRANSFER
        )
    }
}