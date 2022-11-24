package com.midtrans.sdk.uikit.internal.view

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.midtrans.sdk.uikit.internal.util.UiKitConstants

@Composable
fun SnapBulletedList(
    list: List<String>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        list.forEach {
            SnapListItem(index = UiKitConstants.BULLET_LIST, paragraph = it)
        }
    }
}