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
    state: NormalCardItemState,
    creditCard: CreditCard?,
    cardIssuerBank: String?,
    binType: String?,
    onInstallmentTermSelected: (String) -> Unit
) {
    creditCard?.installment?.let { installment ->
        val isRequired = installment.isRequired
        state.isRequiredInstallment = isRequired

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

            val isCreditCard = binType == "CREDIT"
            val isError: Boolean
            val errorMessage = mutableListOf<String>()

            if (!selectedBank.contains("offline")) {
                isError = !isOnUs || !isCreditCard
                if (!isCreditCard) {
                    errorMessage.add(stringResource(id = R.string.installment_dc_error))
                }
                if (!isOnUs) {
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
                //TODO add padding bottom 10dp between dropdown and first error message
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
            if (isError && isRequired) {
                state!!.isEligibleForInstallment = false
                Column(modifier = Modifier.fillMaxWidth(1f).padding(top = 10.dp)) {
                    errorMessage.forEach {
                        ErrorTextInstallment(errorMessage = it)
                    }
                }
            } else {
                state!!.isEligibleForInstallment = true
            }
        }
    }
}