package com.midtrans.sdk.uikit.internal.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SnapTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    isFocused: Boolean,
    onFocusChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = SnapTypography.STYLES.snapTextMediumRegular,
    hint: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    singleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val backgroundColor: Color = if (enabled) {
        SnapColors.getARGBColor(SnapColors.BACKGROUND_FILL_PRIMARY)
    } else {
        SnapColors.getARGBColor(SnapColors.INTERACTIVE_DISABLED)
    }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.then(
            Modifier
                .border(
                    1.dp,
                    when {
                        isError && !isFocused -> {
                            SnapColors.getARGBColor(SnapColors.INTERACTIVE_BORDER_SUPPORT)
                        }

                        isFocused -> SnapColors.getARGBColor(SnapColors.LINK_HOVER)
                        else -> SnapColors.getARGBColor(SnapColors.INTERACTIVE_BORDER_INPUT)
                    },
                    RoundedCornerShape(4.dp)
                )
                .background(backgroundColor, RoundedCornerShape(4.dp))
                .width(200.dp)
                .height(39.dp)
                .padding(top = 8.dp, bottom = 8.dp, start = 12.dp, end = 12.dp)
                .onFocusChanged { focusState ->
                    onFocusChange(focusState.isFocused)
                }),
        keyboardOptions = keyboardOptions,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        visualTransformation = visualTransformation,
        singleLine = singleLine,
        decorationBox = {
            TextFieldDefaults.TextFieldDecorationBox(
                value = value.text,
                innerTextField = it,
                singleLine = singleLine,
                enabled = true,
                placeholder = hint?.let {
                    {
                        Text(text = it, style = SnapTypography.STYLES.snapTextMediumRegular)
                    }
                },
                visualTransformation = visualTransformation,
                // same interaction source as the one passed to BasicTextField to read focus state
                // for text field styling

                interactionSource = interactionSource,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                // keep vertical paddings but change the horizontal
                contentPadding = TextFieldDefaults.textFieldWithoutLabelPadding(
                    start = 0.dp, end = 0.dp, top = 0.dp, bottom = 0.dp
                )
            )
        }
    )
}
