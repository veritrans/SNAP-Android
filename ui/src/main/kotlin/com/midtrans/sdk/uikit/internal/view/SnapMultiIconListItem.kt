package com.midtrans.sdk.uikit.internal.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
            .background(Color.Yellow)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {

            Text(
                text = title,
                modifier = Modifier.padding(top = 16.dp, bottom = 13.dp),
                style = SnapTypography.STYLES.snapTextBig
            )
            Row {
                iconList.forEach {
                    Row(
                        modifier = Modifier.padding(end = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Icon(
                            painter = painterResource(id = it),
                            tint = Color.Unspecified,
                            contentDescription = null,
                            modifier = Modifier.border(1.dp, Color.Black)
                        )
                    }
                }
            }
            Divider(
                thickness = 1.dp,
                color = SnapColors.getARGBColor(LINE_LIGHT_MUTED),
                modifier = Modifier.padding(top = 21.dp)
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.ic_chevron_right),
            tint = Color.Unspecified,
            contentDescription = null,
        )
    }
}