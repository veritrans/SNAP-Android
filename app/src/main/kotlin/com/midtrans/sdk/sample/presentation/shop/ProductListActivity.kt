package com.midtrans.sdk.sample.presentation.shop

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
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

    private val installment: String by lazy {
        intent.getStringExtra(EXTRA_INPUT_INSTALLMENT)
            ?: throw RuntimeException("Installment must not be empty")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        buildUiKit()

        setContent {
            Column() {
                Text(text = installment)
                ProductListPage {
                    val intent = OrderReviewActivity.getOrderReviewActivityIntent(this@ProductListActivity, product = it)
                    startActivity(intent)
                }
            }
        }
    }

    companion object {
        private const val EXTRA_INPUT_COLOR = "productList.extra.inputColor"
        private const val EXTRA_INPUT_INSTALLMENT = "productList.extra.installment"

        fun getProductListActivity(
            activityContext: Context,
            color: String,
            installment: String
        ): Intent {
            return Intent(activityContext, ProductListActivity::class.java).apply {
                putExtra(EXTRA_INPUT_COLOR, color)
                putExtra(EXTRA_INPUT_INSTALLMENT, installment)
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
