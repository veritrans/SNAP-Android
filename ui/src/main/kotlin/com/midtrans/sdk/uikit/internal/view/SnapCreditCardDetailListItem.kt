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
import com.midtrans.sdk.corekit.api.model.CreditCard
import com.midtrans.sdk.corekit.api.model.SavedToken
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.model.PromoData
import com.midtrans.sdk.uikit.internal.util.SnapCreditCardUtil
import com.midtrans.sdk.uikit.internal.util.SnapCreditCardUtil.DEFAULT_ONE_CLICK_CVV_VALUE
import com.midtrans.sdk.uikit.internal.util.SnapCreditCardUtil.formatCreditCardNumber
import com.midtrans.sdk.uikit.internal.util.SnapCreditCardUtil.formatCvv
import com.midtrans.sdk.uikit.internal.util.SnapCreditCardUtil.formatMaskedCard
import com.midtrans.sdk.uikit.internal.util.SnapCreditCardUtil.isCardNumberInvalid
import com.midtrans.sdk.uikit.internal.util.SnapCreditCardUtil.isCvvInvalid
import com.midtrans.sdk.uikit.internal.view.SnapColors.backgroundBorderSolidSecondary
import com.midtrans.sdk.uikit.internal.view.SnapColors.lineLightMuted
import com.midtrans.sdk.uikit.internal.view.SnapColors.supportDangerDefault
import com.midtrans.sdk.uikit.internal.view.SnapColors.supportNeutralFill
import com.midtrans.sdk.uikit.internal.view.SnapColors.textDisabled
import com.midtrans.sdk.uikit.internal.view.SnapColors.textPrimary
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

@Composable
fun SnapCCDetailListItem(
    @DrawableRes startIconId: Int?,
    @DrawableRes endIconId: Int,
    cvvTextField: TextFieldValue,
    state: CardItemState,
    itemTitle: String,
    bankCode: String,
    tokenType: String,
    shouldReveal: Boolean,
    inputTitle: String,
    isInputError: Boolean,
    isInstallmentActive: Boolean,
    isTransactionDenied: State<Boolean>?,
    errorTitle: String,
    onValueChange: (String) -> Unit,
    onEndIconClicked: () -> Unit,
    onCardNumberValueChange: (TextFieldValue) -> Unit,
    onExpiryDateValueChange: (TextFieldValue) -> Unit,
    onCvvValueChange: (TextFieldValue) -> Unit,
    onCardTextFieldFocusedChange: (Boolean) -> Unit,
    onExpiryTextFieldFocusedChange: (Boolean) -> Unit,
    onCvvTextFieldFocusedChange: (Boolean) -> Unit,
    onIsCvvInvalidValueChange: (Boolean) -> Unit,
    onPointBankCheckedChange: (Boolean) -> Unit,
    onInputError: (Int) -> Unit
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
                verticalArrangement = Arrangement.spacedBy(space = 8.dp)
            ) {
                if (isTransactionDenied?.value == true) {
                    Text(
                        text = stringResource(id = R.string.card_error_declined_by_bank_allowed_to_retry),
                        style = SnapTypography.STYLES.snapTextSmallRegular,
                        color = SnapColors.getARGBColor(supportDangerDefault)
                    )
                    onCvvValueChange(TextFieldValue())
                } else if (state.isBinBlocked) {
                    Text(
                        text = stringResource(id = R.string.card_error_bank_blacklisted_by_merchant),
                        style = SnapTypography.STYLES.snapTextSmallRegular,
                        color = SnapColors.getARGBColor(supportDangerDefault)
                    )
                    onCvvValueChange(TextFieldValue())
                } else {
                    Column(
                        modifier = Modifier
                            .background(
                                SnapColors.getARGBColor(supportNeutralFill),
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
                                onCvvValueChange(formatCvv(it))
                                isCvvInvalid = isCvvInvalid(it)
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
                                    color = SnapColors.getARGBColor(supportDangerDefault)
                                )
                                onInputError.invoke(R.string.card_error_empty_cvv)
                            } else {
                                Text(
                                    text = stringResource(id = R.string.card_error_invalid_cvv),
                                    style = SnapTypography.STYLES.snapTextSmallRegular,
                                    color = SnapColors.getARGBColor(supportDangerDefault)
                                )
                                onInputError.invoke(R.string.card_error_invalid_cvv)
                            }
                        }
                    }

                    var isPointOnSavedCardChecked by remember { mutableStateOf(false) }
                    if (tokenType == SavedToken.TWO_CLICKS && bankCode.lowercase() == SnapCreditCardUtil.BANK_BNI && !isInstallmentActive) {
                        PointBankCheckBox(
                            checked = isPointOnSavedCardChecked,
                            isPointBankShown = true,
                            onCheckedChange = {
                                isPointOnSavedCardChecked = it
                                onPointBankCheckedChange(isPointOnSavedCardChecked)
                            },
                            label = stringResource(id = R.string.cc_dc_main_screen_use_point_bank_bni_saved_card)
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
    state: CardItemState,
    isTransactionDenied: State<Boolean>?,
    bankIconState: Int?,
    isPointBankShownState: State<Boolean>?,
    creditCard: CreditCard?,
    onCardNumberValueChange: (TextFieldValue) -> Unit,
    onExpiryDateValueChange: (TextFieldValue) -> Unit,
    onCvvValueChange: (TextFieldValue) -> Unit,
    onCardTextFieldFocusedChange: (Boolean) -> Unit,
    onExpiryTextFieldFocusedChange: (Boolean) -> Unit,
    onCvvTextFieldFocusedChange: (Boolean) -> Unit,
    onSavedCardCheckedChange: (Boolean) -> Unit,
    onInputError: (Int) -> Unit
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
                    isTransactionDenied = isTransactionDenied,
                    bankIcon = bankIconState,
                    creditCard = creditCard,
                    isPointBankShownState = isPointBankShownState,
                    onCardNumberValueChange = {
                        onCardNumberValueChange(it)
                    },
                    onExpiryDateValueChange = {
                        onExpiryDateValueChange(it)
                    },
                    onCvvValueChange = {
                        onCvvValueChange(it)
                    },
                    onCardTextFieldFocusedChange = onCardTextFieldFocusedChange,
                    onExpiryTextFieldFocusedChange = { state.isExpiryTextFieldFocused = it },
                    onCvvTextFieldFocusedChange = { state.isCvvTextFieldFocused = it },
                    onSavedCardCheckedChange = {
                        onSavedCardCheckedChange(it)
                    },
                    onPointBankCheckedChange = { state.isPointBankChecked = it },
                    onInputError = onInputError
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
    isTransactionDenied: State<Boolean>?,
    selectedFormData: FormData?,
    listStates: List<FormData>,
    bankIconState: Int?,
    isPointBankShownState: State<Boolean>?,
    isInstallmentActive: Boolean,
    cardItemState: CardItemState,
    onItemRemoveClicked: (item: SavedCreditCardFormData) -> Unit,
    creditCard: CreditCard?,
    onCardNumberOtherCardValueChange: (TextFieldValue) -> Unit,
    onExpiryOtherCardValueChange: (TextFieldValue) -> Unit,
    onCvvValueChange: (TextFieldValue) -> Unit,
    onSavedCardRadioSelected: (item: FormData?) -> Unit,
    onIsCvvSavedCardInvalidValueChange: (Boolean) -> Unit,
    onSavedCardCheckedChange: (Boolean) -> Unit,
    onPointBankCheckedChange: (Boolean) -> Unit,
    onCardTextFieldFocusedChange: (Boolean) -> Unit,
    onInputError: (Int) -> Unit,
    onCvvSavedCardValueChange: ((TextFieldValue) -> Unit)? = null //Todo: delete later
) {
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(listStates[0].identifier) }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        var isFirstInit by remember { mutableStateOf(true) }

        listStates.forEach { item ->
            var cvvSavedCardTextFieldValue by remember { mutableStateOf(TextFieldValue()) }
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.selectable(
                        selected = (item.identifier == selectedOption),
                        onClick = {
                            if (item.identifier != selectedOption) {
                                onOptionSelected(item.identifier)
                                when (item) {
                                    is SavedCreditCardFormData -> {
                                        cvvSavedCardTextFieldValue =
                                            getCvvTextFieldValueBasedOnTokenType(item.tokenType)
                                        cardItemState.cvv = cvvSavedCardTextFieldValue
                                        onCardNumberOtherCardValueChange(TextFieldValue(item.maskedCardNumber))
                                        onSavedCardRadioSelected(item)
                                        cardItemState.cardItemType =
                                            CardItemState.CardItemType.SAVED_CARD
                                        cardItemState.isCvvInvalid = false
                                        cardItemState.isCardNumberInvalid = false
                                        cardItemState.isExpiryInvalid = false

                                    }
                                    is NewCardFormData -> {
                                        cvvSavedCardTextFieldValue = TextFieldValue("")
                                        cardItemState.cvv = cvvSavedCardTextFieldValue
                                        cardItemState.cardNumber = TextFieldValue("")
                                        cardItemState.expiry = TextFieldValue("")
                                        onSavedCardRadioSelected(item)
                                        cardItemState.cardItemType =
                                            CardItemState.CardItemType.NORMAL_CARD
                                        cardItemState.isCvvInvalid = false
                                        cardItemState.isCardNumberInvalid = false
                                        cardItemState.isExpiryInvalid = false
                                        onCardNumberOtherCardValueChange(cardItemState.cardNumber)
                                    }
                                }
                                onCvvValueChange(cardItemState.cvv)
                            }
                        },
                        role = Role.RadioButton
                    ),
                    verticalAlignment = CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (selectedFormData != null && isTransactionDenied?.value == true) {
                        onOptionSelected(selectedFormData.identifier)
                        onSavedCardRadioSelected(selectedFormData)
                        isFirstInit = false
                    } else if (isFirstInit && isTransactionDenied?.value == false && selectedFormData == null) {
                        onOptionSelected(item.identifier)
                        when (item) {
                            is SavedCreditCardFormData -> {
                                cvvSavedCardTextFieldValue =
                                    getCvvTextFieldValueBasedOnTokenType(item.tokenType)
                                cardItemState.cvv = cvvSavedCardTextFieldValue
                                onCardNumberOtherCardValueChange(TextFieldValue(item.maskedCardNumber))
                                onSavedCardRadioSelected(item)
                                cardItemState.cardItemType =
                                    CardItemState.CardItemType.SAVED_CARD
                            }
                        }
                        onCvvValueChange(cardItemState.cvv)
                        isFirstInit = false
                    }

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
                                state = cardItemState,
                                endIconId = item.endIcon,
                                itemTitle = formatMaskedCard(item.maskedCardNumber),
                                bankCode = item.bankCode,
                                tokenType = item.tokenType,
                                shouldReveal = item.savedCardIdentifier == selectedOption,
                                inputTitle = item.inputTitle,
                                cvvTextField = cvvSavedCardTextFieldValue,
                                isInputError = errorText.isNotBlank(),
                                isInstallmentActive = isInstallmentActive,
                                isTransactionDenied = isTransactionDenied,
                                errorTitle = errorText,
                                onValueChange = {},
                                onEndIconClicked = { onItemRemoveClicked(item) },
                                onCardNumberValueChange = {},
                                onExpiryDateValueChange = {},
                                onCvvValueChange = {
                                    cvvSavedCardTextFieldValue = it
                                    onCvvValueChange(cvvSavedCardTextFieldValue)
                                },
                                onCardTextFieldFocusedChange = {},
                                onExpiryTextFieldFocusedChange = {},
                                onCvvTextFieldFocusedChange = {},
                                onIsCvvInvalidValueChange = {
                                    onIsCvvSavedCardInvalidValueChange(it)
                                },
                                onPointBankCheckedChange = onPointBankCheckedChange,
                                onInputError = onInputError
                            )
                        }

                        is NewCardFormData -> {
                            InputNewCardItem(
                                shouldReveal = item.identifier == selectedOption,
                                state = cardItemState,
                                isTransactionDenied = isTransactionDenied,
                                creditCard = creditCard,
                                bankIconState = bankIconState,
                                isPointBankShownState = isPointBankShownState,
                                onCardNumberValueChange = {
                                    onCardNumberOtherCardValueChange(it)
                                },
                                onExpiryDateValueChange = {
                                    onExpiryOtherCardValueChange(it)
                                },
                                onCvvValueChange = {
                                    onCvvValueChange(it)
                                },
                                onCardTextFieldFocusedChange = onCardTextFieldFocusedChange,
                                onExpiryTextFieldFocusedChange = {},
                                onCvvTextFieldFocusedChange = {},
                                onSavedCardCheckedChange = {
                                    onSavedCardCheckedChange(it)
                                },
                                onInputError = onInputError
                            )
                        }
                    }
                }

                Divider(
                    thickness = 1.dp,
                    color = SnapColors.getARGBColor(lineLightMuted)
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
    val bankCode: String,
    var errorText: MutableState<String>,
    val inputTitle: String,
    var savedCardIdentifier: String,
    var tokenId: String,
    var cvvSavedCardTextField: TextFieldValue,
    var isCvvSavedCardInvalid: Boolean,
    var isPointBankSavedCardChecked: Boolean
) : FormData(savedCardIdentifier)

class NewCardFormData(
    var newCardIdentifier: String
) : FormData(newCardIdentifier)


open class FormData(
    val identifier: String
)

class CardItemState(
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
    isPointBankChecked: Boolean,
    isBinBlocked: Boolean = false,
    isTransactionDenied: Boolean,
    principalIconId: Int?,
    customerEmail: TextFieldValue,
    customerPhone: TextFieldValue,
    promoId: Long,
    promoName: String?,
    promoAmount: String?,
    isInstallmentAllowed: Boolean,
    cardItemType: CardItemType = CardItemType.NORMAL_CARD
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
    var isPointBankChecked by mutableStateOf(isPointBankChecked)
    var customerEmail by mutableStateOf(customerEmail)
    var customerPhone by mutableStateOf(customerPhone)
    var promoId by mutableStateOf(promoId)
    var promoName by mutableStateOf(promoName)
    var promoAmount by mutableStateOf(promoAmount)
    var cardItemType by mutableStateOf(cardItemType)
    var isInstallmentAllowed by mutableStateOf(isInstallmentAllowed)
    var isBinBlocked by mutableStateOf(isBinBlocked)
    var isTransactionDenied by mutableStateOf(isTransactionDenied)

    val iconIdList by mutableStateOf(
        listOf(
            R.drawable.ic_outline_visa_24,
            R.drawable.ic_outline_mastercard_24,
            R.drawable.ic_outline_jcb_24,
            R.drawable.ic_outline_amex_24,
        )
    )

    enum class CardItemType {
        NORMAL_CARD,
        SAVED_CARD
    }
}

private fun getCvvTextFieldValueBasedOnTokenType(tokenType: String): TextFieldValue {
    return if (tokenType == SavedToken.ONE_CLICK) {
        TextFieldValue(
            text = DEFAULT_ONE_CLICK_CVV_VALUE,
            selection = TextRange(DEFAULT_ONE_CLICK_CVV_VALUE.length)
        )
    } else {
        TextFieldValue("")
    }
}

@Composable
fun NormalCardItem(
    state: CardItemState,
    isTransactionDenied: State<Boolean>?,
    bankIcon: Int?,
    creditCard: CreditCard?,
    isPointBankShownState: State<Boolean>?,
    onCardNumberValueChange: (TextFieldValue) -> Unit,
    onExpiryDateValueChange: (TextFieldValue) -> Unit,
    onCvvValueChange: (TextFieldValue) -> Unit,
    onCardTextFieldFocusedChange: (Boolean) -> Unit,
    onExpiryTextFieldFocusedChange: (Boolean) -> Unit,
    onCvvTextFieldFocusedChange: (Boolean) -> Unit,
    onSavedCardCheckedChange: (Boolean) -> Unit,
    onPointBankCheckedChange: (Boolean) -> Unit,
    onInputError: (Int) -> Unit
) {
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

                state.isTransactionDenied = isTransactionDenied?.value ?: false
                SnapTextField(
                    value = state.cardNumber,
                    hint = stringResource(id = R.string.cc_dc_main_screen_placeholder_card_number),
                    isError = state.isCardNumberInvalid,
                    onValueChange = {
                        state.principalIconId =
                            SnapCreditCardUtil.getPrincipalIcon(SnapCreditCardUtil.getCardType(it.text))
                        state.isCardNumberInvalid = isCardNumberInvalid(
                            rawCardNumber = it,
                            isBinBlocked = state.isBinBlocked
                        )
                        onCardNumberValueChange(formatCreditCardNumber(it))
                    },
                    isFocused = state.isCardTexFieldFocused,
                    onFocusChange = { focused ->
                        onCardTextFieldFocusedChange(focused)
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

                if (state.isCardNumberInvalid && !state.isCardTexFieldFocused && !state.isTransactionDenied) {
                    if (state.cardNumber.text.isEmpty()) {
                        Text(
                            text = stringResource(id = R.string.card_error_empty_card_number),
                            style = SnapTypography.STYLES.snapTextSmallRegular,
                            color = SnapColors.getARGBColor(supportDangerDefault)
                        )
                        onInputError.invoke(R.string.card_error_empty_card_number)
                    } else {
                        val textId = when {
                            state.isBinBlocked -> R.string.card_error_bank_blacklisted_by_merchant
                            else -> R.string.card_error_invalid_card_number
                        }

                        Text(
                            text = stringResource(id = textId),
                            style = SnapTypography.STYLES.snapTextSmallRegular,
                            color = SnapColors.getARGBColor(supportDangerDefault)
                        )
                        onInputError.invoke(textId)
                    }
                } else if (state.isTransactionDenied) {
                    state.isCardNumberInvalid = true
                    Text(
                        text = stringResource(id = R.string.card_error_declined_by_bank_allowed_to_retry),
                        style = SnapTypography.STYLES.snapTextSmallRegular,
                        color = SnapColors.getARGBColor(supportDangerDefault)
                    )
                    onInputError.invoke(R.string.card_error_declined_by_bank_allowed_to_retry)
                }

                if (state.isBinBlocked && state.isCardTexFieldFocused) {
                    Text(
                        text = stringResource(id = R.string.card_error_bank_blacklisted_by_merchant),
                        style = SnapTypography.STYLES.snapTextSmallRegular,
                        color = SnapColors.getARGBColor(supportDangerDefault)
                    )
                    onInputError.invoke(R.string.card_error_bank_blacklisted_by_merchant)
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
                                color = SnapColors.getARGBColor(supportDangerDefault)
                            )
                            onInputError.invoke(R.string.card_error_empty_expiry)
                        } else {
                            Text(
                                text = stringResource(id = R.string.card_error_invalid_expiry),
                                style = SnapTypography.STYLES.snapTextSmallRegular,
                                color = SnapColors.getARGBColor(supportDangerDefault)
                            )
                            onInputError.invoke(R.string.card_error_invalid_expiry)
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
                            onCvvValueChange(formatCvv(it))
                            state.isCvvInvalid =
                                formatCvv(it).text.length < SnapCreditCardUtil.FORMATTED_MIN_CVV_LENGTH
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
                                color = SnapColors.getARGBColor(supportDangerDefault)
                            )
                            onInputError.invoke(R.string.card_error_empty_cvv)
                        } else {
                            Text(
                                text = stringResource(id = R.string.card_error_invalid_cvv),
                                style = SnapTypography.STYLES.snapTextSmallRegular,
                                color = SnapColors.getARGBColor(supportDangerDefault)
                            )
                            onInputError.invoke(R.string.card_error_invalid_cvv)
                        }
                    }
                }
            }
        }
    }
    Column(
    ) {
        if (creditCard?.installment == null) {
            isPointBankShownState?.value?.let { isPointBankShown ->
                if (isPointBankShown) {
                    PointBankCheckBox(
                        checked = state.isPointBankChecked,
                        isPointBankShown = isPointBankShown,
                        onCheckedChange = { onPointBankCheckedChange(it) },
                        label = stringResource(id = R.string.cc_dc_main_screen_use_point_bank_bni_saved_card)
                    )
                } else {
                    onPointBankCheckedChange(isPointBankShown)
                }
            }
        }

        creditCard?.saveCard?.let { isCardSaved ->
            if (isCardSaved) {
                LabelledCheckBox(
                    checked = state.isSavedCardChecked,
                    onCheckedChange = { onSavedCardCheckedChange(it) },
                    label = stringResource(id = R.string.cc_dc_main_screen_save_this_card)
                )
            }
        }
    }
}

@Composable
fun PointBankCheckBox(
    checked: Boolean,
    isPointBankShown: Boolean,
    onCheckedChange: ((Boolean) -> Unit),
    label: String,
    modifier: Modifier = Modifier
) {
    if (isPointBankShown) {
        LabelledCheckBox(
            checked = checked,
            onCheckedChange = { onCheckedChange(it) },
            label = label
        )
    } else {
        Row(
            verticalAlignment = CenterVertically,
            modifier = modifier
                .clip(MaterialTheme.shapes.small)
                .requiredHeight(ButtonDefaults.MinHeight)
        ) {
            Checkbox(
                checked = false,
                colors = CheckboxDefaults.colors(
                    uncheckedColor = SnapColors.getARGBColor(textDisabled),
                    disabledColor = SnapColors.getARGBColor(textDisabled)
                ),
                onCheckedChange = null
            )

            Spacer(Modifier.size(6.dp))

            Text(
                text = label,
                color = SnapColors.getARGBColor(textDisabled),
                style = SnapTypography.STYLES.snapTextMediumRegular
            )
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
                checkedColor = SnapColors.getARGBColor(textPrimary)
            ),
            onCheckedChange = null
        )

        Spacer(Modifier.size(6.dp))

        Text(
            text = label,
            color = SnapColors.getARGBColor(textPrimary),
            style = SnapTypography.STYLES.snapTextMediumRegular
        )
    }
}

@Composable
fun PromoLayout(
    promoData: List<PromoData>,
    cardItemState: CardItemState,
    isTransactionDenied: State<Boolean>?,
    allowRetry: Boolean
): () -> Unit {
    Divider(
        color = SnapColors.getARGBColor(backgroundBorderSolidSecondary),
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(top = 16.dp)
    )
    Text(
        text = stringResource(id = R.string.promo_select_promo_title),
        modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
        style = SnapTypography.STYLES.snapTextMediumRegular
    )

    val promoReset = SnapPromoListRadioButton(
        cardItemState = cardItemState,
        isTransactionDenied = isTransactionDenied,
        states = promoData.toMutableList().apply {
            add(
                PromoData(
                    identifier = "0",
                    promoName = stringResource(R.string.cant_continue_promo_dont_want_to_use_promo),
                    discountAmount = ""
                )
            )
        },
        allowRetry = allowRetry,
        onItemSelectedListener = {
            cardItemState.promoId = it.identifier.orEmpty().toLong()
            cardItemState.promoName = it.promoName
            cardItemState.promoAmount = it.discountAmount
        }
    )
    return {
        promoReset.invoke()
    }
}
