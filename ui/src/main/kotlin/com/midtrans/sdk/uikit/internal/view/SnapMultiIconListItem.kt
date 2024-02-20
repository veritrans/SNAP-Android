package com.midtrans.sdk.uikit.internal.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.midtrans.sdk.corekit.api.model.CreditCard
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.view.SnapColors.lineLightMuted

@Composable
fun SnapMultiIconListItem(
    title: String,
    iconList: List<Int>,
    creditCard: CreditCard?,
    paymentType: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {

            if (paymentType == PaymentType.CREDIT_CARD && !creditCard?.savedTokens.isNullOrEmpty()){
                Text(
                    text = title,
                    modifier = Modifier.padding(top = 16.dp),
                    style = SnapTypography.STYLES.snapTextBigRegular
                )
                Text(
                    text = "${creditCard?.savedTokens?.count()} " + stringResource(id = R.string.payment_summary_saved_card),
                    modifier = Modifier.padding(top = 2.5.dp, bottom = 4.dp),
                    style = SnapTypography.STYLES.snapTextSmallRegular,
                    color = SnapColors.getARGBColor(SnapColors.textSecondary)
                )
            } else {
                Text(
                    text = title,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                    style = SnapTypography.STYLES.snapTextBigRegular
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(space = 8.dp, alignment = Alignment.Start)
            ) {
                iconList.forEach {
                    Icon(
                        painter = painterResource(id = it),
                        tint = Color.Unspecified,
                        contentDescription = null
                    )
                }
            }
            Divider(
                thickness = 1.dp,
                color = SnapColors.getARGBColor(lineLightMuted),
                modifier = Modifier.padding(top = 16.dp)
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.ic_chevron_right),
            tint = Color.Unspecified,
            contentDescription = null,
        )
    }
}

@Preview
@Composable
private fun Preview() {
    SnapMultiIconListItem(title = "Credit Card", iconList = listOf(
        R.drawable.ic_outline_permata_40,
        R.drawable.ic_outline_bca_40,
        R.drawable.ic_outline_bni_40,
        R.drawable.ic_outline_bri_40,
        R.drawable.ic_outline_mandiri_40,
        R.drawable.ic_outline_cimb_40

    ) , creditCard = CreditCard(),
        paymentType = "credit_card") {
    }
}