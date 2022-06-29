package com.midtrans.sdk.uikit.internal.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object SnapButtonAttribute {
    enum class Style {
        PRIMARY,
        TERTIARY
    }
}

@Composable
fun SnapButton( //TODO border disabled/enabled color, font, font size, corner radius
    enabled: Boolean = true,
    style: SnapButtonAttribute.Style = SnapButtonAttribute.Style.PRIMARY,
    text: String,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(2.dp),
        border = selectBorderStroke(style),
        colors = selectButtonColors(style),
        content = { Text(
            fontFamily = getPoppinsFontFamily(),
            text = text
        ) }
    )
}

@Composable
private fun selectButtonColors(style: SnapButtonAttribute.Style): ButtonColors {
    return when (style) {
        SnapButtonAttribute.Style.PRIMARY -> ButtonDefaults.buttonColors(
            backgroundColor = SnapColors.getARGBColor(SnapColors.INTERACTIVE_FILL_INVERSE),
            contentColor = SnapColors.getARGBColor(SnapColors.TEXT_INVERSE),
            disabledContentColor = SnapColors.getARGBColor(SnapColors.TEXT_DISABLED),
            disabledBackgroundColor = SnapColors.getARGBColor(SnapColors.INTERACTIVE_DISABLED)
        )
        SnapButtonAttribute.Style.TERTIARY -> ButtonDefaults.buttonColors(
            backgroundColor = Color(SnapColors.TRANSPARENT),
            contentColor = SnapColors.getARGBColor(SnapColors.TEXT_PRIMARY),
            disabledContentColor = SnapColors.getARGBColor(SnapColors.TEXT_DISABLED),
            disabledBackgroundColor = Color(SnapColors.TRANSPARENT)
        )
    }
}

@Composable
private fun selectBorderStroke(style: SnapButtonAttribute.Style): BorderStroke {
    return when (style) {
        SnapButtonAttribute.Style.PRIMARY -> BorderStroke(
            ButtonDefaults.OutlinedBorderSize, SnapColors.getARGBColor(SnapColors.INTERACTIVE_FILL_INVERSE)
        )
        SnapButtonAttribute.Style.TERTIARY -> BorderStroke(
            ButtonDefaults.OutlinedBorderSize, SnapColors.getARGBColor(SnapColors.INTERACTIVE_BORDER_ACTION)
        )
    }
}







