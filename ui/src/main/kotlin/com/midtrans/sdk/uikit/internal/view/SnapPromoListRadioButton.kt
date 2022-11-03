package com.midtrans.sdk.uikit.internal.view;

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.midtrans.sdk.uikit.internal.model.PromoData

object SnapPromoListRadioButton {
}

@Composable
fun SnapPromoListRadioButton(
    states: List<PromoData>,
    onItemSelectedListener: (promodata: PromoData) -> Unit
): () -> Unit {
    val (selectedOption, onOptionSelected) = remember {
        mutableStateOf(states.filter { it.enabled.value }[0].promoName)
    }

    var needReset by remember {
        mutableStateOf(false)
    }

    if(needReset){
        needReset = false
        onOptionSelected(states.filter { it.enabled.value }[0].also { onItemSelectedListener(it) }.promoName)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        states.forEach { item ->
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val enabled by item.enabled
                if((!enabled).and(item.promoName == selectedOption)){
                    onOptionSelected(states.filter { it.enabled.value }[0].also { onItemSelectedListener(it) }.promoName)
                }
                Row(
                    modifier = if (enabled) Modifier.selectable(
                        selected = item.promoName == selectedOption,
                        onClick = {
                            onOptionSelected(item.promoName)
                            onItemSelectedListener(item)
                        },
                        role = Role.RadioButton
                    ) else Modifier,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    RadioButton(
                        selected = item.promoName == selectedOption,
                        onClick = null,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = if (enabled) Color.Black else SnapColors.getARGBColor(
                                SnapColors.iconMuted
                            )
                        ),
                        enabled = enabled
                    )
                    SnapPromoListRadioButtonItem(promoData = item)
                }
            }
        }
    }
    return {
        needReset = true
    }
}

@Composable
fun SnapPromoListRadioButtonItem(promoData: PromoData) {
    val enabled by promoData.enabled
    Column {
        Row() {
            Text(
                text = promoData.promoName,
                modifier = Modifier.weight(1f),
                style = SnapTypography.STYLES.snapTextMediumRegular,
                color = SnapColors.getARGBColor(if (enabled) SnapColors.textPrimary else SnapColors.textMuted)
            )
            if(enabled) {
                Text(
                    text = promoData.discountAmount,
                    style = SnapTypography.STYLES.snapTextMediumRegular,
                    color = SnapColors.getARGBColor(SnapColors.textPrimary)
                )
            }
        }
        promoData.errorMessage?.let { subLeftText ->
            Text(
                text = promoData.installmentTerm?.let {
                    stringResource(id = subLeftText, getInstallmentTermsString(it))
                } ?: run {
                    stringResource(id = subLeftText)
                 },
                style = SnapTypography.STYLES.snapTextSmallRegular,
                color = SnapColors.getARGBColor(if (enabled) SnapColors.textPrimary else SnapColors.textMuted)
            )
        }

    }
}

private fun getInstallmentTermsString(installmentTerms: List<String>) : String {
    var string = ""
    for (term in installmentTerms){
        string += "$term,"
    }
    return string.dropLast(1)
}
