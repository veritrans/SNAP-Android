package com.midtrans.sdk.uikit.internal.presentation.statusscreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
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
import com.midtrans.sdk.uikit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.view.SnapColors
import com.midtrans.sdk.uikit.internal.view.SnapTypography
import kotlinx.parcelize.Parcelize


class ErrorScreenActivity : BaseActivity() {

    private val data: ErrorData by lazy {
        intent.getParcelableExtra(EXTRA_ERROR) as? ErrorData
            ?: throw RuntimeException("Input data must not be empty")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ErrorContent(
                title = data.title,
                content = data.content,
                instruction = data.instruction,
                tokenId = data.tokenId
            )
        }
    }

    @Preview
    @Composable
    private fun ForPreview() {
        ErrorContent(
            title = resources.getString(R.string.expired_title),
            content = resources.getString(R.string.expired_desc)
        )
    }

    @Composable
    private fun ErrorContent(
        title: String,
        content: String,
        instruction: String? = null,
        tokenId: String? = null
    ) {
        Column(
            modifier = Modifier.background(SnapColors.getARGBColor(SnapColors.supportDangerFill))
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
                    color = SnapColors.getARGBColor(SnapColors.textSecondary),
                    modifier = Modifier.padding(top = 24.dp)
                )
                Divider(
                    thickness = 2.dp,
                    color = SnapColors.getARGBColor(SnapColors.supportDangerDefault),
                    modifier = Modifier
                        .width(40.dp)
                        .padding(top = 24.dp),
                )
                Text(
                    text = content,
                    style = SnapTypography.STYLES.snapTextBigRegular,
                    color = SnapColors.getARGBColor(SnapColors.textSecondary),
                    modifier = Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp),
                    textAlign = TextAlign.Center
                )
                instruction?.let {
                    Text(
                        text = it,
                        style = SnapTypography.STYLES.snapTextMediumRegular,
                        color = SnapColors.getARGBColor(SnapColors.textSecondary),
                        modifier = Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp),
                        textAlign = TextAlign.Center
                    )
                }
                tokenId?.let {
                    Text(
                        text = it,
                        style = SnapTypography.STYLES.snapTextMediumMedium,
                        color = SnapColors.getARGBColor(SnapColors.textSecondary),
                        modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
            Text(
                text = resources.getString(R.string.success_screen_v2_info),
                style = SnapTypography.STYLES.snapTextMediumMedium,
                color = SnapColors.getARGBColor(SnapColors.textPrimary),
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(28.dp),
                textAlign = TextAlign.Center
            )
        }
    }

    companion object {
        private const val EXTRA_ERROR = "EXTRA_ERROR"
        fun getIntent(
            activityContext: Context,
            title: String,
            content: String,
            instruction: String? = null,
            tokenId: String? = null
        ): Intent {
            return Intent(activityContext, ErrorScreenActivity::class.java).apply {
                putExtra(
                    EXTRA_ERROR, ErrorData(
                        title = title,
                        content = content,
                        instruction = instruction,
                        tokenId = tokenId
                    )
                )
            }
        }
    }

    @Parcelize
    private data class ErrorData(
        val title: String,
        val content: String,
        val instruction: String? = null,
        val tokenId: String? = null
    ) : Parcelable
}