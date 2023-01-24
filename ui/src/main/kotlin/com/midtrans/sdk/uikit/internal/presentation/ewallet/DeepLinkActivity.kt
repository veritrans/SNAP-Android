package com.midtrans.sdk.uikit.internal.presentation.ewallet

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.model.TransactionResult
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
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
        viewModel.setPaymentType(paymentType)
        viewModel.trackPageViewed(currentStepNumber)
        setContent { Content(url = url, snapToken = snapToken) }
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
    private fun Content(url: String, snapToken: String) {
        if (isValidUrl(url)) {
            try {
                intent = Intent(Intent.ACTION_VIEW)
                intent.setData(Uri.parse(url))
                deepLinkLauncher.launch(intent)
            } catch (e: Throwable) {
                openAppInPlayStore()
            }
        } else {
            openAppInPlayStore()
        }
    }

    @Preview
    @Composable
    private fun ForPreview() {
        Content(url = "http://", snapToken = "")
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

    private fun isValidUrl(url: String): Boolean {
        if (url.contains("gojek://") ||
            url.contains("shopeeid://") ||
            // This is handle for sandbox Simulator
            url.contains("/gopay/partner/") ||
            url.contains("/shopeepay/")
        ) {  // TODO: fill with exact scheme
            return true
        }
        return false
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