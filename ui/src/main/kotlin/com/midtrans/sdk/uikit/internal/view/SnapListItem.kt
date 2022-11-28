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
fun SnapListItem(index: String, paragraph: String) {
    Row(modifier = Modifier.fillMaxWidth(fraction = 1.0f).padding(start = 8.dp, end = 8.dp)) {
        //Number or Symbol
        Text(
            text = index,
            style = SnapTypography.STYLES.snapTextMediumRegular,
            color = SnapColors.getARGBColor(SnapColors.textSecondary),
            modifier = Modifier.width(20.dp)
        )

        //Paragraph
        Text(
            text = Html.fromHtml(paragraph).toAnnotatedString(),
            style = SnapTypography.STYLES.snapTextMediumRegular,
            color = SnapColors.getARGBColor(SnapColors.textSecondary),
            modifier = Modifier.fillMaxWidth(fraction = 1.0f)
        )
    }
}

@Composable
fun SnapBulletListItem(index: String, paragraph: String) {
    Row(modifier = Modifier.padding(start = 8.dp, end = 8.dp)) {
        //Number or Symbol
        Text(
            text = index,
            style = SnapTypography.STYLES.snapTextMediumRegular,
            color = SnapColors.getARGBColor(SnapColors.textSecondary),
            modifier = Modifier.width(20.dp)
        )

        //Paragraph
        Text(
            text = Html.fromHtml(paragraph).toAnnotatedString(),
            style = SnapTypography.STYLES.snapTextMediumRegular,
            color = SnapColors.getARGBColor(SnapColors.textSecondary)
        )
    }
}