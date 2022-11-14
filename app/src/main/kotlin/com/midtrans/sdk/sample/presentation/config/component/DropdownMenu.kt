package com.midtrans.sdk.sample.presentation.config.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.midtrans.sdk.sample.presentation.config.InputState
import com.midtrans.sdk.sample.util.DemoConstant.ACQUIRING_BANK
import com.midtrans.sdk.sample.util.DemoConstant.COLOR_THEME
import com.midtrans.sdk.sample.util.DemoConstant.CREDIT_CARD_PAYMENT_TYPE
import com.midtrans.sdk.sample.util.DemoConstant.CUSTOM_BCA_VA
import com.midtrans.sdk.sample.util.DemoConstant.CUSTOM_BNI_VA
import com.midtrans.sdk.sample.util.DemoConstant.CUSTOM_EXPIRY
import com.midtrans.sdk.sample.util.DemoConstant.INSTALLMENT
import com.midtrans.sdk.sample.util.DemoConstant.IS_INSTALLMENT_REQUIRED
import com.midtrans.sdk.sample.util.DemoConstant.OPTIONAL
import com.midtrans.sdk.uikit.internal.view.SnapText
import com.midtrans.sdk.uikit.internal.view.SnapTextField
import com.midtrans.sdk.uikit.internal.view.SnapTypography

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BasicDropdownMenu(title: String, optionList: List<String>, state: InputState) {
    val options by remember { mutableStateOf(optionList) }
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }

    Column(modifier = Modifier.fillMaxWidth(1f), horizontalAlignment = Alignment.Start) {
        Text(
            modifier = Modifier.padding(top = 16.dp, bottom = 20.dp),
            text = title,
            style = SnapTypography.STYLES.snapTextMediumMedium
        )
        ExposedDropdownMenuBox(
            modifier = Modifier.padding(bottom = 10.dp),
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            SnapTextField(
                modifier = Modifier.fillMaxWidth(1f),
                readOnly = true,
                value = TextFieldValue(selectedOptionText),
                onValueChange = {},
                isFocused = false,
                enabled = true,
                onFocusChange = {},
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                textStyle = SnapTypography.STYLES.snapTextMediumRegular
            )
            ExposedDropdownMenu(
                modifier = Modifier.fillMaxWidth(1f),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        onClick = {
                            selectedOptionText = selectionOption
                            expanded = false
                            when (title) {
                                IS_INSTALLMENT_REQUIRED -> state.isRequired = selectedOptionText != OPTIONAL
                                INSTALLMENT-> state.installment = selectedOptionText
                                ACQUIRING_BANK-> state.acquiringBank = selectedOptionText
                                COLOR_THEME -> state.color = selectedOptionText
                                CUSTOM_EXPIRY -> state.expiry = selectedOptionText
                                CREDIT_CARD_PAYMENT_TYPE -> state.ccPaymentType = selectedOptionText
                            }
                        },
                        enabled = true
                    ) {
                        Text(
                            text = selectionOption,
                            style = SnapTypography.STYLES.snapTextMediumRegular
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomTextField(title: String, state: InputState, modifier: Modifier = Modifier) {
    var textField by remember { mutableStateOf(TextFieldValue()) }
    var textFieldFocused by remember { mutableStateOf(false) }

    Column {
        Text(
            modifier = Modifier.padding(top = 16.dp, bottom = 20.dp),
            text = title,
            style = SnapTypography.STYLES.snapTextMediumMedium
        )
        SnapTextField(
            value = textField,
            onValueChange = {
                textField = it
                when (title) {
                    CUSTOM_BCA_VA -> state.bcaVa = textField.text
                    CUSTOM_BNI_VA -> state.bniVa = textField.text
                }
            },
            isFocused = textFieldFocused,
            onFocusChange = {
                textFieldFocused = it
            },
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        )

        if(state.bcaVa.length > 11 && title == CUSTOM_BCA_VA){
            Text(text = "Numbers only. Length should be within 1 to 11.", color = Color.Red)
        }
        if(state.bniVa.length > 8 && title == CUSTOM_BNI_VA){
            Text(text = "Numbers only. Length should be within 1 to 8.", color = Color.Red)
        }
    }
}