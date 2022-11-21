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
import androidx.lifecycle.ViewModelProvider
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.internal.model.CustomerInfo
import com.midtrans.sdk.uikit.internal.model.ItemInfo
import com.midtrans.sdk.uikit.internal.model.PaymentMethodItem
import com.midtrans.sdk.uikit.internal.model.PaymentTypeItem
import com.midtrans.sdk.uikit.internal.view.*
import javax.inject.Inject

class BankTransferListActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: BankTransferListViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[BankTransferListViewModel::class.java]
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

    private val itemInfo: ItemInfo? by lazy {
        intent.getParcelableExtra(EXTRA_ITEM_INFO) as? ItemInfo
    }

    private val paymentMethodItem: PaymentMethodItem by lazy {
        intent.getParcelableExtra(EXTRA_PAYMENT_METHOD_ITEM) as? PaymentMethodItem
            ?: throw RuntimeException("Order ID must not be empty")
    }

    private val paymentTypeItem: PaymentTypeItem? by lazy {
        intent.getParcelableExtra(EXTRA_PAYMENT_TYPE_ITEM)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UiKitApi.getDefaultInstance().daggerComponent.inject(this)
        paymentTypeItem?.let { paymentType ->
            paymentType.method?.let { paymentMethod ->
                toBankTransferDetail(paymentMethod)
            }
        } ?: run {
            setContent {
                SetupView(paymentMethodItem = paymentMethodItem)
            }
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
                            canExpand = customerInfo != null || itemInfo != null,
                            remainingTime = null
                        ) {
                            expanding = it
                        }
                    },
                    expandingContent = {
                        viewModel.trackOrderDetailsViewed()
                        SnapPaymentOrderDetails(
                            customerInfo = customerInfo,
                            itemInfo = itemInfo
                        )
                    }
                ) {
                    LazyColumn {
                        paymentMethodItem.methods.forEachIndexed { _, method ->
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
    fun SetupView(paymentMethodItem: PaymentMethodItem) {
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
                itemInfo = itemInfo,
                orderId = orderId,
                totalAmount = totalAmount,
                snapToken = snapToken
            )
        resultLauncher.launch(intent)
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
            Pair(PaymentType.OTHER_VA, Pair(R.string.bank_other, R.drawable.ic_bank_other_40))
        )
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            setResult(result.resultCode, result.data)
            finish()
        }

    companion object {
        private const val EXTRA_SNAP_TOKEN = "bankTransfer.extra.snap_token"
        private const val EXTRA_TOTAL_AMOUNT = "bankTransfer.extra.total_amount"
        private const val EXTRA_ORDER_ID = "bankTransfer.extra.order_id"
        private const val EXTRA_CUSTOMER_INFO = "bankTransfer.extra.customer_info"
        private const val EXTRA_ITEM_INFO = "bankTransfer.extra.item_info"
        private const val EXTRA_PAYMENT_METHOD_ITEM = "bankTransfer.extra.payment_method_item"
        private const val EXTRA_PAYMENT_TYPE_ITEM = "bankTransfer.extra.payment_type_it"

        fun getIntent(
            activityContext: Context,
            snapToken: String,
            totalAmount: String,
            orderId: String,
            paymentMethodItem: PaymentMethodItem,
            customerInfo: CustomerInfo? = null,
            itemInfo: ItemInfo? = null,
            paymentTypeItem: PaymentTypeItem? = null
        ): Intent {
            return Intent(activityContext, BankTransferListActivity::class.java).apply {
                putExtra(EXTRA_SNAP_TOKEN, snapToken)
                putExtra(EXTRA_TOTAL_AMOUNT, totalAmount)
                putExtra(EXTRA_ORDER_ID, orderId)
                putExtra(EXTRA_PAYMENT_METHOD_ITEM, paymentMethodItem)
                putExtra(EXTRA_CUSTOMER_INFO, customerInfo)
                putExtra(EXTRA_PAYMENT_TYPE_ITEM, paymentTypeItem)
                putExtra(EXTRA_ITEM_INFO, itemInfo)
            }
        }
    }
}
