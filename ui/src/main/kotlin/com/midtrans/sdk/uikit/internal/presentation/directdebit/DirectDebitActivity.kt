package com.midtrans.sdk.uikit.internal.presentation.directdebit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
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
import com.midtrans.sdk.uikit.internal.view.*
import com.midtrans.sdk.uikit.internal.view.SnapColors.SUPPORT_DANGER_DEFAULT
import kotlinx.android.parcel.Parcelize

class DirectDebitActivity : BaseActivity() {
    private val data: DirectDebitData by lazy {
        intent.getParcelableExtra(EXTRA_DIRECT_DEBIT_DATA) as? DirectDebitData
            ?: throw RuntimeException("Input data must not be empty")
    }

    private val snapToken: String by lazy {
        intent.getStringExtra(EXTRA_SNAP_TOKEN) ?: throw RuntimeException("Snap token must exist")
    }

    private val viewModel: DirectDebitViewModel by lazy {
        ViewModelProvider(this).get(DirectDebitViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { DirectDebitContent(data) }
    }

    @Composable
    @Preview(showBackground = true)
    fun PreviewOnly() {
        DirectDebitContent(
            data = DirectDebitData(
                paymentType = PaymentType.KLIK_BCA,
                amount = "Rp.123999",
                orderId = "order-id",
                name = "Dohn Joe",
                phone = "081234567890",
                addressLines = listOf("address one", "address two")
            )
        )
    }

    @Composable
    private fun DirectDebitContent(data: DirectDebitData) {
        var isCustomerDetailExpanded by remember { mutableStateOf(false) }
        var isInstructionExpanded by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier.background(SnapColors.getARGBColor(SnapColors.OVERLAY_WHITE))
        ) {
            SnapAppBar(
                title = getTitle(paymentType = data.paymentType),
                iconResId = R.drawable.ic_cross
            ) {
                onBackPressed()
            }
            SnapOverlayExpandingBox(
                isExpanded = isCustomerDetailExpanded,
                mainContent = {
                    SnapTotal(
                        amount = data.amount,
                        orderId = data.orderId,
                        canExpand = true,
                        remainingTime = null
                    ) {
                        isCustomerDetailExpanded = it
                    }
                },
                expandingContent = {
                    SnapCustomerDetail(
                        name = data.name,
                        phone = data.phone,
                        addressLines = data.addressLines.orEmpty()
                    )
                },
                followingContent = {
                    Column(
                        modifier = Modifier
                            .verticalScroll(state = rememberScrollState())
                            .padding(top = 16.dp)
                            .fillMaxWidth()

                    ) {
                        var userId by remember { mutableStateOf("") }

                        SnapText(getInstruction(paymentType = data.paymentType))
                        KlikBcaUserIdTextField(paymentType = data.paymentType) { userId = it }
                        SnapInstructionButton(
                            modifier = Modifier.padding(top = 28.dp),
                            isExpanded = isInstructionExpanded,
                            iconResId = R.drawable.ic_help,
                            title = stringResource(R.string.bca_klik_pay_how_to_pay_title),
                            onExpandClick = { isInstructionExpanded = !isInstructionExpanded },
                            expandingContent = {
                                Column {
                                    SnapNumberedList(
                                        list = getHowToPayList(paymentType = data.paymentType)
                                    )
                                }
                            }
                        )
                        SnapButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 40.dp),
                            enabled = true,
                            text = stringResource(R.string.bca_klik_pay_cta),
                            style = SnapButton.Style.PRIMARY
                        ) {
                            viewModel.pay(
                                snapToken = snapToken,
                                paymentType = data.paymentType,
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
    private fun getTitle(paymentType: String): String {
        return when (paymentType) {
            PaymentType.KLIK_BCA -> stringResource(id = R.string.klik_bca_title)
            PaymentType.BCA_KLIKPAY -> stringResource(id = R.string.bca_klik_pay_title)
            PaymentType.CIMB_CLICKS -> stringResource(id = R.string.octo_click_title)
            PaymentType.DANAMON_ONLINE -> stringResource(id = R.string.danamon_title)
            PaymentType.BRI_EPAY -> stringResource(id = R.string.brimo_title)
            else -> ""
        }
    }

    @Composable
    private fun getInstruction(paymentType: String): String {
        return when (paymentType) {
            PaymentType.KLIK_BCA -> stringResource(id = R.string.klik_bca_instruction)
            PaymentType.BCA_KLIKPAY -> stringResource(id = R.string.bca_klik_pay_instruction)
            PaymentType.CIMB_CLICKS -> stringResource(id = R.string.octo_click_instruction)
            PaymentType.DANAMON_ONLINE -> stringResource(id = R.string.danamon_instruction)
            PaymentType.BRI_EPAY -> stringResource(id = R.string.brimo_instruction)
            else -> ""
        }
    }

    @Composable
    private fun getHowToPayList(paymentType: String): List<String> {
        return when (paymentType) {
            PaymentType.KLIK_BCA -> stringArrayResource(id = R.array.klik_bca_how_to_pay).toList()
            PaymentType.BCA_KLIKPAY -> stringArrayResource(id = R.array.bca_klik_pay_how_to_pay).toList()
            PaymentType.CIMB_CLICKS -> stringArrayResource(id = R.array.octo_click_how_to_pay).toList()
            PaymentType.DANAMON_ONLINE -> stringArrayResource(id = R.array.danamon_how_to_pay).toList()
            PaymentType.BRI_EPAY -> stringArrayResource(id = R.array.brimo_how_to_pay).toList()
            else -> {
                listOf("")
            }
        }
    }

    companion object {
        private const val EXTRA_DIRECT_DEBIT_DATA = "EXTRA_DIRECT_DEBIT_DATA"
        private const val EXTRA_SNAP_TOKEN = "EXTRA_SNAP_TOKEN"

        fun getIntent(
            activityContext: Context,
            snapToken: String,
            @PaymentType.Def paymentType: String,
            amount: String,
            orderId: String,
            name: String,
            phone: String,
            addressLines: List<String>?
        ): Intent {
            return Intent(activityContext, DirectDebitActivity::class.java).apply {
                putExtra(EXTRA_SNAP_TOKEN, snapToken)
                putExtra(
                    EXTRA_DIRECT_DEBIT_DATA,
                    DirectDebitData(
                        paymentType = paymentType,
                        amount = amount,
                        orderId = orderId,
                        name = name,
                        phone = phone,
                        addressLines = addressLines
                    )
                )
            }
        }
    }

    @Parcelize
    private data class DirectDebitData(
        val paymentType: String,
        val amount: String,
        val orderId: String,
        val name: String,
        val phone: String,
        val addressLines: List<String>?
    ) : Parcelable
}