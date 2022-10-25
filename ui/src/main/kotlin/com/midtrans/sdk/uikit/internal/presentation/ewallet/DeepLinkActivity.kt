package com.midtrans.sdk.uikit.internal.presentation.ewallet

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.internal.view.GifImage
import com.midtrans.sdk.uikit.internal.view.SnapButton
import com.midtrans.sdk.uikit.internal.view.SnapColors
import com.midtrans.sdk.uikit.internal.view.SnapWebView

class DeepLinkActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { Content(paymentType = paymentType, url = url) }
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
                            startActivity(intent)
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
                        .background(color = SnapColors.getARGBColor(SnapColors.BACKGROUND_FILL_PRIMARY))
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
                        ) {}
                    }
                }
            }
        }
    }

    @Preview
    @Composable
    private fun forPreview() {
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
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=${getAppPackageName()}")
                )
            )
        } catch (error: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=${getAppPackageName()}")
                )
            )
        }
    }

    companion object {
        private const val EXTRA_URL = "deeplinkactivity.extra.url"
        private const val EXTRA_PAYMENT_TYPE = "deeplinkactivity.extra.payment_type"
        private const val GOJEK_PACKAGE_NAME = "com.gojek.app"
        private const val SHOPEE_PACKAGE_NAME = "com.shopee.id"


        fun getIntent(
            activityContext: Context,
            paymentType: String,
            url: String

        ): Intent {
            return Intent(activityContext, DeepLinkActivity::class.java).apply {
                putExtra(EXTRA_URL, url)
                putExtra(EXTRA_PAYMENT_TYPE, paymentType)
            }
        }
    }
}