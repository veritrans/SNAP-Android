package com.midtrans.sdk.uikit.internal.view

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.view.SnapColors.LINE_LIGHT_MUTED

@Composable
fun SnapMultiIconListItem(
    title: String,
    iconList: List<Int>,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {

            Text(
                text = title,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                style = SnapTypography.STYLES.snapTextBigRegular
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(space = 8.dp, alignment = Alignment.Start)
            ) {
                iconList.forEach {
                        Icon(
                            painter = painterResource(id = it),
                            tint = Color.Unspecified,
                            contentDescription = null
                        )
                }
            }
            Divider(
                thickness = 1.dp,
                color = SnapColors.getARGBColor(LINE_LIGHT_MUTED),
                modifier = Modifier.padding(top = 16.dp)
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.ic_chevron_right),
            tint = Color.Unspecified,
            contentDescription = null,
        )
    }
}