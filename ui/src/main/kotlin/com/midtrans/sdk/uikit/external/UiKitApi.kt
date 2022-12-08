package com.midtrans.sdk.uikit.external

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.ui.text.font.FontFamily
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.corekit.core.PaymentMethod
import com.midtrans.sdk.uikit.api.callback.Callback
import com.midtrans.sdk.uikit.api.model.*
import com.midtrans.sdk.uikit.internal.di.DaggerUiKitComponent
import com.midtrans.sdk.uikit.internal.di.UiKitComponent
import com.midtrans.sdk.uikit.internal.model.PaymentTypeItem
import com.midtrans.sdk.uikit.internal.presentation.loadingpayment.LoadingPaymentActivity
import java.lang.ref.WeakReference

class UiKitApi private constructor(val builder: Builder) {

    internal val daggerComponent: UiKitComponent by lazy {
        DaggerUiKitComponent.builder().applicationContext(builder.context).build()
    }

    init {
        setInstance(this)
        buildCoreKit(builder)
    }

    private fun buildCoreKit(builder: Builder) {
        builder.run {
            SnapCore.Builder()
                .withContext(context)
                .withMerchantUrl(merchantUrl)
                .withMerchantClientKey(merchantClientKey)
                .build()
        }
    }

    internal fun getPaymentCallback() = paymentCallback
    internal val customColors = builder.customColors
    internal val customFontFamily = builder.fontFamily
    val uiKitSetting = builder.uiKitSetting

    fun startPaymentWithAndroidX(
        activity: Activity,
        launcher: ActivityResultLauncher<Intent>,
        transactionDetails: SnapTransactionDetail,
        customerDetails: CustomerDetails,
        itemDetails: List<ItemDetails>,
        creditCard: CreditCard,
        userId: String,
        uobEzpayCallback: PaymentCallback? = null,
        snapTokenExpiry: Expiry? = null,
        paymentMethod: PaymentMethod? = null,
        enabledPayment: List<String>? = null,
        permataVa: BankTransferRequest? = null,
        bcaVa: BankTransferRequest? = null,
        bniVa: BankTransferRequest? = null,
        briVa: BankTransferRequest? = null
    ) {
        val intent = LoadingPaymentActivity.getLoadingPaymentIntent(
            activityContext = activity,
            transactionDetails = transactionDetails,
            customerDetails = customerDetails,
            itemDetails = itemDetails,
            creditCard = creditCard,
            userId = userId,
            uobEzpayCallback = uobEzpayCallback,
            paymentType = getPaymentType(paymentMethod),
            expiry = snapTokenExpiry,
            enabledPayments = enabledPayment,
            permataVa = permataVa,
            bcaVa = bcaVa,
            bniVa = bniVa,
            briVa = briVa
        )
        launcher.launch(intent)
    }

    private fun getPaymentType (paymentMethod: PaymentMethod?) : PaymentTypeItem? {
        return when (paymentMethod) {
            PaymentMethod.CREDIT_CARD -> PaymentTypeItem(PaymentType.CREDIT_CARD, null)
            PaymentMethod.BANK_TRANSFER -> PaymentTypeItem(PaymentType.BANK_TRANSFER, null)
            PaymentMethod.BANK_TRANSFER_BCA -> PaymentTypeItem(PaymentType.BANK_TRANSFER, PaymentType.BCA_VA)
            PaymentMethod.BANK_TRANSFER_PERMATA -> PaymentTypeItem(PaymentType.BANK_TRANSFER, PaymentType.PERMATA_VA)
            PaymentMethod.BANK_TRANSFER_MANDIRI -> PaymentTypeItem(PaymentType.BANK_TRANSFER, PaymentType.E_CHANNEL)
            PaymentMethod.SHOPEEPAY -> PaymentTypeItem(PaymentType.SHOPEEPAY, null)
            PaymentMethod.BANK_TRANSFER_BNI -> PaymentTypeItem(PaymentType.BANK_TRANSFER, PaymentType.BNI_VA)
            PaymentMethod.BANK_TRANSFER_BRI -> PaymentTypeItem(PaymentType.BANK_TRANSFER, PaymentType.BRI_VA)
            PaymentMethod.BANK_TRANSFER_OTHER -> PaymentTypeItem(PaymentType.BANK_TRANSFER, PaymentType.OTHER_VA)
            PaymentMethod.GO_PAY -> PaymentTypeItem(PaymentType.GOPAY, null)
            PaymentMethod.BCA_KLIKPAY -> PaymentTypeItem(PaymentType.BCA_KLIKPAY, null)
            PaymentMethod.KLIKBCA -> PaymentTypeItem(PaymentType.KLIK_BCA, null)
            PaymentMethod.CIMB_CLICKS -> PaymentTypeItem(PaymentType.CIMB_CLICKS, null)
            PaymentMethod.EPAY_BRI -> PaymentTypeItem(PaymentType.BRI_EPAY, null)
            PaymentMethod.DANAMON_ONLINE -> PaymentTypeItem(PaymentType.DANAMON_ONLINE, null)
            PaymentMethod.INDOMARET -> PaymentTypeItem(PaymentType.INDOMARET, null)
            PaymentMethod.AKULAKU -> PaymentTypeItem(PaymentType.AKULAKU, null)
            PaymentMethod.ALFAMART -> PaymentTypeItem(PaymentType.ALFAMART, null)
            PaymentMethod.UOB_EZPAY -> PaymentTypeItem(PaymentType.UOB_EZPAY, null)
            PaymentMethod.UOB_EZPAY_APP -> PaymentTypeItem(PaymentType.UOB_EZPAY, PaymentType.UOB_EZPAY_APP)
            PaymentMethod.UOB_EZPAY_WEB -> PaymentTypeItem(PaymentType.UOB_EZPAY, PaymentType.UOB_EZPAY_WEB)
            else -> null
        }
    }

    fun startPaymentWithAndroidXToken(
        activity: Activity,
        launcher: ActivityResultLauncher<Intent>,
        snapToken: String?
    ) {
        val intent = LoadingPaymentActivity.getLoadingPaymentIntent(
            activityContext = activity,
            snapToken = snapToken,
            transactionDetails = SnapTransactionDetail("", 0.0)
        )
        launcher.launch(intent)
    }

    fun startPaymentWithLegacyAndroid(
        activity: Activity,
        requestCode: Int,
        transactionDetails: SnapTransactionDetail,
        customerDetails: CustomerDetails,
        itemDetails: List<ItemDetails>,
        creditCard: CreditCard,
        userId: String,
        uobEzpayCallback: PaymentCallback? = null,
        snapTokenExpiry: Expiry? = null,
        paymentType: PaymentTypeItem? = null,
        enabledPayment: List<String>? = null,
        permataVa: BankTransferRequest? = null,
        bcaVa: BankTransferRequest? = null,
        bniVa: BankTransferRequest? = null,
        briVa: BankTransferRequest? = null
    ) {

        val intent = LoadingPaymentActivity.getLoadingPaymentIntent(
            activityContext = activity,
            transactionDetails = transactionDetails,
            customerDetails = customerDetails,
            itemDetails = itemDetails,
            creditCard = creditCard,
            userId = userId,
            uobEzpayCallback = uobEzpayCallback,
            paymentType = paymentType,
            expiry = snapTokenExpiry,
            enabledPayments = enabledPayment,
            permataVa = permataVa,
            bcaVa = bcaVa,
            bniVa = bniVa,
            briVa = briVa
        )
        activity.startActivityForResult(intent, requestCode)
    }

    fun startPayment(
        activityContext: Context,
        transactionDetails: SnapTransactionDetail,
        customerDetails: CustomerDetails,
        itemDetails: List<ItemDetails>,
        creditCard: CreditCard,
        userId: String,
        uobEzpayCallback: PaymentCallback,
        paymentCallback: Callback<TransactionResult>,
        snapTokenExpiry: Expiry? = null,
        paymentType: PaymentTypeItem? = null,
        enabledPayment: List<String>? = null,
        permataVa: BankTransferRequest? = null,
        bcaVa: BankTransferRequest? = null,
        bniVa: BankTransferRequest? = null,
        briVa: BankTransferRequest? = null
    ) {
        UiKitApi.paymentCallback = paymentCallback

        val intent = LoadingPaymentActivity.getLoadingPaymentIntent(
            activityContext = activityContext,
            transactionDetails = transactionDetails,
            customerDetails = customerDetails,
            itemDetails = itemDetails,
            creditCard = creditCard,
            userId = userId,
            uobEzpayCallback = uobEzpayCallback,
            paymentType = paymentType,
            expiry = snapTokenExpiry,
            enabledPayments = enabledPayment,
            permataVa = permataVa,
            bcaVa = bcaVa,
            bniVa = bniVa,
            briVa = briVa
        )
        activityContext.startActivity(intent)
    }

    //Snap Token Flow
    fun startPayment(
        activityContext: Context,
        snapToken: String? = null,
        paymentType: PaymentTypeItem? = null,
        paymentCallback: Callback<TransactionResult>
    ) {
        UiKitApi.paymentCallback = paymentCallback

        val intent = LoadingPaymentActivity.getLoadingPaymentIntent(
            activityContext = activityContext,
            snapToken = snapToken,
            transactionDetails = com.midtrans.sdk.corekit.api.model.SnapTransactionDetail("", 0.0),
            paymentType = paymentType
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

        fun withFontFamily(fontFamily: FontFamily) = apply {
            this.fontFamily = fontFamily
        }

        fun withUiKitSetting(uiKitSetting: UiKitSetting) = apply {
            this.uiKitSetting = uiKitSetting
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
