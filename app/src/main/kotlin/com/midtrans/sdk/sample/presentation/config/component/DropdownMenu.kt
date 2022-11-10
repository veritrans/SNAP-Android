package com.midtrans.sdk.sample.presentation.config.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.midtrans.sdk.sample.presentation.config.InputState
import com.midtrans.sdk.sample.util.DemoConstant.COLOR_THEME
import com.midtrans.sdk.sample.util.DemoConstant.INSTALLMENT
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
                                INSTALLMENT-> state.installment = selectedOptionText
                                COLOR_THEME -> state.color = selectedOptionText
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