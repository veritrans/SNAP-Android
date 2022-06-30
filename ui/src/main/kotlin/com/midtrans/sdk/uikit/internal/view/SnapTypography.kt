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

    companion object {
        val STYLES by lazy {
            SnapTypography()
        }
    }
}