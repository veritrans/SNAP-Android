package com.midtrans.sdk.uikit.internal.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.midtrans.sdk.uikit.internal.view.SnapColors.SUPPORT_NEUTRAL_FILL


@Composable
fun SnapNumberedList(
    list: List<String>
){
    Column(modifier = Modifier.padding(all = 8.dp).background(color = SnapColors.getARGBColor(SUPPORT_NEUTRAL_FILL))) {
        list.forEachIndexed(){ index: Int, it: String ->
            SnapNumberedListItem(number = "${index+1}.", paragraph = it )
        }
    }
}