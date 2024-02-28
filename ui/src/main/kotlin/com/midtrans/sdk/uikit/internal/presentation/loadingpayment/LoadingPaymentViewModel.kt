package com.midtrans.sdk.uikit.internal.presentation.loadingpayment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.exception.SnapError
import com.midtrans.sdk.corekit.api.model.*
import com.midtrans.sdk.corekit.api.requestbuilder.snaptoken.SnapTokenRequestBuilder
import com.midtrans.sdk.corekit.core.Logger
import com.midtrans.sdk.corekit.internal.analytics.ClickstreamEventAnalytics
import com.midtrans.sdk.corekit.internal.analytics.ClickstreamTracker
import com.midtrans.sdk.corekit.internal.analytics.PageName
import com.midtrans.sdk.corekit.internal.network.model.response.TransactionDetails
import com.midtrans.sdk.uikit.internal.util.CurrencyFormat.currencyFormatRp
import javax.inject.Inject

//TODO will add UT and SnapCore manager after DI is setup
class LoadingPaymentViewModel @Inject constructor(
    snapCore: SnapCore
): ViewModel() {
    private val _paymentOptionLiveData = MutableLiveData<PaymentOption>()
    private val _error = MutableLiveData<SnapError>()
    private val eventAnalytics = snapCore.getEventAnalytics()
    private val clickstreamEventAnalytics = snapCore.getClickStreamEventAnalytics()

//    var clickstreamEventAnalytics = ClickstreamEventAnalytics(clickstreamTracker = ClickstreamTracker())

    fun getPaymentOptionLiveData(): LiveData<PaymentOption> = _paymentOptionLiveData
    fun getErrorLiveData(): LiveData<SnapError> = _error

    fun getPaymentOption(
        transactionDetails: SnapTransactionDetail,
        snapToken: String?,
        customerDetails: CustomerDetails?,
        itemDetails: List<ItemDetails>?,
        creditCard: CreditCard?,
        userId: String?,
        permataVa: BankTransferRequest?,
        bcaVa: BankTransferRequest?,
        bniVa: BankTransferRequest?,
        briVa: BankTransferRequest?,
        cimbVa: BankTransferRequest?,
        enabledPayments: List<String>?,
        expiry: Expiry?,
        promoRequest: PromoRequest?,
        customField1: String?,
        customField2: String?,
        customField3: String?,
        gopayCallback: GopayPaymentCallback?,
        shopeepayCallback: PaymentCallback?,
        uobEzpayCallback: PaymentCallback?
    ) {
        val builder = SnapTokenRequestBuilder()
            .withTransactionDetails(transactionDetails)
            .withCustomerDetails(customerDetails)
            .withItemDetails(itemDetails)
            .withCreditCard(creditCard)
            .withUserId(userId)
            .withPermataVa(permataVa)
            .withBcaVa(bcaVa)
            .withBniVa(bniVa)
            .withBriVa(briVa)
            .withEnabledPayments(enabledPayments)
            .withExpiry(expiry)
            .withPromo(promoRequest)
            .withCustomField1(customField1)
            .withCustomField2(customField2)
            .withCustomField3(customField3)
            .withGopayCallback(gopayCallback)
            .withShopeepayCallback(shopeepayCallback)
            .withUobEzpayCallback(uobEzpayCallback)

        SnapCore.getInstance()
            ?.getPaymentOption(snapToken = snapToken, builder = builder, object :
                Callback<PaymentOption> {
                override fun onSuccess(result: PaymentOption) {
                    Logger.d("Get Payment Option Succesfully")
                    _paymentOptionLiveData.postValue(result)
                }

                override fun onError(error: SnapError) {
                    Logger.e("Error Get Payment Option")
                    _error.postValue(error)
//                    eventAnalytics.trackSnapError(
//                        pageName = PageName.PAYMENT_LIST_PAGE,
//                        paymentMethodName = "not selected",
//                        errorMessage = error.getErrorInformation(),
//                        statusCode = error.getHttpStatusCode()?.toString()
//                    )
                    clickstreamEventAnalytics.trackSnapError(
                        pageName = PageName.PAYMENT_LIST_PAGE,
                        paymentMethodName = "not selected",
                        errorMessage = error.getErrorInformation(),
                        statusCode = error.getHttpStatusCode()?.toString()
                    )
                }
            })
    }

    fun getAmountInString(transactionDetails: TransactionDetails?): String {
        return transactionDetails?.grossAmount?.currencyFormatRp().orEmpty()
    }

    fun getOrderId(transactionDetails: TransactionDetails?): String {
        return transactionDetails?.orderId.orEmpty()
    }

    fun registerCommonProperties(
        isTablet: Boolean,
        merchantUrl: String
    ) {
        val platform = if (isTablet) "Tablet" else "Mobile"
//        eventAnalytics.registerCommonProperties(platform, merchantUrl)
        clickstreamEventAnalytics.registerCommonProperties(platform, merchantUrl)
    }
}