package com.midtrans.sdk.uikit.internal.presentation.paymentoption

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.midtrans.sdk.corekit.api.model.*
import com.midtrans.sdk.corekit.internal.network.model.response.EnabledPayment
import com.midtrans.sdk.corekit.internal.network.model.response.Merchant
import com.midtrans.sdk.corekit.internal.network.model.response.TransactionDetails
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.internal.model.*
import com.midtrans.sdk.uikit.internal.presentation.banktransfer.BankTransferListActivity
import com.midtrans.sdk.uikit.internal.presentation.conveniencestore.ConvenienceStoreActivity
import com.midtrans.sdk.uikit.internal.presentation.creditcard.CreditCardActivity
import com.midtrans.sdk.uikit.internal.presentation.creditcard.SavedCardActivity
import com.midtrans.sdk.uikit.internal.presentation.directdebit.DirectDebitActivity
import com.midtrans.sdk.uikit.internal.presentation.directdebit.UobSelectionActivity
import com.midtrans.sdk.uikit.internal.presentation.ewallet.WalletActivity
import com.midtrans.sdk.uikit.internal.presentation.paylater.PayLaterActivity
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import com.midtrans.sdk.uikit.internal.util.UiKitConstants.STATUS_CANCELED
import com.midtrans.sdk.uikit.internal.view.*
import javax.inject.Inject

class PaymentOptionActivity : BaseActivity() {

    companion object {
        private const val CURRENT_STEP_NUMBER = 1
        private const val NEXT_STEP_NUMBER = 2
        private const val EXTRA_SNAP_TOKEN = "paymentOptionActivity.extra.snap_token"
        private const val EXTRA_TOTAL_AMOUNT = "paymentOptionActivity.extra.total_amount"
        private const val EXTRA_ORDER_ID = "paymentOptionActivity.extra.order_id"
        private const val EXTRA_PAYMENT_LIST = "paymentOptionActivity.extra.payment_list"
        private const val EXTRA_CUSTOMER_DETAILS = "paymentOptionActivity.extra.customer_details"
        private const val EXTRA_ITEM_DETAILS = "paymentOptionActivity.extra.item_details"
        private const val EXTRA_CREDIT_CARD = "paymentOptionActivity.extra.credit_card"
        private const val EXTRA_PROMOS = "paymentOptionActivity.extra.promos"
        private const val EXTRA_MERCHANT_DATA = "paymentOptionActivity.extra.merchant_data"
        private const val EXTRA_TRANSACTION_DETAILS =
            "paymentOptionActivity.extra.transaction_details"
        private const val EXTRA_EXPIRY_TIME = "paymentOptionActivity.extra.expiry_time"
        private const val EXTRA_PAYMENT_TYPE_ITEM = "paymentOptionActivity.extra.payment_type_item"
        private const val EXTRA_ENABLED_PAYMENT = "paymentOptionActivity.extra.enabled_payment"
        private const val EXTRA_TRANSACTION_RESULT =
            "paymentOptionActivity.extra.transaction_result"

        fun openPaymentOptionPage(
            activityContext: Context,
            snapToken: String,
            totalAmount: String,
            transactionDetail: TransactionDetails?,
            orderId: String,
            paymentList: List<PaymentMethod>,
            customerDetails: CustomerDetails?,
            itemDetails: List<ItemDetails>?,
            creditCard: CreditCard?,
            promos: List<Promo>?,
            merchant: Merchant?,
            expiryTime: String?,
            paymentTypeItem: PaymentTypeItem?,
            enabledPayments: List<EnabledPayment>?,
            result: TransactionResponse?
        ): Intent {
            return Intent(activityContext, PaymentOptionActivity::class.java).apply {
                putExtra(EXTRA_SNAP_TOKEN, snapToken)
                putExtra(EXTRA_TOTAL_AMOUNT, totalAmount)
                putExtra(EXTRA_ORDER_ID, orderId)
                putParcelableArrayListExtra(EXTRA_PAYMENT_LIST, ArrayList(paymentList))
                putExtra(EXTRA_CUSTOMER_DETAILS, customerDetails)
                putExtra(EXTRA_CREDIT_CARD, creditCard)
                putExtra(EXTRA_MERCHANT_DATA, merchant)
                putExtra(EXTRA_TRANSACTION_DETAILS, transactionDetail)
                putExtra(EXTRA_EXPIRY_TIME, expiryTime)
                putExtra(EXTRA_PAYMENT_TYPE_ITEM, paymentTypeItem)
                putExtra(EXTRA_TRANSACTION_RESULT, result)
                promos?.also { putParcelableArrayListExtra(EXTRA_PROMOS, ArrayList(it)) }
                itemDetails?.also { putParcelableArrayListExtra(EXTRA_ITEM_DETAILS, ArrayList(it)) }
                enabledPayments?.also {
                    putParcelableArrayListExtra(
                        EXTRA_ENABLED_PAYMENT,
                        ArrayList(it)
                    )
                }
            }
        }
    }

    @Inject
    internal lateinit var vmFactory: ViewModelProvider.Factory

    private val viewModel: PaymentOptionViewModel by lazy {
        ViewModelProvider(this, vmFactory)[PaymentOptionViewModel::class.java]
    }

    private val snapToken: String by lazy {
        intent.getStringExtra(EXTRA_SNAP_TOKEN)
            ?: throw RuntimeException("Snap token must not be empty")
    }

    private val totalAmount: String by lazy {
        intent.getStringExtra(EXTRA_TOTAL_AMOUNT)
            ?: throw RuntimeException("Total amount must not be empty")
    }

    private val orderId: String by lazy {
        intent.getStringExtra(EXTRA_ORDER_ID)
            ?: throw RuntimeException("Order ID must not be empty")
    }

    private val paymentList: List<PaymentMethod> by lazy {
        intent.getParcelableArrayListExtra<PaymentMethod>(EXTRA_PAYMENT_LIST) as? List<PaymentMethod>
            ?: throw RuntimeException("Payment list must not be empty")
    }

    private val promos: List<Promo>? by lazy {
        intent.getParcelableArrayListExtra(EXTRA_PROMOS)
    }

    private val itemDetails: List<ItemDetails>? by lazy {
        intent.getParcelableArrayListExtra(EXTRA_ITEM_DETAILS)
    }

    private val customerDetail: CustomerDetails? by lazy {
        intent.getParcelableExtra(EXTRA_CUSTOMER_DETAILS) as? CustomerDetails
    }

    private val creditCard: CreditCard? by lazy {
        intent.getParcelableExtra(EXTRA_CREDIT_CARD) as? CreditCard
    }

    private val transactionDetails: TransactionDetails? by lazy {
        intent.getParcelableExtra(EXTRA_TRANSACTION_DETAILS) as? TransactionDetails
    }

    private val expiryTime: String? by lazy {
        intent.getStringExtra(EXTRA_EXPIRY_TIME)
    }

    private val paymentTypeItem: PaymentTypeItem? by lazy {
        intent.getParcelableExtra(EXTRA_PAYMENT_TYPE_ITEM)
    }

    private val enabledPayments: List<EnabledPayment>? by lazy {
        intent.getParcelableArrayListExtra(EXTRA_ENABLED_PAYMENT)
    }

    private val merchant: Merchant? by lazy {
        intent.getParcelableExtra(EXTRA_MERCHANT_DATA)
    }

    private val transactionResult: TransactionResponse? by lazy {
        intent.getParcelableExtra(EXTRA_TRANSACTION_RESULT) as? TransactionResponse
    }

    private var customerInfo: CustomerInfo? = null
    private var itemInfo: ItemInfo? = null

    private lateinit var paymentMethods: PaymentMethodList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        UiKitApi.getDefaultInstance().daggerComponent.inject(this)
        viewModel.trackPageViewed(CURRENT_STEP_NUMBER)
        customerInfo = viewModel.getCustomerInfo(customerDetail)
        itemInfo = viewModel.getItemInfo(itemDetails)

        transactionResult?.let { result ->
            paymentMethods = when (result.chargeType) {
                PaymentType.QRIS -> {
                    viewModel.initiateList(paymentList, true)
                }
                PaymentType.GOPAY, PaymentType.SHOPEEPAY -> {
                    viewModel.initiateList(paymentList, false)
                }
                else -> {
                    viewModel.initiateList(paymentList, isTabletDevice())
                }
            }
            handleUsedToken(result)
        } ?: run {
            paymentMethods = viewModel.initiateList(paymentList, isTabletDevice())
        }

        enabledPayments?.let { handleEnabledPayments(it) }

        //TODO: Find More Optimal way for PaymentType that have method (Bank transfer & UOB)
        paymentTypeItem?.let { paymentType ->
            val paymentMethod = paymentMethods.paymentMethods.find { it.type == paymentType.type }
            paymentMethod?.let {
                getOnPaymentItemClick(
                    paymentType = paymentType.type,
                    customerInfo = customerInfo,
                    itemInfo = itemInfo,
                    totalAmount = totalAmount,
                    paymentMethodItem = it,
                    orderId = orderId
                )[paymentType.type]?.invoke()
            }
        } ?: run {
            setContent {
                PaymentOptionPage(
                    totalAmount = totalAmount,
                    orderId = orderId,
                    customerInfo = customerInfo,
                    itemInfo = itemInfo,
                    paymentMethods = paymentMethods,
                    promos = promos,
                    creditCard = creditCard
                )
            }
        }
    }

    private fun handleUsedToken(result: TransactionResponse) {
        val shopeeResponse = "airpay shopee"
        val gopayResponse = "gopay"

        if (result.transactionStatus == UiKitConstants.STATUS_PENDING) {
            val bankTransferList = listOf(
                PaymentType.PERMATA_VA,
                PaymentType.BCA_VA,
                PaymentType.BNI_VA,
                PaymentType.BRI_VA,
                PaymentType.OTHER_VA,
                PaymentType.E_CHANNEL
            )
            val paymentType = if (bankTransferList.contains(result.chargeType)) {
                PaymentType.BANK_TRANSFER
            } else if (result.chargeType == PaymentType.QRIS && result.qrisAcquirer == shopeeResponse) {
                PaymentType.SHOPEEPAY_QRIS
            } else if (result.chargeType == PaymentType.QRIS && result.qrisAcquirer == gopayResponse) {
                PaymentType.GOPAY_QRIS
            } else {
                result.chargeType
            }

            val paymentMethod = paymentMethods.paymentMethods.find { it.type == paymentType }
            paymentMethod?.let { method ->
                paymentType?.let { type ->
                    getOnPaymentItemClick(
                        paymentType = type,
                        customerInfo = customerInfo,
                        itemInfo = itemInfo,
                        totalAmount = totalAmount,
                        paymentMethodItem = method,
                        orderId = orderId
                    )[type]?.invoke()
                }
            }
        }
    }

    private fun handleEnabledPayments(enabledPayments: List<EnabledPayment>) {
        val paymentType = getPaymentType(enabledPayments)
        val paymentMethod = paymentMethods.paymentMethods.find { it.type == paymentType }

        paymentMethod?.let {
            getOnPaymentItemClick(
                paymentType = paymentType,
                customerInfo = customerInfo,
                itemInfo = itemInfo,
                totalAmount = totalAmount,
                paymentMethodItem = paymentMethod,
                orderId = orderId
            )[paymentType]?.invoke()
        }
    }

    private fun getPaymentType(enabledPayments: List<EnabledPayment>): String {
        val bankTransfer: (EnabledPayment) -> Boolean = { it.category == PaymentType.BANK_TRANSFER }
        val isAllBankTransfer = enabledPayments.all(bankTransfer)
        val paymentType = if (isAllBankTransfer) {
            PaymentType.BANK_TRANSFER
        } else if (enabledPayments.size == 1) {
            enabledPayments[0].type
        } else ""

        return paymentType
    }


    override fun onBackPressed() {
        viewModel.trackPaymentListPageClosed()
        val resultIntent = Intent().putExtra(
            UiKitConstants.KEY_TRANSACTION_RESULT,
            TransactionResult(
                status = STATUS_CANCELED,
                transactionId = "",
                paymentType = ""
            )
        )
        setResult(Activity.RESULT_OK, resultIntent)
        super.onBackPressed()
    }

    @Preview
    @Composable
    private fun Preview() {
        PaymentOptionPage(
            totalAmount = "Rp33.990",
            "#00-11-22-33",
            customerInfo = CustomerInfo(
                "Ari Bhakti",
                "087788778212",
                listOf("Jl. ABC", "Rumah DEF")
            ),
            itemInfo = ItemInfo(
                itemDetails = listOf(ItemDetails("001", 890.00, 1, "item")),
                totalAmount = 890.00
            ),
            creditCard = CreditCard(),
            paymentMethods = PaymentMethodList(
                listOf(
                    PaymentMethodItem(
                        type = "bt",
                        titleId = R.string.payment_summary_bank_transfer,
                        icons = listOf(
                            R.drawable.ic_outline_permata_40,
                            R.drawable.ic_outline_bca_40,
                            R.drawable.ic_outline_bni_40,
                            R.drawable.ic_outline_bri_40,
                            R.drawable.ic_outline_mandiri_40
                        )
                    ),
                    PaymentMethodItem(
                        type = "qr-1",
                        titleId = R.string.payment_summary_gopay,
                        icons = listOf(
                            R.drawable.ic_outline_gopay_40_2,
                            R.drawable.ic_outline_qris_40
                        )
                    ),
                    PaymentMethodItem(
                        type = "qr-2",
                        titleId = R.string.payment_summary_shopeepay,
                        icons = listOf(
                            R.drawable.ic_outline_gopay_40_2,
                            R.drawable.ic_outline_qris_40
                        )
                    ),
                    PaymentMethodItem(
                        type = "qr-3",
                        titleId = R.string.payment_summary_gopay,
                        icons = listOf(
                            R.drawable.ic_outline_gopay_40_2,
                            R.drawable.ic_outline_qris_40
                        )
                    ),
                    PaymentMethodItem(
                        type = "qr-4",
                        titleId = R.string.payment_summary_shopeepay,
                        icons = listOf(
                            R.drawable.ic_outline_gopay_40_2,
                            R.drawable.ic_outline_qris_40
                        )
                    ),
                    PaymentMethodItem(
                        type = "qr-5",
                        titleId = R.string.payment_summary_gopay,
                        icons = listOf(
                            R.drawable.ic_outline_gopay_40_2,
                            R.drawable.ic_outline_qris_40
                        )
                    ),
                    PaymentMethodItem(
                        type = "cs",
                        titleId = R.string.payment_summary_indomaret,
                        icons = listOf(
                            R.drawable.ic_outline_alfamart_40,
                            R.drawable.ic_outline_alfamidi_40,
                            R.drawable.ic_outline_dandan_40,
                        )
                    )
                )
            )
        )
    }

    @Composable
    private fun PaymentOptionPage(
        totalAmount: String,
        orderId: String,
        creditCard: CreditCard?,
        customerInfo: CustomerInfo?,
        itemInfo: ItemInfo?,
        paymentMethods: PaymentMethodList,
        promos: List<Promo>? = null
    ) {
        var isExpand by remember { mutableStateOf(false) }
        Column {
            SnapAppBar(
                title = stringResource(id = R.string.payment_summary_select_method),
                iconResId = R.drawable.ic_arrow_left
            ) {
                onBackPressed()
            }
            SnapOverlayExpandingBox(
                isExpanded = isExpand,
                mainContent = {
                    SnapTotal(
                        amount = totalAmount,
                        orderId = orderId,
                        canExpand = customerInfo != null,
                        remainingTime = null,
                        isPromo = !promos.isNullOrEmpty()
                    ) {
                        isExpand = it
                    }
                },
                expandingContent = {
                    viewModel.trackOrderDetailsViewed()
                    SnapPaymentOrderDetails(
                        itemInfo = itemInfo,
                        customerInfo = customerInfo
                    )
                },
                followingContent = {
                    LazyColumn {
                        items(
                            items = paymentMethods.paymentMethods,
                            key = {
                                it.type
                            }
                        ) { payment ->
                            SnapMultiIconListItem(
                                title = stringResource(payment.titleId),
                                iconList = payment.icons,
                                creditCard = creditCard,
                                paymentType = payment.type
                            ) {
                                getOnPaymentItemClick(
                                    paymentType = payment.type,
                                    customerInfo = customerInfo,
                                    itemInfo = itemInfo,
                                    totalAmount = totalAmount,
                                    paymentMethodItem = payment,
                                    orderId = orderId
                                )[payment.type]?.invoke()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxHeight(1f)
                    .padding(all = 16.dp)
                    .background(SnapColors.getARGBColor(SnapColors.overlayWhite))
            )
        }
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                setResult(Activity.RESULT_OK, result.data)
                finish()
            } else {
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
        }

    private fun getOnPaymentItemClick(
        paymentType: String,
        totalAmount: String,
        orderId: String,
        paymentMethodItem: PaymentMethodItem,
        customerInfo: CustomerInfo?,
        itemInfo: ItemInfo?
    ): Map<String, () -> Unit> {

        val eWalletPaymentLauncher = {
            resultLauncher.launch(
                WalletActivity.getIntent(
                    activityContext = this,
                    snapToken = snapToken,
                    orderId = orderId,
                    totalAmount = totalAmount,
                    paymentType = paymentMethodItem.type,
                    customerInfo = customerInfo,
                    itemInfo = itemInfo,
                    stepNumber = NEXT_STEP_NUMBER,
                    result = transactionResult,
                )
            )
        }

        val cStorePaymentLauncher = {
            resultLauncher.launch(
                ConvenienceStoreActivity.getIntent(
                    activityContext = this,
                    snapToken = snapToken,
                    orderId = orderId,
                    totalAmount = totalAmount,
                    paymentType = paymentMethodItem.type,
                    customerInfo = customerInfo,
                    itemInfo = itemInfo,
                    stepNumber = NEXT_STEP_NUMBER
                )
            )
        }
        return mapOf(
            Pair(PaymentType.BANK_TRANSFER) {
                resultLauncher.launch(
                    BankTransferListActivity.getIntent(
                        activityContext = this,
                        snapToken = snapToken,
                        orderId = orderId,
                        withMerchantData = merchant,
                        totalAmount = totalAmount,
                        paymentMethodItem = paymentMethodItem,
                        customerInfo = customerInfo,
                        itemInfo = itemInfo,
                        paymentTypeItem = this.paymentTypeItem,
                        enabledPayments = enabledPayments,
                        result = transactionResult,
                        stepNumber = NEXT_STEP_NUMBER
                    )
                )
            },
            Pair(PaymentType.CREDIT_CARD) {
                resultLauncher.launch(
                    //TODO: Need to revisit, if we need to enable adding a flag on sdk to force Normal Transaction like old ios sdk
                    if (creditCard?.savedTokens.isNullOrEmpty().or(true)) {
                        CreditCardActivity.getIntent(
                            activityContext = this,
                            snapToken = snapToken,
                            transactionDetails = transactionDetails,
                            totalAmount = totalAmount,
                            customerInfo = customerInfo,
                            itemInfo = itemInfo,
                            creditCard = creditCard,
                            expiryTime = expiryTime,
                            withMerchantData = merchant,
                            promos = promos,
                            stepNumber = NEXT_STEP_NUMBER
                        )
                    } else {
                        //TODO currently set to CreditCardActivity for testing purpose
                        SavedCardActivity.getIntent(
                            activityContext = this,
                            snapToken = snapToken,
                            transactionDetails = transactionDetails,
                            totalAmount = totalAmount,
                            customerInfo = customerInfo,
                            creditCard = creditCard
//                            expiryTime = expiryTime //TODO will be fixed by pak wahyu
                        )
                    }
                )
            },
            checkDirectDebitType(paymentType).let {
                Pair(it) {
                    resultLauncher.launch(
                        DirectDebitActivity.getIntent(
                            activityContext = this,
                            snapToken = snapToken,
                            paymentType = it,
                            amount = totalAmount,
                            orderId = orderId,
                            customerInfo = customerInfo,
                            itemInfo = itemInfo,
                            stepNumber = NEXT_STEP_NUMBER,
                            result = transactionResult,
                        )
                    )
                }
            },
            Pair(PaymentType.UOB_EZPAY) {
                resultLauncher.launch(
                    UobSelectionActivity.getIntent(
                        activityContext = this,
                        snapToken = snapToken,
                        uobModes = ArrayList(paymentMethodItem.methods),
                        amount = totalAmount,
                        orderId = orderId,
                        customerInfo = customerInfo,
                        itemInfo = itemInfo,
                        paymentTypeItem = this.paymentTypeItem,
                        stepNumber = NEXT_STEP_NUMBER
                    )
                )
            },
            Pair(PaymentType.SHOPEEPAY, eWalletPaymentLauncher),
            Pair(PaymentType.SHOPEEPAY_QRIS, eWalletPaymentLauncher),
            Pair(PaymentType.GOPAY, eWalletPaymentLauncher),
            Pair(PaymentType.GOPAY_QRIS, eWalletPaymentLauncher),
            Pair(PaymentType.AKULAKU) {
                resultLauncher.launch(
                    PayLaterActivity.getIntent(
                        activityContext = this,
                        snapToken = snapToken,
                        paymentType = paymentType,
                        amount = totalAmount,
                        orderId = orderId,
                        customerInfo = customerInfo,
                        itemInfo = itemInfo,
                        stepNumber = NEXT_STEP_NUMBER,
                        result = transactionResult,
                    )
                )
            },
            Pair(PaymentType.ALFAMART, cStorePaymentLauncher),
            Pair(PaymentType.INDOMARET, cStorePaymentLauncher)
        )
    }

    private fun checkDirectDebitType(paymentType: String): String {
        return when (paymentType) {
            PaymentType.KLIK_BCA,
            PaymentType.BCA_KLIKPAY,
            PaymentType.CIMB_CLICKS,
            PaymentType.BRI_EPAY,
            PaymentType.DANAMON_ONLINE -> paymentType
            else -> ""
        }
    }
}