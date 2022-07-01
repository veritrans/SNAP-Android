package com.midtrans.sdk.uikit.internal.view

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

class SnapTypography private constructor(){
    val snapButton = TextStyle(
        fontFamily = getPoppinsFontFamily(),
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal
    )

    val snapAppBar = TextStyle(
        fontFamily = getRobotoFontFamily(),
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold
    )

    val snapBigNumber = TextStyle(
        fontFamily = getPoppinsFontFamily(),
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold
    )

    val snapTextBig = TextStyle(
        fontFamily = getPoppinsFontFamily(),
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal
    )

    val snapTextMedium = TextStyle(
        fontFamily = getPoppinsFontFamily(),
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal
    )

    val snapTextSmall = TextStyle(
        fontFamily = getPoppinsFontFamily(),
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal
    )
    companion object {
        val STYLES by lazy {
            SnapTypography()
        }
    }
}