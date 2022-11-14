package com.midtrans.sdk.sample.presentation.shop

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.midtrans.sdk.sample.presentation.shop.component.ProductListPage
import com.midtrans.sdk.sample.util.DemoConstant.COLOR_BLUE
import com.midtrans.sdk.sample.util.DemoConstant.COLOR_GREEN
import com.midtrans.sdk.sample.util.DemoConstant.COLOR_RED
import com.midtrans.sdk.uikit.api.model.CustomColors
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

    private val ccPaymentType: String by lazy {
        intent.getStringExtra(EXTRA_INPUT_CCPAYMENTTYPE)
            ?: throw throw RuntimeException("CCPaymentType must not be empty")
    }

    private val isPreAuth: Boolean by lazy {
        intent.getBooleanExtra(EXTRA_INPUT_ISPREAUTH, false)
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
                    ccPaymentType = ccPaymentType,
                    isPreAuth = isPreAuth,
                    bcaVa = bcaVa,
                    bniVa = bniVa,
                    permataVa = permataVa
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
        private const val EXTRA_INPUT_CCPAYMENTTYPE = "productList.extra.ccPaymentType"
        private const val EXTRA_INPUT_BCAVA = "productList.extra.bcaVa"
        private const val EXTRA_INPUT_BNIVA = "productList.extra.bniVa"
        private const val EXTRA_INPUT_PERMATAVA = "productList.extra.permataVa"
        private const val EXTRA_INPUT_ISPREAUTH = "productList.extra.isPreAuth"

        fun getProductListActivity(
            activityContext: Context,
            color: String,
            installmentBank: String,
            isRequiredInstallment: Boolean,
            acquiringBank: String,
            customExpiry: String,
            ccPaymentType: String,
            isPreAuth: Boolean,
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
                putExtra(EXTRA_INPUT_CCPAYMENTTYPE, ccPaymentType)
                putExtra(EXTRA_INPUT_ISPREAUTH, isPreAuth)
                putExtra(EXTRA_INPUT_BCAVA, bcaVa)
                putExtra(EXTRA_INPUT_BNIVA, bniVa)
                putExtra(EXTRA_INPUT_PERMATAVA, permataVa)
            }
        }
    }

    private fun buildUiKit() {
        val builder = UiKitApi.Builder()
            .withContext(this.applicationContext)
            .withMerchantUrl("https://fiesta-point-sample.herokuapp.com/")
            .withMerchantClientKey("SB-Mid-client-hOWJXiCCDRvT0RGr")
            .withFontFamily(AssetFontLoader.fontFamily("fonts/SourceSansPro-Regular.ttf", this))

        getCustomColor(inputColor)?.let { builder.withCustomColors(it) }

        builder.build()
    }

    private fun getCustomColor(inputColor: String): CustomColors? {
        var color: CustomColors? = null
        when (inputColor) {
            COLOR_BLUE -> {
                color = CustomColors(
                    interactiveFillInverse = 0x0e4e95,
                    textInverse = 0xFFFFFF,
                    supportNeutralFill = 0x3e71aa
                )
            }
            COLOR_RED -> {
                color = CustomColors(
                    interactiveFillInverse = 0xb11235,
                    textInverse = 0xFFFFFF,
                    supportNeutralFill = 0xf36b89
                )
            }
            COLOR_GREEN -> {
                color = CustomColors(
                    interactiveFillInverse = 0x32ad4a,
                    textInverse = 0xFFFFFF,
                    supportNeutralFill = 0x5bbd6e
                )
            }
        }
        return color
    }
}