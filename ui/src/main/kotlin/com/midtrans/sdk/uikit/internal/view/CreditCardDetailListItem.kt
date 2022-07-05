package com.midtrans.sdk.uikit.internal.view

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import com.midtrans.sdk.uikit.R

object CreditCardDetailListItem {
}

@Composable
fun SnapCCDetailListItem(
    shouldReveal: Boolean,
    onValueChange: (String) -> Unit,
    onRemoveClicked: () -> Unit
) {
    Log.e("wahyu", "shouldReveal + $shouldReveal")
    Column() {
        Row() {
            Icon(
                painter = painterResource(id = R.drawable.ic_chevron_right),
                contentDescription = null,
                tint = Color.Unspecified
            )
            Text(
                text = "dfdfdfdff",
                modifier = Modifier.weight(weight = 1.0f)
            )
            IconButton(onClick = { }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bri),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }
        }
        Column() {
            var text by remember { mutableStateOf("") }
            AnimatedVisibility(
                visible = shouldReveal,
                enter = fadeIn(
                    initialAlpha = 0.4f
                ),
//                exit = slideIn {  }(
//                    animationSpec = tween(durationMillis = 250)
//                )
            ) {
                Text(text = "fdfdfd")
                TextField(value = text, enabled = true, readOnly = false, onValueChange = {
                    text = it
                    onValueChange(it)
                })
            }

        }
    }
}

@Composable
fun CcRadioGroup(
    states: List<String>,
    onValueChange: (item: String, cvv: String) -> Unit,
    onItemRemoveClicked: (item: String) -> Unit
) {
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(states[0]) }

    Column() {
        states.forEach { item ->
            Row(
                modifier = Modifier.selectable(
                    selected = (item == selectedOption),
                    onClick = {
                        onOptionSelected(item)
                    },
                    role = Role.RadioButton
                )
            ) {
                RadioButton(
                    selected = item == selectedOption,
                    onClick = null
                )
                SnapCCDetailListItem(item == selectedOption,
                    { onValueChange(selectedOption, it) }, { onItemRemoveClicked(item) }
                )
            }
        }
    }
}

