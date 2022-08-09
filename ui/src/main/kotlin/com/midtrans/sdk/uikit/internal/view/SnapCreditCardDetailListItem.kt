package com.midtrans.sdk.uikit.internal.view

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
import kotlin.math.min

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
            horizontalArrangement = Arrangement.spacedBy(8.dp)
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

//@Composable
//fun InputNewCardItem(
//    shouldReveal: Boolean,
//    data: NewCardFormData,
//    onCardNumberValueChange: (String) -> Unit,
//    onExpiryDateValueChange: (String) -> Unit,
//    onCvvValueChange: (String) -> Unit
//) {
//    val principalIconId by data.principalIconId
//    val iconIdList by remember {
//        mutableStateOf(
//            listOf(
//                R.drawable.ic_outline_visa_24,
//                R.drawable.ic_outline_jcb_24,
//                R.drawable.ic_outline_amex_24,
//                R.drawable.ic_outline_mastercard_24
//            )
//        )
//    }
//
//    Column(
//        verticalArrangement = Arrangement.spacedBy(space = 16.dp)
//    ) {
////        Text(
////            text = "Gunakan kartu lain",
////            style = SnapTypography.STYLES.snapTextMediumMedium
////        )
//
//        AnimatedVisibility(
//            visible = shouldReveal,
//            enter = expandVertically()
//        ) {
//            Column(
//                verticalArrangement = Arrangement.spacedBy(space = 16.dp)
//            ) {
//
//                Column {
//                    Row(
//                        horizontalArrangement = Arrangement.spacedBy(2.dp)
//                    ) {
//                        Text(
//                            text = "Nomor kartu",
//                            modifier = Modifier.weight(1f),
//                            style = SnapTypography.STYLES.snapTextSmallRegular
//                        )
//
//                        if (principalIconId != null) {
//                            Icon(
//                                painter = painterResource(id = principalIconId!!),
//                                contentDescription = null,
//                                tint = Color.Unspecified
//                            )
//                        } else {
//                            iconIdList.forEach {
//                                Icon(
//                                    painter = painterResource(id = it),
//                                    contentDescription = null,
//                                    tint = Color.Unspecified
//                                )
//                            }
//                        }
//
//                    }
//
//                    var newCardNumberText by remember { mutableStateOf(TextFieldValue()) }
//                    val bankIconId by remember { data.bankIconId }
//                    SnapTextField(
//                        value = newCardNumberText,
//                        hint = "0000 0000 0000 0000",
//                        onValueChange = { value ->
//                            newCardNumberText = formatForCreditCard(value)
//                            onCardNumberValueChange(value.text.replace(" ", ""))
//                        },
//                        trailingIcon = bankIconId?.let {
//                            {
//                                Icon(
//                                    painter = painterResource(id = it),
//                                    contentDescription = null,
//                                    tint = Color.Unspecified
//                                )
//                            }
//                        },
//                        modifier = Modifier.fillMaxWidth(1.0f),
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
//                    )
//                    val isCardNumberInvalid by remember { data.isCardNumberInvalid }
//                    if (isCardNumberInvalid) {
//                        Text(
//                            text = "Nomor kartu tidak berlaku",
//                            style = SnapTypography.STYLES.snapTextSmallRegular,
//                            color = SnapColors.getARGBColor(SUPPORT_DANGER_DEFAULT)
//                        )
//                    }
//                }
//
//                Row(
//                    horizontalArrangement = Arrangement.spacedBy(space = 28.dp)
//                ) {
//                    Column(
//                        modifier = Modifier.weight(1.0f)
//                    ) {
//
//                        var expiryDate by remember { mutableStateOf(TextFieldValue()) }
//                        Text(
//                            text = "Masa berlaku",
//                            style = SnapTypography.STYLES.snapTextSmallRegular
//                        )
//                        SnapTextField(
//                            hint = "MM/YY",
//                            value = expiryDate,
//                            onValueChange = {
//                                expiryDate = formatExpiryDate(it)
//                                onExpiryDateValueChange(it.text)
//                            },
//                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
//                        )
//                        val isExpiryDateInvalid by remember { data.isExpiryDateInvalid }
//                        if (isExpiryDateInvalid) {
//                            Text(
//                                text = "Masa berlaku tidak valid",
//                                style = SnapTypography.STYLES.snapTextSmallRegular,
//                                color = SnapColors.getARGBColor(SUPPORT_DANGER_DEFAULT)
//                            )
//                        }
//                    }
//
//                    Column(
//                        modifier = Modifier.weight(1.0f)
//                    ) {
//                        val isCvvInvalid by remember { data.isCvvInvalid }
//                        var cardCvv by remember {
//                            mutableStateOf("")
//                        }
//                        Text(
//                            text = "CVV",
//                            style = SnapTypography.STYLES.snapTextSmallRegular
//                        )
//                        SnapTextField(
//                            value = cardCvv,
//                            hint = "***",
//                            onValueChange = { value ->
//                                cardCvv = value
//                                onCvvValueChange(value)
//                            },
//                            visualTransformation = PasswordVisualTransformation(),
//                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
//                        )
//                        if (isCvvInvalid) {
//                            Text(
//                                text = "Nomor CVV tidak valid",
//                                style = SnapTypography.STYLES.snapTextSmallRegular,
//                                color = SnapColors.getARGBColor(SUPPORT_DANGER_DEFAULT)
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//}



fun formatExpiryDate(input: TextFieldValue): TextFieldValue {
    var processed = input.text.replace("/", "")
    processed = processed.replace("(\\d{2})(?=\\d)".toRegex(), "$1/")
    val length = min(processed.length, 5)
    val output = input.copy(processed.substring(0 until length), TextRange(length))
    return output
}

@Composable
fun SnapSavedCardRadioGroup(
    states: List<FormData>,
    onValueChange: (item: String, cvv: String) -> Unit,
    onItemRemoveClicked: (item: String) -> Unit,
    onCardNumberValueChange: (String) -> Unit,
    onExpiryDateValueChange: (String) -> Unit,
    onCvvValueChange: (String) -> Unit
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

//                        is NewCardFormData -> {
//                            InputNewCardItem(
//                                shouldReveal = item.identifier == selectedOption,
//                                data = item,
//                                onCardNumberValueChange = onCardNumberValueChange,
//                                onExpiryDateValueChange = onExpiryDateValueChange,
//                                onCvvValueChange = onCvvValueChange
//                            )
//                        }

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
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    val interactionSource = remember { MutableInteractionSource() }
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.then(
            Modifier
                .border(
                    1.dp,
                    when {
                        isError && !isFocused -> {
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
                    onFocusChange(focusState.isFocused)
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
) : FormData(title)

class NewCardFormData(
    var title: String,
    var isCardNumberInvalid: MutableState<Boolean>,
    var isExpiryDateInvalid: MutableState<Boolean>,
    var isCvvInvalid: MutableState<Boolean>,
    var bankIconId: MutableState<Int?>,
    var principalIconId: MutableState<Int?>
) : FormData(title)


open class FormData(
    public val identifier: String
)

class NormalCardItemState(
    cardNumber: TextFieldValue,
    expiry: TextFieldValue,
    cvv: TextFieldValue,
    isCardNumberInvalid: Boolean,
    isExpiryInvalid: Boolean,
    isCvvInvalid: Boolean,
    isCardTexFieldFocused: Boolean,
    isExpiryTextFieldFocused: Boolean,
    isCvvTextFieldFocused: Boolean,
    principalIconId: Int?,
    bankIconId: Int?,
){
    var cardNumber by mutableStateOf(cardNumber)
    var expiry by mutableStateOf(expiry)
    var cvv by mutableStateOf(cvv)
    var isCardNumberInvalid by mutableStateOf(isCardNumberInvalid)
    var isExpiryInvalid by mutableStateOf(isExpiryInvalid)
    var isCvvInvalid by mutableStateOf(isCvvInvalid)
    var isCardTexFieldFocused by mutableStateOf(isCardTexFieldFocused)
    var isExpiryTextFieldFocused by mutableStateOf(isExpiryTextFieldFocused)
    var isCvvTextFieldFocused by mutableStateOf(isCvvTextFieldFocused)
    var principalIconId by mutableStateOf(principalIconId)
    var bankIconId by mutableStateOf(bankIconId)

    val iconIdList by mutableStateOf(
        listOf (
            R.drawable.ic_outline_visa_24,
            R.drawable.ic_outline_jcb_24,
            R.drawable.ic_outline_amex_24,
            R.drawable.ic_outline_mastercard_24
        )
    )
}

fun formatCreditCard(input: TextFieldValue): TextFieldValue {

    var processed: String = input.text.replace("\\D", "").replace(" ", "")
    // insert a space after all groups of 4 digits that are followed by another digit
    // insert a space after all groups of 4 digits that are followed by another digit
    processed = processed.replace("(\\d{4})(?=\\d)".toRegex(), "$1 ")
    val length = min(processed.length, 19)
    val output = input.copy(text = processed.substring(0 until length), selection = TextRange(length))
    return output
}

fun formatCVV(input: TextFieldValue): TextFieldValue{
    val length = min(input.text.length, 3)
    val output = input.copy(input.text.substring(0 until length), TextRange(length))
    return output
}


@Composable
fun NormalCardItem(
    state: NormalCardItemState,
    onCardNumberValueChange: (TextFieldValue) -> Unit,
    onExpiryDateValueChange: (TextFieldValue) -> Unit,
    onCvvValueChange: (TextFieldValue) -> Unit,
    onCardTextFieldFocusedChange: (Boolean) -> Unit,
    onExpiryTextFieldFocusedChange: (Boolean) -> Unit,
    onCvvTextFieldFocusedChange: (Boolean) -> Unit,
) {
    val formattedMaxCvvLength = 3
    val formattedMaxCardNumberLength = 19
    val formattedMaxExpiryLength = 5

    Column(
        verticalArrangement = Arrangement.spacedBy(space = 16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(space = 16.dp)
        ) {

            Column {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "Nomor kartu",
                        modifier = Modifier.weight(1f),
                        style = SnapTypography.STYLES.snapTextSmallRegular
                    )

                    if (state.principalIconId != null && state.cardNumber.text.isNotEmpty()) {
                        Icon(
                            painter = painterResource(id = state.principalIconId!!),
                            contentDescription = null,
                            tint = Color.Unspecified
                        )
                    } else if (state.cardNumber.text.isEmpty()){
                        state.iconIdList.forEach {
                            Icon(
                                painter = painterResource(id = it),
                                contentDescription = null,
                                tint = Color.Unspecified
                            )
                        }
                    }
                }
                SnapTextField(
                    value = state.cardNumber,
                    hint = "0000 0000 0000 0000",
                    isError = state.isCardNumberInvalid,
                    onValueChange = {
                        state.principalIconId = getPrincipalIcon(getCardType(it.text))
                        var cardLength = formatCreditCard(it).text.length
                        state.bankIconId = getBankIcon(getBankName(it).toString())
                        state.isCardNumberInvalid = cardLength != formattedMaxCardNumberLength
                        onCardNumberValueChange(formatCreditCard(it))
                    },
                    isFocused = state.isCardTexFieldFocused,
                    onFocusChange = {
                        onCardTextFieldFocusedChange(it)
                    },
                    trailingIcon = state.bankIconId?.let {
                        {
                            Icon(
                                painter = painterResource(id = it),
                                contentDescription = null,
                                tint = Color.Unspecified
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(1.0f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                if (state.isCardNumberInvalid && !state.isCardTexFieldFocused) {
                    Text(
                        text = "Nomor kartu tidak berlaku",
                        style = SnapTypography.STYLES.snapTextSmallRegular,
                        color = SnapColors.getARGBColor(SUPPORT_DANGER_DEFAULT)
                    )
                }
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
                        hint = "MM/YY",
                        value = state.expiry,
                        onValueChange = {
                            onExpiryDateValueChange(formatExpiryDate(it))
                            state.isExpiryInvalid = formatExpiryDate(it).text.isNotBlank() && formatExpiryDate(it).text.length != formattedMaxExpiryLength
                        },
                        isError = state.isExpiryInvalid,
                        isFocused = state.isExpiryTextFieldFocused,
                        onFocusChange = {
                            onExpiryTextFieldFocusedChange(it)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    if (state.isExpiryInvalid && !state.isExpiryTextFieldFocused) {
                        Text(
                            text = "Masa berlaku tidak valid",
                            style = SnapTypography.STYLES.snapTextSmallRegular,
                            color = SnapColors.getARGBColor(SUPPORT_DANGER_DEFAULT)
                        )
                    }
                }

                Column(
                    modifier = Modifier.weight(1.0f)
                ) {
                    Text(
                        text = "CVV",
                        style = SnapTypography.STYLES.snapTextSmallRegular
                    )
                    SnapTextField(
                        value = state.cvv,
                        hint = "***",
                        onValueChange = {
                            onCvvValueChange(formatCVV(it))
                            state.isCvvInvalid = formatCVV(it).text.isNotBlank() && formatCVV(it).text.length != formattedMaxCvvLength
                        },
                        isError = state.isCvvInvalid,
                        isFocused = state.isCvvTextFieldFocused,
                        onFocusChange = {
                            onCvvTextFieldFocusedChange(it)
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    if (state.isCvvInvalid && !state.isCvvTextFieldFocused) {
                        Text(
                            text = "Nomor CVV tidak valid",
                            style = SnapTypography.STYLES.snapTextSmallRegular,
                            color = SnapColors.getARGBColor(SUPPORT_DANGER_DEFAULT)
                        )
                    }
                }
            }
        }
    }
}

//TODO: Need to find better solution about principal icon
fun getCardType(cardNumber: String): String {
    return try {
        if (cardNumber.isEmpty()) {
            ""
        } else {
            if (cardNumber[0] == '4') {
                "visa"
            } else if (cardNumber[0] == '5' && (cardNumber[1] == '1' || cardNumber[1] == '2'
                        || cardNumber[1] == '3' || cardNumber[1] == '4' || cardNumber[1] == '5')
            ) {
                "mastercard"
            } else if (cardNumber[0] == '3' && (cardNumber[1] == '4' || cardNumber[1] == '7')) {
                "amex"
            } else if (cardNumber.startsWith("35") || cardNumber.startsWith("2131") || cardNumber.startsWith(
                    "1800"
                )
            ) {
                "jcb"
            } else {
                ""
            }
        }
    } catch (e: RuntimeException) {
        ""
    }
}

fun getPrincipalIcon(cardType: String): Int? {
    return when (cardType) {
        "visa" -> R.drawable.ic_outline_visa_24
        "mastercard" -> R.drawable.ic_outline_mastercard_24
        "amex" -> R.drawable.ic_outline_amex_24
        "jcb" -> R.drawable.ic_arrow_left
        else -> null
    }
}

fun getBankName(cardNumber: TextFieldValue): String? {
    val length = min(cardNumber.text.length, 8)
    val output = cardNumber.copy(cardNumber.text.substring(0 until length), TextRange(length))

    return when(output.text){
        "411111" -> "bri"
        "52" -> "bni"
        "51" -> "mandiri"
        "41" -> "cimb"
        "42" -> "mega"
        "43" -> "bca"
        else -> null
    }
}
fun getBankIcon(bank: String): Int? {
    return when(bank){
        "bri" -> R.drawable.ic_outline_bri_24
        "bni" -> R.drawable.ic_bank_bni_24
        "mandiri" -> R.drawable.ic_bank_mandiri_24
        "bca" -> R.drawable.ic_bank_bca_24
        "cimb" -> R.drawable.ic_bank_cimb_24
        "mega" -> R.drawable.ic_bank_mega_24
        else -> null
    }
}

