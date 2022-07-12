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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

object SnapPromoListRadioButton {
}

@Composable
fun SnapPromoListRadioButton(
    states: List<PromoData>,
    onItemSelectedListener: (promodata: PromoData) -> Unit
) {
    val (selectedOption, onOptionSelected) = remember {
        mutableStateOf(states[0].leftText)
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        states.forEach { item ->
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val enabled by item.enabled
                Row(
                    modifier = if (enabled) Modifier.selectable(
                        selected = (item.leftText == selectedOption),
                        onClick = {
                            onOptionSelected(item.leftText)
                            onItemSelectedListener(item)
                        },
                        role = Role.RadioButton
                    ) else Modifier,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    RadioButton(
                        selected = item.leftText == selectedOption,
                        onClick = null,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = if (enabled) Color.Black else SnapColors.getARGBColor(
                                SnapColors.ICON_MUTED
                            )
                        ),
                        enabled = enabled
                    )
                    SnapPromoListRadioButtonItem(promoData = item)
                }
            }
        }
    }
}

@Composable
fun SnapPromoListRadioButtonItem(promoData: PromoData) {
    val enabled by promoData.enabled
    Column {
        Row() {
            Text(
                text = promoData.leftText,
                modifier = Modifier.weight(1f),
                style = SnapTypography.STYLES.snapTextMediumRegular,
                color = SnapColors.getARGBColor(if (enabled) SnapColors.TEXT_PRIMARY else SnapColors.TEXT_MUTED)
            )
            Text(
                text = promoData.rightText,
                style = SnapTypography.STYLES.snapTextMediumRegular,
                color = SnapColors.getARGBColor(if (enabled) SnapColors.TEXT_PRIMARY else SnapColors.TEXT_MUTED)
            )
        }
        promoData.subLeftText?.let {
            Text(
                text = it,
                style = SnapTypography.STYLES.snapTextSmallRegular,
                color = SnapColors.getARGBColor(if (enabled) SnapColors.TEXT_PRIMARY else SnapColors.TEXT_MUTED)
            )
        }

    }
}

data class PromoData(
    val leftText: String,
    val rightText: String,
    val subLeftText: String? = null,
    var enabled: MutableState<Boolean> = mutableStateOf(true)
)