package com.midtrans.sdk.uikit.internal.presentation.paymentoption

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.activity.compose.setContent
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.midtrans.sdk.corekit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.external.model.PaymentList
import com.midtrans.sdk.uikit.internal.model.PaymentMethodItem
import com.midtrans.sdk.uikit.internal.model.PaymentMethodList
import com.midtrans.sdk.uikit.internal.view.SnapAppBar
import com.midtrans.sdk.uikit.internal.view.SnapColors
import com.midtrans.sdk.uikit.internal.view.SnapCustomerDetail
import com.midtrans.sdk.uikit.internal.view.SnapMultiIconListItem
import com.midtrans.sdk.uikit.internal.view.SnapOverlayExpandingBox
import com.midtrans.sdk.uikit.internal.view.SnapTotal
import kotlinx.android.parcel.Parcelize
import kotlin.math.sqrt

class PaymentOptionActivity : BaseActivity() {

    companion object {
        const val EXTRA_TOTAL_AMOUNT = "paymentOptionActivity.extra.total_amount"
        const val EXTRA_ORDER_ID = "paymentOptionActivity.extra.order_id"
        const val EXTRA_PAYMENT_LIST = "paymentOptionActivity.extra.payment_list"
        const val EXTRA_CUSTOMER_DETAIL = "paymentOptionActivity.extra.customer_detail"

        fun openPaymentOptionPage(
            activityContext: Context,
            totalAmount: String,
            orderId: String,
            paymentList: PaymentList,
            customerName: String,
            customerPhone: String,
            addressLines: List<String>
        ): Intent {
            return Intent(activityContext, PaymentOptionActivity::class.java).apply {
                putExtra(EXTRA_TOTAL_AMOUNT, totalAmount)
                putExtra(EXTRA_ORDER_ID, orderId)
                putExtra(EXTRA_PAYMENT_LIST, paymentList)
                putExtra(
                    EXTRA_CUSTOMER_DETAIL,
                    CustomerDetail(customerName, customerPhone, addressLines)
                )
            }
        }
    }

    private val viewModel: PaymentOptionViewModel by lazy {
        ViewModelProvider(this).get(PaymentOptionViewModel::class.java)
    }

    private val totalAmount: String by lazy {
        intent.getStringExtra(EXTRA_TOTAL_AMOUNT)
            ?: throw RuntimeException("Total amount must not be empty")
    }

    private val orderId: String by lazy {
        intent.getStringExtra(EXTRA_ORDER_ID)
            ?: throw RuntimeException("Order ID must not be empty")
    }

    private val paymentList: PaymentList by lazy {
        intent.getParcelableExtra(EXTRA_PAYMENT_LIST) as? PaymentList
            ?: throw RuntimeException("Payment list must not be empty")
    }

    private val customerDetail: CustomerDetail by lazy {
        intent.getParcelableExtra(EXTRA_CUSTOMER_DETAIL) as? CustomerDetail
            ?: throw RuntimeException("Customer detail must not be empty")
    }

    private lateinit var paymentMethods: PaymentMethodList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        paymentMethods = viewModel.initiateList(paymentList, isTabletDevice())

        setContent {
            PaymentOptionPage(
                totalAmount = totalAmount,
                orderId = orderId,
                customerDetail = customerDetail,
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
            CustomerDetail(
                "Ari Bhakti",
                "087788778212",
                listOf("Jl. ABC", "Rumah DEF")
            ),
            PaymentMethodList(
                listOf(
                    PaymentMethodItem(
                        type = "bt",
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
                        icons = listOf(
                            R.drawable.ic_outline_gopay_40_2,
                            R.drawable.ic_outline_qris_40
                        )
                    ),
                    PaymentMethodItem(
                        type = "qr-2",
                        icons = listOf(
                            R.drawable.ic_outline_gopay_40_2,
                            R.drawable.ic_outline_qris_40
                        )
                    ),
                    PaymentMethodItem(
                        type = "qr-3",
                        icons = listOf(
                            R.drawable.ic_outline_gopay_40_2,
                            R.drawable.ic_outline_qris_40
                        )
                    ),
                    PaymentMethodItem(
                        type = "qr-4",
                        icons = listOf(
                            R.drawable.ic_outline_gopay_40_2,
                            R.drawable.ic_outline_qris_40
                        )
                    ),
                    PaymentMethodItem(
                        type = "qr-5",
                        icons = listOf(
                            R.drawable.ic_outline_gopay_40_2,
                            R.drawable.ic_outline_qris_40
                        )
                    ),
                    PaymentMethodItem(
                        type = "cs",
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
        customerDetail: CustomerDetail,
        paymentMethods: PaymentMethodList
    ) {
        var isExpand by remember { mutableStateOf(false) }
        Column {
            SnapAppBar(title = "Payment Methods", iconResId = R.drawable.ic_arrow_left) {
                onBackPressed()
            }
            Column(
                modifier = Modifier.padding(all = 16.dp).weight(1f)
            ) {
                SnapOverlayExpandingBox(
                    isExpanded = isExpand,
                    mainContent = {
                        SnapTotal(
                            amount = totalAmount,
                            orderId = orderId,
                            remainingTime = null
                        ) {
                            isExpand = it
                        }
                    },
                    expandingContent = {
                        SnapCustomerDetail(
                            name = customerDetail.name,
                            phone = customerDetail.phone,
                            addressLines = customerDetail.addressLines
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
                                    title = payment.type,
                                    iconList = payment.icons
                                ) {
                                    Toast.makeText(this@PaymentOptionActivity, "${payment.type}", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                )
            }
        }
    }

    @Parcelize
    private data class CustomerDetail(
        val name: String,
        val phone: String,
        val addressLines: List<String>
    ) : Parcelable
}