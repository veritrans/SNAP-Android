package com.midtrans.sdk.uikit.internal.presentation.ewallet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.exception.SnapError
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.api.model.TransactionResult
import com.midtrans.sdk.corekit.core.Logger
import com.midtrans.sdk.corekit.internal.analytics.PageName
import com.midtrans.sdk.uikit.internal.base.BaseViewModel
import com.midtrans.sdk.uikit.internal.util.UiKitConstants.STATUS_CODE_200
import com.midtrans.sdk.uikit.internal.util.UiKitConstants.STATUS_CODE_201
import com.midtrans.sdk.uikit.internal.util.UiKitConstants.STATUS_PENDING
import com.midtrans.sdk.uikit.internal.util.UiKitConstants.STATUS_SUCCESS
import javax.inject.Inject

internal class DeepLinkViewModel @Inject constructor(
    private val snapCore: SnapCore
) : BaseViewModel() {

    init {
        eventAnalytics = snapCore.getEventAnalytics()
    }

    private lateinit var paymentType: String
    private val _checkStatusResultLiveData = MutableLiveData<TransactionResult>()
    val checkStatusResultLiveData: LiveData<TransactionResult> = _checkStatusResultLiveData

    fun checkStatus(snapToken: String) {
        snapCore.getTransactionStatus(
            snapToken = snapToken,
            callback = object : Callback<TransactionResponse> {
                override fun onSuccess(result: TransactionResponse) {
                    Logger.d("Deep Link get transaction status succesfully")
                    result.run {
                        trackErrorStatusCode(
                            pageName = getPageName(this@DeepLinkViewModel.paymentType),
                            paymentMethodName = this@DeepLinkViewModel.paymentType,
                            errorMessage = statusMessage.orEmpty(),
                            statusCode = statusCode.orEmpty()
                        )
                        val status = when (statusCode) {
                            STATUS_CODE_200 -> STATUS_SUCCESS
                            STATUS_CODE_201 -> STATUS_PENDING
                            else -> transactionStatus.orEmpty()
                        }
                        _checkStatusResultLiveData.value =  TransactionResult(
                            status = status,
                            transactionId = transactionId.orEmpty(),
                            paymentType = this@DeepLinkViewModel.paymentType
                        )
                    }
                }

                override fun onError(error: SnapError) {
                    Logger.e("Deep Link error get transaction status")
                    trackSnapError(
                        pageName = getPageName(paymentType),
                        paymentMethodName = paymentType,
                        error = error
                    )
                }
            }
        )
    }

    fun setPaymentType(paymentType: String) {
        this.paymentType = paymentType
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

    fun trackPageViewed(stepNumber: Int) {
        trackPageViewed(
            pageName = getPageName(paymentType),
            paymentMethodName = paymentType,
            stepNumber = stepNumber.toString()
        )
    }

    private fun getPageName(paymentType: String): String {
        return when (paymentType) {
            PaymentType.GOPAY -> PageName.GOPAY_DEEPLINK_PAGE
            PaymentType.GOPAY_QRIS -> PageName.GOPAY_QR_PAGE
            PaymentType.SHOPEEPAY -> PageName.SHOPEEPAY_DEEPLINK_PAGE
            PaymentType.SHOPEEPAY_QRIS -> PageName.SHOPEEPAY_QR_PAGE
            else -> ""
        }
    }
}