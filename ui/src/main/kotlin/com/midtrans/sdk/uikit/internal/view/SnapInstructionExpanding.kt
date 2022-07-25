package com.midtrans.sdk.uikit.internal.view

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.midtrans.sdk.uikit.R

@Composable
fun SnapInstructionExpanding(
    isExpanded: Boolean = true,
    @DrawableRes iconResId: Int,
    title: String,
    expandingContent: @Composable (() -> Unit)? = null
) {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(13.5.dp),
            modifier = Modifier.height(19.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .height(19.dp)
            ) {
                Icon(
                    painter = painterResource(id = iconResId),
                    contentDescription = null
                )
                Text(
                    text = title,
                    style = SnapTypography.STYLES.snapTextMediumMedium
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.ic_double_caret_vertical),
                contentDescription = null
            )
        }
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            expandingContent?.invoke()
        }
    }
}