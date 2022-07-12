package com.midtrans.sdk.uikit.internal.view

import android.text.Html
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SnapNumberedListItem(number: String, paragraph: String) {
    Row(modifier = Modifier.fillMaxWidth(fraction = 1.0f).padding(8.dp)) {
        //Number
        Text(
            text = number,
            style = SnapTypography.STYLES.snapTextMediumRegular,
            color = SnapColors.getARGBColor(SnapColors.TEXT_SECONDARY),
            modifier = Modifier.width(20.dp)
        )

        //Paragraph
        Text(
            text = Html.fromHtml(paragraph).toAnnotatedString(),
            style = SnapTypography.STYLES.snapTextMediumRegular,
            color = SnapColors.getARGBColor(SnapColors.TEXT_SECONDARY),
            modifier = Modifier.fillMaxWidth(fraction = 1.0f)
        )
    }
}