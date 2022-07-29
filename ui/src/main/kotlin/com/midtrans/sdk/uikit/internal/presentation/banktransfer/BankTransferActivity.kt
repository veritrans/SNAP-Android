package com.midtrans.sdk.uikit.internal.presentation.banktransfer

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.midtrans.sdk.corekit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.internal.view.SnapCustomerDetail
import com.midtrans.sdk.uikit.internal.view.SnapOverlayExpandingBox
import com.midtrans.sdk.uikit.internal.view.SnapTotal
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.view.SnapSingleIconListItem

class BankTransferActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ForPreview()
        }
    }


    @Composable
    fun Content(
        bankList: List<Pair<String, Int>>
    ) {
        var expanding by remember {
            mutableStateOf(false)
        }
        Box(modifier = Modifier.padding(16.dp).fillMaxHeight(1f)) {
            SnapOverlayExpandingBox(
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
                LazyColumn() {
                    bankList.forEachIndexed { index, s ->
                        item {
                            SnapSingleIconListItem(title = s.first, iconResId = s.second)
                        }
                    }
                }
            }.apply {

            }
        }
    }

    @Composable
    @Preview
    fun ForPreview() {
        Content(
            bankList = listOf(
                Pair("Mandiri", R.drawable.ic_bank_mandiri_40),
                Pair("Permata", R.drawable.ic_bank_permata_40),
                Pair("BCA", R.drawable.ic_bank_bca_40),
                Pair("Mandiri", R.drawable.ic_bank_mandiri_40),
                Pair("Permata", R.drawable.ic_bank_permata_40),
                Pair("BCA", R.drawable.ic_bank_bca_40),
                Pair("Mandiri", R.drawable.ic_bank_mandiri_40),
                Pair("Permata", R.drawable.ic_bank_permata_40),
                Pair("BCA", R.drawable.ic_bank_bca_40),
                Pair("BCA", R.drawable.ic_bank_bca_40)
            )
        )
    }
}