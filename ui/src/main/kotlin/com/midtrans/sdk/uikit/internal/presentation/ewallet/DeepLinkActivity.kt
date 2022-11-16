package com.midtrans.sdk.uikit.internal.presentation.ewallet

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.model.TransactionResult
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import com.midtrans.sdk.uikit.internal.view.GifImage
import com.midtrans.sdk.uikit.internal.view.SnapButton
import com.midtrans.sdk.uikit.internal.view.SnapColors
import com.midtrans.sdk.uikit.internal.view.SnapWebView
import javax.inject.Inject

internal class DeepLinkActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: DeepLinkViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(DeepLinkViewModel::class.java)
    }

    private val currentStepNumber: Int by lazy {
        intent.getIntExtra(EXTRA_STEP_NUMBER, 0)
    }


    private val deepLinkLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.checkStatus(snapToken)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UiKitApi.getDefaultInstance().daggerComponent.inject(this)
        viewModel.trackPageViewed(paymentType, currentStepNumber)
        setContent { Content(paymentType = paymentType, url = url) }
        observeData()
    }

    private fun observeData() {
        viewModel.checkStatusResultLiveData.observe(this) {
            setResult(it)
            finish()
        }
    }

    private fun setResult(data: TransactionResult) {
        val resultIntent = Intent().putExtra(UiKitConstants.KEY_TRANSACTION_RESULT, data)
        setResult(Activity.RESULT_OK, resultIntent)
    }

    @Composable
    private fun Content(paymentType: String, url: String) {
        var loading by remember { mutableStateOf(true) }

        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
                .fillMaxHeight(1f)
        ) {
            SnapWebView(
                title = "",
                paymentType = paymentType,
                url = url,
                onPageStarted = { loading = true },
                onPageFinished = { loading = false },
                urlLoadingOverride = { webview, url ->
                    Log.e("urlOverload", url)

                    if (url.contains("gojek") || url.contains("shopee")) {  // TODO: fill with exact scheme
                        try {
                            intent = Intent(Intent.ACTION_VIEW)
                            intent.setData(Uri.parse(url))
                            deepLinkLauncher.launch(intent)
                            true
                        } catch (e: Throwable) {
                            openAppInPlayStore()
                            true
                        }
                    } else {
                        false
                    }
                }
            )
            if (loading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .fillMaxHeight(1f)
                        .background(color = SnapColors.getARGBColor(SnapColors.backgroundFillPrimary))
                ) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        GifImage(
                            gifResId = R.drawable.gif_loading_ios,
                            modifier = Modifier.width(50.dp).height(50.dp)
                        )
                        redirectionTitle[paymentType]?.let {
                            Text(text = stringResource(id = it))
                        }
                    }
                    redirectionCta[paymentType]?.let {
                        SnapButton(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth(1f)
                                .padding(16.dp),
                            text = stringResource(id = it),
                            style = SnapButton.Style.TERTIARY
                        ) {
                            viewModel.trackSnapButtonClicked(
                                ctaName = getStringResourceInEnglish(it),
                                paymentType = paymentType
                            )
                        }
                    }
                }
            }
        }
    }

    @Preview
    @Composable
    private fun ForPreview() {
        Content(paymentType = PaymentType.GOPAY, url = "http://")
    }

    private fun getAppPackageName(): String {
        return when (paymentType) {
            PaymentType.GOPAY -> GOJEK_PACKAGE_NAME
            PaymentType.GOPAY_QRIS -> GOJEK_PACKAGE_NAME
            PaymentType.SHOPEEPAY -> SHOPEE_PACKAGE_NAME
            PaymentType.SHOPEEPAY_QRIS -> SHOPEE_PACKAGE_NAME
            else -> GOJEK_PACKAGE_NAME
        }
    }

    private val url: String by lazy {
        intent.getStringExtra(EXTRA_URL) ?: throw RuntimeException("Url must not be empty")
    }

    private val paymentType: String by lazy {
        intent.getStringExtra(EXTRA_PAYMENT_TYPE)
            ?: throw RuntimeException("Payment type must not be empty")
    }

    private val snapToken: String by lazy {
        intent.getStringExtra(EXTRA_SNAP_TOKEN).orEmpty()
    }

    private val redirectionTitle by lazy {
        mapOf(
            Pair(PaymentType.GOPAY, R.string.redirection_screen_gopay_main_message),
            Pair(PaymentType.SHOPEEPAY, R.string.redirection_screen_shopeepay_main_message),
        )
    }

    private val redirectionCta by lazy {
        mapOf(
            Pair(PaymentType.GOPAY, R.string.redirection_screen_gopay_cta),
            Pair(PaymentType.SHOPEEPAY, R.string.redirection_screen_shopeepay_cta),
        )
    }

    private fun openAppInPlayStore() {
        try {
            deepLinkLauncher.launch(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=${getAppPackageName()}")
                )
            )
        } catch (error: ActivityNotFoundException) {
            deepLinkLauncher.launch(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=${getAppPackageName()}")
                )
            )
        }
    }

    override fun onBackPressed() {
        viewModel.checkStatus(snapToken)
    }

    companion object {
        private const val EXTRA_URL = "deeplinkactivity.extra.url"
        private const val EXTRA_PAYMENT_TYPE = "deeplinkactivity.extra.payment_type"
        private const val EXTRA_SNAP_TOKEN = "deeplinkactivity.extra.snap_token"
        private const val EXTRA_STEP_NUMBER = "deeplinkactivity.extra.step_number"
        private const val GOJEK_PACKAGE_NAME = "com.gojek.app"
        private const val SHOPEE_PACKAGE_NAME = "com.shopee.id"

        fun getIntent(
            activityContext: Context,
            paymentType: String,
            url: String,
            snapToken: String,
            stepNumber: Int
        ): Intent {
            return Intent(activityContext, DeepLinkActivity::class.java).apply {
                putExtra(EXTRA_URL, url)
                putExtra(EXTRA_PAYMENT_TYPE, paymentType)
                putExtra(EXTRA_SNAP_TOKEN, snapToken)
                putExtra(EXTRA_STEP_NUMBER, stepNumber)
            }
        }
    }
}