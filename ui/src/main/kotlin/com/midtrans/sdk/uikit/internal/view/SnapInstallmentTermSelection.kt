package com.midtrans.sdk.uikit.internal.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.midtrans.sdk.corekit.api.model.CreditCard
import com.midtrans.sdk.uikit.R

@Composable
fun SnapInstallmentTermSelectionMenu(
    creditCard: CreditCard?,
    cardIssuerBank: String?,
    binType: String?,
    cardNumber: TextFieldValue,
    onInstallmentTermSelected: (String) -> Unit,
    onInstallmentAllowed: (Boolean) -> Unit
) {
    creditCard?.installment?.let { installment ->
        val isRequired = installment.isRequired

        installment.terms?.let { terms ->
            var isOnUs = false
            var selectedBank = ""
            val termList = when {
                terms.containsKey(cardIssuerBank?.lowercase()) -> {
                    val key = terms.keys.toList()[0]
                    selectedBank = key
                    isOnUs = true
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

            val isCreditCard = binType.contains("credit", true)
            var isError = false
            val errorMessage = mutableListOf<String>()

            if (!selectedBank.contains("offline")) {
                isError = !isOnUs || !isCreditCard
                if (!isCreditCard) {
                    errorMessage.add(stringResource(id = R.string.installment_dc_error))
                }
                if (!isOnUs) {
                    errorMessage.add(stringResource(id = R.string.installment_cc_not_match_installment))
                }
            }

            termList
                ?.map { term -> stringResource(id = R.string.installment_term, term) }
                ?.toMutableList()
                ?.let { options ->
                    if (!isRequired) {
                        options.add(0, stringResource(id = R.string.installment_full_payment))
                    }

                    Divider(
                        thickness = 1.dp,
                        color = SnapColors.getARGBColor(SnapColors.LINE_LIGHT_MUTED),
                        modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                    )
                    InstallmentDropdownMenu(
                        title = stringResource(R.string.installment_title),
                        binType = binType,
                        isRequired = isRequired,
                        isError = isError,
                        isErrorVisible = isError && isRequired && cardNumber.text.length >= 8,
                        errorMessages = errorMessage,
                        cardNumber = cardNumber,
                        optionList = options.toList(),
                        onOptionsSelected = { selectedTerm ->
                            selectedTerm
                                .filter { it.isDigit() }
                                .takeIf { it.isDigitsOnly() }
                                ?.let { onInstallmentTermSelected("${selectedBank}_$it") }
                        },
                        onInstallmentAllowed = { onInstallmentAllowed(it) }
                    )
                }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun InstallmentDropdownMenu(
    title: String,
    isError: Boolean,
    isErrorVisible: Boolean,
    errorMessages: List<String>,
    optionList: List<String>,
    onOptionsSelected: (String) -> Unit,
    onInstallmentAllowed: (Boolean) -> Unit
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

        if (isErrorVisible) {
            onInstallmentAllowed.invoke(false)
            errorMessages.forEach { ErrorTextInstallment(errorMessage = it) }
        } else {
            onInstallmentAllowed.invoke(true)
        }
    }
}


@Composable
private fun ErrorTextInstallment(errorMessage: String) {
    Text(
        text = errorMessage,
        style = SnapTypography.STYLES.snapTextSmallRegular,
        color = SnapColors.getARGBColor(SnapColors.SUPPORT_DANGER_DEFAULT)
    )
}