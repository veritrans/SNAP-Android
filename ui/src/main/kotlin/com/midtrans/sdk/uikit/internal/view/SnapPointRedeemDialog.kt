package com.midtrans.sdk.uikit.internal.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.midtrans.sdk.uikit.internal.view.SnapColors.BACKGROUND_BORDER_SOLID_SECONDARY
import kotlinx.coroutines.launch


object SnapPointRedeemDialog {
}

@Composable
fun SnapPointRedeemDialogContent(
    data: SnapPointRedeemDialogData,
    onValueChange: (value: String) -> Unit,
    onClick: () -> Unit
) {
    val info by remember {
        data.infoMessage
    }
    val total by remember {
        data.total
    }
    var textFieldText by remember {
        mutableStateOf("")
    }
    val isError by remember { data.isError }
    Column(
        modifier = Modifier.padding(24.dp)
    ) {
        Text(
            text = data.title,
            style = SnapTypography.STYLES.snapAppBar,
            color = SnapColors.getARGBColor(SnapColors.TEXT_PRIMARY),
            modifier = Modifier.padding(bottom = 24.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Point discount",
                style = SnapTypography.STYLES.snapTextMediumRegular,
                modifier = Modifier.weight(1f)
            )
            Text(text = "Rp ")

            //TODO: Need to fix this later when implementing card point
//            SnapTextField(
//                value = textFieldText,
//                onValueChange = { value ->
//                    textFieldText = value.filter { it.isDigit() }
//                    onValueChange(textFieldText.ifEmpty { "0" })
//                },
//                modifier = Modifier.width(117.dp),
//                isError = isError,
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
//            )
        }
        Text(
            text = info,
            style = SnapTypography.STYLES.snapTextSmallRegular,
            color = SnapColors.getARGBColor(if (!isError) SnapColors.SUPPORT_INFO_DEFAULT else SnapColors.SUPPORT_DANGER_DEFAULT),
            modifier = Modifier.padding(top = 16.dp)
        )

        Divider(
            thickness = 1.dp,
            color = SnapColors.getARGBColor(BACKGROUND_BORDER_SOLID_SECONDARY),
            modifier = Modifier.padding(top = 24.dp, bottom = 24.dp)
        )

        Row(modifier = Modifier.padding(bottom = 24.dp)) {
            Text(text = "Total dibayar", modifier = Modifier.weight(1f))
            Text(text = total)
        }
        SnapButton(
            text = "Bayar",
            style = SnapButton.Style.PRIMARY,
            modifier = Modifier.fillMaxWidth(1f),
            enabled = !isError && textFieldText.isNotBlank(),
            onClick = onClick
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SnapBottomSheet(
    content: @Composable (() -> Unit)
): DialogToggle {
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Expanded)
    val coroutine = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
        modifier = Modifier.padding(0.dp),
        sheetContent = {
            content()
        }
    ) {}

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
    var total: MutableState<String>,
    var isError: MutableState<Boolean>,
    var infoMessage: MutableState<String>
)

