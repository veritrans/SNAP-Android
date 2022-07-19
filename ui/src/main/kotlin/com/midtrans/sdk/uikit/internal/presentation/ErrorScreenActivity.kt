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


class ErrorScreenActivity : BaseActivity() {

    private val data: ErrorData by lazy {
        intent.getParcelableExtra(EXTRA_ERROR) as? ErrorData
            ?: throw RuntimeException("Input data must not be empty")
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContent {
            ErrorContent(
                title = data.title,
                content = data.content
            )
        }
    }

    @Preview
    @Composable
    private fun ErrorContent(
        title: String = "Transaksi sudah kedaluarsa",
        content: String = "Transaksi gagal diproses karena sudah melewati batas waktu pembayaran..."
    ) {
        Column(
            modifier = Modifier.background(SnapColors.getARGBColor(SnapColors.SUPPORT_DANGER_FILL))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(1f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_exclamation),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.padding(top = 88.dp)
                )
                Text(
                    text = title,
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


                Text(
                    text = content,
                    style = SnapTypography.STYLES.snapTextMediumRegular,
                    color = SnapColors.getARGBColor(SnapColors.TEXT_SECONDARY),
                    modifier = Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp),
                    textAlign = TextAlign.Center
                )
            }

            Text(
                text = "Anda dapat menutup halaman ini",
                style = SnapTypography.STYLES.snapTextMediumMedium,
                color = SnapColors.getARGBColor(SnapColors.TEXT_PRIMARY),
                modifier = Modifier.fillMaxWidth(1f).padding(28.dp),
                textAlign = TextAlign.Center
            )
        }
    }

    companion object {
        private const val EXTRA_ERROR = "EXTRA_ERROR"
        fun getIntent(
            activityContext: Context,
            title: String,
            content: String
        ): Intent {
            return Intent(activityContext, SuccessScreenActivity::class.java).apply {
                putExtra(
                    EXTRA_ERROR, ErrorData(
                        title = title,
                        content = content
                    )
                )
            }
        }
    }

    @Parcelize
    private data class ErrorData(
        val title: String,
        val content: String
    ) : Parcelable
}