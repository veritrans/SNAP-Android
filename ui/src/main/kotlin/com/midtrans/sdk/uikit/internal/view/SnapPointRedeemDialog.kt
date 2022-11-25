package com.midtrans.sdk.uikit.internal.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.util.CurrencyFormat.currencyFormatRp
import com.midtrans.sdk.uikit.internal.util.SnapCreditCardUtil
import com.midtrans.sdk.uikit.internal.view.SnapColors.backgroundBorderSolidSecondary
import kotlinx.coroutines.launch


object SnapPointRedeemDialog {
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PointBankCard(
    data: SnapPointRedeemDialogData,
    onSheetStateChange: (ModalBottomSheetState) -> Unit,
    onClick: (pointInputted: Double) -> Unit
): DialogToggle {

    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    return SnapBottomSheet(
        sheetState = sheetState,
        onSheetStateChange = onSheetStateChange,
        content = {
            SnapPointRedeemDialogContent(
                data = data,
                onClick = onClick
            )
        }
    )
}

@Composable
fun SnapPointRedeemDialogContent(
    data: SnapPointRedeemDialogData,
    onClick: (pointInputted: Double) -> Unit
) {
    var pointAmountInputted by remember {
        mutableStateOf(
            SnapCreditCardUtil.formatMaxPointDiscount(
                input = TextFieldValue(data.pointBalanceAmount.toLong().toString()),
                totalAmount = data.total.toLong(),
                pointBalanceAmount = data.pointBalanceAmount
            ).first
        )
    }
    var displayedTotalFinal by remember {
        mutableStateOf(
            SnapCreditCardUtil.formatMaxPointDiscount(
                input = pointAmountInputted,
                totalAmount = data.total.toLong(),
                pointBalanceAmount = data.pointBalanceAmount
            ).second
        )
    }
    var isError by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.padding(24.dp)
    ) {
        Text(
            text = data.title,
            style = SnapTypography.STYLES.snapAppBar,
            color = SnapColors.getARGBColor(SnapColors.textPrimary),
            modifier = Modifier.padding(bottom = 24.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.point_discount),
                style = SnapTypography.STYLES.snapTextMediumRegular,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stringResource(id = R.string.point_rp_currency),
                modifier = Modifier.padding(end = 8.dp)
            )

            SnapTextField(
                value = pointAmountInputted,
                onValueChange = { input ->
                    SnapCreditCardUtil.formatMaxPointDiscount(input, totalAmount = data.total.toLong(), pointBalanceAmount = data.pointBalanceAmount).let { triple ->
                       triple.first.let {
                           pointAmountInputted = it
                       }
                        triple.second.let {
                            displayedTotalFinal = it
                        }
                        triple.third.let {
                            isError = it
                        }
                    }
                    onValueChange(pointAmountInputted.text.ifEmpty { "0" }.toDouble())
                },
                modifier = Modifier.width(117.dp),
                isError = isError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isFocused = false,
                onFocusChange = {}
            )
        }

        if (pointAmountInputted.text.isEmpty()) {
            Text(
                text = stringResource(id = R.string.point_amount_of_points, data.pointBalanceAmount.currencyFormatRp()),
                style = SnapTypography.STYLES.snapTextSmallRegular,
                color = SnapColors.getARGBColor(SnapColors.supportInfoDefault),
                modifier = Modifier.padding(top = 16.dp)
            )
        } else if (isError){
            Text(
                text = stringResource(id = R.string.point_insufficient_title),
                style = SnapTypography.STYLES.snapTextSmallRegular,
                color = SnapColors.getARGBColor(SnapColors.supportDangerDefault),
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        Divider(
            thickness = 1.dp,
            color = SnapColors.getARGBColor(backgroundBorderSolidSecondary),
            modifier = Modifier.padding(top = 24.dp, bottom = 24.dp)
        )

        Row(modifier = Modifier.padding(bottom = 24.dp)) {
            Text(
                text = stringResource(id = R.string.point_total_payment),
                modifier = Modifier.weight(1f)
            )
            Text(text = displayedTotalFinal)
        }
        SnapButton(
            text = stringResource(id = R.string.point_pay_cta),
            style = SnapButton.Style.PRIMARY,
            modifier = Modifier.fillMaxWidth(1f),
            enabled = pointAmountInputted.text.isNotEmpty() && !isError,
            onClick = {
               onClick (pointAmountInputted.text.ifEmpty { "0" }.toDouble())
            }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SnapBottomSheet(
    sheetState: ModalBottomSheetState,
    onSheetStateChange: (ModalBottomSheetState) -> Unit,
    content: @Composable (() -> Unit)
): DialogToggle {
    val coroutine = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
        modifier = Modifier.padding(0.dp),
        sheetContent = {
            content()
        }
    ) {}
    onSheetStateChange(sheetState)

    return object : DialogToggle {
        override fun show() {
            coroutine.launch { sheetState.show() }
        }

        override fun hide() {
            coroutine.launch { sheetState.hide() }
        }
    }
}

interface DialogToggle {
    fun show()
    fun hide()
}

data class SnapPointRedeemDialogData(
    val title: String,
    var displayedTotal: String,
    var total: Double,
    var isError: Boolean,
    var infoMessage: String,
    var pointBalanceAmount: Double
)

