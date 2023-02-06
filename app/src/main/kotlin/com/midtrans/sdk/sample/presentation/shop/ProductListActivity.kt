package com.midtrans.sdk.sample.presentation.shop

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.midtrans.sdk.sample.model.ListItem
import com.midtrans.sdk.sample.presentation.shop.component.ProductListPage
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.AssetFontLoader

class ProductListActivity : ComponentActivity() {

    private val inputColor: String by lazy {
        intent.getStringExtra(EXTRA_INPUT_COLOR)
            ?: throw RuntimeException("Input Color must not be empty")
    }

    private val installmentBank: String by lazy {
        intent.getStringExtra(EXTRA_INPUT_INSTALLMENT)
            ?: throw RuntimeException("Installment must not be empty")
    }

    private val isRequiredInstallment: Boolean by lazy {
        intent.getBooleanExtra(EXTRA_INPUT_ISREQUIRED, false)
    }

    private val acquiringBank: String by lazy {
        intent.getStringExtra(EXTRA_INPUT_ACQUIRINGBANK)
            ?: throw RuntimeException("Acquiring Bank must not be empty")
    }

    private val customExpiry: String by lazy {
        intent.getStringExtra(EXTRA_INPUT_EXPIRY)
            ?: throw throw RuntimeException("Expiry must not be empty")
    }

    private val authenticationType: String by lazy {
        intent.getStringExtra(EXTRA_INPUT_CCAUTHENTICATIONTYPE)
            ?: throw throw RuntimeException("CC Authentication Type must not be empty")
    }

    private val isPreAuth: Boolean by lazy {
        intent.getBooleanExtra(EXTRA_INPUT_ISPREAUTH, false)
    }

    private val isBniPointsOnly: Boolean by lazy {
        intent.getBooleanExtra(EXTRA_INPUT_ISBNIPOINTS, false)
    }

    private val isShowAllPaymentChannels: Boolean by lazy {
        intent.getBooleanExtra(EXTRA_INPUT_ISSHOWALLPAYMENT, true)
    }

    private val paymentChannels: ArrayList<ListItem> by lazy {
        intent.getParcelableArrayListExtra(EXTRA_INPUT_PAYMENTCHANNELS)
            ?: throw RuntimeException("paymentChannels must not be empty")
    }

    private val bcaVa: String by lazy {
        intent.getStringExtra(EXTRA_INPUT_BCAVA)
            ?: throw throw RuntimeException("BCAva must not be empty")
    }

    private val bniVa: String by lazy {
        intent.getStringExtra(EXTRA_INPUT_BNIVA)
            ?: throw throw RuntimeException("BCAva must not be empty")
    }

    private val permataVa: String by lazy {
        intent.getStringExtra(EXTRA_INPUT_PERMATAVA)
            ?: throw throw RuntimeException("BCAva must not be empty")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        buildUiKit()

        setContent {
            ProductListPage {
                val intent = OrderReviewActivity.getOrderReviewActivityIntent(
                    activityContext = this@ProductListActivity,
                    product = it,
                    installmentBank = installmentBank,
                    isRequiredInstallment = isRequiredInstallment,
                    acquiringBank = acquiringBank,
                    customExpiry = customExpiry,
                    authenticationType = authenticationType,
                    isPreAuth = isPreAuth,
                    isBniPointsOnly = isBniPointsOnly,
                    isShowAllPaymentChannels = isShowAllPaymentChannels,
                    paymentChannels = paymentChannels,
                    bcaVa = bcaVa,
                    bniVa = bniVa,
                    permataVa = permataVa,
                    color = inputColor
                )
                startActivity(intent)
            }
        }
    }

    companion object {
        private const val EXTRA_INPUT_COLOR = "productList.extra.inputColor"
        private const val EXTRA_INPUT_INSTALLMENT = "productList.extra.installment"
        private const val EXTRA_INPUT_ISREQUIRED = "productList.extra.isRequired"
        private const val EXTRA_INPUT_ACQUIRINGBANK = "productList.extra.acquiringBank"
        private const val EXTRA_INPUT_EXPIRY = "productList.extra.inputExpiry"
        private const val EXTRA_INPUT_CCAUTHENTICATIONTYPE = "productList.extra.ccAuthenticationType"
        private const val EXTRA_INPUT_BCAVA = "productList.extra.bcaVa"
        private const val EXTRA_INPUT_BNIVA = "productList.extra.bniVa"
        private const val EXTRA_INPUT_PERMATAVA = "productList.extra.permataVa"
        private const val EXTRA_INPUT_ISPREAUTH = "productList.extra.isPreAuth"
        private const val EXTRA_INPUT_ISBNIPOINTS = "productList.extra.isBniPoints"
        private const val EXTRA_INPUT_ISSHOWALLPAYMENT = "productList.extra.isShowAllPayment"
        private const val EXTRA_INPUT_PAYMENTCHANNELS = "productList.extra.paymentChannels"

        fun getProductListActivity(
            activityContext: Context,
            color: String,
            installmentBank: String,
            isRequiredInstallment: Boolean,
            acquiringBank: String,
            customExpiry: String,
            authenticationType: String,
            isPreAuth: Boolean,
            isBniPointsOnly: Boolean,
            isShowAllPaymentChannels: Boolean,
            paymentChannels: ArrayList<ListItem>,
            bcaVa: String,
            bniVa: String,
            permataVa: String
        ): Intent {
            return Intent(activityContext, ProductListActivity::class.java).apply {
                putExtra(EXTRA_INPUT_COLOR, color)
                putExtra(EXTRA_INPUT_INSTALLMENT, installmentBank)
                putExtra(EXTRA_INPUT_ISREQUIRED, isRequiredInstallment)
                putExtra(EXTRA_INPUT_ACQUIRINGBANK, acquiringBank)
                putExtra(EXTRA_INPUT_EXPIRY, customExpiry)
                putExtra(EXTRA_INPUT_CCAUTHENTICATIONTYPE, authenticationType)
                putExtra(EXTRA_INPUT_ISPREAUTH, isPreAuth)
                putExtra(EXTRA_INPUT_ISBNIPOINTS, isBniPointsOnly)
                putExtra(EXTRA_INPUT_ISSHOWALLPAYMENT, isShowAllPaymentChannels)
                putExtra(EXTRA_INPUT_PAYMENTCHANNELS, paymentChannels)
                putExtra(EXTRA_INPUT_BCAVA, bcaVa)
                putExtra(EXTRA_INPUT_BNIVA, bniVa)
                putExtra(EXTRA_INPUT_PERMATAVA, permataVa)
            }
        }
    }

    private fun buildUiKit() {
        UiKitApi.Builder()
            .withContext(this.applicationContext)
            .withMerchantUrl("")
            .withMerchantClientKey("SB-Mid-client-hOWJXiCCDRvT0RGr")
            .withFontFamily(AssetFontLoader.fontFamily("fonts/SourceSansPro-Regular.ttf", this))
            .build()
    }
}