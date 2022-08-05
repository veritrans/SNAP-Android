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
import androidx.compose.runtime.livedata.observeAsState
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
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

class BankTransferDetailActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: BankTransferDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Content(
                totalAmount = totalAmount,
                orderId = orderId,
                customerDetail = customerDetail,
                vaNumberState = viewModel.vaNumberLiveData.observeAsState(),
                billingNumberState = viewModel.billingNumberLiveData.observeAsState(),
                bankName = paymentType,
                companyCodeState = viewModel.companyCodeLiveData.observeAsState()
            )
        }
        viewModel.chargeBankTransfer(
            snapToken = snapToken,
            paymentType = paymentType,
            customerEmail = null
        )
    }

    @Composable
    private fun Content(
        totalAmount: String,
        orderId: String,
        bankName: String,
        customerDetail: CustomerDetail,
        vaNumberState: State<String?>,
        billingNumberState: State<String?>,
        companyCodeState: State<String?>
    ) {
        val vaNumber by remember { vaNumberState }
        val billingNumber by remember { billingNumberState }
        val companyCode by remember { companyCodeState }
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
                        Text(
                            text = stringResource(id = it),
                            style = SnapTypography.STYLES.snapTextBigRegular,
                            color = SnapColors.getARGBColor(SnapColors.TEXT_SECONDARY)
                        )
                    }
                    getInfoField(
                        vaNumber = vaNumber,
                        companyCode = companyCode,
                        billingNumber = billingNumber,
                        destinationBankCode = destinationBankCode
                    )[bankName]?.invoke()

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
                                    val selected = item.first == selectedOption
                                    Column(
                                        modifier = Modifier.selectable(
                                            selected = selected,
                                            onClick = {
                                                onOptionSelected(if (selected) 0 else item.first)
                                            },
                                            role = Role.RadioButton
                                        )
                                    ) {
                                        Row {
                                            Text(
                                                text = stringResource(id = item.first),
                                                modifier = Modifier.weight(1f),
                                                style = SnapTypography.STYLES.snapTextMediumMedium,
                                                color = SnapColors.getARGBColor(SnapColors.TEXT_PRIMARY)
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
                //TODO: Click action
            }
        }
    }

    @Composable
    fun getDetailWithVaNumber(vaNumber: String?): @Composable (() -> Unit) {
        return {
            var copied by remember {
                mutableStateOf(false)
            }
            vaNumber?.let {
                SnapCopyableInfoListItem(
                    title = stringResource(id = R.string.general_instruction_va_number_title),
                    info = it,
                    copied = copied,
                    onCopyClicked = { copied = true }
                )
            }
        }
    }

    @Composable
    private fun getInfoField(
        vaNumber: String?,
        billingNumber: String?,
        destinationBankCode: String?,
        companyCode: String?
    ): Map<String, @Composable (() -> Unit)> {
        return mapOf(
            Pair(BANK_BCA, getDetailWithVaNumber(vaNumber = vaNumber)),
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
            Pair(BANK_BNI, getDetailWithVaNumber(vaNumber = vaNumber)),
            Pair(BANK_BRI, getDetailWithVaNumber(vaNumber = vaNumber)),
            Pair(BANK_PERMATA, getDetailWithVaNumber(vaNumber = vaNumber)),
            Pair(BANK_OTHERS) {
                var copiedBankCode by remember {
                    mutableStateOf(false)
                }
                var copiedVa by remember {
                    mutableStateOf(false)
                }
                destinationBankCode?.let {
                    SnapCopyableInfoListItem(
                        title = stringResource(id = R.string.general_instruction_destination_bank_code_permata_only),
                        info = it,
                        copied = copiedBankCode,
                        onCopyClicked = { label -> copiedBankCode = true }
                    )
                }

                vaNumber?.let {
                    SnapCopyableInfoListItem(
                        title = stringResource(id = R.string.general_instruction_va_number_title),
                        info = it,
                        copied = copiedVa,
                        onCopyClicked = { copiedVa = true }
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
            customerDetail = CustomerDetail(
                name = "ari bakti",
                phone = "4123123123123",
                addressLines = listOf("jalan", "jalan", "jalan")
            ),
            vaNumberState = remember { mutableStateOf("23421312") },
            companyCodeState = remember { mutableStateOf("32323") },
            bankName = "bni",
            billingNumberState = remember { mutableStateOf("2323222222") }
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

    private val paymentType: String by lazy {
        intent.getStringExtra(EXTRA_PAYMENTTYPE)
            ?: throw RuntimeException("Bank name must not be empty")
    }
    private val destinationBankCode: String? by lazy {
        intent.getStringExtra(EXTRA_DESTINATIONBANKCODE)
    }
    private val snapToken: String by lazy {
        intent.getStringExtra(EXTRA_SNAPTOKEN).orEmpty()
    }

    private val paymentInstruction by lazy {
        mapOf(
            Pair(BANK_BCA, bcaPaymentInstruction),
            Pair(BANK_MANDIRI, mandiriPaymentInstruction),
            Pair(BANK_BNI, bniPaymentInstruction),
            Pair(BANK_BRI, briPaymentInstruction),
            Pair(BANK_PERMATA, permataPaymentInstruction),
            Pair(BANK_OTHERS, otherBankPaymentInstruction)
        )
    }

    private val generalInstruction by lazy {
        mapOf(
            Pair(BANK_BCA, R.string.general_instruction_bca),
            Pair(BANK_MANDIRI, R.string.general_instruction_mandiri),
            Pair(BANK_BNI, R.string.general_instruction_bni_bri_permata_other_bank),
            Pair(BANK_BRI, R.string.general_instruction_bni_bri_permata_other_bank),
            Pair(BANK_PERMATA, R.string.general_instruction_bni_bri_permata_other_bank),
            Pair(BANK_OTHERS, R.string.general_instruction_bni_bri_permata_other_bank)
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

    private val permataPaymentInstruction by lazy {
        listOf(
            Pair(
                R.string.permata_instruction_permata_title,
                R.array.permata_instruction_permata
            ),
            Pair(
                R.string.permata_instruction_other_bank_title,
                R.array.permata_instruction_other_bank
            )
        )
    }

    private val briPaymentInstruction by lazy {
        listOf(
            Pair(
                R.string.bri_instruction_atm_title,
                R.array.bri_instruction_atm
            ),
            Pair(
                R.string.bri_instruction_ib_title,
                R.array.bri_instruction_ib
            ),
            Pair(
                R.string.bri_instruction_brimo_title,
                R.array.bri_instruction_brimo
            ),
            Pair(
                R.string.bri_instruction_other_bank_title,
                R.array.bri_instruction_other_bank
            )
        )
    }

    private val otherBankPaymentInstruction by lazy {
        listOf(
            Pair(
                R.string.other_bank_instruction_prima_title,
                R.array.other_bank_instruction_prima
            ),
            Pair(
                R.string.other_bank_instruction_atm_bersama_title,
                R.array.other_bank_instruction_atm_bersama
            ),
            Pair(
                R.string.other_bank_instruction_alto_title,
                R.array.other_bank_instruction_alto
            )
        )
    }

    companion object {
        private const val EXTRA_TOTAL_AMOUNT = "bankTransfer.extra.total_amount"
        private const val EXTRA_ORDER_ID = "bankTransfer.extra.order_id"
        private const val EXTRA_CUSTOMER_DETAIL = "bankTransfer.extra.customer_detail"
        private const val EXTRA_PAYMENTTYPE = "bankTransfer.extra.paymenttype"
        private const val EXTRA_SNAPTOKEN = "bankTransfer.extra.snaptoken"
        private const val EXTRA_DESTINATIONBANKCODE = "bankTransfer.extra.destinationbankcode"

        const val BANK_BCA = "bca"
        const val BANK_BNI = "bni"
        const val BANK_MANDIRI = "mandiri"
        const val BANK_PERMATA = "permata"
        const val BANK_BRI = "bri"
        const val BANK_OTHERS = "other banks"

        fun getIntent(
            activityContext: Context,
            snapToken: String,
            paymentType: String,
            totalAmount: String,
            orderId: String,
            destinationBankCode: String? = null,
            customerName: String,
            customerPhone: String,
            addressLines: List<String>
        ): Intent {
            return Intent(activityContext, BankTransferDetailActivity::class.java).apply {
                putExtra(EXTRA_TOTAL_AMOUNT, totalAmount)
                putExtra(EXTRA_ORDER_ID, orderId)
                putExtra(EXTRA_SNAPTOKEN, snapToken)
                putExtra(
                    EXTRA_CUSTOMER_DETAIL,
                    CustomerDetail(customerName, customerPhone, addressLines)
                )
                putExtra(EXTRA_PAYMENTTYPE, paymentType)

                destinationBankCode?.let {
                    putExtra(EXTRA_DESTINATIONBANKCODE, it)
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