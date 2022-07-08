package com.midtrans.sdk.uikit.internal.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
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
                    enter = expandVertically()
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
    onExpandClick: () -> Unit
) {
    Column {
        Row {
            Text(text = "total")
            Text(text = "bayar dalam")
            Text(text = "01:59:32")
        }
        Row {
            Text(text = "rp399.000")
            IconButton(onClick = { onExpandClick.invoke() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_chevron_right),
                    contentDescription = null
                )

            }
        }
        Text(text = "Order Id #12341234213")
    }
}

