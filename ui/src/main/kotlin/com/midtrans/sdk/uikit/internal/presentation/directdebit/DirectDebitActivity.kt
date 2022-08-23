package com.midtrans.sdk.uikit.internal.presentation.directdebit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.di.DaggerUiKitComponent
import com.midtrans.sdk.uikit.internal.model.CustomerInfo
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import com.midtrans.sdk.uikit.internal.view.*
import com.midtrans.sdk.uikit.internal.view.SnapColors.SUPPORT_DANGER_DEFAULT
import javax.inject.Inject

class DirectDebitActivity : BaseActivity() {

    @Inject
    internal lateinit var vmFactory: ViewModelProvider.Factory

    private val snapToken: String by lazy {
        intent.getStringExtra(EXTRA_SNAP_TOKEN)
            ?: throw RuntimeException("Snap token must not be empty")
    }

    private val paymentType: String by lazy {
        intent.getStringExtra(EXTRA_PAYMENT_TYPE)
            ?: throw RuntimeException("Payment type must not be empty")
    }

    private val amount: String by lazy {
        intent.getStringExtra(EXTRA_AMOUNT)
            ?: throw RuntimeException("Total amount must not be empty")
    }

    private val orderId: String by lazy {
        intent.getStringExtra(EXTRA_ORDER_ID)
            ?: throw RuntimeException("Order ID must not be empty")
    }

    private val customerInfo: CustomerInfo? by lazy {
        intent.getParcelableExtra(EXTRA_CUSTOMER_INFO) as? CustomerInfo
    }

    private val viewModel: DirectDebitViewModel by lazy {
        ViewModelProvider(this, vmFactory).get(DirectDebitViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerUiKitComponent.builder()
            .applicationContext(this.applicationContext)
            .build()
            .inject(this)

        setContent {
            DirectDebitContent(
                paymentType = paymentType,
                amount = amount,
                orderId = orderId,
                customerInfo = customerInfo,
                url = viewModel.getRedirectUrl().observeAsState("").value
            )
        }
    }

    @Composable
    @Preview(showBackground = true)
    fun PreviewOnly() {
        DirectDebitContent(
            paymentType = PaymentType.KLIK_BCA,
            amount = "Rp.123999",
            orderId = "order-id",
            customerInfo = CustomerInfo(
                name = "Dohn Joe",
                phone = "081234567890",
                addressLines = listOf("address one", "address two")
            ),
            url = "url"
        )
    }

    @Composable
    private fun DirectDebitContent(
        paymentType: String,
        amount: String,
        orderId: String,
        customerInfo: CustomerInfo?,
        url: String
    ) {
        var isCustomerDetailExpanded by remember { mutableStateOf(false) }
        var isInstructionExpanded by remember { mutableStateOf(false) }
        val title = stringResource(getTitleId(paymentType = paymentType))

        if (url.isEmpty()) {
            Column(
                modifier = Modifier.background(SnapColors.getARGBColor(SnapColors.OVERLAY_WHITE))
            ) {
                SnapAppBar(
                    title = title,
                    iconResId = R.drawable.ic_cross
                ) {
                    onBackPressed()
                }
                SnapOverlayExpandingBox(
                    isExpanded = isCustomerDetailExpanded,
                    mainContent = {
                        SnapTotal(
                            amount = amount,
                            orderId = orderId,
                            canExpand = customerInfo != null,
                            remainingTime = null
                        ) {
                            isCustomerDetailExpanded = it
                        }
                    },
                    expandingContent = customerInfo?.let {
                        {
                            SnapCustomerDetail(
                                name = customerInfo.name,
                                phone = customerInfo.phone,
                                addressLines = customerInfo.addressLines
                            )
                        }
                    },
                    followingContent = {
                        Column(
                            modifier = Modifier
                                .verticalScroll(state = rememberScrollState())
                                .padding(top = 16.dp)
                                .fillMaxWidth()

                        ) {
                            var userId by remember { mutableStateOf("") }

                            SnapText(stringResource(getInstructionId(paymentType = paymentType)))
                            KlikBcaUserIdTextField(paymentType = paymentType) { userId = it }
                            SnapInstructionButton(
                                modifier = Modifier.padding(top = 28.dp),
                                isExpanded = isInstructionExpanded,
                                iconResId = R.drawable.ic_help,
                                title = stringResource(R.string.bca_klik_pay_how_to_pay_title),
                                onExpandClick = { isInstructionExpanded = !isInstructionExpanded },
                                expandingContent = {
                                    Column {
                                        SnapNumberedList(
                                            list = stringArrayResource(getHowToPayId(paymentType = paymentType)).toList()
                                        )
                                    }
                                }
                            )
                            SnapButton(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 40.dp),
                                enabled = enableButton(paymentType, userId),
                                text = stringResource(R.string.bca_klik_pay_cta),
                                style = SnapButton.Style.PRIMARY
                            ) {
                                viewModel.payDirectDebit(
                                    snapToken = snapToken,
                                    paymentType = paymentType,
                                    userId = userId
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .background(SnapColors.getARGBColor(SnapColors.OVERLAY_WHITE))
                        .fillMaxHeight(1f)
                        .padding(all = 16.dp)
                )
            }
        } else {
            SnapWebView(
                title = title,
                paymentType = paymentType,
                url = url,
                onPageStarted = {
                    Log.d("WebView", "Started")
                    finishDirectDebitPayment()
                },
                onPageFinished = { }
            )
        }
    }

    private fun finishDirectDebitPayment() { //TODO temporary solution for showing Toast on sample app
        val data = Intent()
        data.putExtra(UiKitConstants.KEY_TRANSACTION_RESPONSE, UiKitConstants.STATUS_PENDING)
        setResult(RESULT_OK, data)
        finish()
    }

    private fun enableButton(paymentType: String, userId: String): Boolean {
        return when (paymentType) {
            PaymentType.KLIK_BCA -> userId.isNotEmpty()
            else -> true
        }
    }

    @Composable
    private fun KlikBcaUserIdTextField(
        paymentType: String,
        onUserIdChanged: (String) -> Unit
    ) {
        if (paymentType == PaymentType.KLIK_BCA) {
            var userId by remember { mutableStateOf(TextFieldValue()) }
            var isError by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(top = 12.dp)
                    .background(color = SnapColors.getARGBColor(SnapColors.SUPPORT_NEUTRAL_FILL))
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(top = 8.dp, bottom = 12.dp, start = 16.dp, end = 16.dp)
                ) {
                    SnapText(
                        text = stringResource(id = R.string.klik_bca_id_field_title),
                        style = SnapText.Style.SMALL
                    )
                    SnapTextField(
                        modifier = Modifier.fillMaxWidth(1f),
                        value = userId,
                        hint = stringResource(id = R.string.klik_bca_id_field_label),
                        onValueChange = {
                            userId = it
                            onUserIdChanged(userId.text)
                            isError = userId.text.isEmpty()
                        },
                        isError = isError,
                        isFocused = true,
                        onFocusChange = { },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )
                    if (userId.text.isEmpty()) {
                        Text(
                            text = stringResource(id = R.string.klik_bca_validation_error),
                            style = SnapTypography.STYLES.snapTextSmallRegular,
                            color = SnapColors.getARGBColor(SUPPORT_DANGER_DEFAULT)
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun getTitleId(paymentType: String): Int {
        return when (paymentType) {
            PaymentType.KLIK_BCA -> R.string.klik_bca_title
            PaymentType.BCA_KLIKPAY -> R.string.bca_klik_pay_title
            PaymentType.CIMB_CLICKS -> R.string.octo_click_title
            PaymentType.DANAMON_ONLINE -> R.string.danamon_title
            PaymentType.BRI_EPAY -> R.string.brimo_title
            else -> 0
        }
    }

    @Composable
    private fun getInstructionId(paymentType: String): Int {
        return when (paymentType) {
            PaymentType.KLIK_BCA -> R.string.klik_bca_instruction
            PaymentType.BCA_KLIKPAY -> R.string.bca_klik_pay_instruction
            PaymentType.CIMB_CLICKS -> R.string.octo_click_instruction
            PaymentType.DANAMON_ONLINE -> R.string.danamon_instruction
            PaymentType.BRI_EPAY -> R.string.brimo_instruction
            else -> 0
        }
    }

    @Composable
    private fun getHowToPayId(paymentType: String): Int {
        return when (paymentType) {
            PaymentType.KLIK_BCA -> R.array.klik_bca_how_to_pay
            PaymentType.BCA_KLIKPAY -> R.array.bca_klik_pay_how_to_pay
            PaymentType.CIMB_CLICKS -> R.array.octo_click_how_to_pay
            PaymentType.DANAMON_ONLINE -> R.array.danamon_how_to_pay
            PaymentType.BRI_EPAY -> R.array.brimo_how_to_pay
            else -> 0
        }
    }

    companion object {
        private const val EXTRA_SNAP_TOKEN = "directDebit.extra.snap_token"
        private const val EXTRA_PAYMENT_TYPE = "directDebit.extra.payment_type"
        private const val EXTRA_AMOUNT = "directDebit.extra.amount"
        private const val EXTRA_ORDER_ID = "directDebit.extra.order_id"
        private const val EXTRA_CUSTOMER_INFO = "directDebit.extra.customer_info"

        fun getIntent(
            activityContext: Context,
            snapToken: String,
            @PaymentType.Def paymentType: String,
            amount: String,
            orderId: String,
            customerInfo: CustomerInfo?
        ): Intent {
            return Intent(activityContext, DirectDebitActivity::class.java).apply {
                putExtra(EXTRA_SNAP_TOKEN, snapToken)
                putExtra(EXTRA_PAYMENT_TYPE, paymentType)
                putExtra(EXTRA_AMOUNT, amount)
                putExtra(EXTRA_ORDER_ID, orderId)
                putExtra(EXTRA_CUSTOMER_INFO, customerInfo)
            }
        }
    }
}