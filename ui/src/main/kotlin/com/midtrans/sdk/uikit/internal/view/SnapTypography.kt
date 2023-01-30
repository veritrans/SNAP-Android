package com.midtrans.sdk.uikit.internal.view

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

class SnapTypography private constructor(){
    val snapButton get() = TextStyle(
        fontFamily = getPoppinsFontFamily(),
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal
    )

    val snapAppBar get() = TextStyle(
        fontFamily = getRobotoFontFamily(),
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold
    )

    val snapBigNumberSemiBold get() = TextStyle(
        fontFamily = getPoppinsFontFamily(),
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold
    )

    val snapTextBigRegular get() = TextStyle(
        fontFamily = getPoppinsFontFamily(),
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal
    )

    val snapTextMediumRegular get() = TextStyle(
        fontFamily = getPoppinsFontFamily(),
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal
    )

    val snaTextBodySmall = snapTextMediumRegular

    val snapTextSmallRegular get() = TextStyle(
        fontFamily = getPoppinsFontFamily(),
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal
    )

    val snapTextMediumMedium get() = TextStyle(
        fontFamily = getPoppinsFontFamily(),
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold
    )

    val snapTextLabelMedium get() = TextStyle(
        fontFamily = getPoppinsFontFamily(),
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium
    )

    val snapHeadingL get() = TextStyle(
        fontFamily = getPoppinsFontFamily(),
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold
    )

    val snapHeading2Xl get() = TextStyle(
        fontFamily = getPoppinsFontFamily(),
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold
    )
    companion object {
        val STYLES by lazy {
            SnapTypography()
        }
    }
}
