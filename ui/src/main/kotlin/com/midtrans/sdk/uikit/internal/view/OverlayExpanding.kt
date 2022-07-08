package com.midtrans.sdk.uikit.internal.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout

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
    var _isExpanded by remember { mutableStateOf(isExpanded) }

    ConstraintLayout {
        val main = createRef()
        val expanding = createRef()
        val following = createRef()
        Box(modifier = Modifier.constrainAs(main){}){
            mainContent?.invoke()
        }

        if (_isExpanded){
            Box(modifier = Modifier.constrainAs(expanding){
                top.linkTo(main.bottom)
            }.zIndex(2f)){
                expandingContent?.invoke()
            }
        }
        Box(modifier = Modifier.constrainAs(following) {
            top.linkTo(main.bottom)
        }){
            followingContent?.invoke()
        }
    }
}