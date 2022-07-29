package com.midtrans.sdk.uikit.internal.presentation.banktransfer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import com.midtrans.sdk.corekit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.internal.view.SnapCustomerDetail
import com.midtrans.sdk.uikit.internal.view.SnapOverlayExpandingBox
import com.midtrans.sdk.uikit.internal.view.SnapTotal
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.view.SnapSingleIconListItem
import kotlinx.android.parcel.Parcelize

class BankTransferListActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            setupView()
        }
    }


    @Composable
    fun Content(
        bankList: List<Pair<String, Int>>
    ) {
        var expanding by remember {
            mutableStateOf(false)
        }
        Box(
            modifier = Modifier
                .padding(16.dp)
                .height(600.dp)
        ) {
            SnapOverlayExpandingBox(
                modifier = Modifier.fillMaxHeight(1f),
                isExpanded = expanding,
                mainContent = {
                    SnapTotal(
                        amount = totalAmount,
                        orderId = orderId,
                        remainingTime = null
                    ) {
                        expanding = it
                    }
                },
                expandingContent = {
                    customerDetail.run {
                        SnapCustomerDetail(
                            name = name,
                            phone = phone,
                            addressLines = addressLines
                        )
                    }
                }
            ) {
                LazyColumn {
                    bankList.forEach() { s ->
                        item {
                            SnapSingleIconListItem(title = s.first, iconResId = s.second,
                                modifier = Modifier.clickable {
                                    toBankTransfer2(s.first)
                                }
                            )
                        }
                    }
                }
            }
        }
    }

//TODO: bank list from payment method list
    @Composable
    fun setupView() {
        Content(
            bankList = listOf(
                Pair("Mandiri", R.drawable.ic_bank_mandiri_40),
                Pair("BCA", R.drawable.ic_bank_bca_40),
                Pair("BNI", R.drawable.ic_bank_bni_40)
            )
        )
    }

    private fun toBankTransfer2(bank: String) {
        startActivity(
            BankTransferDetailActivity.getIntent(
                activityContext = this,
                bankName = bank.toLowerCase(Locale.current),
                customerName = customerDetail.name,
                customerPhone = customerDetail.phone,
                addressLines = customerDetail.addressLines,
                orderId = orderId,
                totalAmount = totalAmount,
                companyCode = companyCode,
                billingNumber = billingNumber,
                vaNumber = vaNumber

            )
        )
    }

    private val totalAmount: String by lazy {
        intent.getStringExtra(EXTRA_TOTAL_AMOUNT)
            ?: throw RuntimeException("Total amount must not be empty")
    }

    private val orderId: String by lazy {
        intent.getStringExtra(EXTRA_ORDER_ID)
            ?: throw RuntimeException("Order ID must not be empty")
    }

    private val customerDetail: CustomerDetail by lazy {
        intent.getParcelableExtra(EXTRA_CUSTOMER_DETAIL) as? CustomerDetail
            ?: throw RuntimeException("Customer detail must not be empty")
    }


    private val vaNumber: String? by lazy {
        intent.getStringExtra(EXTRA_VANUMBER)
    }
    private val companyCode: String? by lazy {
        intent.getStringExtra(EXTRA_COMPANYCODE)
    }
    private val billingNumber: String? by lazy {
        intent.getStringExtra(EXTRA_BILLINGNUMBER)
    }

    companion object {
        private const val EXTRA_TOTAL_AMOUNT = "bankTransfer.extra.total_amount"
        private const val EXTRA_ORDER_ID = "bankTransfer.extra.order_id"
        private const val EXTRA_CUSTOMER_DETAIL = "bankTransfer.extra.customer_detail"
        private const val EXTRA_VANUMBER = "bankTransfer.extra.vanumber"
        private const val EXTRA_COMPANYCODE = "bankTransfer.extra.companycode"
        private const val EXTRA_BILLINGNUMBER = "bankTransfer.extra.billingnumber"


        fun getIntent(
            activityContext: Context,
            totalAmount: String,
            orderId: String,
            vaNumber: String? = null,
            companyCode: String? = null,
            billingNumber: String? = null,

            customerName: String,
            customerPhone: String,
            addressLines: List<String>
        ): Intent {
            return Intent(activityContext, BankTransferListActivity::class.java).apply {
                putExtra(EXTRA_TOTAL_AMOUNT, totalAmount)
                putExtra(EXTRA_ORDER_ID, orderId)
                putExtra(
                    EXTRA_CUSTOMER_DETAIL,
                    CustomerDetail(customerName, customerPhone, addressLines)
                )
                vaNumber?.let {
                    putExtra(EXTRA_VANUMBER, it)

                }
                companyCode?.let {
                    putExtra(EXTRA_COMPANYCODE, it)
                }
                billingNumber?.let {
                    putExtra(EXTRA_BILLINGNUMBER, it)
                }
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