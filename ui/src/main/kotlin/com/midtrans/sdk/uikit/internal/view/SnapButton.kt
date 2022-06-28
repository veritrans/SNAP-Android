package com.midtrans.sdk.uikit.internal.view

import androidx.annotation.ColorRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

object SnapButton {

    enum class Style {
        PRIMARY,
        TERTIARY
    }

    enum class  State{
        ENABLED,
        DISABLED
    }

    @Composable
    fun SnapButton(
        style: Style = Style.PRIMARY,
        state: State = State.ENABLED,
        text: String,
        onClick: () -> Unit
    ) { OutlinedButton(
            onClick= onClick,
//            modifier = null,
            enabled = true,
            shape: Shape = MaterialTheme.shapes.small,
            border: BorderStroke? = ButtonDefaults.outlinedBorder,
            colors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
            contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
        )
    }

    @Composable
    private fun selectBackgroundColor(style: Style): ButtonColors {
        return when(style){
            Style.PRIMARY -> buttonColors(
                backgroundColor = Color(SnapColors.INTERACTIVE_FILL_INVERSE),
                contentColor = Color(SnapColors.TEXT_INVERSE),
                disabledContentColor = Color(SnapColors.TEXT_DISABLED),
                disabledBackgroundColor = Color(SnapColors.INTERACTIVE_DISABLED)
            )
            Style.TERTIARY -> buttonColors(
                backgroundColor = Color(SnapColors.INTERACTIVE_FILL_DEFAULT),
                contentColor = Color(SnapColors.TEXT_PRIMARY),
                disabledContentColor = Color(SnapColors.TEXT_DISABLED),
                disabledBackgroundColor = Color(SnapColors.INTERACTIVE_DISABLED)
            )
        }
    }


}