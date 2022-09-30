package com.midtrans.sdk.uikit.internal.presentation.paymentoption

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.midtrans.sdk.corekit.internal.network.model.response.MerchantData
import com.midtrans.sdk.corekit.internal.network.model.response.TransactionDetails
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.internal.model.CustomerInfo
import com.midtrans.sdk.uikit.internal.model.PaymentMethodItem
import com.midtrans.sdk.uikit.internal.model.PaymentMethodList
import com.midtrans.sdk.uikit.internal.presentation.banktransfer.BankTransferListActivity
import com.midtrans.sdk.uikit.internal.presentation.conveniencestore.ConvenienceStoreActivity
import com.midtrans.sdk.uikit.internal.presentation.creditcard.CreditCardActivity
import com.midtrans.sdk.uikit.internal.presentation.creditcard.SavedCardActivity
import com.midtrans.sdk.uikit.internal.presentation.directdebit.DirectDebitActivity
import com.midtrans.sdk.uikit.internal.presentation.directdebit.UobSelectionActivity
import com.midtrans.sdk.uikit.internal.presentation.ewallet.WalletActivity
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import com.midtrans.sdk.uikit.internal.view.*

class PaymentOptionActivity : BaseActivity() {

    companion object {
        const val EXTRA_SNAP_TOKEN = "paymentOptionActivity.extra.snap_token"
        const val EXTRA_TOTAL_AMOUNT = "paymentOptionActivity.extra.total_amount"
        const val EXTRA_ORDER_ID = "paymentOptionActivity.extra.order_id"
        const val EXTRA_PAYMENT_LIST = "paymentOptionActivity.extra.payment_list"
        const val EXTRA_CUSTOMER_DETAILS = "paymentOptionActivity.extra.customer_details"
        const val EXTRA_CREDIT_CARD = "paymentOptionActivity.extra.credit_card"
        const val EXTRA_PROMOS = "paymentOptionActivity.extra.promos"
        const val EXTRA_MERCHANT_DATA = "paymentOptionActivity.extra.merchant_data"
        const val EXTRA_TRANSACTION_DETAILS = "paymentOptionActivity.extra.transaction_details"
        const val EXTRA_EXPIRY_TIME = "paymentOptionActivity.extra.expiry_time"

        fun openPaymentOptionPage(
            activityContext: Context,
            snapToken: String,
            totalAmount: String,
            transactionDetail: TransactionDetails?,
            orderId: String,
            paymentList: List<PaymentMethod>,
            customerDetails: CustomerDetails?,
            creditCard: CreditCard?,
            promos: List<PromoResponse>?,
            merchantData: MerchantData?,
            expiryTime: String?
        ): Intent {
            return Intent(activityContext, PaymentOptionActivity::class.java).apply {
                putExtra(EXTRA_SNAP_TOKEN, snapToken)
                putExtra(EXTRA_TOTAL_AMOUNT, totalAmount)
                putExtra(EXTRA_ORDER_ID, orderId)
                putParcelableArrayListExtra(EXTRA_PAYMENT_LIST, ArrayList(paymentList))
                putExtra(EXTRA_CUSTOMER_DETAILS, customerDetails)
                putExtra(EXTRA_CREDIT_CARD, creditCard)
                putExtra(EXTRA_MERCHANT_DATA, merchantData)
                putExtra(EXTRA_TRANSACTION_DETAILS, transactionDetail)
                putExtra(EXTRA_EXPIRY_TIME, expiryTime)
            }
        }
    }

    private val viewModel: PaymentOptionViewModel by lazy {
        ViewModelProvider(this).get(PaymentOptionViewModel::class.java)
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

    private var customerInfo: CustomerInfo? = null

    private lateinit var paymentMethods: PaymentMethodList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        paymentMethods = viewModel.initiateList(paymentList, isTabletDevice())
        customerInfo = viewModel.getCustomerInfo(customerDetail)

        setContent {
            PaymentOptionPage(
                totalAmount = totalAmount,
                orderId = orderId,
                customerInfo = customerInfo,
                paymentMethods = paymentMethods
            )
        }
    }

    @Preview
    @Composable
    private fun Preview() {
        PaymentOptionPage(
            "Rp33.990",
            "#00-11-22-33",
            CustomerInfo(
                "Ari Bhakti",
                "087788778212",
                listOf("Jl. ABC", "Rumah DEF")
            ),
            PaymentMethodList(
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
        customerInfo: CustomerInfo?,
        paymentMethods: PaymentMethodList
    ) {
        var isExpand by remember { mutableStateOf(false) }
        Column {
            SnapAppBar(title = "Payment Methods", iconResId = R.drawable.ic_arrow_left) {
                onBackPressed()
            }
            SnapOverlayExpandingBox(
                isExpanded = isExpand,
                mainContent = {
                    SnapTotal(
                        amount = totalAmount,
                        orderId = orderId,
                        canExpand = customerInfo != null,
                        remainingTime = null
                    ) {
                        isExpand = it
                    }
                },
                expandingContent = customerInfo?.let {
                    {
                        SnapCustomerDetail(
                            name = it.name,
                            phone = it.phone,
                            addressLines = it.addressLines
                        )
                    }
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
                    .background(SnapColors.getARGBColor(SnapColors.OVERLAY_WHITE))
            )
        }
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result?.data?.let {
                    val transactionResult = it.getParcelableExtra<TransactionResult>(UiKitConstants.KEY_TRANSACTION_RESULT) as TransactionResult
                    UiKitApi.getDefaultInstance().paymentCallback.onSuccess(transactionResult) //TODO temporary for direct debit, revisit after real callback like the one in MidtransSdk implemented
                }
                finish()
            }
        }

    private fun getOnPaymentItemClick(
        paymentType: String,
        totalAmount: String,
        orderId: String,
        paymentMethodItem: PaymentMethodItem,
        customerInfo: CustomerInfo?,
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
                        totalAmount = totalAmount,
                        paymentMethodItem = paymentMethodItem,
                        customerInfo = customerInfo,
                        destinationBankCode = "009 - Permata"  //TODO: clarify if always permata or something
                    )
                )
            },
            Pair("credit_card") {
                resultLauncher.launch(
                    //TODO: Need to revisit, if we need to enable adding a flag on sdk to force Normal Transaction like old ios sdk
                    if (creditCard?.savedTokens.isNullOrEmpty().or(true)) {
                        CreditCardActivity.getIntent(
                            activityContext = this,
                            snapToken = snapToken,
                            transactionDetails = transactionDetails,
                            totalAmount = totalAmount,
                            customerInfo = customerInfo,
                            creditCard = creditCard,
                            expiryTime = expiryTime
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
                            customerInfo = customerInfo
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
                        customerInfo = customerInfo
                    )
                )
            },
            Pair(PaymentType.SHOPEEPAY, eWalletPaymentLauncher),
            Pair(PaymentType.SHOPEEPAY_QRIS, eWalletPaymentLauncher),
            Pair(PaymentType.GOPAY, eWalletPaymentLauncher),
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