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
import com.midtrans.sdk.corekit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.R
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
                    generalInstruction[bankName]?.let {
                        Text(text = stringResource(id = it))
                    }
                    getInfoField()[bankName]?.invoke()

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
                                paymentInstruction[bankName]?.forEach { item ->
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
                text = stringResource(id = R.string.i_have_already_paid),
                modifier = Modifier.fillMaxWidth(1f)
            ) {

            }
        }
    }

    @Composable
    private fun getInfoField(): Map<String, @Composable (() -> Unit)> {
        return mapOf(
            Pair(BANK_BCA) {
                var copied by remember {
                    mutableStateOf(false)
                }
                vaNumber?.let {
                    SnapCopyableInfoListItem(
                        title = stringResource(id = R.string.general_instruction_va_number_title),
                        info = it,
                        copied = copied,
                        onCopyClicked = { label -> copied = true }
                    )
                }
            },
            Pair(BANK_MANDIRI) {
                var companyCodeCopied by remember {
                    mutableStateOf(false)
                }

                var billingNumberCopied by remember {
                    mutableStateOf(false)
                }
                companyCode?.let {
                    SnapCopyableInfoListItem(
                        title = stringResource(id = R.string.general_instruction_company_code_mandiri_only),
                        info = it,
                        copied = companyCodeCopied,
                        onCopyClicked = { label -> companyCodeCopied = true }
                    )
                }
                billingNumber?.let {
                    SnapCopyableInfoListItem(
                        title = stringResource(id = R.string.general_instruction_billing_number_mandiri_only),
                        info = it,
                        copied = billingNumberCopied,
                        onCopyClicked = { label -> billingNumberCopied = true }
                    )
                }
            },
            Pair(BANK_BNI) {
                var copied by remember {
                    mutableStateOf(false)
                }
                vaNumber?.let {
                    SnapCopyableInfoListItem(
                        title = stringResource(id = R.string.general_instruction_va_number_title),
                        info = it,
                        copied = copied,
                        onCopyClicked = { label -> copied = true }
                    )
                }
            }
        )
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
            ?: throw RuntimeException("Bank name must not be empty")
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
        private const val EXTRA_BANK = "bankTransfer.extra.bank"
        private const val EXTRA_VANUMBER = "bankTransfer.extra.vanumber"
        private const val EXTRA_COMPANYCODE = "bankTransfer.extra.companycode"
        private const val EXTRA_BILLINGNUMBER = "bankTransfer.extra.billingnumber"

        const val BANK_BCA = "bca"
        const val BANK_BNI = "bni"
        const val BANK_MANDIRI = "mandiri"

        fun getIntent(
            activityContext: Context,
            bankName: String,
            totalAmount: String,
            orderId: String,
            vaNumber: String? = null,
            companyCode: String? = null,
            billingNumber: String? = null,

            customerName: String,
            customerPhone: String,
            addressLines: List<String>
        ): Intent {
            return Intent(activityContext, BankTransfer2::class.java).apply {
                putExtra(EXTRA_TOTAL_AMOUNT, totalAmount)
                putExtra(EXTRA_ORDER_ID, orderId)
                putExtra(
                    EXTRA_CUSTOMER_DETAIL,
                    CustomerDetail(customerName, customerPhone, addressLines)
                )
                putExtra(EXTRA_BANK, bankName)
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

    private val paymentInstruction by lazy {
        mapOf(
            Pair(BANK_BCA, bcaPaymentInstruction),
            Pair(BANK_MANDIRI, mandiriPaymentInstruction),
            Pair(BANK_BNI, bniPaymentInstruction)
        )
    }

    private val generalInstruction by lazy {
        mapOf(
            Pair(BANK_BCA, R.string.general_instruction_bca),
            Pair(BANK_MANDIRI, R.string.general_instruction_mandiri),
            Pair(BANK_BNI, R.string.general_instruction_bni_bri_permata_other_bank)
        )
    }

    private val bcaPaymentInstruction by lazy {
        listOf(
            Pair(
                R.string.bca_instruction_atm_title,
                R.array.mandiri_instruction_atm
            ),
            Pair(
                R.string.bca_instruction_klikbca_title,
                R.array.bca_instruction_klikbca
            ),
            Pair(
                R.string.bca_instruction_mbca_titel,
                R.array.bca_instruction_mbca
            )
        )
    }

    private val mandiriPaymentInstruction by lazy {
        listOf(
            Pair(
                R.string.mandiri_instruction_atm_title,
                R.array.mandiri_instruction_atm
            ),
            Pair(
                R.string.mandiri_instruction_internet_banking_title,
                R.array.mandiri_instruction_internet_banking
            )
        )
    }

    private val bniPaymentInstruction by lazy {
        listOf(
            Pair(
                R.string.bni_instruction_atm_title,
                R.array.bni_instruction_atm
            ),
            Pair(
                R.string.bni_instruction_internet_banking_title,
                R.array.bni_instruction_internet_banking
            ),
            Pair(
                R.string.bni_instruction_mobile_banking_title,
                R.array.bni_instruction_mobile_banking
            ),
            Pair(
                R.string.bni_instruction_other_banks_title,
                R.array.bni_instruction_other_banks
            )
        )
    }
}