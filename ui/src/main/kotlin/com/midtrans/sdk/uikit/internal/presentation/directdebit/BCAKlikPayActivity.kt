package com.midtrans.sdk.uikit.internal.presentation.directdebit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.text.style.BackgroundColorSpan
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.midtrans.sdk.corekit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.view.*
import kotlinx.android.parcel.Parcelize

class BCAKlikPayActivity : BaseActivity() {

    private val data: BCAKlikPayData by lazy {
        intent.getParcelableExtra(EXTRA_BCA_KLIK_PAY_DATA) as? BCAKlikPayData
            ?: throw RuntimeException("Input data must not be empty")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { BCAKlikPayContent(data) }
    }

    @Composable
    @Preview(showBackground = true)
    fun PreviewOnly() {
        BCAKlikPayContent(data = data)
    }

    @Composable
    private fun BCAKlikPayContent(data: BCAKlikPayData) {
        Column(
            modifier = Modifier.verticalScroll(state = rememberScrollState())
        ) {
            SnapAppBar(title = "BCA KlikPay", iconResId = R.drawable.ic_cross) {
                onBackPressed()
            }
            var isExpanded by remember { mutableStateOf(false) }
            SnapOverlayExpandingBox(
                isExpanded = isExpanded,
                mainContent = {
                    SnapTotal(
                        amount = data.amount,
                        orderId = data.orderId,
                        remainingTime = "22:22:22" //TODO get remaining time?
                    ) {
                        isExpanded = it
                    }
                },
                expandingContent = {
                    SnapCustomerDetail( //TODO check if customer detail is mandatory
                        name = data.name.orEmpty(),
                        phone = data.phone.orEmpty(),
                        addressLines = data.address.orEmpty()
                    )
                },
                followingContent = {
                    Column() {
                        SnapText("Mohon untuk menyelesaikan pembayaran melalui situs web BCA KlikPay")
                        
                        val words = listOf<String>(
                            "This is <b>bolt</b> <i>italic</i> <u>underline</u> Lorem ipsumgalksnfdlsan jklnlkjfnasd lkj nfaklsdjnf ljkasndf n lad lasldasdla halo halo bandung ibukkota priangan, kokwaowkeaowkeo awas pusing",
                            "This is <b>bolt</b> <i>italic</i> <u>underline</u> Lorem ipsumgalksnfdlsan jklnlkjfnasd lkj nfaklsdjnf ljkasndf n lad, ga tau mau nulis apa, bingung semuanya",
                            "This is <b>bolt</b> <i>italic</i> <u>underline</u> Lorem ipsumgalksnfdlsan jklnlkjfnasd lkj nfaklsdjnf ljkasndf n lad, halo hallo lagi, kamu lagi ngapain",
                            "This is <b>bolt</b> <i>italic</i> <u>underline</u> Lorem ipsumgalksnfdlsan jklnlkjfnasd lkj nfaklsdjnf ljkasndf n lad",
                            "This is <b>bolt</b> <i>italic</i> <u>underline</u> Lorem ipsumgalksnfdlsan jklnlkjfnasd lkj nfaklsdjnf ljkasndf n lad",
                            "This is <b>bolt</b> <i>italic</i> <u>underline</u> Lorem ipsumgalksnfdlsan jklnlkjfnasd lkj nfaklsdjnf ljkasndf n lad",
                        )
                        SnapNumberedList(list = words)

                        SnapButton(
                            enabled = true,
                            text = "Bayar sekarang",
                            style = SnapButton.Style.PRIMARY
                        ) {
                            Log.d("SnapButton", "Clicked")
                        }
                    }
                }
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
            address: List<String>?
        ): Intent {
            return Intent(activityContext, BCAKlikPayActivity::class.java).apply {
                putExtra(
                    EXTRA_BCA_KLIK_PAY_DATA,
                    BCAKlikPayData(
                        amount = amount,
                        orderId = orderId,
                        name = name,
                        phone = phone,
                        address = address
                    )
                )
            }
        }
    }

    @Parcelize
    private data class BCAKlikPayData(
        val amount: String,
        val orderId: String,
        val name: String?,
        val phone: String?,
        val address: List<String>?
    ) : Parcelable
}