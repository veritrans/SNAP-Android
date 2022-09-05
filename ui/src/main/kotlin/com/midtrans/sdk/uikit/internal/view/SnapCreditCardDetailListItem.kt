package com.midtrans.sdk.uikit.internal.view

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.midtrans.sdk.corekit.api.model.SavedToken
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.view.SnapColors.BACKGROUND_BORDER_SOLID_SECONDARY
import com.midtrans.sdk.uikit.internal.view.SnapColors.INTERACTIVE_BORDER_INPUT
import com.midtrans.sdk.uikit.internal.view.SnapColors.INTERACTIVE_BORDER_SUPPORT
import com.midtrans.sdk.uikit.internal.view.SnapColors.LINK_HOVER
import com.midtrans.sdk.uikit.internal.view.SnapColors.SUPPORT_DANGER_DEFAULT
import com.midtrans.sdk.uikit.internal.view.SnapColors.SUPPORT_NEUTRAL_FILL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

object CreditCardDetailListItem {
}

@Composable
fun SnapCCDetailListItem(
    @DrawableRes startIconId: Int?,
    @DrawableRes endIconId: Int,
    cvvTextField: TextFieldValue,
    itemTitle: String,
    shouldReveal: Boolean,
    inputTitle: String,
    isInputError: Boolean,
    errorTitle: String,
    onValueChange: (String) -> Unit,
    onEndIconClicked: () -> Unit,
    onCardNumberValueChange: (TextFieldValue) -> Unit,
    onExpiryDateValueChange: (TextFieldValue) -> Unit,
    onCvvValueChange: (TextFieldValue) -> Unit,
    onCardTextFieldFocusedChange: (Boolean) -> Unit,
    onExpiryTextFieldFocusedChange: (Boolean) -> Unit,
    onCvvTextFieldFocusedChange: (Boolean) -> Unit,
    onIsCvvInvalidValueChange: (Boolean) -> Unit
) {
    Column {
        Row(
            verticalAlignment = CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            startIconId?.let {
                Icon(
                    painter = painterResource(id = it),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }
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
                Text(
                    text = inputTitle,
                    style = SnapTypography.STYLES.snapTextSmallRegular
                )
                val formattedMaxCvvLength = 3
                var isCvvInvalid by remember { mutableStateOf(false)}
                var isCvvTextFieldFocused by remember { mutableStateOf(false)}
                SnapTextField(
                    value = cvvTextField,
                    onValueChange = {
                        onCvvValueChange(formatCVV(it))
                        isCvvInvalid = formatCVV(it).text.length != formattedMaxCvvLength
                        onIsCvvInvalidValueChange(isCvvInvalid)
                    },
                    modifier = Modifier.width(69.dp),

                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = isCvvInvalid,
                    isFocused = isCvvTextFieldFocused,
                    onFocusChange = {
                        isCvvTextFieldFocused = it
                    },
                )
                if (isCvvInvalid && !isCvvTextFieldFocused) {
                    if (cvvTextField.text.isEmpty()){
                        Text(
                            text = stringResource(id = R.string.card_error_empty_cvv),
                            style = SnapTypography.STYLES.snapTextSmallRegular,
                            color = SnapColors.getARGBColor(SUPPORT_DANGER_DEFAULT)
                        )
                    } else{
                        Text(
                            text = stringResource(id = R.string.card_error_invalid_cvv),
                            style = SnapTypography.STYLES.snapTextSmallRegular,
                            color = SnapColors.getARGBColor(SUPPORT_DANGER_DEFAULT)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InputNewCardItem(
    shouldReveal: Boolean,
    state: NormalCardItemState,
    bankIconState: Int?,
    onCardNumberValueChange: (TextFieldValue) -> Unit,
    onExpiryDateValueChange: (TextFieldValue) -> Unit,
    onCvvValueChange: (TextFieldValue) -> Unit,
    onCardTextFieldFocusedChange: (Boolean) -> Unit,
    onExpiryTextFieldFocusedChange: (Boolean) -> Unit,
    onCvvTextFieldFocusedChange: (Boolean) -> Unit,
    onSavedCardCheckedChange: (Boolean) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = 16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.cc_dc_saved_card_use_another_card),
            style = SnapTypography.STYLES.snapTextMediumMedium
        )

        AnimatedVisibility(
            visible = shouldReveal,
            enter = expandVertically()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(space = 16.dp)
            ) {
                NormalCardItem(
                    state = state,
                    bankIcon = bankIconState,
                    onCardNumberValueChange = {
                        onCardNumberValueChange(it)
                    },
                    onExpiryDateValueChange = {
                        onExpiryDateValueChange(it)
                    },
                    onCvvValueChange = {
                        onCvvValueChange(it)
                    },
                    onCardTextFieldFocusedChange = {
                        state.isCardTexFieldFocused = it
                    },
                    onExpiryTextFieldFocusedChange = { state.isExpiryTextFieldFocused = it},
                    onCvvTextFieldFocusedChange = { state.isCvvTextFieldFocused = it},
                    onSavedCardCheckedChange = {
                        onSavedCardCheckedChange(it)}
                )
            }
        }
    }
}



fun formatExpiryDate(input: TextFieldValue): TextFieldValue {
    val maxNumberOfMonth = 12

    var digit = input.text.filter {
        it.isDigit()
    }
    if (digit.length >= 2){
        var firstTwoDigit = digit.substring(0 until 2)
        var processed = if (firstTwoDigit.toInt() <= maxNumberOfMonth ){
            digit.replace("/", "")
        } else {
            var adjustedDigit = "0"+digit
            adjustedDigit.replace("/", "")
        }
        processed = processed.replace("(\\d{2})(?=\\d)".toRegex(), "$1/")
        val length = min(processed.length, 5)
        val output = input.copy(processed.substring(0 until length), TextRange(length))
        return output
    } else {
        return if (input.text.isEmpty()){
            input.copy()
        } else {
            input.copy(digit)
        }
    }
}

fun checkIsCardExpired(cardExpiry: String): Boolean{
    val sdf = SimpleDateFormat("MM/yy")
    var currentDate = sdf.format(Date())
    return sdf.parse(cardExpiry).before(sdf.parse(currentDate))
}

@Composable
fun SnapSavedCardRadioGroup(
    modifier: Modifier,
    listStates: List<FormData>,
    normalCardItemState: NormalCardItemState,
    onItemRemoveClicked: (item: SavedCreditCardFormData) -> Unit,
    cvvTextField: TextFieldValue,
    onCvvSavedCardValueChange: (TextFieldValue) -> Unit,
    onCardNumberOtherCardValueChange: (TextFieldValue) -> Unit,
    onExpiryOtherCardValueChange: (TextFieldValue) -> Unit,
    onCvvOtherCardValueChange: (TextFieldValue) -> Unit,
    onSavedCardRadioSelected: (item: FormData) -> Unit,
    onIsCvvSavedCardInvalidValueChange: (Boolean) -> Unit,
    onSavedCardCheckedChange: (Boolean) -> Unit

) {
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(listStates[0].identifier) }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        listStates.forEach { item ->
            var cvvSavedCardTextFieldValue by remember { mutableStateOf(TextFieldValue()) }
            var newCardState = remember {
                NormalCardItemState(
                    cardNumber = TextFieldValue(),
                    expiry = TextFieldValue(),
                    cvv = TextFieldValue(),
                    isCardNumberInvalid = false,
                    isExpiryInvalid = false,
                    isCvvInvalid = false,
                    isCardTexFieldFocused = false,
                    isExpiryTextFieldFocused = false,
                    isCvvTextFieldFocused = false,
                    principalIconId = null,
                    isSavedCardChecked = true
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.selectable(
                        selected = (item.identifier == selectedOption),
                        onClick = {
                            onOptionSelected(item.identifier)
                            onSavedCardRadioSelected(item)
                            when (item){
                                is SavedCreditCardFormData -> {
                                    cvvSavedCardTextFieldValue = if (item.tokenType == SavedToken.ONE_CLICK){
                                        TextFieldValue("123", selection = TextRange(3))
                                    } else {
                                        TextFieldValue("")
                                    }
                                    normalCardItemState.cvv = TextFieldValue("")
                                }
                                is NewCardFormData -> {
                                    cvvSavedCardTextFieldValue = TextFieldValue("")
                                    normalCardItemState.cvv = TextFieldValue("")
                                }
                            }
                            onCvvOtherCardValueChange(normalCardItemState.cvv)
                            onCvvSavedCardValueChange(cvvSavedCardTextFieldValue)
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
                            item.tokenType
                            SnapCCDetailListItem(
                                startIconId = item.startIcon,
                                endIconId = item.endIcon,
                                itemTitle = formatMaskedCard(item.maskedCardNumber),
                                shouldReveal = item.savedCardIdentifier == selectedOption,
                                inputTitle = item.inputTitle,
                                cvvTextField = cvvSavedCardTextFieldValue ,
                                isInputError = errorText.isNotBlank(),
                                errorTitle = errorText,
                                onValueChange = {},
                                onEndIconClicked = { onItemRemoveClicked(item) },
                                onCardNumberValueChange ={},
                                onExpiryDateValueChange ={},
                                onCvvValueChange = {
                                    cvvSavedCardTextFieldValue = it
                                    onCvvSavedCardValueChange(cvvSavedCardTextFieldValue)
                                },
                                onCardTextFieldFocusedChange = {},
                                onExpiryTextFieldFocusedChange = {},
                                onCvvTextFieldFocusedChange = {},
                                onIsCvvInvalidValueChange = {
                                    onIsCvvSavedCardInvalidValueChange(it)
                                }
                            )
                        }

                        is NewCardFormData -> {
                            InputNewCardItem(
                                shouldReveal = item.identifier == selectedOption,
                                state = normalCardItemState,
                                bankIconState = item.bankIconId,
                                onCardNumberValueChange = { onCardNumberOtherCardValueChange(it)},
                                onExpiryDateValueChange = { onExpiryOtherCardValueChange(it)},
                                onCvvValueChange = { onCvvOtherCardValueChange(it)},
                                onCardTextFieldFocusedChange = {},
                                onExpiryTextFieldFocusedChange = {},
                                onCvvTextFieldFocusedChange = {},
                                onSavedCardCheckedChange = {
                                    onSavedCardCheckedChange(it)
                                }
                            )
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

data class SavedCreditCardFormData(
    val startIcon: Int?,
    val endIcon: Int,
    val maskedCardNumber: String,
    val displayedMaskedCard: String,
    val tokenType: String,
    var errorText: MutableState<String>,
    val inputTitle: String,
    var savedCardIdentifier: String,
    var tokenId: String,
    var cvvSavedCardTextField: TextFieldValue,
    var isCvvSavedCardInvalid: Boolean
) : FormData(savedCardIdentifier){}

class NewCardFormData(
    var newCardIdentifier: String,
    var bankIconId: Int?,
) : FormData(newCardIdentifier)


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
    isSavedCardChecked: Boolean,
    principalIconId: Int?,
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
    var isSavedCardChecked by mutableStateOf(isSavedCardChecked)

    val iconIdList by mutableStateOf(
        listOf (
            R.drawable.ic_outline_visa_24,
            R.drawable.ic_outline_mastercard_24,
            R.drawable.ic_outline_jcb_24,
            R.drawable.ic_outline_amex_24,
        )
    )
}

private fun formatMaskedCard(maskedCard: String): String {
    val lastFourDigit = maskedCard.substring(startIndex = maskedCard.length - 4, endIndex = maskedCard.length)
    return "**** **** **** $lastFourDigit"
}

fun formatCreditCard(input: TextFieldValue): TextFieldValue {
    var digit = input.text.filter {
        it.isDigit()
    }
    var processed: String = digit.replace("\\D", "").replace(" ", "")
    // insert a space after all groups of 4 digits that are followed by another digit
    // insert a space after all groups of 4 digits that are followed by another digit
    processed = processed.replace("(\\d{4})(?=\\d)".toRegex(), "$1 ")
    val length = min(processed.length, 19)
    val output = input.copy(text = processed.substring(0 until length), selection = TextRange(length))
    return output
}

fun formatCVV(input: TextFieldValue): TextFieldValue{

    var digit = input.text.filter {
        it.isDigit()
    }
    val length = min(digit.length, 3)
    val output = input.copy(digit.substring(0 until length), TextRange(length))
    return output
}


@Composable
fun NormalCardItem(
    state: NormalCardItemState,
    bankIcon: Int?,
    onCardNumberValueChange: (TextFieldValue) -> Unit,
    onExpiryDateValueChange: (TextFieldValue) -> Unit,
    onCvvValueChange: (TextFieldValue) -> Unit,
    onCardTextFieldFocusedChange: (Boolean) -> Unit,
    onExpiryTextFieldFocusedChange: (Boolean) -> Unit,
    onCvvTextFieldFocusedChange: (Boolean) -> Unit,
    onSavedCardCheckedChange: (Boolean) -> Unit
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
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = stringResource(id = R.string.cc_dc_main_screen_card_number),
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
                    hint = stringResource(id = R.string.cc_dc_main_screen_placeholder_card_number),
                    isError = state.isCardNumberInvalid,
                    onValueChange = {
                        state.principalIconId = getPrincipalIcon(getCardType(it.text))
                        var cardLength = formatCreditCard(it).text.length
                        state.isCardNumberInvalid = cardLength != formattedMaxCardNumberLength
                        onCardNumberValueChange(formatCreditCard(it))
                    },
                    isFocused = state.isCardTexFieldFocused,
                    onFocusChange = {
                        onCardTextFieldFocusedChange(it)
                    },
                    trailingIcon = bankIcon?.let {
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
                    if (state.cardNumber.text.isEmpty()){
                        Text(
                            text = stringResource(id = R.string.card_error_empty_card_number),
                            style = SnapTypography.STYLES.snapTextSmallRegular,
                            color = SnapColors.getARGBColor(SUPPORT_DANGER_DEFAULT)
                        )
                    } else {
                        Text(
                            text = stringResource(id = R.string.card_error_invalid_card_number),
                            style = SnapTypography.STYLES.snapTextSmallRegular,
                            color = SnapColors.getARGBColor(SUPPORT_DANGER_DEFAULT)
                        )
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(space = 28.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1.0f)
                ) {
                    Text(
                        text = stringResource(id = R.string.cc_dc_main_screen_expiry),
                        style = SnapTypography.STYLES.snapTextSmallRegular
                    )
                    var isCardExpired = true
                    SnapTextField(
                        hint = stringResource(id = R.string.cc_dc_main_screen_placeholder_expiry),
                        value = state.expiry,
                        onValueChange = {
                            onExpiryDateValueChange(formatExpiryDate(it))
                            if (formatExpiryDate(it).text.length == 5){
                                isCardExpired = checkIsCardExpired(formatExpiryDate(it).text)
                            }
                            state.isExpiryInvalid = formatExpiryDate(it).text.length == formattedMaxExpiryLength && isCardExpired || formatExpiryDate(it).text.length != formattedMaxExpiryLength
                        },
                        isError = state.isExpiryInvalid,
                        isFocused = state.isExpiryTextFieldFocused,
                        onFocusChange = {
                            onExpiryTextFieldFocusedChange(it)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    if (state.isExpiryInvalid && !state.isExpiryTextFieldFocused) {
                        if (state.expiry.text.isEmpty()){
                            Text(
                                text = stringResource(id = R.string.card_error_empty_expiry),
                                style = SnapTypography.STYLES.snapTextSmallRegular,
                                color = SnapColors.getARGBColor(SUPPORT_DANGER_DEFAULT)
                            )
                        } else {
                            Text(
                                text = stringResource(id = R.string.card_error_invalid_expiry),
                                style = SnapTypography.STYLES.snapTextSmallRegular,
                                color = SnapColors.getARGBColor(SUPPORT_DANGER_DEFAULT)
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier.weight(1.0f)
                ) {
                    Text(
                        text = stringResource(id = R.string.cc_dc_main_screen_cvv),
                        style = SnapTypography.STYLES.snapTextSmallRegular
                    )
                    SnapTextField(
                        value = state.cvv,
                        hint = stringResource(id = R.string.cc_dc_main_screen_placeholder_cvv),
                        onValueChange = {
                            onCvvValueChange(formatCVV(it))
                            state.isCvvInvalid = formatCVV(it).text.length != formattedMaxCvvLength
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
                        if (state.cvv.text.isEmpty()){
                            Text(
                                text = stringResource(id = R.string.card_error_empty_cvv),
                                style = SnapTypography.STYLES.snapTextSmallRegular,
                                color = SnapColors.getARGBColor(SUPPORT_DANGER_DEFAULT)
                            )
                        } else{
                            Text(
                                text = stringResource(id = R.string.card_error_invalid_cvv),
                                style = SnapTypography.STYLES.snapTextSmallRegular,
                                color = SnapColors.getARGBColor(SUPPORT_DANGER_DEFAULT)
                            )
                        }
                    }
                }
            }
            Row(
            ) {
                LabelledCheckBox(checked = state.isSavedCardChecked,
                    onCheckedChange = { onSavedCardCheckedChange(it) },
                    label = stringResource(id = R.string.cc_dc_main_screen_save_this_card)
                )
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

@Composable
fun LabelledCheckBox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit),
    label: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .clickable(
                indication = rememberRipple(color = Color.LightGray),
                interactionSource = remember { MutableInteractionSource() },
                onClick = { onCheckedChange(!checked) }
            )
            .requiredHeight(ButtonDefaults.MinHeight)
    ) {
        Checkbox(
            checked = checked,
            colors = CheckboxDefaults.colors(checkmarkColor = Color.White, uncheckedColor = Color.Black, checkedColor = Color.Black ),
            onCheckedChange = null
        )

        Spacer(Modifier.size(6.dp))

        Text(
            text = label,
        )
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


