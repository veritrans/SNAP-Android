package com.midtrans.sdk.uikit.internal.presentation.directdebit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.view.*
import kotlinx.android.parcel.Parcelize

class DirectDebitActivity : BaseActivity() {
    private val data: DirectDebitData by lazy {
        intent.getParcelableExtra(EXTRA_DIRECT_DEBIT_DATA) as? DirectDebitData
            ?: throw RuntimeException("Input data must not be empty")
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
                paymentType = PaymentType.BCA_KLIKPAY,
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
        var isInstructionExpanded by remember { mutableStateOf(true) }
        Column(
            modifier = Modifier.background(SnapColors.getARGBColor(SnapColors.OVERLAY_WHITE))
        ) {
            SnapAppBar(
                title = stringResource(R.string.bca_klik_pay_title),
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
                        SnapText(stringResource(R.string.bca_klik_pay_instruction))
                        SnapInstructionButton(
                            modifier = Modifier.padding(top = 28.dp),
                            isExpanded = isInstructionExpanded,
                            iconResId = R.drawable.ic_help,
                            title = stringResource(R.string.bca_klik_pay_how_to_pay_title),
                            onExpandClick = { isInstructionExpanded = !isInstructionExpanded },
                            expandingContent = {
                                Column {
                                    SnapNumberedList(
                                        list = listOf(
                                            stringResource(R.string.bca_klik_pay_how_to_pay_1),
                                            stringResource(R.string.bca_klik_pay_how_to_pay_2),
                                            stringResource(R.string.bca_klik_pay_how_to_pay_3),
                                            stringResource(R.string.bca_klik_pay_how_to_pay_4),
                                            stringResource(R.string.bca_klik_pay_how_to_pay_5),
                                            stringResource(R.string.bca_klik_pay_how_to_pay_6),
                                        )
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
                            Log.d("SnapButton", "Clicked")
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

    companion object {
        private const val EXTRA_DIRECT_DEBIT_DATA = "EXTRA_BCA_KLIK_PAY_DATA"

        fun getIntent(
            activityContext: Context,
            @PaymentType.Def paymentType: String,
            amount: String,
            orderId: String,
            name: String,
            phone: String,
            addressLines: List<String>?
        ): Intent {
            return Intent(activityContext, DirectDebitActivity::class.java).apply {
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