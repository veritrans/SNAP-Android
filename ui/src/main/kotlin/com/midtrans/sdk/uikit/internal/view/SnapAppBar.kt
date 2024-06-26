package com.midtrans.sdk.uikit.internal.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.midtrans.sdk.uikit.internal.view.SnapColors.backgroundFillLight

@Composable
fun SnapAppBar(
    title: String,
    @DrawableRes
    iconResId: Int,
    rightIconRestId: Int? = null,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .height(64.dp)
            .background(color = SnapColors.getARGBColor(backgroundFillLight))
            .fillMaxWidth(fraction = 1.0f)
    ) {
        IconButton(onClick = onClick) {
            Icon(
                painter = painterResource(id = iconResId),
                tint = Color.Unspecified,
                contentDescription = null
            )
        }

        Text(
            text = title,
            style = SnapTypography.STYLES.snapAppBar,
            color = SnapColors.getARGBColor(SnapColors.textPrimary)
        )

        rightIconRestId?.let {
            Icon(
                painter = painterResource(id = it),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }
    }
}
