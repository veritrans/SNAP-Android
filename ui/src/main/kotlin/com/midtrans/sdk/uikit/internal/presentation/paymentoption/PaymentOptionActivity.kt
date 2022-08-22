package com.midtrans.sdk.uikit.internal.presentation.paymentoption

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
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
import com.midtrans.sdk.corekit.internal.base.BaseActivity
import com.midtrans.sdk.corekit.internal.network.model.response.MerchantData
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.model.CustomerInfo
import com.midtrans.sdk.uikit.internal.model.PaymentMethodItem
import com.midtrans.sdk.uikit.internal.model.PaymentMethodList
import com.midtrans.sdk.uikit.internal.presentation.banktransfer.BankTransferListActivity
import com.midtrans.sdk.uikit.internal.presentation.creditcard.CreditCardActivity
import com.midtrans.sdk.uikit.internal.presentation.creditcard.SavedCardActivity
import com.midtrans.sdk.uikit.internal.presentation.directdebit.DirectDebitActivity
import com.midtrans.sdk.uikit.internal.view.*
import kotlin.math.sqrt

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

        fun openPaymentOptionPage(
            activityContext: Context,
            snapToken: String,
            totalAmount: String,
            orderId: String,
            paymentList: List<PaymentMethod>,
            customerDetails: CustomerDetails?,
            creditCard: CreditCard?,
            promos: List<PromoResponse>?,
            merchantData: MerchantData?
        ): Intent {
            return Intent(activityContext, PaymentOptionActivity::class.java).apply {
                putExtra(EXTRA_SNAP_TOKEN, snapToken)
                putExtra(EXTRA_TOTAL_AMOUNT, totalAmount)
                putExtra(EXTRA_ORDER_ID, orderId)
                putParcelableArrayListExtra(EXTRA_PAYMENT_LIST, ArrayList(paymentList))
                putExtra(EXTRA_CUSTOMER_DETAILS, customerDetails)
                putExtra(EXTRA_CREDIT_CARD, creditCard)
                putExtra(EXTRA_MERCHANT_DATA, merchantData)
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

    private fun isTabletDevice(): Boolean {
        val metrics = DisplayMetrics()
        this.windowManager.defaultDisplay.getMetrics(metrics)

        val yInches = metrics.heightPixels / metrics.ydpi
        val xInches = metrics.widthPixels / metrics.xdpi
        val diagonalInches = sqrt((xInches * xInches + yInches * yInches).toDouble())
        val hasTabletAttribute = resources.getBoolean(R.bool.isTablet)

        return diagonalInches >= 6.5 && hasTabletAttribute
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
                                iconList = payment.icons
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

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
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
        return mapOf(
            Pair("bank_transfer") {
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
                    if (creditCard?.savedTokens.isNullOrEmpty()) {
                        CreditCardActivity.getIntent(
                            activityContext = this,
                            snapToken = snapToken,
                            orderId = orderId,
                            totalAmount = totalAmount,
                            customerInfo = customerInfo,
                            creditCard = creditCard
                        )
                    } else {
                        SavedCardActivity.getIntent(
                            activityContext = this,
                            snapToken = snapToken,
                            orderId = orderId,
                            totalAmount = totalAmount,
                            customerInfo = customerInfo,
                            creditCard = creditCard
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
            }
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