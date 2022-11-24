package com.midtrans.sdk.uikit.internal.presentation.statusscreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import androidx.compose.ui.unit.dp
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.internal.view.SnapBulletedList
import com.midtrans.sdk.uikit.internal.view.SnapColors
import com.midtrans.sdk.uikit.internal.view.SnapTypography

class InProgressActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InProgressContent()
        }
    }

    @Composable
    private fun InProgressContent() {
        Column(
            modifier = Modifier.background(SnapColors.getARGBColor(SnapColors.supportInfoFill))
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
                    modifier = Modifier.padding(top = 88.dp)
                )
                Text(
                    text = resources.getString(R.string.in_progress_title),
                    style = SnapTypography.STYLES.snapHeadingL,
                    color = SnapColors.getARGBColor(SnapColors.textSecondary),
                    modifier = Modifier.padding(top = 24.dp)
                )
                Divider(
                    thickness = 2.dp,
                    color = SnapColors.getARGBColor(SnapColors.supportSuccessDefault),
                    modifier = Modifier
                        .width(40.dp)
                        .padding(top = 24.dp),
                )
                SnapBulletedList(
                    list = resources.getStringArray(R.array.in_progress_desc).toList(),
                    modifier = Modifier.padding(top = 24.dp)
                )
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
        fun getIntent(
            activityContext: Context
        ): Intent {
            return Intent(activityContext, InProgressActivity::class.java)
        }
    }
}