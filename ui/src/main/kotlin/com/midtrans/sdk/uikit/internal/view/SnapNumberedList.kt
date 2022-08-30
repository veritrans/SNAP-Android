package com.midtrans.sdk.uikit.internal.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.midtrans.sdk.uikit.internal.view.SnapColors.SUPPORT_NEUTRAL_FILL

@Composable
fun SnapNumberedList(
    list: List<String>
) {
    Column(
        modifier = Modifier
            .background(color = SnapColors.getARGBColor(SUPPORT_NEUTRAL_FILL))
            .padding(start = 4.dp, end = 4.dp, top = 8.dp, bottom = 8.dp)
    ) {
        list.forEachIndexed { index: Int, it: String ->
            SnapNumberedListItem(number = "${index + 1}.", paragraph = it)
        }
    }
}