package com.midtrans.sdk.sample.presentation.shop

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import com.midtrans.sdk.sample.presentation.shop.component.ProductListPage
import com.midtrans.sdk.uikit.api.model.CustomColors
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.AssetFontLoader

class ProductListActivity : ComponentActivity() {

    private val inputColor: String by lazy {
        intent.getStringExtra(EXTRA_INPUT_COLOR)
            ?: throw RuntimeException("Order ID must not be empty")
    }

    private val installment: String by lazy {
        intent.getStringExtra(EXTRA_INPUT_INSTALLMENT)
            ?: throw RuntimeException("Order ID must not be empty")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        buildUiKit()

        setContent {
            Column {
                ProductListPage {
                    val intent = OrderReviewActivity.getOrderReviewActivityIntent(
                        this@ProductListActivity,
                        it
                    )
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
        UiKitApi.Builder()
            .withContext(this.applicationContext)
            .withMerchantUrl("https://fiesta-point-sample.herokuapp.com/")
            .withMerchantClientKey("SB-Mid-client-hOWJXiCCDRvT0RGr")
            .withFontFamily(AssetFontLoader.fontFamily("fonts/SourceSansPro-Regular.ttf", this))
            .withCustomColors(
                getCustomColor(inputColor)
            )
            .build()
    }

    private fun getCustomColor(inputColor: String): CustomColors {
        var color = CustomColors(
            interactiveFillInverse = 0X383942,
            textInverse = 0xFFFFFF,
            supportNeutralFill = 0XEFF2F6
        )
        when (inputColor) {
            "Blue" -> {
                color = CustomColors(
                    interactiveFillInverse = 0x0e4e95,
                    textInverse = 0xFFFFFF,
                    supportNeutralFill = 0x3e71aa
                )
            }
            "Red" -> {
                color = CustomColors(
                    interactiveFillInverse = 0xb11235,
                    textInverse = 0xFFFFFF,
                    supportNeutralFill = 0xf36b89
                )
            }
            "Green" -> {
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
