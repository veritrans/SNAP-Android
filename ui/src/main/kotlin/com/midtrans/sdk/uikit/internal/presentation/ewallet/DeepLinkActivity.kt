package com.midtrans.sdk.uikit.internal.presentation.ewallet

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.uikit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.internal.view.SnapWebView

class DeepLinkActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { Content() }
    }

    @Composable
    private fun Content() {
        SnapWebView(
            title = "Pay",
            paymentType = PaymentType.GOPAY,
            url = url,
            onPageStarted = { },
            onPageFinished = { },
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
    }

    private fun getAppPackageName(): String {
        return when (paymentType) {
            PaymentType.GOPAY -> "com.gojek.app"
            PaymentType.SHOPEEPAY -> "com.shopee.id"
            PaymentType.SHOPEEPAY_QRIS -> "com.shopee.id"
            else -> "com.gojek.app"
        }
    }

    private val url: String by lazy {
        intent.getStringExtra(EXTRA_URL) ?: throw RuntimeException("Url must not be empty")
    }

    private val paymentType: String by lazy {
        intent.getStringExtra(EXTRA_PAYMENT_TYPE)
            ?: throw RuntimeException("Payment type must not be empty")
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