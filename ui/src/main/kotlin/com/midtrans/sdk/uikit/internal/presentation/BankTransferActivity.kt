package com.midtrans.sdk.uikit.internal.presentation

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.midtrans.sdk.uikit.internal.view.SnapCustomerDetail
import com.midtrans.sdk.uikit.internal.view.SnapOverlayExpandingBox
import com.midtrans.sdk.uikit.internal.view.SnapTotal

class BankTransferActivity{


    @Composable
    @Preview
    fun Content(){
        var expanding by remember {
            mutableStateOf(false)
        }

        SnapOverlayExpandingBox(
            isExpanded = expanding,
            mainContent = {
                SnapTotal(
                    amount = "Rp399.000",
                    orderId = "#121231231231",
                    remainingTime = "22:22:22"
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
            },
            followingContent = {}
        )
    }
}