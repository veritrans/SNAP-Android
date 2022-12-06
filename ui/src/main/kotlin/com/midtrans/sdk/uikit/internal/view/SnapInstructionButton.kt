package com.midtrans.sdk.uikit.internal.view

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import com.midtrans.sdk.corekit.api.model.PaymentType


@Composable
fun SnapInstruction(
    paymentType: String,
    isInstructionExpanded: Boolean,
    onExpandClick: (() -> Unit)
) {
    Column(
        modifier = Modifier
            .verticalScroll(state = rememberScrollState())
            .padding(top = 16.dp)
            .fillMaxWidth()

    ) {
        SnapText(stringResource(getInstructionId(paymentType = paymentType)))
        SnapInstructionButton(
            modifier = Modifier.padding(top = 28.dp),
            isExpanded = isInstructionExpanded,
            iconResId = R.drawable.ic_help,
            title = stringResource(R.string.payment_instruction_how_to_pay_title),
            onExpandClick = {
                onExpandClick.invoke()
            },
            expandingContent = {
                Column {
                    AnimatedVisibility(visible = isInstructionExpanded) {
                        SnapNumberedList(
                            list = stringArrayResource(getHowToPayId(paymentType = paymentType)).toList()
                        )
                    }
                }
            }
        )
    }
}

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

private fun getHowToPayId(paymentType: String): Int {
    return when (paymentType) {
        PaymentType.AKULAKU -> R.array.akulaku_how_to_pay
        else -> 0
    }
}

private fun getInstructionId(paymentType: String): Int {
    return when (paymentType) {
        PaymentType.AKULAKU -> R.string.akulaku_instruction
        else -> 0
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