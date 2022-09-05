package com.midtrans.sdk.uikit.internal.presentation.errorcard

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.midtrans.sdk.uikit.internal.view.DialogToggle
import com.midtrans.sdk.uikit.internal.view.SnapBottomSheet

object ErrorCard {
    @Composable
    fun ErrorCard(): DialogToggle{
        return SnapBottomSheet {
            Text(text = )
        }
    }

    const val ERROR_CHANGE_PAYMENT_METHOD = 1
    const val ERROR_RETRY = 2
    const val ERROR_BACK_TO_MERCHANT = 3
    const val ERROR_CARD_REJECTED = 4

    private const val errorComponentMap = mapOf(
        Pair(ERROR_CHANGE_PAYMENT_METHOD, ErrorComponent(title = ))
    )

    private data class ErrorComponent(
        val title: Int,
        val message: Int,
        val cta: Int
    )
}