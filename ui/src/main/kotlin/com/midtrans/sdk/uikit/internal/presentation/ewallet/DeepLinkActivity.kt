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
import com.midtrans.sdk.uikit.internal.view.AnimatedIcon
import com.midtrans.sdk.uikit.internal.view.SnapButton
import com.midtrans.sdk.uikit.internal.view.SnapColors
import com.midtrans.sdk.uikit.internal.view.SnapWebView

class DeepLinkActivity : BaseActivity() {

    private val url: String by lazy {
        intent.getStringExtra(EXTRA_URL) ?: throw RuntimeException("Url must not be empty")
    }

    private val paymentType: String by lazy {
        intent.getStringExtra(EXTRA_PAYMENT_TYPE)
            ?: throw RuntimeException("Payment type must not be empty")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Content(paymentType = paymentType, url = url)
        }
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
                urlLoadingOverride = { _, url ->
                    Log.d("urlOverload", url)

                    openDeeplink(paymentType, url)
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
                        AnimatedIcon(resId = R.drawable.ic_midtrans_animated).start()
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
    private fun ForPreview() {
        Content(paymentType = PaymentType.GOPAY, url = "http://")
    }

    private fun openDeeplink(
        paymentType: String,
        url: String
    ): Boolean {
        return when (paymentType) {
            PaymentType.GOPAY, PaymentType.SHOPEEPAY, PaymentType.UOB_EZPAY_APP -> {
                try {
                    intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                    finish()
                    true
                } catch (e: Throwable) {
//                    openAppInPlayStore()
                    false
                }
            }
            else -> {
                false
            }
        }
    }

    private val redirectionTitle by lazy {
        mapOf(
            Pair(PaymentType.GOPAY, R.string.redirection_screen_gopay_main_message),
            Pair(PaymentType.SHOPEEPAY, R.string.redirection_screen_shopeepay_main_message),
            Pair(PaymentType.UOB_EZPAY_APP, R.string.redirection_to_uob_tmrw_message) //TODO get correct copy for uob app
        )
    }

    private val redirectionCta by lazy {
        mapOf(
            Pair(PaymentType.GOPAY, R.string.redirection_screen_gopay_cta),
            Pair(PaymentType.SHOPEEPAY, R.string.redirection_screen_shopeepay_cta),
            Pair(PaymentType.UOB_EZPAY_APP, R.string.redirection_to_uob_tmrw_message) //TODO get correct copy for uob app
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

    private fun getAppPackageName(): String {
        return when (paymentType) {
            PaymentType.GOPAY -> GOJEK_PACKAGE_NAME
            PaymentType.SHOPEEPAY -> SHOPEE_PACKAGE_NAME
            PaymentType.SHOPEEPAY_QRIS -> SHOPEE_PACKAGE_NAME
            PaymentType.UOB_EZPAY_APP -> UOB_TMRW_PACKAGE_NAME
            else -> GOJEK_PACKAGE_NAME
        }
    }

    companion object {
        private const val EXTRA_URL = "deeplinkactivity.extra.url"
        private const val EXTRA_PAYMENT_TYPE = "deeplinkactivity.extra.payment_type"
        private const val GOJEK_PACKAGE_NAME = "com.gojek.app"
        private const val SHOPEE_PACKAGE_NAME = "com.shopee.id"
        private const val UOB_TMRW_PACKAGE_NAME = "com.uob.id.digitalbank"

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