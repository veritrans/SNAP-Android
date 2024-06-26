package com.midtrans.sdk.uikit.external

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.ui.text.font.FontFamily
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.core.Logger
import com.midtrans.sdk.corekit.core.PaymentMethod
import com.midtrans.sdk.corekit.models.BcaBankTransferRequestModel
import com.midtrans.sdk.corekit.models.BillingAddress
import com.midtrans.sdk.corekit.models.ExpiryModel
import com.midtrans.sdk.corekit.models.PermataBankTransferRequestModel
import com.midtrans.sdk.corekit.models.ShippingAddress
import com.midtrans.sdk.corekit.models.snap.BankTransferRequestModel
import com.midtrans.sdk.uikit.api.callback.Callback
import com.midtrans.sdk.uikit.api.model.*
import com.midtrans.sdk.uikit.internal.di.DaggerUiKitComponent
import com.midtrans.sdk.uikit.internal.di.UiKitComponent
import com.midtrans.sdk.uikit.internal.model.PaymentTypeItem
import com.midtrans.sdk.uikit.internal.presentation.loadingpayment.LoadingPaymentActivity
import com.midtrans.sdk.uikit.internal.util.AssetFontLoader.fontFamily
import java.lang.ref.WeakReference

class UiKitApi private constructor(val builder: Builder) {

    internal val daggerComponent: UiKitComponent by lazy {
        DaggerUiKitComponent.builder().applicationContext(builder.context).build()
    }

    private var isMerchantUrlAvailable = true

    init {
        setInstance(this)
        buildCoreKit(builder)
    }

    private fun buildCoreKit(builder: Builder) {
        builder.run {
            Logger.enabled = enableLog
            if (merchantUrl.isEmpty()) {
                isMerchantUrlAvailable = false
            } else {
                SnapCore.Builder()
                    .withContext(context)
                    .withMerchantUrl(merchantUrl)
                    .withMerchantClientKey(merchantClientKey)
                    .enableLog(enableLog)
                    .build()
            }
        }
    }

    private fun getPaymentType(paymentMethod: PaymentMethod?): PaymentTypeItem? {
        return when (paymentMethod) {
            PaymentMethod.CREDIT_CARD -> PaymentTypeItem(PaymentType.CREDIT_CARD, null)
            PaymentMethod.BANK_TRANSFER -> PaymentTypeItem(PaymentType.BANK_TRANSFER, null)
            PaymentMethod.BANK_TRANSFER_BCA -> PaymentTypeItem(
                PaymentType.BANK_TRANSFER,
                PaymentType.BCA_VA
            )
            PaymentMethod.BANK_TRANSFER_PERMATA -> PaymentTypeItem(
                PaymentType.BANK_TRANSFER,
                PaymentType.PERMATA_VA
            )
            PaymentMethod.BANK_TRANSFER_MANDIRI -> PaymentTypeItem(
                PaymentType.BANK_TRANSFER,
                PaymentType.E_CHANNEL
            )
            PaymentMethod.SHOPEEPAY -> PaymentTypeItem(PaymentType.SHOPEEPAY, null)
            PaymentMethod.BANK_TRANSFER_BNI -> PaymentTypeItem(
                PaymentType.BANK_TRANSFER,
                PaymentType.BNI_VA
            )
            PaymentMethod.BANK_TRANSFER_BRI -> PaymentTypeItem(
                PaymentType.BANK_TRANSFER,
                PaymentType.BRI_VA
            )
            PaymentMethod.BANK_TRANSFER_CIMB -> PaymentTypeItem(
                PaymentType.BANK_TRANSFER,
                PaymentType.CIMB_VA
            )
            PaymentMethod.BANK_TRANSFER_OTHER -> PaymentTypeItem(
                PaymentType.BANK_TRANSFER,
                PaymentType.OTHER_VA
            )
            PaymentMethod.GO_PAY -> PaymentTypeItem(PaymentType.GOPAY, null)
            PaymentMethod.BCA_KLIKPAY -> PaymentTypeItem(PaymentType.BCA_KLIKPAY, null)
            PaymentMethod.KLIKBCA -> PaymentTypeItem(PaymentType.KLIK_BCA, null)
            PaymentMethod.CIMB_CLICKS -> PaymentTypeItem(PaymentType.CIMB_CLICKS, null)
            PaymentMethod.EPAY_BRI -> PaymentTypeItem(PaymentType.BRI_EPAY, null)
            PaymentMethod.DANAMON_ONLINE -> PaymentTypeItem(PaymentType.DANAMON_ONLINE, null)
            PaymentMethod.INDOMARET -> PaymentTypeItem(PaymentType.INDOMARET, null)
            PaymentMethod.AKULAKU -> PaymentTypeItem(PaymentType.AKULAKU, null)
            PaymentMethod.KREDIVO -> PaymentTypeItem(PaymentType.KREDIVO, null)
            PaymentMethod.ALFAMART -> PaymentTypeItem(PaymentType.ALFAMART, null)
            PaymentMethod.UOB_EZPAY -> PaymentTypeItem(PaymentType.UOB_EZPAY, null)
            PaymentMethod.UOB_EZPAY_APP -> PaymentTypeItem(
                PaymentType.UOB_EZPAY,
                PaymentType.UOB_EZPAY_APP
            )
            PaymentMethod.UOB_EZPAY_WEB -> PaymentTypeItem(
                PaymentType.UOB_EZPAY,
                PaymentType.UOB_EZPAY_WEB
            )
            else -> null
        }
    }

    internal fun getPaymentCallback() = paymentCallback
    var customColors = builder.customColors
    var customFontFamily = builder.fontFamily
    val uiKitSetting = builder.uiKitSetting

    fun startPaymentUiFlow(
        activity: Activity,
        launcher: ActivityResultLauncher<Intent>,
        transactionDetails: SnapTransactionDetail,
        customerDetails: CustomerDetails? = null,
        itemDetails: List<ItemDetails>,
        creditCard: CreditCard? = null,
        userId: String? = null,
        uobEzpayCallback: PaymentCallback? = null,
        gopayCallback: GopayPaymentCallback? = null,
        shopeepayCallback: PaymentCallback? = null,
        snapTokenExpiry: Expiry? = null,
        paymentMethod: PaymentMethod? = null,
        enabledPayment: List<String>? = null,
        permataVa: BankTransferRequest? = null,
        bcaVa: BankTransferRequest? = null,
        bniVa: BankTransferRequest? = null,
        briVa: BankTransferRequest? = null,
        cimbVa: BankTransferRequest? = null,
        customField1: String? = null,
        customField2: String? = null,
        customField3: String? = null,
    ) {
        val intent = LoadingPaymentActivity.getLoadingPaymentIntent(
            activityContext = activity,
            transactionDetails = transactionDetails,
            customerDetails = customerDetails,
            itemDetails = itemDetails,
            creditCard = creditCard,
            userId = userId,
            gopayCallback = gopayCallback,
            shopeepayCallback = shopeepayCallback,
            uobEzpayCallback = uobEzpayCallback,
            paymentType = getPaymentType(paymentMethod),
            expiry = snapTokenExpiry,
            enabledPayments = enabledPayment,
            permataVa = permataVa,
            bcaVa = bcaVa,
            bniVa = bniVa,
            briVa = briVa,
            cimbVa = cimbVa,
            customField1 = customField1,
            customField2 = customField2,
            customField3 = customField3,
            isMerchantUrlAvailable = isMerchantUrlAvailable,
            isSnapTokenAvailable = true
        )
        launcher.launch(intent)
    }

    fun startPaymentUiFlow(
        activity: Activity,
        launcher: ActivityResultLauncher<Intent>,
        snapToken: String?,
        paymentMethod: PaymentMethod? = null
    ) {
        var isSnapTokenAvailable = true
        if (snapToken.isNullOrEmpty()) isSnapTokenAvailable = false

        val intent = LoadingPaymentActivity.getLoadingPaymentIntent(
            activityContext = activity,
            snapToken = snapToken,
            transactionDetails = SnapTransactionDetail("", 0.0),
            isMerchantUrlAvailable = isMerchantUrlAvailable,
            isSnapTokenAvailable = isSnapTokenAvailable,
            paymentType = getPaymentType(paymentMethod)
        )
        launcher.launch(intent)
    }

    fun runPaymentLegacy(
        activityContext: Context,
        transactionDetails: SnapTransactionDetail,
        customerDetails: com.midtrans.sdk.corekit.models.CustomerDetails? = null,
        itemDetails: List<ItemDetails>,
        creditCard: com.midtrans.sdk.corekit.models.snap.CreditCard? = null,
        userId: String? = null,
        uobEzpayCallback: PaymentCallback? = null,
        paymentCallback: Callback<TransactionResult>,
        snapTokenExpiry: ExpiryModel? = null,
        paymentMethod: PaymentMethod? = null,
        enabledPayment: List<String>? = null,
        permataVa: PermataBankTransferRequestModel? = null,
        bcaVa: BcaBankTransferRequestModel? = null,
        bniVa: BankTransferRequestModel? = null,
        briVa: BankTransferRequestModel? = null,
        cimbVa: BankTransferRequestModel? = null,
        gopayCallback: GopayPaymentCallback? = null,
        shopeepayCallback: PaymentCallback? = null,
        customField1: String? = null,
        customField2: String? = null,
        customField3: String? = null,
    ) {
        UiKitApi.paymentCallback = paymentCallback

        val intent = LoadingPaymentActivity.getLoadingPaymentIntent(
            activityContext = activityContext,
            transactionDetails = transactionDetails,
            customerDetails = convertToRevamp(customerDetails),
            itemDetails = itemDetails,
            creditCard = convertToRevamp(creditCard),
            userId = userId,
            paymentType = getPaymentType(paymentMethod),
            expiry = convertToRevamp(snapTokenExpiry),
            enabledPayments = enabledPayment,
            permataVa = convertToRevamp(permataVa),
            bcaVa = convertToRevamp(bcaVa),
            bniVa = convertToRevamp(bniVa),
            briVa = convertToRevamp(briVa),
            cimbVa = convertToRevamp(cimbVa),
            gopayCallback = gopayCallback,
            shopeepayCallback = shopeepayCallback,
            uobEzpayCallback = uobEzpayCallback,
            customField1 = customField1,
            customField2 = customField2,
            customField3 = customField3,
            isMerchantUrlAvailable = isMerchantUrlAvailable,
            isSnapTokenAvailable = true
        )
        activityContext.startActivity(intent)
    }

    private fun convertToRevamp(creditCard: com.midtrans.sdk.corekit.models.snap.CreditCard?): CreditCard? {
        return creditCard?.let {
            CreditCard(it.isSaveCard, it.tokenId, it.authentication, it.channel, it.bank, it.savedTokens, it.whitelistBins, it.blacklistBins, convertToRevamp(it.installment), it.type)
        }
    }

    private fun convertToRevamp(installment: com.midtrans.sdk.corekit.models.snap.Installment?): Installment? {
        return installment?.let {
            Installment(installment.isRequired, installment.terms)
        }
    }

    private fun convertToRevamp(permataVa: PermataBankTransferRequestModel?): com.midtrans.sdk.corekit.api.model.BankTransferRequest? {
        return permataVa?.let {
            com.midtrans.sdk.corekit.api.model.BankTransferRequest(it.vaNumber, null, null, it.recipientName)
        }
    }

    private fun convertToRevamp(va: BankTransferRequestModel?): com.midtrans.sdk.corekit.api.model.BankTransferRequest? {
        return va?.let {
            com.midtrans.sdk.corekit.api.model.BankTransferRequest(it.vaNumber, null, null, null)
        }
    }

    private fun convertToRevamp(bcaVa: BcaBankTransferRequestModel?): com.midtrans.sdk.corekit.api.model.BankTransferRequest? {
        return bcaVa?.let {
            com.midtrans.sdk.corekit.api.model.BankTransferRequest(it.vaNumber, it.freeText, it.subCompanyCode, null)
        }
    }

    private fun convertToRevamp(expiry: ExpiryModel?): Expiry? {
        return expiry?.let {
            Expiry(it.startTime, it.unit, it.duration)
        }
    }

    private fun convertToRevamp(customerDetails: com.midtrans.sdk.corekit.models.CustomerDetails?): CustomerDetails? {
        return customerDetails?.let {
            CustomerDetails(
                it.customerIdentifier,
                it.firstName,
                it.lastName,
                it.email,
                it.phone,
                convertToRevamp(it.shippingAddress),
                convertToRevamp(it.billingAddress)
            )
        }
    }

    private fun convertToRevamp(billingAddress: BillingAddress?): Address? {
        return billingAddress?.let {
            Address(
                it.firstName,
                it.lastName,
                it.address,
                it.city,
                it.postalCode,
                it.phone,
                it.countryCode
            )
        }
    }

    private fun convertToRevamp(shippingAddress: ShippingAddress?): Address? {
        return shippingAddress?.let {
            Address(
                it.firstName,
                it.lastName,
                it.address,
                it.city,
                it.postalCode,
                it.phone,
                it.countryCode
            )
        }
    }

    //Snap Token Flow
    fun runPaymentTokenLegacy(
        activityContext: Context,
        snapToken: String? = null,
        paymentMethod: PaymentMethod? = null,
        paymentCallback: Callback<TransactionResult>
    ) {
        UiKitApi.paymentCallback = paymentCallback

        var isSnapTokenAvailable = true
        if (snapToken.isNullOrEmpty()) isSnapTokenAvailable = false

        val intent = LoadingPaymentActivity.getLoadingPaymentIntent(
            activityContext = activityContext,
            snapToken = snapToken,
            transactionDetails = com.midtrans.sdk.corekit.api.model.SnapTransactionDetail("", 0.0),
            paymentType = getPaymentType(paymentMethod),
            isMerchantUrlAvailable = isMerchantUrlAvailable,
            isSnapTokenAvailable = isSnapTokenAvailable
        )
        activityContext.startActivity(intent)
    }

    class Builder {
        internal lateinit var context: Context
        internal lateinit var merchantUrl: String
        internal lateinit var merchantClientKey: String
        internal var customColors: CustomColors? = null
        internal var fontFamily: FontFamily? = null
        internal var uiKitSetting: UiKitSetting = UiKitSetting()
        internal var enableLog: Boolean = false

        private fun customColorThemeToCustomColors(customColorTheme: CustomColorTheme): CustomColors {
            return CustomColors(customColorTheme)
        }

        private fun stringToFamilyFont(fontFamily: String) : FontFamily {
            return fontFamily(fontFamily, context)
        }

        fun withContext(context: Context) = apply {
            this.context = context.applicationContext
        }

        fun withMerchantUrl(merchantUrl: String) = apply {
            this.merchantUrl = merchantUrl
        }

        fun withMerchantClientKey(merchantClientKey: String) = apply {
            this.merchantClientKey = merchantClientKey
        }

        fun withCustomColors(customColors: CustomColors) = apply {
            this.customColors = customColors
        }

        fun withColorTheme(customColorTheme: CustomColorTheme) = apply {
            this.customColors = customColorThemeToCustomColors(customColorTheme)
        }

        fun withFontFamily(fontFamily: String) = apply {
            this.fontFamily = stringToFamilyFont(fontFamily)
        }

        fun enableLog(enabled: Boolean) = apply {
            this.enableLog = enabled
        }

        @Throws(RuntimeException::class)
        fun build(): UiKitApi {
            if (!this::context.isInitialized) {
                throw Throwable(message = "context is required")
            }
            if (!this::merchantUrl.isInitialized) {
                throw Throwable(message = "merchantUrl is required")
            }
            if (!this::merchantClientKey.isInitialized) {
                throw Throwable(message = "merchantClientKey is required")
            }
            UiKitApi(this)

            return instance!!
        }
    }

    companion object {
        private var paymentCallbackWeakReference: WeakReference<Callback<TransactionResult>?> =
            WeakReference(null)
        private var paymentCallback: Callback<TransactionResult>?
            private set(value) {
                paymentCallbackWeakReference = WeakReference(value)
            }
            get() = paymentCallbackWeakReference.get()

        private var instance: UiKitApi? = null


        fun getDefaultInstance(): UiKitApi {
            return instance!!
        }

        //TODO: should getDefault Instance always not - null?
        internal fun getDefaultInstanceNullable(): UiKitApi? {
            return instance
        }

        private fun setInstance(uiKitApi: UiKitApi) {
            instance = uiKitApi
        }
    }
}
