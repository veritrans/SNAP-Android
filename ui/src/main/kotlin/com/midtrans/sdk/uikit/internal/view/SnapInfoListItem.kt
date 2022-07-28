package com.midtrans.sdk.uikit.internal.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

object SnapInfoListItem {
}

@Composable
@Preview
fun SnapCopyableInfoListItem(
    title: String? = null,
    info: String? = null,
    modifier: Modifier = Modifier.fillMaxWidth(1f),
    onCopyClicked: (info: String) -> Unit = {}
) {
    Column {
        title?.let {
            Text(
                text = it,
                style = SnapTypography.STYLES.snapTextSmallRegular,
                color = SnapColors.getARGBColor(SnapColors.TEXT_SECONDARY)
            )
        }

        Row() {
            info?.let {
                Text(
                    text = "1234456454324123",
                    modifier = Modifier.weight(1f),
                    color = SnapColors.getARGBColor(SnapColors.TEXT_PRIMARY),
                    style = SnapTypography.STYLES.snapTextMediumRegular
                )
            }

            Text(text = "Salin")
        }
        Divider(
            thickness = 1.dp,
            color = SnapColors.getARGBColor(SnapColors.BACKGROUND_BORDER_SOLID_SECONDARY),
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}