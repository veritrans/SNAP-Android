package com.midtrans.sdk.uikit.internal.view

import androidx.annotation.DrawableRes
import androidx.annotation.IntegerRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource

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
    horizontalArrangement = Arrangement.Center){
        IconButton(onClick = onClick) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = null
            )
        }

        Text(text = title,
            style = SnapTypography.STYLES.snapAppBar
        )
    }
}
