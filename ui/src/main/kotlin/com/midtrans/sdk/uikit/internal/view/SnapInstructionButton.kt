package com.midtrans.sdk.uikit.internal.view

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.view.SnapColors.link
import androidx.compose.runtime.*

@Composable
fun SnapInstructionButton(
    modifier: Modifier = Modifier,
    isExpanded: Boolean = true,
    @DrawableRes iconResId: Int,
    title: String,
    onExpandClick: (() -> Unit),
    expandingContent: @Composable (() -> Unit)? = null
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(13.dp),
            modifier = Modifier.height(19.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    painter = painterResource(id = iconResId),
                    contentDescription = null,
                    tint = SnapColors.getARGBColor(link),
                    modifier = Modifier
                        .height(16.dp)
                        .width(16.dp)
                )
                Text(
                    text = title,
                    color = SnapColors.getARGBColor(link),
                    style = SnapTypography.STYLES.snapTextMediumMedium
                )
            }
            IconButton(onClick = {
                onExpandClick.invoke()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_double_caret_vertical),
                    contentDescription = null,
                    tint = SnapColors.getARGBColor(link)
                )
            }
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

@Composable
@Preview
private fun ForPreview() {
    var isExpanded by remember {
        mutableStateOf(false)
    }
    SnapInstructionButton(
        modifier = Modifier.fillMaxWidth(1f),
        isExpanded = isExpanded,
        title = "bla bla",
        iconResId = R.drawable.ic_outline_mandiri_40,
        onExpandClick = { isExpanded = !isExpanded },
        expandingContent = {
            SnapNumberedList(list = listOf("anu", "anu", "nganu"))
        }
    )
}