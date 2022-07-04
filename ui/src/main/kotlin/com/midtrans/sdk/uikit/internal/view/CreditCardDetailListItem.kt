package com.midtrans.sdk.uikit.internal.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.LiveData
import com.midtrans.sdk.uikit.R

object CreditCardDetailListItem {
}

@Composable
fun SnapCCDetailListItem(state: Boolean){
    var ccvVisibility = state

    Column() {
        Row() {
            Icon(
                painter = painterResource(id = R.drawable.ic_chevron_right),
                contentDescription = null,
                tint = Color.Unspecified
            )
            Text(text = "dfdfdfdff",
            modifier = Modifier.weight(weight = 1.0f))
            IconButton(onClick = { }) {
                Icon(painter = painterResource(id = R.drawable.ic_bri), contentDescription = null, tint = Color.Unspecified)
            }
        }
        Column() {
            AnimatedVisibility(
                visible = ccvVisibility,
                enter = fadeIn(
                    initialAlpha = 0.4f
                ),
                exit = fadeOut(
                    animationSpec = tween(durationMillis = 250)
                )
            ) {
                Text(text = "dffdfdf")
                TextField(value = "", onValueChange = {})
            }

        }
    }
}