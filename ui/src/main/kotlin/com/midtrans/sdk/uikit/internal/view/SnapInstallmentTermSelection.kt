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
            var selectedBank: String
            var termList: List<Int>?

            getSelectedBankAndTerms(cardIssuerBank.orEmpty(), terms).also {
                selectedBank = it.first
                termList = it.second
            }

            val isOnUs = checkIsOnUs(
                issuerBank = cardIssuerBank.orEmpty(),
                enabledBanks = terms.keys.toList()
            )
            val isBinComplete = cardNumber.text.length >= 8
            val isCreditCard = binType?.contains("credit", true) ?: false
            val isOfflineInstallment = selectedBank.contains("offline")
            val isError = !isOfflineInstallment && (!isOnUs || !isCreditCard)
            val isEnabled = !isError && isBinComplete
            val errorMessageIdList = getErrorMessageIdList(
                isOfflineInstallment = isOfflineInstallment,
                isCreditCard = isCreditCard,
                isOnUs = isOnUs
            )

            termList
                ?.takeIf { it.isNotEmpty() }
                ?.map { term -> stringResource(id = R.string.installment_term, term) }
                ?.toMutableList()
                ?.let { options ->
                    if (!isRequired) {
                        options.add(0, stringResource(id = R.string.installment_full_payment))
                    }

                    InstallmentDropdownMenu(
                        title = stringResource(R.string.installment_title),
                        enabled = isEnabled,
                        isErrorVisible = isError && isRequired && isBinComplete,
                        errorMessageIdList = errorMessageIdList,
                        optionList = options.toList(),
                        onOptionsSelected = { selectedTerm ->
                            selectedTerm
                                .filter { it.isDigit() }
                                .takeIf { it.isDigitsOnly() && it.isNotEmpty() }
                                ?.let { onInstallmentTermSelected("${selectedBank}_$it") }
                                ?: run { onInstallmentTermSelected("") }
                        },
                        onInstallmentAllowed = { onInstallmentAllowed(it) }
                    )
                }
        }
    }
}

private fun checkIsOnUs(
    issuerBank: String,
    enabledBanks: List<String>
): Boolean {
    return enabledBanks.contains(issuerBank.lowercase())
}

private fun getSelectedBankAndTerms(
    issuerBank: String,
    terms: Map<String, List<Int>>
): Pair<String, List<Int>?> {
    var selectedBank = ""

    val termList = when {
        terms.containsKey(issuerBank.lowercase()) -> {
            terms
                .filter { it.key.contains(issuerBank, true) }
                .map { it.key }
                .let {
                    selectedBank = it[0]
                    terms[selectedBank]
                }
        }
        terms.containsKey("offline") -> {
            selectedBank = "offline"
            terms[selectedBank]
        }
        else -> {
            terms.values.toList()[0]
        }
    }

    return Pair(selectedBank, termList)
}

private fun getErrorMessageIdList(
    isCreditCard: Boolean,
    isOnUs: Boolean,
    isOfflineInstallment: Boolean
): List<Int> {
    val errorMessage = mutableListOf<Int>()

    if (!isOfflineInstallment) {
        if (!isCreditCard) {
            errorMessage.add(R.string.installment_dc_error)
        }
        if (!isOnUs) {
            errorMessage.add(R.string.installment_cc_not_match_installment)
        }
    }

    return errorMessage
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun InstallmentDropdownMenu(
    title: String,
    enabled: Boolean,
    isErrorVisible: Boolean,
    errorMessageIdList: List<Int>,
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
                enabled = enabled,
                onFocusChange = {},
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                textStyle = SnapTypography.STYLES.snapTextMediumRegular
            )

            if (enabled) {
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
                            },
                            enabled = enabled
                        ) {
                            Text(
                                text = selectionOption,
                                style = SnapTypography.STYLES.snapTextMediumRegular
                            )
                        }
                    }
                }
            } else {
                selectedOptionText = options[0]
            }
            onOptionsSelected(selectedOptionText)
        }

        if (isErrorVisible) {
            onInstallmentAllowed.invoke(false)
            errorMessageIdList.forEach {
                ErrorTextInstallment(errorMessage = stringResource(id = it))
            }
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
        color = SnapColors.getARGBColor(SnapColors.supportDangerDefault)
    )
}