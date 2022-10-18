package com.midtrans.sdk.uikit.internal.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object SnapButton {
    enum class Style {
        PRIMARY,
        TERTIARY
    }
}

@Composable
fun SnapButton( //TODO border disabled/enabled color, font, font size, corner radius
    enabled: Boolean = true,
    style: SnapButton.Style = SnapButton.Style.PRIMARY,
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(4.dp),
        border = selectBorderStroke(style),
        colors = selectButtonColors(style),
        modifier = modifier,
        content = {
            Text(
                style = SnapTypography.STYLES.snapButton,
                text = text
            )
        }
    )
}

@Composable
private fun selectButtonColors(style: SnapButton.Style): ButtonColors {
    return when (style) {
        SnapButton.Style.PRIMARY -> ButtonDefaults.buttonColors(
            backgroundColor = SnapColors.getARGBColor(SnapColors.interactiveFillInverse),
            contentColor = SnapColors.getARGBColor(SnapColors.textInverse),
            disabledContentColor = SnapColors.getARGBColor(SnapColors.textDisabled),
            disabledBackgroundColor = SnapColors.getARGBColor(SnapColors.interactiveFillInverse)
        )
        SnapButton.Style.TERTIARY -> ButtonDefaults.buttonColors(
            backgroundColor = Color(SnapColors.transparent),
            contentColor = SnapColors.getARGBColor(SnapColors.textPrimary),
            disabledContentColor = SnapColors.getARGBColor(SnapColors.textDisabled),
            disabledBackgroundColor = Color(SnapColors.transparent)
        )
    }
}

@Composable
private fun selectBorderStroke(style: SnapButton.Style): BorderStroke {
    return when (style) {
        SnapButton.Style.PRIMARY -> BorderStroke(
            ButtonDefaults.OutlinedBorderSize,
            SnapColors.getARGBColor(SnapColors.interactiveFillInverse)
        )
        SnapButton.Style.TERTIARY -> BorderStroke(
            ButtonDefaults.OutlinedBorderSize,
            SnapColors.getARGBColor(SnapColors.interactiveBorderAction)
        )
    }
}







