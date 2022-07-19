package com.midtrans.sdk.uikit.internal.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.os.PersistableBundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.midtrans.sdk.corekit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.view.SnapButton
import com.midtrans.sdk.uikit.internal.view.SnapColors
import com.midtrans.sdk.uikit.internal.view.SnapTypography
import kotlinx.android.parcel.Parcelize


class SuccessScreenActivity : BaseActivity() {

    private val data: SuccessData by lazy {
        intent.getParcelableExtra(EXTRA_TOTAL) as? SuccessData
            ?: throw RuntimeException("Input data must not be empty")
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContent {
            SuccessContent(
                total = data.total,
                orderId = data.orderId,
                isWithBackButton = !data.total.isNullOrEmpty() && !data.orderId.isNullOrEmpty()
            )
        }
    }

    @Preview
    @Composable
    private fun forPreview() {
        SuccessContent(
            total = "Rp399.000",
            orderId = "Order ID #333333333",
            isWithBackButton = true
        )
    }

    @Composable
    private fun SuccessContent(
        total: String?,
        orderId: String?,
        isWithBackButton: Boolean
    ) {
        Column(
            modifier = Modifier.background(SnapColors.getARGBColor(SnapColors.SUPPORT_SUCCESS_FILL))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(1f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_success),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.padding(top = if (isWithBackButton) 80.dp else 125.dp)
                )
                Text(
                    text = "Pembayaran berhasil!",
                    style = SnapTypography.STYLES.snapHeadingL,
                    color = SnapColors.getARGBColor(SnapColors.TEXT_SECONDARY),
                    modifier = Modifier.padding(top = 24.dp)
                )
                Divider(
                    thickness = 2.dp,
                    color = SnapColors.getARGBColor(SnapColors.SUPPORT_SUCCESS_DEFAULT),
                    modifier = Modifier
                        .width(40.dp)
                        .padding(top = 24.dp),
                )

                total?.let {
                    Text(
                        text = total,
                        style = SnapTypography.STYLES.snapHeading2Xl,
                        color = SnapColors.getARGBColor(SnapColors.TEXT_SECONDARY),
                        modifier = Modifier.padding(top = 24.dp)
                    )
                }

                orderId?.let {
                    Text(
                        text = orderId,
                        style = SnapTypography.STYLES.snapTextMediumRegular,
                        color = SnapColors.getARGBColor(SnapColors.TEXT_SECONDARY),
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }
            if (isWithBackButton) {
                SnapButton(
                    text = "Kembali ke merchant",
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(16.dp)
                ) {
                    finish()
                }
            } else {
                Text(
                    text = "Anda dapat menutup halaman ini",
                    style = SnapTypography.STYLES.snapTextMediumMedium,
                    color = SnapColors.getARGBColor(SnapColors.TEXT_PRIMARY),
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(28.dp),
                    textAlign = TextAlign.Center
                )
            }

        }
    }

    companion object {
        private const val EXTRA_TOTAL = "EXTRA_TOTAL"
        fun getIntent(
            activityContext: Context,
            total: String?,
            orderId: String?
        ): Intent {
            return Intent(activityContext, SuccessScreenActivity::class.java).apply {
                putExtra(
                    EXTRA_TOTAL, SuccessData(
                        total = total,
                        orderId = orderId
                    )
                )
            }
        }
    }

    @Parcelize
    private data class SuccessData(
        val total: String?,
        val orderId: String?
    ) : Parcelable
}