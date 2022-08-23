package com.midtrans.sdk.uikit.external

import android.content.Context
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.model.CustomerDetails
import com.midtrans.sdk.corekit.api.model.SnapTransactionDetail
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.uikit.internal.presentation.loadingpayment.LoadingPaymentActivity
import java.util.*

class UiKitApi {
    lateinit var paymentCallback: Callback<TransactionResponse>

    init {
        setInstance(this)
    }

    fun startPayment( //TODO revisit this implementation, currently for showing status in Sample App
        activityContext: Context,
        transactionDetails: SnapTransactionDetail,
        customerDetail: CustomerDetails,
        paymentCallback: Callback<TransactionResponse>
    ) {
        this.paymentCallback = paymentCallback

        val intent = LoadingPaymentActivity.getLoadingPaymentIntent(
            activityContext = activityContext,
            transactionDetails = SnapTransactionDetail(
                orderId = UUID.randomUUID().toString(),
                grossAmount = 15005.00
            ),
            customerDetails = CustomerDetails(
                firstName = "Ari",
                lastName = "Bhakti",
                email = "aribhakti@email.com",
                phone = "087788778212"
            )
        )
        activityContext.startActivity(intent)
    }

    companion object {
        private lateinit var instance: UiKitApi

        fun getDefaultInstance(): UiKitApi {
            return instance
        }

        private fun setInstance(uiKitApi: UiKitApi) {
            instance = uiKitApi
        }
    }
}
