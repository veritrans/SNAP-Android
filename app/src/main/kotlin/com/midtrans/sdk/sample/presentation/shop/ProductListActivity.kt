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

    private val isSavedCard: Boolean by lazy {
        intent.getBooleanExtra(EXTRA_INPUT_SAVEDCARD, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        buildUiKit()

        setContent {
            ProductListPage {
                val intent = OrderReviewActivity.getOrderReviewActivityIntent(
                    activityContext = this,
                    product = it,
                    installmentBank = installmentBank,
                    isRequiredInstallment = isRequiredInstallment,
                    acquiringBank = acquiringBank,
                    customExpiry = customExpiry,
                    isSavedCard = isSavedCard
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
        private const val EXTRA_INPUT_SAVEDCARD = "productList.extra.savedCard"

        fun getProductListActivity(
            activityContext: Context,
            color: String,
            installmentBank: String,
            isRequiredInstallment: Boolean,
            acquiringBank: String,
            customExpiry: String,
            isSavedCard: Boolean
        ): Intent {
            return Intent(activityContext, ProductListActivity::class.java).apply {
                putExtra(EXTRA_INPUT_COLOR, color)
                putExtra(EXTRA_INPUT_INSTALLMENT, installmentBank)
                putExtra(EXTRA_INPUT_ISREQUIRED, isRequiredInstallment)
                putExtra(EXTRA_INPUT_ACQUIRINGBANK, acquiringBank)
                putExtra(EXTRA_INPUT_EXPIRY, customExpiry)
                putExtra(EXTRA_INPUT_SAVEDCARD, isSavedCard)
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
