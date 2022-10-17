package com.midtrans.sdk.uikit.internal.presentation.loadingpayment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.midtrans.sdk.corekit.api.model.*
import com.midtrans.sdk.corekit.internal.network.model.request.BankTransferRequest
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.internal.model.PaymentMethodItem
import com.midtrans.sdk.uikit.internal.presentation.paymentoption.PaymentOptionActivity
import com.midtrans.sdk.uikit.internal.view.AnimatedIcon

class LoadingPaymentActivity : BaseActivity() {

    companion object {
        private const val EXTRA_TRANSACTION_DETAIL =
            "loadingPaymentActivity.extra.transaction_detail"
        private const val EXTRA_SNAP_TOKEN = "loadingPaymentActivity.extra.snap_token"
        private const val EXTRA_CUSTOMER_DETAILS = "loadingPaymentActivity.extra.customer_details"
        private const val EXTRA_ITEM_DETAILS = "loadingPaymentActivity.extra.item_details"
        private const val EXTRA_CREDIT_CARD = "loadingPaymentActivity.extra.credit_card"
        private const val EXTRA_USER_ID = "loadingPaymentActivity.extra.user_id"
        private const val EXTRA_PERMATA_VA = "loadingPaymentActivity.extra.permata_va"
        private const val EXTRA_BCA_VA = "loadingPaymentActivity.extra.bca_va"
        private const val EXTRA_BNI_VA = "loadingPaymentActivity.extra.bni_va"
        private const val EXTRA_BRI_VA = "loadingPaymentActivity.extra.bri_va"
        private const val EXTRA_ENABLED_PAYMENTS = "loadingPaymentActivity.extra.enabled_payments"
        private const val EXTRA_EXPIRY = "loadingPaymentActivity.extra.expiry"
        private const val EXTRA_PROMO = "loadingPaymentActivity.extra.promo"
        private const val EXTRA_CUSTOM_FIELD1 = "loadingPaymentActivity.extra.custom_field1"
        private const val EXTRA_CUSTOM_FIELD2 = "loadingPaymentActivity.extra.custom_field2"
        private const val EXTRA_CUSTOM_FIELD3 = "loadingPaymentActivity.extra.custom_field3"
        private const val EXTRA_GOPAY_CALLBACK = "loadingPaymentActivity.extra.gopay_callback"
        private const val EXTRA_SHOPEEPAY_CALLBACK =
            "loadingPaymentActivity.extra.shopeepay_callback"
        private const val EXTRA_UOB_EZPAY_CALLBACK =
            "loadingPaymentActivity.extra.uob_ezpay_callback"
        private const val EXTRA_PAYMENT_TYPE = "loadingPaymentActivity.extra.payment_type"

        fun getLoadingPaymentIntent(
            activityContext: Context,
            transactionDetails: SnapTransactionDetail,
            snapToken: String? = null,
            customerDetails: CustomerDetails? = null,
            itemDetails: List<ItemDetails>? = null,
            creditCard: CreditCard? = null,
            userId: String? = null,
            permataVa: BankTransferRequest? = null,
            bcaVa: BankTransferRequest? = null,
            bniVa: BankTransferRequest? = null,
            briVa: BankTransferRequest? = null,
            enabledPayments: List<String>? = null,
            expiry: Expiry? = null,
            promoRequest: PromoRequest? = null,
            customField1: String? = null,
            customField2: String? = null,
            customField3: String? = null,
            gopayCallback: GopayPaymentCallback? = null,
            shopeepayCallback: PaymentCallback? = null,
            uobEzpayCallback: PaymentCallback? = null,
            paymentType: PaymentMethodItem? = null
        ): Intent {
            return Intent(activityContext, LoadingPaymentActivity::class.java).apply {
                putExtra(EXTRA_TRANSACTION_DETAIL, transactionDetails)
                putExtra(EXTRA_SNAP_TOKEN, snapToken)
                putExtra(EXTRA_CUSTOMER_DETAILS, customerDetails)
                putExtra(EXTRA_ITEM_DETAILS, itemDetails?.let { ArrayList(it) })
                putExtra(EXTRA_CREDIT_CARD, creditCard)
                putExtra(EXTRA_USER_ID, userId)
                putExtra(EXTRA_PERMATA_VA, permataVa)
                putExtra(EXTRA_BCA_VA, bcaVa)
                putExtra(EXTRA_BNI_VA, bniVa)
                putExtra(EXTRA_BRI_VA, briVa)
                putStringArrayListExtra(EXTRA_ENABLED_PAYMENTS, enabledPayments?.let { ArrayList(it) })
                putExtra(EXTRA_EXPIRY, expiry)
                putExtra(EXTRA_PROMO, promoRequest)
                putExtra(EXTRA_CUSTOM_FIELD1, customField1)
                putExtra(EXTRA_CUSTOM_FIELD2, customField2)
                putExtra(EXTRA_CUSTOM_FIELD3, customField3)
                putExtra(EXTRA_GOPAY_CALLBACK, gopayCallback)
                putExtra(EXTRA_SHOPEEPAY_CALLBACK, shopeepayCallback)
                putExtra(EXTRA_UOB_EZPAY_CALLBACK, uobEzpayCallback)
                putExtra(EXTRA_PAYMENT_TYPE, paymentType)
            }
        }
    }

    private val transactionDetails: SnapTransactionDetail by lazy {
        intent.getParcelableExtra(EXTRA_TRANSACTION_DETAIL) as? SnapTransactionDetail
            ?: throw RuntimeException("Transaction detail must be present")
    }
    private val snapToken: String? by lazy {
        intent.getStringExtra(EXTRA_SNAP_TOKEN)
    }
    private val customerDetails: CustomerDetails? by lazy {
        intent.getParcelableExtra(EXTRA_CUSTOMER_DETAILS) as? CustomerDetails
    }
    private val itemDetails: List<ItemDetails>? by lazy {
        intent.getParcelableArrayListExtra(EXTRA_ITEM_DETAILS)
    }
    private val creditCard: CreditCard? by lazy {
        intent.getParcelableExtra(EXTRA_CREDIT_CARD) as? CreditCard
    }
    private val userId: String? by lazy {
        intent.getStringExtra(EXTRA_USER_ID)
    }
    private val permataVa: BankTransferRequest? by lazy {
        intent.getParcelableExtra(EXTRA_PERMATA_VA) as? BankTransferRequest
    }
    private val bcaVa: BankTransferRequest? by lazy {
        intent.getParcelableExtra(EXTRA_BCA_VA) as? BankTransferRequest
    }
    private val bniVa: BankTransferRequest? by lazy {
        intent.getParcelableExtra(EXTRA_BNI_VA) as? BankTransferRequest
    }
    private val briVa: BankTransferRequest? by lazy {
        intent.getParcelableExtra(EXTRA_BRI_VA) as? BankTransferRequest
    }
    private val enabledPayments: List<String>? by lazy {
        intent.getStringArrayListExtra(EXTRA_ENABLED_PAYMENTS)
    }
    private val expiry: Expiry? by lazy {
        intent.getParcelableExtra(EXTRA_EXPIRY) as? Expiry
    }
    private val promoRequest: PromoRequest? by lazy {
        intent.getParcelableExtra(EXTRA_PROMO) as? PromoRequest
    }
    private val customField1: String? by lazy {
        intent.getStringExtra(EXTRA_CUSTOM_FIELD1)
    }
    private val customField2: String? by lazy {
        intent.getStringExtra(EXTRA_CUSTOM_FIELD2)
    }
    private val customField3: String? by lazy {
        intent.getStringExtra(EXTRA_CUSTOM_FIELD3)
    }
    private val gopayCallback: GopayPaymentCallback? by lazy {
        intent.getSerializableExtra(EXTRA_GOPAY_CALLBACK) as? GopayPaymentCallback
    }
    private val shopeepayCallback: PaymentCallback? by lazy {
        intent.getSerializableExtra(EXTRA_SHOPEEPAY_CALLBACK) as? PaymentCallback
    }
    private val uobEzpayCallback: PaymentCallback? by lazy {
        intent.getSerializableExtra(EXTRA_UOB_EZPAY_CALLBACK) as? PaymentCallback
    }
    private val paymentType: PaymentMethodItem? by lazy {
        intent.getParcelableExtra(EXTRA_PAYMENT_TYPE)
    }
    private val viewModel: LoadingPaymentViewModel by lazy {
        ViewModelProvider(this).get(LoadingPaymentViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
        setContent {
            LoadAnimation()
        }
        viewModel.getPaymentOption(
            transactionDetails = transactionDetails,
            snapToken = snapToken,
            customerDetails = customerDetails,
            itemDetails = itemDetails,
            creditCard = creditCard,
            userId = userId,
            permataVa = permataVa,
            bcaVa = bcaVa,
            bniVa = bniVa,
            briVa = briVa,
            enabledPayments = enabledPayments,
            expiry = expiry,
            promoRequest = promoRequest,
            customField1 = customField1,
            customField2 = customField2,
            customField3 = customField3,
            gopayCallback = gopayCallback,
            shopeepayCallback = shopeepayCallback,
            uobEzpayCallback = uobEzpayCallback
        )
    }

    private fun initObserver() {
        viewModel.getPaymentOptionLiveData().observe(this, Observer {
            val intent = PaymentOptionActivity.openPaymentOptionPage(
                activityContext = this,
                snapToken = it.token,
                totalAmount = viewModel.getAmountInString(transactionDetails),
                transactionDetail = it.transactionDetails,
                orderId = viewModel.getOrderId(transactionDetails),
                paymentList = it.options,
                customerDetails = customerDetails,
                creditCard = it.creditCard,
                promos = it.promos,
                merchant = it.merchantData,
                expiryTime = it.expiryTme,
                paymentType = paymentType
            )
            startActivity(intent)
            finish()
        })
        viewModel.getErrorLiveData().observe(this, Observer {
            //TODO revisit this error handle after discussing how to handle error when we are unable to get snap token / trx detail during loading screen
            Toast.makeText(this, "Error caught ${it.javaClass.simpleName}", Toast.LENGTH_SHORT)
                .show()
        })
    }

    @Composable
    private fun LoadAnimation() {
        Column(
            modifier = Modifier.fillMaxSize(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedIcon(resId = R.drawable.ic_midtrans_animated).start()
        }
    }
}