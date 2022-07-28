package com.midtrans.sdk.uikit.internal.view

import android.text.Html
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    style: SnapText.Style = SnapText.Style.MEDIUM,
    modifier: Modifier = Modifier
) {
    Text(
        text = Html.fromHtml(text).toAnnotatedString(),
        style = getTextStyle(style),
        modifier = modifier
    )
}

private fun getTextStyle(style: SnapText.Style): TextStyle{
    return when(style){
        SnapText.Style.BIG -> SnapTypography.STYLES.snapTextBigRegular
        SnapText.Style.MEDIUM -> SnapTypography.STYLES.snapTextMediumRegular
        SnapText.Style.SMALL -> SnapTypography.STYLES.snapTextSmallRegular
    }
}