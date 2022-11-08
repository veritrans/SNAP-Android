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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.util.CurrencyFormat.currencyFormatRp

@Composable
@Preview
fun SnapOverlayExpandingBox(
    modifier: Modifier = Modifier,
    isExpanded: Boolean = true,
    mainContent: @Composable (() -> Unit)? = null,
    expandingContent: @Composable (() -> Unit)? = null,
    followingContent: @Composable (() -> Unit)? = null
) {

    ConstraintLayout(
        modifier = modifier
    ) {
        val main = createRef()
        val expanding = createRef()
        val following = createRef()
        Box(modifier = Modifier.constrainAs(main) {
            top.linkTo(parent.top)
        }) {
            mainContent?.invoke()
        }

        Box(modifier = Modifier
            .constrainAs(expanding) {
                top.linkTo(main.bottom)
            }
            .background(SnapColors.getARGBColor(SnapColors.overlayWhite))
            .padding(top = 24.dp)
            .zIndex(2f)) {
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                expandingContent?.invoke()
            }
        }

        Box(modifier = Modifier.constrainAs(following) {
            top.linkTo(main.bottom)
            bottom.linkTo(parent.bottom)
            height = Dimension.fillToConstraints
        }) {
            followingContent?.invoke()
        }
    }
}

@Composable
fun SnapTotal(
    amount: String,
    orderId: String,
    remainingTime: String?,
    canExpand: Boolean,
    isPromo: Boolean = false,
    onExpandClick: (isExpand: Boolean) -> Unit
) {
    var isExpand by remember { mutableStateOf(false) }
    var iconExpand by remember { mutableStateOf(R.drawable.ic_chevron_down) }
    Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = stringResource(id = R.string.payment_summary_total),
                style = SnapTypography.STYLES.snapTextMediumMedium,
                modifier = Modifier.weight(1f),
                color = SnapColors.getARGBColor(SnapColors.textMuted)
            )
            remainingTime?.let {
                Text(
                    text = stringResource(id = R.string.cc_dc_main_screen_remaining_payment_time),
                    style = SnapTypography.STYLES.snapTextSmallRegular,
                    color = SnapColors.getARGBColor(SnapColors.textMuted)
                )
                Text(
                    text = remainingTime,
                    style = SnapTypography.STYLES.snapTextSmallRegular,
                    color = SnapColors.getARGBColor(SnapColors.supportInfoDefault)
                )
            }
        }
        Row {
            Text(
                text = amount,
                style = SnapTypography.STYLES.snapBigNumberSemiBold,
                color = SnapColors.getARGBColor(SnapColors.textPrimary)
            )
            if(isPromo){
                Icon(
                    modifier = Modifier.padding(start = 8.dp, top = 8.dp),
                    painter = painterResource(id = R.drawable.ic_promo),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }
            Box(modifier = Modifier.weight(1f))
            if (canExpand) {
                IconButton(onClick = {
                    isExpand = !isExpand
                    iconExpand =
                        if (isExpand) R.drawable.ic_chevron_up else R.drawable.ic_chevron_down
                    onExpandClick.invoke(isExpand)
                }) {
                    Icon(
                        painter = painterResource(id = iconExpand),
                        contentDescription = null
                    )

                }
            }
        }
        Text(
            text = String.format(stringResource(id = R.string.payment_summary_order_id)+"%s", orderId),
            style = SnapTypography.STYLES.snapTextSmallRegular,
            color = SnapColors.getARGBColor(SnapColors.textMuted)
        )
    }
}

@Composable
fun SnapCustomerDetail(
    name: String,
    phone: String,
    addressLines: List<String>
) {
    Column(
        modifier = Modifier.background(color = SnapColors.getARGBColor(SnapColors.backgroundFillPrimary))
    ) {
        Text(
            text = stringResource(id = R.string.payment_details_customer_detail_title),
            style = SnapTypography.STYLES.snapTextSmallRegular,
            color = SnapColors.getARGBColor(SnapColors.textPrimary)
        )
        Row(modifier = Modifier.padding(top = 12.dp)) {
            Text(
                text = name,
                style = SnapTypography.STYLES.snapTextSmallRegular,
                color = SnapColors.getARGBColor(SnapColors.textSecondary),
                modifier = Modifier.weight(1f)
            )
            Text(
                text = phone, style = SnapTypography.STYLES.snapTextSmallRegular,
                color = SnapColors.getARGBColor(SnapColors.textSecondary)
            )
        }
        addressLines.forEach {
            Text(
                text = it, style = SnapTypography.STYLES.snapTextSmallRegular,
                color = SnapColors.getARGBColor(SnapColors.textSecondary)
            )
        }
    }
}

@Composable
fun SnapItemDetail(
    quantity: Int,
    itemName: String,
    price: Double
) {
    Column(
        modifier = Modifier.background(color = SnapColors.getARGBColor(SnapColors.backgroundFillPrimary))
    ) {
        Text(
            text = stringResource(id = R.string.payment_details_title),
            style = SnapTypography.STYLES.snapTextSmallRegular,
            color = SnapColors.getARGBColor(SnapColors.textPrimary)
        )
        Row(modifier = Modifier.padding(top = 12.dp)) {
            Text(
                text = "$quantity $itemName",
                style = SnapTypography.STYLES.snapTextSmallRegular,
                color = SnapColors.getARGBColor(SnapColors.textSecondary),
                modifier = Modifier.weight(1f)
            )
            Text(
                text = price.currencyFormatRp(),
                style = SnapTypography.STYLES.snapTextSmallRegular,
                color = SnapColors.getARGBColor(SnapColors.textSecondary)
            )
        }
    }
}

