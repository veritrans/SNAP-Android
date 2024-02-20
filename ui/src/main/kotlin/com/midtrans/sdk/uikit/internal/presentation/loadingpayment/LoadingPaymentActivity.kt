package com.midtrans.sdk.uikit.internal.presentation.loadingpayment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.midtrans.sdk.corekit.api.model.*
import com.midtrans.sdk.corekit.core.Logger
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.api.model.PublicTransactionResult
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.internal.model.PaymentTypeItem
import com.midtrans.sdk.uikit.internal.presentation.paymentoption.PaymentOptionActivity
import com.midtrans.sdk.uikit.internal.presentation.statusscreen.ErrorScreenActivity
import com.midtrans.sdk.uikit.internal.presentation.statusscreen.InProgressActivity
import com.midtrans.sdk.uikit.internal.presentation.statusscreen.SuccessScreenActivity
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import com.midtrans.sdk.uikit.internal.util.UiKitConstants.STATUS_INVALID
import com.midtrans.sdk.uikit.internal.view.AnimatedIcon
import retrofit2.HttpException
import javax.inject.Inject

class LoadingPaymentActivity : BaseActivity() {

    companion object {
        private const val EXTRA_TRANSACTION_DETAIL = "loadingPaymentActivity.extra.transaction_detail"
        private const val EXTRA_SNAP_TOKEN = "loadingPaymentActivity.extra.snap_token"
        private const val EXTRA_CUSTOMER_DETAILS = "loadingPaymentActivity.extra.customer_details"
        private const val EXTRA_ITEM_DETAILS = "loadingPaymentActivity.extra.item_details"
        private const val EXTRA_CREDIT_CARD = "loadingPaymentActivity.extra.credit_card"
        private const val EXTRA_USER_ID = "loadingPaymentActivity.extra.user_id"
        private const val EXTRA_PERMATA_VA = "loadingPaymentActivity.extra.permata_va"
        private const val EXTRA_BCA_VA = "loadingPaymentActivity.extra.bca_va"
        private const val EXTRA_BNI_VA = "loadingPaymentActivity.extra.bni_va"
        private const val EXTRA_BRI_VA = "loadingPaymentActivity.extra.bri_va"
        private const val EXTRA_CIMB_VA = "loadingPaymentActivity.extra.cimb_va"
        private const val EXTRA_ENABLED_PAYMENTS = "loadingPaymentActivity.extra.enabled_payments"
        private const val EXTRA_EXPIRY = "loadingPaymentActivity.extra.expiry"
        private const val EXTRA_PROMO = "loadingPaymentActivity.extra.promo"
        private const val EXTRA_CUSTOM_FIELD1 = "loadingPaymentActivity.extra.custom_field1"
        private const val EXTRA_CUSTOM_FIELD2 = "loadingPaymentActivity.extra.custom_field2"
        private const val EXTRA_CUSTOM_FIELD3 = "loadingPaymentActivity.extra.custom_field3"
        private const val EXTRA_GOPAY_CALLBACK = "loadingPaymentActivity.extra.gopay_callback"
        private const val EXTRA_SHOPEEPAY_CALLBACK = "loadingPaymentActivity.extra.shopeepay_callback"
        private const val EXTRA_UOB_EZPAY_CALLBACK = "loadingPaymentActivity.extra.uob_ezpay_callback"
        private const val EXTRA_PAYMENT_TYPE = "loadingPaymentActivity.extra.payment_type"
        private const val EXTRA_SNAP_TOKEN_VALID = "loadingPaymentActivity.extra.is_snap_token_valid"
        private const val EXTRA_MERCHANT_URL_AVAILABLE = "loadingPaymentActivity.extra.is_merchant_url_available"

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
            cimbVa: BankTransferRequest? = null,
            enabledPayments: List<String>? = null,
            expiry: Expiry? = null,
            promoRequest: PromoRequest? = null,
            customField1: String? = null,
            customField2: String? = null,
            customField3: String? = null,
            gopayCallback: GopayPaymentCallback? = null,
            shopeepayCallback: PaymentCallback? = null,
            uobEzpayCallback: PaymentCallback? = null,
            paymentType: PaymentTypeItem? = null,
            isSnapTokenAvailable: Boolean,
            isMerchantUrlAvailable: Boolean
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
                putExtra(EXTRA_CIMB_VA, cimbVa)
                putStringArrayListExtra(
                    EXTRA_ENABLED_PAYMENTS,
                    enabledPayments?.let { ArrayList(it) })
                putExtra(EXTRA_EXPIRY, expiry)
                putExtra(EXTRA_PROMO, promoRequest)
                putExtra(EXTRA_CUSTOM_FIELD1, customField1)
                putExtra(EXTRA_CUSTOM_FIELD2, customField2)
                putExtra(EXTRA_CUSTOM_FIELD3, customField3)
                putExtra(EXTRA_GOPAY_CALLBACK, gopayCallback)
                putExtra(EXTRA_SHOPEEPAY_CALLBACK, shopeepayCallback)
                putExtra(EXTRA_UOB_EZPAY_CALLBACK, uobEzpayCallback)
                putExtra(EXTRA_PAYMENT_TYPE, paymentType)
                putExtra(EXTRA_SNAP_TOKEN_VALID, isSnapTokenAvailable)
                putExtra(EXTRA_MERCHANT_URL_AVAILABLE, isMerchantUrlAvailable)
            }
        }
    }

    @Inject
    internal lateinit var vmFactory: ViewModelProvider.Factory

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
    private val cimbVa: BankTransferRequest? by lazy {
        intent.getParcelableExtra(EXTRA_CIMB_VA) as? BankTransferRequest
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
    private val paymentType: PaymentTypeItem? by lazy {
        intent.getParcelableExtra(EXTRA_PAYMENT_TYPE)
    }
    private val isSnapTokenAvailable: Boolean by lazy {
        intent.getBooleanExtra(EXTRA_SNAP_TOKEN_VALID, true)
    }
    private val isMerchantUrlAvailable: Boolean by lazy {
        intent.getBooleanExtra(EXTRA_MERCHANT_URL_AVAILABLE, true)
    }
    private val viewModel: LoadingPaymentViewModel by lazy {
        ViewModelProvider(this, vmFactory).get(LoadingPaymentViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!isMerchantUrlAvailable) {
            Logger.e(resources.getString(R.string.invalid_merchant_base_url))
            val data = Intent()
            data.putExtra(
                UiKitConstants.KEY_TRANSACTION_RESULT,
                com.midtrans.sdk.uikit.api.model.TransactionResult(
                    status = STATUS_INVALID,
                    transactionId = "",
                    paymentType = "",
                    message = resources.getString(R.string.invalid_merchant_base_url)
                )
            )
            setResult(Activity.RESULT_OK, data)
            finish()
        } else if (!isSnapTokenAvailable) {
            Logger.e(resources.getString(R.string.invalid_snap_token))
            val data = Intent()
            data.putExtra(
                UiKitConstants.KEY_TRANSACTION_RESULT,
                com.midtrans.sdk.uikit.api.model.TransactionResult(
                    status = STATUS_INVALID,
                    transactionId = "",
                    paymentType = "",
                    message = resources.getString(R.string.invalid_snap_token)
                )
            )
            setResult(Activity.RESULT_OK, data)
            finish()
        } else {
            UiKitApi.getDefaultInstance().daggerComponent.inject(this)

            initObserver()
            setContent {
                LoadAnimation()
            }
            loadPaymentOptions()
        }
    }

    private fun loadPaymentOptions(token: String? = snapToken) {
        viewModel.registerCommonProperties(
            isTablet = isTabletDevice(),
            merchantUrl = UiKitApi.getDefaultInstance().builder.merchantUrl
        )
        viewModel.getPaymentOption(
            transactionDetails = transactionDetails,
            snapToken = token,
            customerDetails = customerDetails,
            itemDetails = itemDetails,
            creditCard = creditCard,
            userId = userId,
            permataVa = permataVa,
            bcaVa = bcaVa,
            bniVa = bniVa,
            briVa = briVa,
            cimbVa = cimbVa,
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

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    result?.run {
                        data?.also {
                            val transactionResult =
                                it.getParcelableExtra<TransactionResult>(UiKitConstants.KEY_TRANSACTION_RESULT)
                            val resultIntent = Intent()
                            transactionResult?.let {
                                val resultForHost = PublicTransactionResult(transactionResult)
                                resultIntent.putExtra(
                                    UiKitConstants.KEY_TRANSACTION_RESULT,
                                    resultForHost
                                )
                                setResult(RESULT_OK, resultIntent)
                                UiKitApi.getDefaultInstance().getPaymentCallback()
                                    ?.onSuccess(resultForHost)
                            }
                        } ?: setResult(Activity.RESULT_OK)
                    }
                    finish()
                }
                Activity.RESULT_CANCELED -> {
                    finish()
                }
                UiKitConstants.ACTIVITY_CC_CHANGES -> {
                    result?.run {
                        data?.also {
                            val snapToken = it.getStringExtra(UiKitConstants.KEY_SNAP_TOKEN)
                            loadPaymentOptions(snapToken)
                        }
                    }
                }
                else -> {
                    loadPaymentOptions()
                }
            }
        }

    private fun initObserver() {
        viewModel.getPaymentOptionLiveData().observe(this, Observer {
            val intent = PaymentOptionActivity.openPaymentOptionPage(
                activityContext = this,
                snapToken = it.token,
                totalAmount = viewModel.getAmountInString(it.transactionDetails),
                transactionDetail = it.transactionDetails,
                orderId = viewModel.getOrderId(it.transactionDetails),
                paymentList = it.options,
                customerDetails = customerDetails,
                itemDetails = itemDetails,
                creditCard = it.creditCard,
                promos = it.promos,
                merchant = it.merchantData,
                expiryTime = it.expiryTme,
                paymentTypeItem = paymentType,
                enabledPayments = it.enabledPayment,
                result = it.result
            )

            resultLauncher.launch(intent)
        })
        viewModel.getErrorLiveData().observe(this, Observer {
            //TODO revisit this error handle after discussing how to handle error when we are unable to get snap token / trx detail during loading screen
            if (it.cause is HttpException) {
                val exception: HttpException = it.cause as HttpException
                when (exception.code()) {
                    400 -> {
                        val expiredIntent = ErrorScreenActivity.getIntent(
                            activityContext = this@LoadingPaymentActivity,
                            title = resources.getString(R.string.expired_title),
                            content = resources.getString(R.string.expired_desc)
                        )
                        resultLauncher.launch(expiredIntent)
                    }
                    404 -> {
                        val transactionNotFoundIntent = ErrorScreenActivity.getIntent(
                            activityContext = this@LoadingPaymentActivity,
                            title = resources.getString(R.string.no_record_wrong_url_title),
                            content = resources.getString(R.string.no_record_wrong_url_desc),
                            instruction = resources.getString(R.string.no_record_wrong_url_instruction_1),
                            tokenId = resources.getString(R.string.no_record_wrong_url_instruction_2, snapToken)
                        )
                        resultLauncher.launch(transactionNotFoundIntent)
                    }
                    409 -> {
                        val alreadySettledIntent = SuccessScreenActivity.getIntent(
                            activityContext = this@LoadingPaymentActivity,
                            total = "",
                            orderId = null,
                            transactionResult = TransactionResult("", "", ""),
                            stepNumber = 0
                        )
                        resultLauncher.launch(alreadySettledIntent)
                    }
                    406 -> {
                        val inProgressIntent =
                            InProgressActivity.getIntent(activityContext = this@LoadingPaymentActivity)
                        resultLauncher.launch(inProgressIntent)
                    }
                    else -> {
                        Toast.makeText(this,"Error caught ${it.javaClass.simpleName}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
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