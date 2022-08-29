package com.midtrans.sdk.uikit.internal.presentation.ewallet

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
            title = "ngetes tes",
            paymentType = PaymentType.GOPAY,
            url = url,
            onPageStarted = { },
            onPageFinished = { },
            urlLoadingOverride = { webview, url ->
                Log.e("urlOverload", url)
                if (url.contains("whatsapp://")) {
                    intent = Intent(Intent.ACTION_VIEW)
                    intent.setData(Uri.parse(url))
                    startActivity(intent)

                    true
                } else {
                    false
                }
            }
        )
    }

    private val url: String by lazy {
        intent.getStringExtra(EXTRA_URL) ?: throw RuntimeException("Url must not be empty")
    }

    companion object {
        private const val EXTRA_URL = "deeplinkactivity.extra.url"


        fun getIntent(
            activityContext: Context,
            paymentType: String,
            url: String

        ): Intent {
            return Intent(activityContext, DeepLinkActivity::class.java).apply {
                putExtra(EXTRA_URL, url)
            }
        }
    }
}