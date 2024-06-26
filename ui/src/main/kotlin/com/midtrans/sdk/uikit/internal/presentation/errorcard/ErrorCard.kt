package com.midtrans.sdk.uikit.internal.presentation.errorcard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.midtrans.sdk.corekit.api.exception.SnapError
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.presentation.errorcard.ErrorCard.errorComponentMap
import com.midtrans.sdk.uikit.internal.view.*
import retrofit2.HttpException
import java.net.UnknownHostException

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ErrorCard(
    type: Int,
    onClick: (Int) -> Unit = {},
    onSheetStateChange: (ModalBottomSheetState) -> Unit
): DialogToggle {

    var errorSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Expanded,
        confirmStateChange = { false }
    )
    return SnapBottomSheet(
        sheetState = errorSheetState,
        onSheetStateChange = {
            onSheetStateChange(it)
        }
    ) {
        ErrorContent(type = type, onClick = onClick)
    }
}

@Composable
private fun ErrorContent(type: Int, onClick: (Int) -> Unit = {}) {
    errorComponentMap[type]?.run {
        Column(
            modifier = Modifier
                .background(SnapColors.getARGBColor(SnapColors.backgroundFillPrimary))
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
            SnapButton(
                text = stringResource(id = cta),
                modifier = Modifier.fillMaxWidth(1f)
            ) {
                onClick.invoke(cta)
            }
        }
    }
}

object ErrorCard {

    const val TIMEOUT_ERROR_DIALOG_FROM_BANK = 1
    const val SYSTEM_ERROR_DIALOG_ALLOW_RETRY = 2
    const val SYSTEM_ERROR_DIALOG_DISALLOW_RETRY = 3
    const val TID_MID_ERROR_OTHER_PAY_METHOD_AVAILABLE = 4
    const val TID_MID_ERROR_OTHER_PAY_METHOD_NOT_AVAILABLE = 5
    const val CARD_ERROR_DECLINED_DISALLOW_RETRY = 6
    const val INCORRECT_CARD_INFO = 7
    internal val errorComponentMap = mapOf(
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
            TID_MID_ERROR_OTHER_PAY_METHOD_AVAILABLE, ErrorComponent(
                title = R.string.tidmid_error_other_pay_method_available_title,
                message = R.string.tidmid_error_other_pay_method_available_content,
                cta = R.string.tidmid_error_other_pay_method_available_cta
            )
        ),
        Pair(
            TID_MID_ERROR_OTHER_PAY_METHOD_NOT_AVAILABLE, ErrorComponent(
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
        Pair(
            INCORRECT_CARD_INFO, ErrorComponent(
                title = R.string.incorrect_card_info_title,
                message = R.string.incorrect_card_info_content,
                cta = R.string.incorrect_card_info_cta
            )
        )
    )

    internal data class ErrorComponent(
        val title: Int,
        val message: Int,
        val cta: Int
    )

    fun getErrorCardType(
        transactionResponse: TransactionResponse,
        allowRetry: Boolean = false
    ): Int? {
        var errorType = when (transactionResponse.statusCode) {
            "503" -> TIMEOUT_ERROR_DIALOG_FROM_BANK
            "402" -> TID_MID_ERROR_OTHER_PAY_METHOD_AVAILABLE
            "500" -> if (allowRetry) SYSTEM_ERROR_DIALOG_ALLOW_RETRY else SYSTEM_ERROR_DIALOG_DISALLOW_RETRY
            else -> null
        }

        if (errorType == null) {
            errorType = when (transactionResponse.transactionStatus) {
                "deny" -> CARD_ERROR_DECLINED_DISALLOW_RETRY
                else -> null
            }
        }
        return errorType
    }

    fun getErrorCardType(snapError: SnapError, allowRetry: Boolean = false): Int? {
        return when (val exception = snapError.cause) {
            is HttpException -> getErrorForHttpException(exception, allowRetry)
            is UnknownHostException -> SYSTEM_ERROR_DIALOG_ALLOW_RETRY
            else -> null
        }
    }

    private fun getErrorForHttpException(
        httpException: HttpException,
        allowRetry: Boolean = false
    ): Int {
        return when (httpException.code()) {
            503 -> TIMEOUT_ERROR_DIALOG_FROM_BANK
            500 -> if (allowRetry) SYSTEM_ERROR_DIALOG_ALLOW_RETRY else SYSTEM_ERROR_DIALOG_DISALLOW_RETRY
            else -> SYSTEM_ERROR_DIALOG_ALLOW_RETRY
        }
    }
}


@Composable
@Preview
private fun forPreview() {
    ErrorContent(type = ErrorCard.TIMEOUT_ERROR_DIALOG_FROM_BANK)
}
