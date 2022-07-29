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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.midtrans.sdk.corekit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.view.*
import kotlinx.android.parcel.Parcelize

class BcaKlikPayActivity : BaseActivity() {
    private val data: BcaKlikPayData by lazy {
        intent.getParcelableExtra(EXTRA_BCA_KLIK_PAY_DATA) as? BcaKlikPayData
            ?: throw RuntimeException("Input data must not be empty")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { BCAKlikPayContent(data) }
    }

    @Composable
    @Preview(showBackground = true)
    fun PreviewOnly() {
        BCAKlikPayContent(
            data = BcaKlikPayData(
                amount = "999",
                orderId = "order",
                name = "saya",
                phone = "08123456789",
                addressLines = listOf("Ini Alamat 1")
            )
        )
    }

    @Composable
    private fun BCAKlikPayContent(data: BcaKlikPayData) {
        var isExpanded by remember { mutableStateOf(false) }
        Column {
            SnapAppBar(title = stringResource(R.string.bca_klik_pay_title), iconResId = R.drawable.ic_cross) {
                onBackPressed()
            }
            SnapOverlayExpandingBox(
                isExpanded = isExpanded,
                mainContent = {
                    SnapTotal(
                        amount = data.amount,
                        orderId = data.orderId,
                        remainingTime = null
                    ) {
                        isExpanded = it
                    }
                },
                expandingContent = {
                    SnapCustomerDetail(
                        name = data.name.orEmpty(),
                        phone = data.phone.orEmpty(),
                        addressLines = data.addressLines.orEmpty()
                    )
                },
                followingContent = {
                    Column {
                        Column(
                            Modifier.verticalScroll(enabled = true, state = rememberScrollState())
                        ) {
                            SnapText(stringResource(R.string.bca_klik_pay_instruction))
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
                        SnapButton(
                            enabled = true,
                            text = stringResource(R.string.bca_klik_pay_cta),
                            style = SnapButton.Style.PRIMARY
                        ) {
                            Log.d("SnapButton", "Clicked")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxHeight(1f)
                    .padding(all = 16.dp)
                    .background(SnapColors.getARGBColor(SnapColors.OVERLAY_WHITE))
            )
        }
    }

    companion object {
        private const val EXTRA_BCA_KLIK_PAY_DATA = "EXTRA_BCA_KLIK_PAY_DATA"

        fun getIntent(
            activityContext: Context,
            amount: String,
            orderId: String,
            name: String?,
            phone: String?,
            addressLines: List<String>?
        ): Intent {
            return Intent(activityContext, BcaKlikPayActivity::class.java).apply {
                putExtra(
                    EXTRA_BCA_KLIK_PAY_DATA,
                    BcaKlikPayData(
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
    private data class BcaKlikPayData(
        val amount: String,
        val orderId: String,
        val name: String?,
        val phone: String?,
        val addressLines: List<String>?
    ) : Parcelable
}