package com.midtrans.sdk.uikit.internal.view

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.view.SnapColors.BACKGROUND_BORDER_SOLID_SECONDARY
import com.midtrans.sdk.uikit.internal.view.SnapColors.INTERACTIVE_BORDER_INPUT
import com.midtrans.sdk.uikit.internal.view.SnapColors.INTERACTIVE_BORDER_SUPPORT
import com.midtrans.sdk.uikit.internal.view.SnapColors.LINK_HOVER
import com.midtrans.sdk.uikit.internal.view.SnapColors.SUPPORT_DANGER_DEFAULT
import com.midtrans.sdk.uikit.internal.view.SnapColors.SUPPORT_NEUTRAL_FILL

object CreditCardDetailListItem {
}

@Composable
fun SnapCCDetailListItem(
    @DrawableRes startIconId: Int,
    @DrawableRes endIconId: Int,
    itemTitle: String,
    shouldReveal: Boolean,
    inputTitle: String,
    isInputError: Boolean,
    errorTitle: String,
    onValueChange: (String) -> Unit,
    onEndIconClicked: () -> Unit
) {
    Column {
        Row(
            verticalAlignment = CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                painter = painterResource(id = startIconId),
                contentDescription = null,
                tint = Color.Unspecified
            )
            Text(
                text = itemTitle,
                style = SnapTypography.STYLES.snapTextBigRegular,
                modifier = Modifier
                    .weight(weight = 1.0f)
            )
            if (shouldReveal) {
                IconButton(onClick = { onEndIconClicked() }) {
                    Icon(
                        painter = painterResource(id = endIconId),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = shouldReveal,
            enter = expandVertically()
        ) {
            Column(
                modifier = Modifier
                    .background(
                        SnapColors.getARGBColor(SUPPORT_NEUTRAL_FILL),
                        RoundedCornerShape(4.dp)
                    )
                    .fillMaxWidth(1f)
                    .padding(start = 16.dp, bottom = 8.dp, top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                var text by remember { mutableStateOf("") }
                Text(
                    text = inputTitle,
                    style = SnapTypography.STYLES.snapTextSmallRegular
                )
                SnapTextField(
                    value = text,
                    onValueChange = { value ->
                        if (value.length <= 3) {
                            text = value.filter { it.isDigit() }
                            onValueChange(value.filter { it.isDigit() })
                        }
                    },
                    modifier = Modifier.width(69.dp),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                if (isInputError)
                    Text(
                        text = errorTitle,
                        color = SnapColors.getARGBColor(SUPPORT_DANGER_DEFAULT),
                        style = SnapTypography.STYLES.snapTextSmallRegular
                    )
            }
        }
    }
}

@Composable
fun InputNewCardItem(
    shouldReveal: Boolean
) {
    val iconIdList =
        mutableListOf(R.drawable.ic_bri, R.drawable.ic_bri, R.drawable.ic_bri, R.drawable.ic_bri)
    Column(
        verticalArrangement = Arrangement.spacedBy(space = 16.dp)
    ) {
        Text(
            text = "Gunakan kartu lain",
            style = SnapTypography.STYLES.snapTextMediumMedium
        )

        AnimatedVisibility(
            visible = shouldReveal,
            enter = expandVertically()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(space = 16.dp)
            ) {

                Column() {

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = "Nomor kartu",
                            modifier = Modifier.weight(1f),
                            style = SnapTypography.STYLES.snapTextSmallRegular
                        )

                        iconIdList.forEach {
                            Icon(
                                painter = painterResource(id = it),
                                contentDescription = null,
                                tint = Color.Unspecified
                            )
                        }
                    }

                    var newCardNumberText by remember { mutableStateOf(TextFieldValue()) }
                    SnapTextField(
                        value = newCardNumberText,
                        hint = "1234567890",
                        onValueChange = { value ->
                            Log.e("wahyu", "value cc $value")
                            newCardNumberText = formatForCreditCard(value)
                        },
                        trailingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_bri),
                                contentDescription = null,
                                tint = Color.Unspecified
                            )
                        },
                        modifier = Modifier.fillMaxWidth(1.0f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Text(
                        text = "Nomor kartu tidak berlaku",
                        style = SnapTypography.STYLES.snapTextSmallRegular
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(space = 28.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(1.0f)
                    ) {

                        Text(
                            text = "Masa berlaku",
                            style = SnapTypography.STYLES.snapTextSmallRegular
                        )
                        SnapTextField(
                            value = "1234567890",
                            onValueChange = { value ->

                            },
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        Text(
                            text = "Masa berlaku tidak valid",
                            style = SnapTypography.STYLES.snapTextSmallRegular
                        )
                    }
                    Column(
                        modifier = Modifier.weight(1.0f)
                    ) {

                        Text(
                            text = "CVV",
                            style = SnapTypography.STYLES.snapTextSmallRegular
                        )
                        SnapTextField(
                            value = "1234567890",
                            onValueChange = { value ->

                            },
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        Text(
                            text = "Nomor CVV tidak valid",
                            style = SnapTypography.STYLES.snapTextSmallRegular
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CcRadioGroup(
    states: List<FormData>,
    onValueChange: (item: String, cvv: String) -> Unit,
    onItemRemoveClicked: (item: String) -> Unit
) {
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(states[0].identifier) }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        states.forEach { item ->
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.selectable(
                        selected = (item.identifier == selectedOption),
                        onClick = {
                            onOptionSelected(item.identifier)
                        },
                        role = Role.RadioButton
                    ),
                    verticalAlignment = CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    RadioButton(
                        selected = item.identifier == selectedOption,
                        onClick = null,
                        colors = RadioButtonDefaults.colors(selectedColor = Color.Black)
                    )
                    when (item) {
                        is SavedCreditCardFormData -> {
                            val errorText by item.errorText
                            SnapCCDetailListItem(
                                startIconId = item.startIcon,
                                endIconId = item.endIcon,
                                itemTitle = item.maskedCardNumber,
                                shouldReveal = item.identifier == selectedOption,
                                inputTitle = item.inputTitle,
                                isInputError = errorText.isNotBlank(),
                                errorTitle = errorText,
                                onValueChange = { onValueChange(selectedOption, it) },
                                onEndIconClicked = { onItemRemoveClicked(item.identifier) }
                            )
                        }

                        is NewCardFormData -> {
                            InputNewCardItem(shouldReveal = item.identifier == selectedOption)
                        }
                    }
                }

                Divider(
                    thickness = 1.dp,
                    color = SnapColors.getARGBColor(BACKGROUND_BORDER_SOLID_SECONDARY)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SnapTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = SnapTypography.STYLES.snapTextMediumRegular,
    hint: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    val interactionSource = remember { MutableInteractionSource() }
    var isFocused by remember { mutableStateOf(false) }
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.then(
            Modifier
                .border(
                    1.dp,
                    when {
                        isError -> {
                            SnapColors.getARGBColor(INTERACTIVE_BORDER_SUPPORT)
                        }

                        isFocused -> SnapColors.getARGBColor(LINK_HOVER)
                        else -> SnapColors.getARGBColor(INTERACTIVE_BORDER_INPUT)
                    },
                    RoundedCornerShape(4.dp)
                )
                .background(Color.White, RoundedCornerShape(4.dp))
                .width(200.dp)
                .height(39.dp)
                .padding(top = 8.dp, bottom = 8.dp, start = 12.dp, end = 12.dp)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                }),
        keyboardOptions = keyboardOptions,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        visualTransformation = visualTransformation,
        decorationBox = {
            TextFieldDefaults.TextFieldDecorationBox(
                value = value.text,
                innerTextField = it,
                singleLine = true,
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SnapTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = SnapTypography.STYLES.snapTextMediumRegular,
    hint: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    val interactionSource = remember { MutableInteractionSource() }
    var isFocused by remember { mutableStateOf(false) }
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.then(
            Modifier
                .border(
                    1.dp,
                    when {
                        isError -> {
                            SnapColors.getARGBColor(INTERACTIVE_BORDER_SUPPORT)
                        }

                        isFocused -> SnapColors.getARGBColor(LINK_HOVER)
                        else -> SnapColors.getARGBColor(INTERACTIVE_BORDER_INPUT)
                    },
                    RoundedCornerShape(4.dp)
                )
                .background(Color.White, RoundedCornerShape(4.dp))
                .width(200.dp)
                .height(39.dp)
                .padding(top = 8.dp, bottom = 8.dp, start = 12.dp, end = 12.dp)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                }),
        keyboardOptions = keyboardOptions,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        visualTransformation = visualTransformation,
        decorationBox = {
            TextFieldDefaults.TextFieldDecorationBox(
                value = value,
                innerTextField = it,
                singleLine = true,
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



data class SavedCreditCardFormData(
    val startIcon: Int,
    val endIcon: Int,
    val maskedCardNumber: String,
    var errorText: MutableState<String>,
    val inputTitle: String,
    var title: String
) : FormData(title) {

}


class NewCardFormData(
    var title: String,
    var isCardNumberValid: MutableState<Boolean>,
    var isExpiryDateValid: MutableState<Boolean>,
    var isCvvValid: MutableState<Boolean>,
    var bankIconId: MutableState<Int?>,
    var principalIconId: MutableState<Int?>
    ) : FormData(title)

open class FormData(
    public val identifier: String
)

fun formatForCreditCard(input: TextFieldValue): TextFieldValue{
    var processed: String = input.text.replace("\\D", "").replace(" ", "")
    // insert a space after all groups of 4 digits that are followed by another digit
    // insert a space after all groups of 4 digits that are followed by another digit
    processed = processed.replace("(\\d{4})(?=\\d)".toRegex(), "$1 ")
    val output = input.copy(text = processed, selection = TextRange(processed.length))
    return output
}
