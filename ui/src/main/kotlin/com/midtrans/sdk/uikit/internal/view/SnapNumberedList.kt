package com.midtrans.sdk.uikit.internal.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.midtrans.sdk.uikit.internal.view.SnapColors.supportNeutralFill

@Composable
fun SnapNumberedList(
    list: List<String>
) {
    Column(modifier = Modifier.background(color = SnapColors.getARGBColor(supportNeutralFill))) {
        list.forEachIndexed { index: Int, it: String ->
            SnapListItem(index = "${index + 1}.", paragraph = it)
        }
    }
}