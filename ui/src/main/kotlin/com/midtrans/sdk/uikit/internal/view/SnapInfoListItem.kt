package com.midtrans.sdk.uikit.internal.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.midtrans.sdk.uikit.R
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

object SnapInfoListItem {
}

@Composable
fun SnapCopyableInfoListItem(
    title: String? = null,
    info: String? = null,
    copied: Boolean = false,
    modifier: Modifier = Modifier.fillMaxWidth(1f),
    onCopyClicked: (info: String) -> Unit = {}
) {
    Column {
        title?.let {
            Text(
                text = it,
                style = SnapTypography.STYLES.snapTextSmallRegular,
                color = SnapColors.getARGBColor(SnapColors.TEXT_SECONDARY)
            )
        }

        Row {
            info?.let {
                Text(
                    text = it,
                    modifier = Modifier.weight(1f),
                    color = SnapColors.getARGBColor(SnapColors.TEXT_PRIMARY),
                    style = SnapTypography.STYLES.snapTextMediumRegular
                )
            }


            androidx.compose.animation.AnimatedVisibility(
                visible = copied,
                enter = fadeIn(),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(id = R.string.general_instruction_copied_icon_text),
                        modifier = Modifier.clickable(onClick = { onCopyClicked(info.orEmpty()) }),
                        color = SnapColors.getARGBColor(SnapColors.INTERACTIVE_FILL_BRAND_DEFAULT),
                        style = SnapTypography.STYLES.snapTextMediumMedium
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.ic_tick_circle),
                        contentDescription = null,
                        tint = SnapColors.getARGBColor(SnapColors.INTERACTIVE_FILL_BRAND_DEFAULT),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }


            if (!copied) {
                Text(
                    text = stringResource(id = R.string.general_instruction_copy_icon_text),
                    modifier = Modifier.clickable(onClick = { onCopyClicked(info.orEmpty()) }),
                    color = SnapColors.getARGBColor(SnapColors.LINK),
                    style = SnapTypography.STYLES.snapTextMediumMedium
                )
            }

        }
        Divider(
            thickness = 1.dp,
            color = SnapColors.getARGBColor(SnapColors.BACKGROUND_BORDER_SOLID_SECONDARY),
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Preview
@Composable
private fun ForPreview() {
    var copied by remember {
        mutableStateOf(false)
    }
    SnapCopyableInfoListItem(
        title = "ada info apa",
        info = "inilah infonya",
        copied = copied,
        onCopyClicked = { label -> copied = true }
    )
}