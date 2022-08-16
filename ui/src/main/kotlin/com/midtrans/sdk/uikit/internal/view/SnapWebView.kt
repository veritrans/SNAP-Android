package com.midtrans.sdk.uikit.internal.view

import android.graphics.Bitmap
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.midtrans.sdk.corekit.api.model.PaymentType

@Composable
@Preview(showBackground = true)
fun WebViewPreview() {
    SnapWebView(
        paymentType = PaymentType.BCA_KLIKPAY,
        url = "https://simulator.sandbox.midtrans.com/gopay/ui/index",
        onWebViewStarted = { Log.d("SnapWebView", "Started") },
        onWebViewFinished = { Log.d("SnapWebView", "Finish") }
    )
}

@Composable
fun SnapWebView(
    @PaymentType.Def paymentType: String,
    url: String,
    onWebViewStarted: () -> Unit,
    onWebViewFinished: () -> Unit
) {
    AndroidView(factory = {
        WebView(it).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    Log.d("SnapWebView", "onPageStarted visibility : ${view?.visibility}")
                    onWebViewStarted.invoke()
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    Log.d("SnapWebView", "onPageFinished url : $url")
                    finishWebView(
                        paymentType = paymentType,
                        url = url.orEmpty(),
                        onWebViewFinished = onWebViewFinished
                    )
                }
            }
            settings.apply { //NOTE: following settings in old midtrans sdk
                allowFileAccess = false
                javaScriptEnabled = true //TODO check do we need to enable javascript
                domStorageEnabled = true
                loadWithOverviewMode = true
                useWideViewPort = true
                builtInZoomControls = true
                displayZoomControls = false
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
            setInitialScale(1)
            resumeTimers()
            loadUrl(url)
        }
    }, update = {
        it.loadUrl(url)
    })

    BackHandler(true) {
        //NOTE: disable back button
    }
}

private inline fun finishWebView(
    paymentType: String,
    url: String,
    onWebViewFinished: () -> Unit
) {
    when (paymentType) {
        PaymentType.KLIK_BCA -> containPattern(
            url = url,
            pattern = SnapWebViewClient.CALLBACK_KLIK_BCA,
            onPatternContained = onWebViewFinished
        )
        PaymentType.BCA_KLIKPAY -> containPattern(
            url = url,
            pattern = SnapWebViewClient.CALLBACK_BCA_KLIK_PAY,
            onPatternContained = onWebViewFinished
        )
        PaymentType.BRI_EPAY -> containPattern(
            url = url,
            pattern = SnapWebViewClient.CALLBACK_BRI_EPAY,
            onPatternContained = onWebViewFinished
        )
        PaymentType.CIMB_CLICKS -> containPattern(
            url = url,
            pattern = SnapWebViewClient.CALLBACK_CIMB_CLICKS,
            onPatternContained = onWebViewFinished
        )
        PaymentType.DANAMON_ONLINE -> containPattern(
            url = url,
            pattern = SnapWebViewClient.CALLBACK_DANAMON_ONLINE,
            onPatternContained = onWebViewFinished
        )
    }
}

private inline fun containPattern(
    url: String,
    pattern: String,
    onPatternContained: () -> Unit
) {
    if (url.contains(pattern, true)) {
        onPatternContained.invoke()
    }
}

private class SnapWebViewClient(
    private val onPageFinishedListener: (String) -> Unit
) : WebViewClient() {
    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        Log.d("SnapWebView", "onPageFinished url : $url")
        onPageFinishedListener.invoke(url.orEmpty())
    }

    companion object {
        const val CALLBACK_KLIK_BCA = "/inquiry" //TODO check what is klik bca pattern
        const val CALLBACK_BCA_KLIK_PAY = "?id="
        const val CALLBACK_CIMB_CLICKS = "cimb-clicks/response"
        const val CALLBACK_DANAMON_ONLINE = "/callback?signature="
        const val CALLBACK_BRI_EPAY = "briPayment?tid="
    }
}
