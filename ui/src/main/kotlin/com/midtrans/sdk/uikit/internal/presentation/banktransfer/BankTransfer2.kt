package com.midtrans.sdk.uikit.internal.presentation.banktransfer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.midtrans.sdk.corekit.api.model.VaNumber
import com.midtrans.sdk.corekit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.presentation.paymentoption.PaymentOptionActivity
import com.midtrans.sdk.uikit.internal.view.*
import kotlinx.android.parcel.Parcelize

class BankTransfer2 : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ForPreview()
        }
    }

    @Composable
    private fun Content(
        totalAmount: String,
        orderId: String,
        customerDetail: CustomerDetail
    ) {
        var expanding by remember {
            mutableStateOf(false)
        }
        val state = rememberScrollState()

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxHeight(1f)
        ) {
            SnapOverlayExpandingBox(
                modifier = Modifier.weight(1f),
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
                Column(
                    modifier = Modifier
                        .padding(top = 28.dp)
                        .verticalScroll(state),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(text = "lakukan pembayaran dari rekening bank mandiri")
                    SnapCopyableInfoListItem(
                        title = stringResource(id = R.string.general_instruction_company_code_mandiri_only),
                        info = "70012"
                    )

                    SnapCopyableInfoListItem(
                        title = stringResource(R.string.general_instruction_billing_number_mandiri_only),
                        info = "8098038r0qrerq"
                    )

                    var copied by remember {
                        mutableStateOf(false)
                    }
                    SnapCopyableInfoListItem(
                        title = "ada info apa",
                        info = "inilah infonya",
                        copied = copied,
                        onCopyClicked = { label -> copied = true }
                    )

                    var isExpanded by remember { mutableStateOf(false) }
                    SnapInstructionButton(
                        isExpanded = isExpanded,
                        iconResId = R.drawable.ic_help,
                        title = stringResource(id = R.string.kredivo_how_to_pay_title),
                        onExpandClick = { isExpanded = !isExpanded },
                        expandingContent = {
                            val (selectedOption, onOptionSelected) = remember { mutableStateOf(0) }
                            Column(
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                mandiriPaymentInstruction.forEach { item ->
                                    var selected = item.first == selectedOption
                                    Column(
                                        modifier = Modifier.selectable(
                                            selected = selected,
                                            onClick = {
                                                onOptionSelected(if (selected) 0 else item.first)
                                            },
                                            role = Role.RadioButton
                                        )
                                    ) {
                                        Row() {
                                            Text(
                                                text = stringResource(id = item.first),
                                                modifier = Modifier.weight(1f)
                                            )
                                            Icon(
                                                painter = painterResource(id = if (selected) R.drawable.ic_chevron_up else R.drawable.ic_chevron_down),
                                                contentDescription = null
                                            )
                                        }
                                        AnimatedVisibility(visible = selected) {
                                            SnapNumberedList(list = stringArrayResource(id = item.second).toList())

                                        }
                                        Divider(
                                            thickness = 1.dp,
                                            color = SnapColors.getARGBColor(SnapColors.BACKGROUND_BORDER_SOLID_SECONDARY),
                                            modifier = Modifier.padding(top = 16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            }

            SnapButton(
                text = "Saya sudah bayar",
                modifier = Modifier.fillMaxWidth(1f)
            ) {

            }
        }
    }

    @Composable
    @Preview
    fun ForPreview() {
        Content(
            totalAmount = "Rp.199.000",
            orderId = "#1231231233121",
            CustomerDetail(
                name = "ari bakti",
                phone = "4123123123123",
                addressLines = listOf("jalan", "jalan", "jalan")
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

    private val bankName: String by lazy {
        intent.getStringExtra(EXTRA_BANK)
            ?: throw RuntimeException("Bank detail must not be empty")
    }

    companion object {
        private const val EXTRA_TOTAL_AMOUNT = "bankTransfer.extra.total_amount"
        private const val EXTRA_ORDER_ID = "bankTransfer.extra.order_id"
        private const val EXTRA_CUSTOMER_DETAIL = "bankTransfer.extra.customer_detail"
        private const val EXTRA_BANK = "bankTransfer.extra.bank"

        fun openPaymentOptionPage(
            activityContext: Context,
            bankName: String,
            totalAmount: String,
            orderId: String,
            vaNumber: VaNumber,

            customerName: String,
            customerPhone: String,
            addressLines: List<String>
        ): Intent {
            return Intent(activityContext, PaymentOptionActivity::class.java).apply {
                putExtra(EXTRA_TOTAL_AMOUNT, totalAmount)
                putExtra(EXTRA_ORDER_ID, orderId)
                putExtra(
                    EXTRA_CUSTOMER_DETAIL,
                    CustomerDetail(customerName, customerPhone, addressLines)
                )
                putExtra(EXTRA_BANK, bankName)
            }
        }
    }

    @Parcelize
    private data class CustomerDetail(
        val name: String,
        val phone: String,
        val addressLines: List<String>
    ) : Parcelable

    private val mandiriPaymentInstruction by lazy {
        listOf(
            Pair<Int, Int>(R.string.mandiri_instruction_atm_title, R.array.mandiri_instruction_atm),
            Pair(
                R.string.mandiri_instruction_internet_banking_title,
                R.array.mandiri_instruction_internet_banking
            )
        )
    }
}