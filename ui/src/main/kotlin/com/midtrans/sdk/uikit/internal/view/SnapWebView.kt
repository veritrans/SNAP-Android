package com.midtrans.sdk.uikit.internal.view

import android.util.Log
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.midtrans.sdk.corekit.api.exception.SnapError
import com.midtrans.sdk.corekit.api.model.PaymentType

@Composable
@Preview(showBackground = true)
fun WebViewPreview() {
    SnapWebView(
        paymentType = PaymentType.BCA_KLIKPAY,
        url = "https://www.geeksforgeeks.org/webview-in-android-using-jetpack-compose/",
        onWebViewFinished = {
            Log.d("SnapWebView", "Preview")
        }
    )
}

@Composable
fun SnapWebView(
    @PaymentType.Def paymentType: String,
    url: String,
    onWebViewFinished: () -> Unit
) {
    AndroidView(factory = {
        WebView(it).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            webViewClient = object : WebViewClient() {
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
            loadUrl(url)
        }
    }, update = {
        it.loadUrl(url)
    })
}


private inline fun finishWebView(
    paymentType: String,
    url: String,
    onWebViewFinished: () -> Unit
) {
    when (paymentType) {
        PaymentType.BCA_KLIKPAY -> containPattern(
            url = url,
            pattern = SnapWebViewClient.CALLBACK_BCA_KLIK_PAY,
            onPatternMatched = onWebViewFinished
        )
        PaymentType.BRI_EPAY -> containPattern(
            url = url,
            pattern = SnapWebViewClient.CALLBACK_BRI_EPAY,
            onPatternMatched = onWebViewFinished
        )
        PaymentType.CIMB_CLICKS -> containPattern(
            url = url,
            pattern = SnapWebViewClient.CALLBACK_CIMB_CLICKS,
            onPatternMatched = onWebViewFinished
        )
        PaymentType.DANAMON_ONLINE -> containPattern(
            url = url,
            pattern = SnapWebViewClient.CALLBACK_DANAMON_ONLINE,
            onPatternMatched = onWebViewFinished
        )
    }
}


private inline fun containPattern(
    url: String,
    pattern: String,
    onPatternMatched: () -> Unit
) {
    if (url.contains(pattern, true)) {
        onPatternMatched.invoke()
    } else {
        throw SnapError(message = "Callback url is incorrect")
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
        const val CALLBACK_BCA_KLIK_PAY = "?id="
        const val CALLBACK_CIMB_CLICKS = "cimb-clicks/response"
        const val CALLBACK_DANAMON_ONLINE = "/callback?signature="
        const val CALLBACK_BRI_EPAY = "briPayment?tid="
    }
}
