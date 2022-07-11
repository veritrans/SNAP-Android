package com.midtrans.sdk.uikit.internal.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import com.midtrans.sdk.uikit.R

class OverlayExpanding {
}

@Composable
@Preview
fun OverlayExpandingBox(
    isExpanded: Boolean = true,
    mainContent: @Composable (() -> Unit)? = null,
    expandingContent: @Composable (() -> Unit)? = null,
    followingContent: @Composable (() -> Unit)? = null
) {

    ConstraintLayout {
        val main = createRef()
        val expanding = createRef()
        val following = createRef()
        Box(modifier = Modifier.constrainAs(main) {}) {
            mainContent?.invoke()
        }

        if (true) {
            Box(modifier = Modifier
                .constrainAs(expanding) {
                    top.linkTo(main.bottom)
                }
                .zIndex(2f)) {
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    expandingContent?.invoke()
                }
            }
        }
        Box(modifier = Modifier.constrainAs(following) {
            top.linkTo(main.bottom)
        }) {
            followingContent?.invoke()
        }
    }
}

@Composable
fun SnapTotal(
    amount: String,
    orderId: String,
    remainingTime: String,
    onExpandClick: (isExpand: Boolean) -> Unit
) {
    var isExpand by remember { mutableStateOf(false) }
    var iconExpand by remember { mutableStateOf(R.drawable.ic_chevron_down) }
    Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "total",
                style = SnapTypography.STYLES.snapTextMediumMedium,
                modifier = Modifier.weight(1f),
                color = SnapColors.getARGBColor(SnapColors.TEXT_MUTED)
            )
            Text(
                text = "bayar dalam",
                style = SnapTypography.STYLES.snapTextSmallRegular,
                color = SnapColors.getARGBColor(SnapColors.TEXT_MUTED)
            )
            Text(
                text = remainingTime,
                style = SnapTypography.STYLES.snapTextSmallRegular,
                color = SnapColors.getARGBColor(SnapColors.SUPPORT_INFO_DEFAULT)
            )
        }
        Row {
            Text(
                text = amount,
                style = SnapTypography.STYLES.snapBigNumberSemiBold,
                color = SnapColors.getARGBColor(SnapColors.TEXT_PRIMARY),
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = {
                isExpand = !isExpand
                iconExpand = if (isExpand) R.drawable.ic_chevron_up else R.drawable.ic_chevron_down
                onExpandClick.invoke(isExpand)
            }) {
                Icon(
                    painter = painterResource(id = iconExpand),
                    contentDescription = null
                )

            }
        }
        Text(
            text = orderId,
            style = SnapTypography.STYLES.snapTextSmallRegular,
            color = SnapColors.getARGBColor(SnapColors.TEXT_MUTED)
        )
    }
}

