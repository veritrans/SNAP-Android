package com.midtrans.sdk.uikit.internal.presentation.banktransfer

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.rxjava2.subscribeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.IntentCompat.getParcelableExtra
import androidx.lifecycle.ViewModelProvider
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.model.TransactionResult
import com.midtrans.sdk.corekit.internal.network.model.response.Merchant
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.internal.model.CustomerInfo
import com.midtrans.sdk.uikit.internal.model.ItemInfo
import com.midtrans.sdk.uikit.internal.presentation.statusscreen.ErrorScreenActivity
import com.midtrans.sdk.uikit.internal.util.DateTimeUtil
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import com.midtrans.sdk.uikit.internal.util.UiKitConstants.STATUS_PENDING
import com.midtrans.sdk.uikit.internal.view.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class BankTransferDetailActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: BankTransferDetailViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[BankTransferDetailViewModel::class.java]
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
        getParcelableExtra(intent, EXTRA_CUSTOMER_DETAIL, CustomerInfo::class.java)
    }

    private val itemInfo: ItemInfo? by lazy {
        getParcelableExtra(intent, EXTRA_ITEM_INFO, ItemInfo::class.java)
    }

    private val paymentType: String by lazy {
        intent.getStringExtra(EXTRA_PAYMENT_TYPE)
            ?: throw RuntimeException("Bank name must not be empty")
    }

    private val snapToken: String by lazy {
        intent.getStringExtra(EXTRA_SNAP_TOKEN).orEmpty()
    }

    private val currentStepNumber: Int by lazy {
        intent.getIntExtra(EXTRA_STEP_NUMBER, 0)
    }

    private val merchant: Merchant? by lazy {
        getParcelableExtra(intent, EXTRA_MERCHANT_DATA, Merchant::class.java)
    }

    private val expiryTime: String? by lazy {
        intent.getStringExtra(EXTRA_EXPIRY_TIME)
    }

    private var isFirstInit = true

    private val errorScreenLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            setResult(result.resultCode, result?.data)
            finish()
            isFirstInit = false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UiKitApi.getDefaultInstance().daggerComponent.inject(this)
        viewModel.trackPageViewed(paymentType, currentStepNumber)
        viewModel.setDefaultExpiryTime(expiryTime)
        viewModel.merchant = merchant

        if (DateTimeUtil.getExpiredSeconds(viewModel.getExpiredHour()) <= 0L && isFirstInit) {
            launchExpiredErrorScreen()
        } else {
            setContent {
                Content(
                    totalAmount = totalAmount,
                    orderId = orderId,
                    customerInfo = customerInfo,
                    itemInfo = itemInfo,
                    vaNumberState = viewModel.vaNumberLiveData.observeAsState(initial = null),
                    billingNumberState = viewModel.billingNumberLiveData.observeAsState(initial = null),
                    bankName = paymentType,
                    companyCodeState = viewModel.companyCodeLiveData.observeAsState(initial = null),
                    destinationBankCode = viewModel.bankCodeLiveData.observeAsState(),
                    remainingTimeState = updateExpiredTime().subscribeAsState(initial = "00:00"),
                    errorState = viewModel.isBankTransferChargeErrorLiveData.observeAsState(initial = false),
                    viewModel = viewModel
                )
            }
            chargeBankTransfer()
            observeData()
        }
    }

    private fun chargeBankTransfer() {
        viewModel.chargeBankTransfer(
            snapToken = snapToken,
            paymentType = paymentType,
            customerEmail = null
        )
    }

    private fun observeData() {
        viewModel.transactionResult.observe(this) {
            val data = Intent()
            data.putExtra(
                UiKitConstants.KEY_TRANSACTION_RESULT,
                it
            )
            setResult(Activity.RESULT_OK, data)
        }

        //TODO: handle error ui/dialog
        viewModel.errorLiveData.observe(this) {
            val data = Intent()
            it?.let {
                data.putExtra(
                    UiKitConstants.KEY_ERROR_NAME,
                    it::class.java.simpleName
                )
                data.putExtra(
                    UiKitConstants.KEY_ERROR_MESSAGE,
                    it.message
                )
                setResult(Activity.RESULT_CANCELED, data)
            }
        }
    }

    private fun updateExpiredTime(): Observable<String> {
        return Observable
            .interval(1L, TimeUnit.SECONDS)
            .map { viewModel.getExpiredHour() }
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun setResult(data: TransactionResult) {
        val resultIntent = Intent().putExtra(UiKitConstants.KEY_TRANSACTION_RESULT, data)
        setResult(Activity.RESULT_OK, resultIntent)
    }

    private fun launchExpiredErrorScreen() {
        val expiredTransactionResult = TransactionResult(
            status = UiKitConstants.STATUS_FAILED,
            paymentType = paymentType,
            message = resources.getString(R.string.expired_desc)
        )
        if (isShowPaymentStatusPage()) {
            errorScreenLauncher.launch(
                ErrorScreenActivity.getIntent(
                    activityContext = this@BankTransferDetailActivity,
                    title = resources.getString(R.string.expired_title),
                    content = resources.getString(R.string.expired_desc),
                    transactionResult = expiredTransactionResult
                )
            )
        } else {
            setResult(expiredTransactionResult)
            finish()
        }
    }

    @Composable
    private fun Content(
        totalAmount: String,
        orderId: String,
        bankName: String,
        customerInfo: CustomerInfo?,
        itemInfo: ItemInfo?,
        vaNumberState: State<String?>,
        billingNumberState: State<String?>,
        companyCodeState: State<String?>,
        destinationBankCode: State<String?>,
        remainingTimeState: State<String>,
        errorState: State<Boolean>,
        viewModel: BankTransferDetailViewModel?
    ) {
        val billingNumber by remember { billingNumberState }
        val companyCode by remember { companyCodeState }
        val remainingTime by remember { remainingTimeState }
        var expanding by remember {
            mutableStateOf(false)
        }
        val state = rememberScrollState()

        if (DateTimeUtil.getExpiredSeconds(remainingTime) <= 0L && isFirstInit) {
            if (viewModel?.isExpired?.value == true) {
                launchExpiredErrorScreen()
            } else {
                val data = Intent()
                data.putExtra(
                    UiKitConstants.KEY_TRANSACTION_RESULT,
                    TransactionResult(
                        status = STATUS_PENDING,
                        transactionId = viewModel?.transactionResult?.value?.transactionId ?: STATUS_PENDING,
                        paymentType = paymentType
                    )
                )
                setResult(RESULT_OK, data)
                finish()
            }
        }

        Column {
            title[bankName]?.let {
                SnapAppBar(title = stringResource(id = it), iconResId = R.drawable.ic_cross) {
                    onBackPressed()
                }
            }
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
                            canExpand = customerInfo != null || itemInfo != null,
                            remainingTime = remainingTime
                        ) {
                            expanding = it
                        }
                    },
                    expandingContent = {
                        viewModel?.trackOrderDetailsViewed(paymentType)
                        SnapPaymentOrderDetails(
                            customerInfo = customerInfo,
                            itemInfo = itemInfo
                        )
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
                                color = SnapColors.getARGBColor(SnapColors.textSecondary)
                            )
                        }
                        getInfoField(
                            vaNumber = vaNumberState.value,
                            companyCode = companyCode,
                            billingNumber = billingNumber,
                            destinationBankCode = destinationBankCode.value,
                            viewModel = viewModel,
                            isChargeResponseError = errorState.value
                        )[bankName]?.invoke()

                        var isExpanded by remember { mutableStateOf(false) }
                        SnapInstructionButton(
                            isExpanded = isExpanded,
                            iconResId = R.drawable.ic_help,
                            title = stringResource(id = R.string.payment_instruction_how_to_pay_title),
                            onExpandClick = {
                                viewModel?.trackHowToPayClicked(paymentType)
                                isExpanded = !isExpanded
                            },
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
                                                    color = SnapColors.getARGBColor(SnapColors.textPrimary)
                                                )
                                                Icon(
                                                    painter = painterResource(id = if (selected) R.drawable.ic_chevron_up else R.drawable.ic_chevron_down),
                                                    contentDescription = null
                                                )
                                            }
                                            AnimatedVisibility(visible = selected) {
                                                val instructionList = stringArrayResource(id = item.second).toMutableList()
                                                destinationBankCode.value?.let {
                                                    instructionList.forEachIndexed{ index, value -> instructionList[index] =
                                                        value.replace(BANK_CODE_MARK, it.split(" ")[0]) }
                                                }
                                                SnapNumberedList(list = instructionList)
                                            }
                                            Divider(
                                                thickness = 1.dp,
                                                color = SnapColors.getARGBColor(SnapColors.backgroundBorderSolidSecondary),
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
                    viewModel?.trackSnapButtonClicked(
                        ctaName = getStringResourceInEnglish(R.string.i_have_already_paid),
                        paymentType = paymentType
                    )
                    onBackPressed()
                }
            }
        }
    }

    @Composable
    fun getDetailWithVaNumber(
        vaNumber: String?,
        paymentType: String,
        isChargeResponseError: Boolean,
        viewModel: BankTransferDetailViewModel?
    ): @Composable (() -> Unit) {
        val clipboardManager: ClipboardManager = LocalClipboardManager.current
        return {
            var copied by remember {
                mutableStateOf(false)
            }
            vaNumber.let {
                SnapCopyableInfoListItem(
                    title = stringResource(id = R.string.general_instruction_va_number_title),
                    info = it,
                    isError = isChargeResponseError,
                    copied = copied,
                    onCopyClicked = { label ->
                        copied = true
                        clipboardManager.setText(AnnotatedString(text = label))
                        viewModel?.trackAccountNumberCopied(paymentType)
                    },
                    onReloadClicked = {
                        viewModel?.trackReloadClicked(paymentType)
                        chargeBankTransfer()
                    }
                )
            }
        }
    }

    @SuppressLint("ServiceCast")
    @Composable
    private fun getInfoField(
        vaNumber: String?,
        billingNumber: String?,
        destinationBankCode: String?,
        companyCode: String?,
        isChargeResponseError: Boolean,
        viewModel: BankTransferDetailViewModel?
    ): Map<String, @Composable (() -> Unit)> {
        val clipboardManager: ClipboardManager = LocalClipboardManager.current
        return mapOf(
            Pair(
                PaymentType.BCA_VA,
                getDetailWithVaNumber(
                    vaNumber = vaNumber,
                    paymentType = PaymentType.BCA_VA,
                    viewModel = viewModel,
                    isChargeResponseError = isChargeResponseError
                )
            ),
            Pair(PaymentType.E_CHANNEL) {
                var companyCodeCopied by remember {
                    mutableStateOf(false)
                }

                var billingNumberCopied by remember {
                    mutableStateOf(false)
                }
                companyCode.let {
                    SnapCopyableInfoListItem(
                        title = stringResource(id = R.string.general_instruction_company_code_mandiri_only),
                        info = it,
                        isError = isChargeResponseError,
                        copied = companyCodeCopied,
                        onCopyClicked = { label ->
                            companyCodeCopied = true
                            clipboardManager.setText(AnnotatedString(text = label))
                            viewModel?.trackAccountNumberCopied(paymentType)
                        },
                        onReloadClicked = {
                            viewModel?.trackReloadClicked(paymentType)
                            chargeBankTransfer()
                        }
                    )
                }
                billingNumber.let {
                    SnapCopyableInfoListItem(
                        title = stringResource(id = R.string.general_instruction_billing_number_mandiri_only),
                        info = it,
                        isError = isChargeResponseError,
                        copied = billingNumberCopied,
                        onCopyClicked = { label ->
                            billingNumberCopied = true
                            clipboardManager.setText(AnnotatedString(text = label))
                            viewModel?.trackAccountNumberCopied(paymentType = PaymentType.E_CHANNEL)

                        }
                    )
                }
            },
            Pair(PaymentType.BNI_VA,  getDetailWithVaNumber(
                vaNumber = vaNumber,
                paymentType = PaymentType.BNI_VA,
                viewModel = viewModel,
                isChargeResponseError = isChargeResponseError
            )),
            Pair(PaymentType.BRI_VA,  getDetailWithVaNumber(
                vaNumber = vaNumber,
                paymentType = PaymentType.BRI_VA,
                viewModel = viewModel,
                isChargeResponseError = isChargeResponseError
            )),
            Pair(PaymentType.CIMB_VA,  getDetailWithVaNumber(
                vaNumber = vaNumber,
                paymentType = PaymentType.CIMB_VA,
                viewModel = viewModel,
                isChargeResponseError = isChargeResponseError
            )),
            Pair(PaymentType.PERMATA_VA,  getDetailWithVaNumber(
                vaNumber = vaNumber,
                paymentType = PaymentType.PERMATA_VA,
                viewModel = viewModel,
                isChargeResponseError = isChargeResponseError
            )),
            Pair(PaymentType.OTHER_VA) {
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
                        onCopyClicked = { label ->
                            copiedBankCode = true
                            clipboardManager.setText(AnnotatedString(text = label))
                            viewModel?.trackAccountNumberCopied(paymentType = PaymentType.OTHER_VA)
                        }
                    )
                }

                vaNumber.let {
                    SnapCopyableInfoListItem(
                        title = stringResource(id = R.string.general_instruction_va_number_title),
                        info = it,
                        isError = isChargeResponseError,
                        copied = copiedVa,
                        onCopyClicked = { label ->
                            copiedVa = true
                            clipboardManager.setText(AnnotatedString(text = label))
                            viewModel?.trackAccountNumberCopied(paymentType = PaymentType.OTHER_VA)
                        },
                        onReloadClicked = {
                            viewModel?.trackReloadClicked(paymentType)
                            chargeBankTransfer()
                        }
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
            customerInfo = CustomerInfo(
                name = "ari bakti",
                phone = "4123123123123",
                addressLines = listOf("jalan", "jalan", "jalan")
            ),
            itemInfo = null,
            vaNumberState = remember { mutableStateOf("32323") },
            companyCodeState = remember { mutableStateOf("32323") },
            bankName = "bni",
            billingNumberState = remember { mutableStateOf("2323222222") },
            destinationBankCode = remember { mutableStateOf("123") },
            remainingTimeState = remember { mutableStateOf("00:00") },
            viewModel = null,
            errorState = remember { mutableStateOf(false) }
        )
    }

    private val paymentInstruction by lazy {
        mapOf(
            Pair(PaymentType.BCA_VA, bcaPaymentInstruction),
            Pair(PaymentType.E_CHANNEL, mandiriPaymentInstruction),
            Pair(PaymentType.BNI_VA, bniPaymentInstruction),
            Pair(PaymentType.BRI_VA, briPaymentInstruction),
            Pair(PaymentType.CIMB_VA, cimbPaymentInstruction),
            Pair(PaymentType.PERMATA_VA, permataPaymentInstruction),
            Pair(PaymentType.OTHER_VA, otherBankPaymentInstruction)
        )
    }

    private val generalInstruction by lazy {
        mapOf(
            Pair(PaymentType.BCA_VA, R.string.general_instruction_bca),
            Pair(PaymentType.E_CHANNEL, R.string.general_instruction_mandiri),
            Pair(PaymentType.BNI_VA, R.string.general_instruction_bni_bri_permata_other_bank),
            Pair(PaymentType.BRI_VA, R.string.general_instruction_bni_bri_permata_other_bank),
            Pair(PaymentType.CIMB_VA, R.string.general_instruction_cimb),
            Pair(PaymentType.PERMATA_VA, R.string.general_instruction_bni_bri_permata_other_bank),
            Pair(PaymentType.OTHER_VA, R.string.general_instruction_bni_bri_permata_other_bank)
        )
    }

    private val bcaPaymentInstruction by lazy {
        listOf(
            Pair(
                R.string.bca_instruction_atm_title,
                R.array.bca_instruction_atm
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
            ),
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

    private val cimbPaymentInstruction by lazy {
        listOf(
            Pair(
                R.string.cimb_instruction_atm_title,
                R.array.cimb_instruction_atm
            ),
            Pair(
                R.string.cimb_instruction_octo_clicks_title,
                R.array.cimb_instruction_octo_clicks
            ),
            Pair(
                R.string.cimb_instruction_octo_mobile_title,
                R.array.cimb_instruction_octo_mobile
            ),
            Pair(
                R.string.cimb_instruction_other_bank_title,
                R.array.cimb_instruction_other_bank
            )
        )
    }

    private val title by lazy {
        mapOf(
            Pair(PaymentType.BCA_VA, R.string.bank_bca),
            Pair(PaymentType.E_CHANNEL, R.string.bank_mandiri),
            Pair(PaymentType.PERMATA_VA, R.string.bank_permata),
            Pair(PaymentType.BRI_VA, R.string.bank_bri),
            Pair(PaymentType.CIMB_VA, R.string.bank_cimb),
            Pair(PaymentType.BNI_VA, R.string.bank_bni),
            Pair(PaymentType.OTHER_VA, R.string.bank_other),
        )
    }

    companion object {
        private const val EXTRA_TOTAL_AMOUNT = "bankTransfer.extra.total_amount"
        private const val EXTRA_ORDER_ID = "bankTransfer.extra.order_id"
        private const val EXTRA_CUSTOMER_DETAIL = "bankTransfer.extra.customer_detail"
        private const val EXTRA_ITEM_INFO = "bankTransfer.extra.item_info"
        private const val EXTRA_PAYMENT_TYPE = "bankTransfer.extra.payment_type"
        private const val EXTRA_SNAP_TOKEN = "bankTransfer.extra.snap_token"
        private const val EXTRA_STEP_NUMBER = "bankTransfer.extra.step_number"
        private const val BANK_CODE_MARK = "--BANK_CODE--"
        private const val EXTRA_MERCHANT_DATA = "bankTransfer.extra.merchant_data"
        private const val EXTRA_EXPIRY_TIME = "bankTransfer.extra.expiry_time"

        fun getIntent(
            activityContext: Context,
            snapToken: String,
            paymentType: String,
            totalAmount: String,
            orderId: String,
            merchantData: Merchant? = null,
            customerInfo: CustomerInfo? = null,
            itemInfo: ItemInfo? = null,
            stepNumber: Int,
            expiryTime: String?
        ): Intent {
            return Intent(activityContext, BankTransferDetailActivity::class.java).apply {
                putExtra(EXTRA_TOTAL_AMOUNT, totalAmount)
                putExtra(EXTRA_ORDER_ID, orderId)
                putExtra(EXTRA_SNAP_TOKEN, snapToken)
                putExtra(EXTRA_CUSTOMER_DETAIL, customerInfo)
                putExtra(EXTRA_ITEM_INFO, itemInfo)
                putExtra(EXTRA_PAYMENT_TYPE, paymentType)
                putExtra(EXTRA_STEP_NUMBER, stepNumber)
                merchantData?.let { putExtra(EXTRA_MERCHANT_DATA, merchantData) }
                putExtra(EXTRA_EXPIRY_TIME, expiryTime)
            }
        }
    }
}