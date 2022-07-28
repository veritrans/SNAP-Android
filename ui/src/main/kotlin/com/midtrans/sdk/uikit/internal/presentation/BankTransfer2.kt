package com.midtrans.sdk.uikit.internal.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.midtrans.sdk.corekit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.view.*

class BankTransfer2 : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ForPreview()
        }
    }


    @Composable
    fun Content(
    ) {
        var expanding by remember {
            mutableStateOf(false)
        }

        Column(modifier = Modifier
            .padding(16.dp)
            .fillMaxHeight(1f)) {
            SnapOverlayExpandingBox(
                modifier = Modifier.weight(1f),
                isExpanded = expanding,
                mainContent = {
                    SnapTotal(
                        amount = "Rp399.000",
                        orderId = "#121231231231",
                        remainingTime = null
                    ) {
                        expanding = it
                    }
                },
                expandingContent = {
                    SnapCustomerDetail(
                        name = "Ari Bhakti S",
                        phone = "081123405678",
                        addressLines = listOf(
                            "The House BLok 3 No.2",
                            "Jalan Raya 123",
                            "Jakarta Selatan 123450"
                        )
                    )
                }
            ) {
                Column(
                    modifier = Modifier.padding(top = 28.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(text = "lakukan pembayaran dari rekening bank mandiri")
                    SnapCopyableInfoListItem(
                        title = "kode prerusahaan",
                        info = "70012"
                    )

                    SnapCopyableInfoListItem(
                        title = resources.getString(R.string.general_instruction_billing_number_mandiri_only),
                        info = "8098038r0qrerq"
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

        )

    }
}