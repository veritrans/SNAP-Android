package com.midtrans.sdk.uikit.internal.presentation.banktransfer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.uikit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.model.CustomerInfo
import com.midtrans.sdk.uikit.internal.model.PaymentMethodItem
import com.midtrans.sdk.uikit.internal.view.*

class BankTransferListActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            setupView(paymentMethodItem = paymentMethodItem)
        }
    }

    @Composable
    fun Content(
        paymentMethodItem: PaymentMethodItem
    ) {
        var expanding by remember {
            mutableStateOf(false)
        }
        Column {
            SnapAppBar(
                title = stringResource(id = R.string.payment_summary_bank_transfer),
                iconResId = R.drawable.ic_arrow_left
            ) {
                onBackPressed()
            }

            Box(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                SnapOverlayExpandingBox(
                    modifier = Modifier.fillMaxHeight(1f),
                    isExpanded = expanding,
                    mainContent = {
                        SnapTotal(
                            amount = totalAmount,
                            orderId = orderId,
                            canExpand = customerInfo != null,
                            remainingTime = null
                        ) {
                            expanding = it
                        }
                    },
                    expandingContent = {
                        customerInfo?.run {
                            SnapCustomerDetail(
                                name = name,
                                phone = phone,
                                addressLines = addressLines
                            )
                        }
                    }
                ) {
                    LazyColumn {
                        paymentMethodItem.methods.forEachIndexed { index, method ->
                            item {
                                bankNameMap[method]?.let {
                                    SnapSingleIconListItem(title = stringResource(it.first),
                                        iconResId = it.second,
                                        modifier = Modifier.clickable {
                                            toBankTransferDetail(method)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun setupView(paymentMethodItem: PaymentMethodItem) {
        Content(
            paymentMethodItem = paymentMethodItem
        )
    }

    private fun toBankTransferDetail(method: String) {
        val intent =
            BankTransferDetailActivity.getIntent(
                activityContext = this,
                paymentType = method,
                customerInfo = customerInfo,
                orderId = orderId,
                totalAmount = totalAmount,
                destinationBankCode = destinationBankCode,
                snapToken = snapToken
            )
        resultLauncher.launch(intent)
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

    private val customerInfo: CustomerInfo? by lazy {
        intent.getParcelableExtra(EXTRA_CUSTOMER_INFO) as? CustomerInfo
    }

    private val destinationBankCode: String? by lazy {
        intent.getStringExtra(EXTRA_DESTINATIONBANKCODE)
    }

    private val paymentMethodItem: PaymentMethodItem by lazy {
        intent.getParcelableExtra(EXTRA_PAYMENT_METHOD_ITEM) as? PaymentMethodItem
            ?: throw RuntimeException("Order ID must not be empty")
    }

    private val bankNameMap by lazy {
        mapOf(
            Pair(PaymentType.BCA_VA, Pair(R.string.bank_bca, R.drawable.ic_bank_bca_40)),
            Pair(
                PaymentType.PERMATA_VA,
                Pair(R.string.bank_permata, R.drawable.ic_bank_permata_40)
            ),
            Pair(PaymentType.BRI_VA, Pair(R.string.bank_bri, R.drawable.ic_bank_bri_40)),
            Pair(PaymentType.BNI_VA, Pair(R.string.bank_bni, R.drawable.ic_bank_bni_40)),
            Pair(PaymentType.E_CHANNEL, Pair(R.string.bank_mandiri, R.drawable.ic_bank_mandiri_40)),
            Pair(PaymentType.OTHER_VA, Pair(R.string.bank_other, R.drawable.ic_bank_other_img))
        )
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                setResult(RESULT_OK)
                finish()
            }
        }

    companion object {
        private const val EXTRA_SNAP_TOKEN = "bankTransfer.extra.snap_token"
        private const val EXTRA_TOTAL_AMOUNT = "bankTransfer.extra.total_amount"
        private const val EXTRA_ORDER_ID = "bankTransfer.extra.order_id"
        private const val EXTRA_DESTINATIONBANKCODE = "bankTransfer.extra.destinationbankcode"
        private const val EXTRA_CUSTOMER_INFO = "bankTransfer.extra.customer_info"
        private const val EXTRA_PAYMENT_METHOD_ITEM = "bankTransfer.extra.payment_method_item"

        fun getIntent(
            activityContext: Context,
            snapToken: String,
            totalAmount: String,
            orderId: String,
            paymentMethodItem: PaymentMethodItem,
            destinationBankCode: String? = null,
            customerInfo: CustomerInfo? = null
        ): Intent {
            return Intent(activityContext, BankTransferListActivity::class.java).apply {
                putExtra(EXTRA_SNAP_TOKEN, snapToken)
                putExtra(EXTRA_TOTAL_AMOUNT, totalAmount)
                putExtra(EXTRA_ORDER_ID, orderId)
                putExtra(EXTRA_PAYMENT_METHOD_ITEM, paymentMethodItem)
                putExtra(
                    EXTRA_CUSTOMER_INFO,
                    customerInfo
                )
                destinationBankCode?.let {
                    putExtra(EXTRA_DESTINATIONBANKCODE, it)
                }
            }
        }
    }
}
