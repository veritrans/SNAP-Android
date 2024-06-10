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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.IntentCompat.getParcelableExtra
import androidx.lifecycle.ViewModelProvider
import com.midtrans.sdk.corekit.api.model.TransactionResult
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import com.midtrans.sdk.uikit.internal.view.SnapButton
import com.midtrans.sdk.uikit.internal.view.SnapColors
import com.midtrans.sdk.uikit.internal.view.SnapTypography
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

class SuccessScreenActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: SuccessScreenViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(SuccessScreenViewModel::class.java)
    }

    private val data: SuccessData by lazy {
        getParcelableExtra(intent, EXTRA_TOTAL, SuccessData::class.java)
            ?: throw RuntimeException("Input data must not be empty")
    }

    private val transactionResult: TransactionResult? by lazy {
        getParcelableExtra(intent, UiKitConstants.KEY_TRANSACTION_RESULT, TransactionResult::class.java)
    }

    private val currentStepNumber: Int by lazy {
        intent.getIntExtra(EXTRA_STEP_NUMBER, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UiKitApi.getDefaultInstance().daggerComponent.inject(this)
        viewModel.trackPageViewed(
            paymentType = transactionResult?.paymentType.orEmpty(),
            stepNumber = currentStepNumber
        )
        setContent {
            SuccessContent(
                total = data.total,
                orderId = data.orderId?.let { stringResource(id = R.string.payment_summary_order_id) + data.orderId.toString()},
                isWithBackButton = !data.total.isNullOrEmpty() && !data.orderId.isNullOrEmpty()
            )
        }

        setResult(RESULT_OK, Intent().putExtra(UiKitConstants.KEY_TRANSACTION_RESULT, transactionResult))
    }

    @Preview
    @Composable
    private fun ForPreview() {
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
            modifier = Modifier.background(SnapColors.getARGBColor(SnapColors.supportSuccessFill))
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
                    text = stringResource(id = R.string.success_screen_v2_status),
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

                total?.let {
                    Text(
                        text = total,
                        style = SnapTypography.STYLES.snapHeading2Xl,
                        color = SnapColors.getARGBColor(SnapColors.textSecondary),
                        modifier = Modifier.padding(top = 24.dp)
                    )
                }

                orderId?.let {
                    Text(
                        text = orderId,
                        style = SnapTypography.STYLES.snapTextMediumRegular,
                        color = SnapColors.getARGBColor(SnapColors.textSecondary),
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }
            if (isWithBackButton) {
                SnapButton(
                    text = stringResource(id = R.string.success_screen_v1_cta),
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(16.dp)
                ) {
                    viewModel.trackSnapButtonClicked(
                        ctaName = getStringResourceInEnglish(R.string.success_screen_v1_cta),
                        paymentType = transactionResult?.paymentType.orEmpty()
                    )
                    finish()
                }
            } else {
                Text(
                    text = stringResource(id = R.string.success_screen_v2_info),
                    style = SnapTypography.STYLES.snapTextMediumMedium,
                    color = SnapColors.getARGBColor(SnapColors.textPrimary),
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(28.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    companion object {
        private const val EXTRA_TOTAL = "statusScreen.extra.total"
        private const val EXTRA_STEP_NUMBER = "statusScreen.extra.step_number"

        fun getIntent(
            activityContext: Context,
            total: String,
            orderId: String?,
            transactionResult: TransactionResult,
            stepNumber: Int
        ): Intent {
            return Intent(activityContext, SuccessScreenActivity::class.java).apply {
                putExtra(
                    EXTRA_TOTAL, SuccessData(
                        total = total,
                        orderId = orderId
                    )
                )
                putExtra(EXTRA_STEP_NUMBER, stepNumber)
                putExtra(UiKitConstants.KEY_TRANSACTION_RESULT, transactionResult)
            }
        }
    }

    @Parcelize
    private data class SuccessData(
        val total: String?,
        val orderId: String?
    ) : Parcelable
}