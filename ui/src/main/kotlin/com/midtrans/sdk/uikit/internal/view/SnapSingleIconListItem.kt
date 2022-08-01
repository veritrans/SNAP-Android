package com.midtrans.sdk.uikit.internal.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

object SnapSingleIconListItem {
}

@Composable
fun SnapSingleIconListItem(
    title: String,
    @DrawableRes iconResId: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.then(Modifier.height(88.dp))) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
        ) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                tint = Color.Unspecified
            )

            Text(
                text = title,
                color = SnapColors.getARGBColor(SnapColors.TEXT_PRIMARY),
                style = SnapTypography.STYLES.snapTextBigRegular
            )
        }
        Divider(
            thickness = 1.dp,
            color = SnapColors.getARGBColor(SnapColors.LINE_LIGHT_MUTED)
        )
    }
}
