package com.midtrans.sdk.uikit.internal.view

import androidx.annotation.DrawableRes
import androidx.annotation.IntegerRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

object SnapAppBar {
}

@Composable
fun SnapAppBar(
    title: String,
    @DrawableRes
    iconResId: Int,
    onClick: () -> Unit
){
    Row (verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Start,
    modifier = Modifier.height(64.dp).background(color = Color.Cyan).fillMaxWidth(fraction = 1.0f)){
        IconButton(onClick = onClick,
        modifier = Modifier.mar) {
            Icon(
                painter = painterResource(id = iconResId),
                tint = Color.Unspecified,
                contentDescription = null,
            )
        }

        Text(text = title,
            style = SnapTypography.STYLES.snapAppBar
        )
    }
}
