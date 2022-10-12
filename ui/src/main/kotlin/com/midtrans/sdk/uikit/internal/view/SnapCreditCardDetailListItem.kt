package com.midtrans.sdk.uikit.internal.view

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.midtrans.sdk.corekit.api.model.CreditCard
import com.midtrans.sdk.corekit.api.model.SavedToken
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.util.SnapCreditCardUtil
import com.midtrans.sdk.uikit.internal.view.SnapColors.BACKGROUND_BORDER_SOLID_SECONDARY
import com.midtrans.sdk.uikit.internal.view.SnapColors.SUPPORT_DANGER_DEFAULT
import com.midtrans.sdk.uikit.internal.view.SnapColors.SUPPORT_NEUTRAL_FILL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

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
                var isCvvInvalid by remember { mutableStateOf(false) }
                var isCvvTextFieldFocused by remember { mutableStateOf(false) }
                SnapTextField(
                    value = cvvTextField,
                    onValueChange = {
                        onCvvValueChange(formatCVV(it))
                        isCvvInvalid = formatCVV(it).text.length < SnapCreditCardUtil.FORMATTED_MIN_CVV_LENGTH
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
                    if (cvvTextField.text.isEmpty()) {
                        Text(
                            text = stringResource(id = R.string.card_error_empty_cvv),
                            style = SnapTypography.STYLES.snapTextSmallRegular,
                            color = SnapColors.getARGBColor(SUPPORT_DANGER_DEFAULT)
                        )
                    } else {
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
    creditCard: CreditCard?,
    binType: String?,
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
                    binType = binType,
                    cardIssuerBank = null,
                    creditCard = creditCard,
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
                    onExpiryTextFieldFocusedChange = { state.isExpiryTextFieldFocused = it },
                    onCvvTextFieldFocusedChange = { state.isCvvTextFieldFocused = it },
                    onSavedCardCheckedChange = {
                        onSavedCardCheckedChange(it)
                    },
                    onInstallmentTermSelected = { }
                )
            }
        }
    }
}


fun formatExpiryDate(input: TextFieldValue): TextFieldValue {
    val maxNumberOfMonth = 12

    val digit = input.text.filter {
        it.isDigit()
    }
    if (digit.length >= 2) {
        val firstTwoDigit = digit.substring(0 until 2)
        var processed = if (firstTwoDigit.toInt() <= maxNumberOfMonth) {
            digit.replace("/", "")
        } else {
            val adjustedDigit = "0$digit"
            adjustedDigit.replace("/", "")
        }
        processed = processed.replace("(\\d{2})(?=\\d)".toRegex(), "$1/")
        val length = min(processed.length, 5)
        return input.copy(processed.substring(0 until length), TextRange(length))
    } else {
        return if (input.text.isEmpty()) {
            input.copy()
        } else {
            input.copy(digit)
        }
    }
}

fun checkIsCardExpired(cardExpiry: String): Boolean {
    val sdf = SimpleDateFormat("MM/yy", Locale.getDefault())
    val currentDate = sdf.format(Date())
    return sdf.parse(cardExpiry).before(sdf.parse(currentDate))
}

@Composable
fun SnapSavedCardRadioGroup(
    modifier: Modifier,
    listStates: List<FormData>,
    bankIconState: Int?,
    normalCardItemState: NormalCardItemState,
    onItemRemoveClicked: (item: SavedCreditCardFormData) -> Unit,
    creditCard: CreditCard?,
    binType: String?,
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
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.selectable(
                        selected = (item.identifier == selectedOption),
                        onClick = {
                            onOptionSelected(item.identifier)
                            onSavedCardRadioSelected(item)
                            when (item) {
                                is SavedCreditCardFormData -> {
                                    cvvSavedCardTextFieldValue =
                                        formatCvvTextFieldBasedOnTokenType(item.tokenType)
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
                                cvvTextField = cvvSavedCardTextFieldValue,
                                isInputError = errorText.isNotBlank(),
                                errorTitle = errorText,
                                onValueChange = {},
                                onEndIconClicked = { onItemRemoveClicked(item) },
                                onCardNumberValueChange = {},
                                onExpiryDateValueChange = {},
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
                                creditCard = creditCard,
                                bankIconState = bankIconState,
                                binType = binType,
                                onCardNumberValueChange = { onCardNumberOtherCardValueChange(it) },
                                onExpiryDateValueChange = { onExpiryOtherCardValueChange(it) },
                                onCvvValueChange = { onCvvOtherCardValueChange(it) },
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
) : FormData(savedCardIdentifier)

class NewCardFormData(
    var newCardIdentifier: String
) : FormData(newCardIdentifier)


open class FormData(
    val identifier: String
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
    isSaveCardChecked: Boolean,
    isEligibleForInstallment: Boolean,
    isRequiredInstallment: Boolean,
    principalIconId: Int?,
    customerEmail: TextFieldValue,
    customerPhone: TextFieldValue
) {
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
    var isSavedCardChecked by mutableStateOf(isSaveCardChecked)
    var isEligibleForInstallment by mutableStateOf(isEligibleForInstallment)
    var isRequiredInstallment by mutableStateOf(isRequiredInstallment)
    var customerEmail by mutableStateOf(customerEmail)
    var customerPhone by mutableStateOf(customerPhone)

    val iconIdList by mutableStateOf(
        listOf(
            R.drawable.ic_outline_visa_24,
            R.drawable.ic_outline_mastercard_24,
            R.drawable.ic_outline_jcb_24,
            R.drawable.ic_outline_amex_24,
        )
    )
}

private fun formatCvvTextFieldBasedOnTokenType(tokenType: String): TextFieldValue {
    val output = if (tokenType == SavedToken.ONE_CLICK) {
        TextFieldValue(
            SnapCreditCardUtil.DEFAULT_ONE_CLICK_CVV_VALUE, selection = TextRange(
                SnapCreditCardUtil.DEFAULT_ONE_CLICK_CVV_VALUE.length
            )
        )
    } else {
        TextFieldValue("")
    }
    return output
}

private fun formatMaskedCard(maskedCard: String): String {
    val lastFourDigit =
        maskedCard.substring(startIndex = maskedCard.length - 4, endIndex = maskedCard.length)
    return "**** **** **** $lastFourDigit"
}

fun formatCreditCard(input: TextFieldValue): TextFieldValue {
    val digit = input.text.filter {
        it.isDigit()
    }
    var processed: String = digit.replace("\\D", "").replace(" ", "")
    // insert a space after all groups of 4 digits that are followed by another digit
    processed = processed.replace("(\\d{4})(?=\\d)".toRegex(), "$1 ")
    val length = min(processed.length, SnapCreditCardUtil.FORMATTED_MAX_CARD_NUMBER_LENGTH)
    return input.copy(text = processed.substring(0 until length), selection = TextRange(length))
}

fun formatCVV(input: TextFieldValue): TextFieldValue {
    val digit = input.text.filter {
        it.isDigit()
    }
    val length = min(digit.length, SnapCreditCardUtil.FORMATTED_MAX_CVV_LENGTH)
    return input.copy(digit.substring(0 until length), TextRange(length))
}

@Composable
fun NormalCardItem(
    state: NormalCardItemState,
    bankIcon: Int?,
    binType: String?,
    cardIssuerBank: String?,
    creditCard: CreditCard?,
    onCardNumberValueChange: (TextFieldValue) -> Unit,
    onExpiryDateValueChange: (TextFieldValue) -> Unit,
    onCvvValueChange: (TextFieldValue) -> Unit,
    onCardTextFieldFocusedChange: (Boolean) -> Unit,
    onExpiryTextFieldFocusedChange: (Boolean) -> Unit,
    onCvvTextFieldFocusedChange: (Boolean) -> Unit,
    onSavedCardCheckedChange: (Boolean) -> Unit,
    onInstallmentTermSelected: (String) -> Unit
) {
    var isBinBlocked by remember { mutableStateOf(false) }
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
                        modifier = Modifier
                            .weight(1f)
                            .padding(bottom = 4.dp),
                        style = SnapTypography.STYLES.snapTextSmallRegular,
                    )
                    if (state.principalIconId != null && state.cardNumber.text.isNotEmpty()) {
                        Icon(
                            painter = painterResource(id = state.principalIconId!!),
                            contentDescription = null,
                            tint = Color.Unspecified
                        )
                    } else if (state.cardNumber.text.isEmpty()) {
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
                        state.principalIconId =
                            SnapCreditCardUtil.getPrincipalIcon(SnapCreditCardUtil.getCardType(it.text))
                        val cardLength = formatCreditCard(it).text.length
                        isBinBlocked = SnapCreditCardUtil.isBinBlocked(
                            SnapCreditCardUtil.getCardNumberFromTextField(it), creditCard
                        )
                        state.isCardNumberInvalid =
                            cardLength != SnapCreditCardUtil.FORMATTED_MAX_CARD_NUMBER_LENGTH
                                    || !SnapCreditCardUtil.isValidCardNumber(SnapCreditCardUtil.getCardNumberFromTextField(it))
                                    || isBinBlocked
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
                    if (state.cardNumber.text.isEmpty()) {
                        Text(
                            text = stringResource(id = R.string.card_error_empty_card_number),
                            style = SnapTypography.STYLES.snapTextSmallRegular,
                            color = SnapColors.getARGBColor(SUPPORT_DANGER_DEFAULT)
                        )
                    } else {
                        Text(
                            text = stringResource(id = if (isBinBlocked) R.string.card_error_bank_blacklisted_by_merchant else R.string.card_error_invalid_card_number),
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
                        style = SnapTypography.STYLES.snapTextSmallRegular,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    var isCardExpired = true
                    SnapTextField(
                        hint = stringResource(id = R.string.cc_dc_main_screen_placeholder_expiry),
                        value = state.expiry,
                        onValueChange = {
                            onExpiryDateValueChange(formatExpiryDate(it))
                            if (formatExpiryDate(it).text.length == 5) {
                                isCardExpired = checkIsCardExpired(formatExpiryDate(it).text)
                            }
                            state.isExpiryInvalid =
                                formatExpiryDate(it).text.length == SnapCreditCardUtil.FORMATTED_MAX_EXPIRY_LENGTH &&
                                        isCardExpired ||
                                        formatExpiryDate(it).text.length != SnapCreditCardUtil.FORMATTED_MAX_EXPIRY_LENGTH
                        },
                        isError = state.isExpiryInvalid,
                        isFocused = state.isExpiryTextFieldFocused,
                        onFocusChange = {
                            onExpiryTextFieldFocusedChange(it)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    if (state.isExpiryInvalid && !state.isExpiryTextFieldFocused) {
                        if (state.expiry.text.isEmpty()) {
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
                        style = SnapTypography.STYLES.snapTextSmallRegular,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    SnapTextField(
                        value = state.cvv,
                        hint = stringResource(id = R.string.cc_dc_main_screen_placeholder_cvv),
                        onValueChange = {
                            onCvvValueChange(formatCVV(it))
                            state.isCvvInvalid = formatCVV(it).text.length < SnapCreditCardUtil.FORMATTED_MIN_CVV_LENGTH
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
                        if (state.cvv.text.isEmpty()) {
                            Text(
                                text = stringResource(id = R.string.card_error_empty_cvv),
                                style = SnapTypography.STYLES.snapTextSmallRegular,
                                color = SnapColors.getARGBColor(SUPPORT_DANGER_DEFAULT)
                            )
                        } else {
                            Text(
                                text = stringResource(id = R.string.card_error_invalid_cvv),
                                style = SnapTypography.STYLES.snapTextSmallRegular,
                                color = SnapColors.getARGBColor(SUPPORT_DANGER_DEFAULT)
                            )
                        }
                    }
                }
            }
            creditCard?.saveCard?.let { isCardSaved ->
                if (isCardSaved) {
                    Row {
                        LabelledCheckBox(
                            checked = state.isSavedCardChecked,
                            onCheckedChange = { onSavedCardCheckedChange(it) },
                            label = stringResource(id = R.string.cc_dc_main_screen_save_this_card)
                        )
                    }
                }
            }

            creditCard?.installment?.let { installment -> //TODO should create a method instead
                val isRequired = installment.isRequired
                state.isRequiredInstallment = isRequired

                installment.terms?.let { terms ->
                    var selectedBank = ""
                    val termList = when {
                        terms.containsKey(cardIssuerBank?.lowercase()) -> {
                            val key = terms.keys.toList()[0]
                            selectedBank = key
                            terms[key]
                        }
                        terms.containsKey("offline") -> {
                            val key = terms.keys.toList()[0]
                            selectedBank = key
                            terms[key]
                        }
                        else -> {
                            val key = terms.keys.toList()[0]
                            terms[key]
                        }
                    }

                    val isCCMatch = terms.containsKey(cardIssuerBank?.lowercase())
                    val isCreditCard = binType == "CREDIT"
                    val isError: Boolean
                    val errorMessage = mutableListOf<String>()

                    if (!selectedBank.contains("offline")){
                        isError = !isCCMatch || !isCreditCard
                        if (!isCreditCard) {
                            errorMessage.add(stringResource(id = R.string.installment_dc_error))
                        }
                        if (!isCCMatch) {
                            errorMessage.add(stringResource(id = R.string.installment_cc_not_match_installment))
                        }
                    } else {
                        isError = false
                    }

                    termList
                        ?.takeIf { it.isNotEmpty() }
                        ?.map { term -> stringResource(id = R.string.installment_term, term) }
                        ?.toMutableList()
                        ?.let { options ->
                            if (!isRequired) {
                                options.add(0, stringResource(id = R.string.installment_full_payment))
                            }

                            InstallmentDropdownMenu(
                                state = state,
                                title = stringResource(R.string.installment_title),
                                binType = binType,
                                isRequired = isRequired,
                                isError = isError,
                                errorMessage = errorMessage,
                                optionList = options.toList(),
                                onOptionsSelected = { selectedTerm ->
                                    selectedTerm
                                        .filter { it.isDigit() }
                                        .takeIf { it.isDigitsOnly() }
                                        ?.let { onInstallmentTermSelected("${selectedBank}_$it") }
                                }
                            )
                        }
                }
            }
        }
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
        verticalAlignment = CenterVertically,
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
            colors = CheckboxDefaults.colors(
                checkmarkColor = Color.White,
                uncheckedColor = Color.Black,
                checkedColor = Color.Black
            ),
            onCheckedChange = null
        )

        Spacer(Modifier.size(6.dp))

        Text(
            text = label,
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun InstallmentDropdownMenu(
    state: NormalCardItemState?,
    title: String,
    isError: Boolean,
    isRequired: Boolean,
    errorMessage: List<String>,
    binType: String?,
    optionList: List<String>,
    onOptionsSelected: (String) -> Unit
) {
    val options by remember { mutableStateOf(optionList) }
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }

    Column(
        modifier = Modifier.fillMaxWidth(1f),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            modifier = Modifier.padding(bottom = 20.dp),
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
                enabled = !isError,
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
                            onOptionsSelected(selectionOption)
                            expanded = false
                        },
                        enabled = !isError
                    ) {
                        Text(
                            text = selectionOption,
                            style = SnapTypography.STYLES.snapTextMediumRegular
                        )
                    }
                }
            }
        }

        binType?.let {
            if(isError && isRequired) {
                state!!.isEligibleForInstallment = false
                errorMessage.forEach{
                    ErrorTextInstallment(errorMessage = it)
                }
            } else {
                state!!.isEligibleForInstallment = true
            }
        }
    }
}

@Composable
fun ErrorTextInstallment(
    errorMessage: String
) {
    Text(
        text = errorMessage,
        style = SnapTypography.STYLES.snapTextSmallRegular,
        color = SnapColors.getARGBColor(SUPPORT_DANGER_DEFAULT)
    )
}
