package com.midtrans.sdk.uikit.internal.presentation.errorcard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.view.*

internal object ErrorCard {
    @Composable
    fun ErrorCard(type: Int, onClick: () -> Unit = {}): DialogToggle {
        return SnapBottomSheet {
            ErrorContent(type = type, onClick = onClick)
        }
    }

    @Composable
    fun ErrorContent(type: Int, onClick: () -> Unit = {}) {
        errorComponentMap[type]?.run {
            Column(
                modifier = Modifier
                    .background(SnapColors.getARGBColor(SnapColors.BACKGROUND_FILL_PRIMARY))
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(title),
                    style = SnapTypography.STYLES.snapTextLabelMedium
                )
                Text(
                    text = stringResource(id = message),
                    style = SnapTypography.STYLES.snaTextBodySmall,
                    modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                )
                SnapButton(text = stringResource(id = cta), modifier = Modifier.fillMaxWidth(1f)) {
                    onClick.invoke()
                }
            }
        }
    }


    const val TIMEOUT_ERROR_DIALOG_FROM_BANK = 1
    const val SYSTEM_ERROR_DIALOG_ALLOW_RETRY = 2
    const val SYSTEM_ERROR_DIALOG_DISALLOW_RETRY = 3
    const val TIDMID_ERROR_OTHER_PAY_METHOD_AVAILABLE = 4
    const val TIDMID_ERROR_OTHER_PAY_METHOD_NOT_AVAILABLE = 5
    const val CARD_ERROR_DECLINED_DISALLOW_RETRY = 6
    private val errorComponentMap = mapOf(
        Pair(
            TIMEOUT_ERROR_DIALOG_FROM_BANK, ErrorComponent(
                title = R.string.timeout_error_dialog_from_bank_title,
                message = R.string.timeout_error_dialog_from_bank_content,
                cta = R.string.timeout_error_dialog_from_bank_cta
            )
        ),
        Pair(
            SYSTEM_ERROR_DIALOG_ALLOW_RETRY, ErrorComponent(
                title = R.string.system_error_dialog_allow_retry_title,
                message = R.string.system_error_dialog_allow_retry_content,
                cta = R.string.system_error_dialog_allow_retry_cta
            )
        ),
        Pair(
            SYSTEM_ERROR_DIALOG_DISALLOW_RETRY, ErrorComponent(
                title = R.string.system_error_dialog_disallow_retry_title,
                message = R.string.system_error_dialog_disallow_retry_content,
                cta = R.string.system_error_dialog_disallow_retry_cta
            )
        ),
        Pair(
            TIDMID_ERROR_OTHER_PAY_METHOD_AVAILABLE, ErrorComponent(
                title = R.string.tidmid_error_other_pay_method_available_title,
                message = R.string.tidmid_error_other_pay_method_available_content,
                cta = R.string.tidmid_error_other_pay_method_available_cta
            )
        ),
        Pair(
            TIDMID_ERROR_OTHER_PAY_METHOD_NOT_AVAILABLE, ErrorComponent(
                title = R.string.tidmid_error_other_pay_method_not_available_title,
                message = R.string.tidmid_error_other_pay_method_not_available_content,
                cta = R.string.tidmid_error_other_pay_method_not_available_cta
            )
        ),
        Pair(
            CARD_ERROR_DECLINED_DISALLOW_RETRY, ErrorComponent(
                title = R.string.card_error_declined_disallow_retry_title,
                message = R.string.card_error_declined_disallow_retry_content,
                cta = R.string.card_error_declined_disallow_retry_cta
            )
        ),

        )

    private data class ErrorComponent(
        val title: Int,
        val message: Int,
        val cta: Int
    )
}

class ErrorPreview() {
    @Composable
    @Preview
    private fun forPreview() {
        ErrorCard.ErrorContent(type = ErrorCard.TIMEOUT_ERROR_DIALOG_FROM_BANK)
    }
}