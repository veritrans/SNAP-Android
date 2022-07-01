package com.midtrans.sdk.uikit.internal.view

import android.text.Html
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle

object SnapText {
    enum class Style {
        BIG,
        MEDIUM,
        SMALL
    }
}


@Composable
fun SnapText(
    text: String,
    style: SnapText.Style = SnapText.Style.MEDIUM
) {
    Text(
        text = Html.fromHtml(text).toAnnotatedString(),
        style = getTextStyle(style)
    )
}

private fun getTextStyle(style: SnapText.Style): TextStyle{
    return when(style){
        SnapText.Style.BIG -> SnapTypography.STYLES.snapTextBig
        SnapText.Style.MEDIUM -> SnapTypography.STYLES.snapTextMedium
        SnapText.Style.SMALL -> SnapTypography.STYLES.snapTextSmall
    }
}