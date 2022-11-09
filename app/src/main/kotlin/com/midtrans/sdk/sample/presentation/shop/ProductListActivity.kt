package com.midtrans.sdk.sample.presentation.shop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.midtrans.sdk.sample.presentation.shop.component.ProductListPage
import com.midtrans.sdk.uikit.api.model.CustomColors
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.AssetFontLoader

class ProductListActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildUiKit()
        setContent {
            ProductListPage {
                val intent = OrderReviewActivity.getOrderReviewActivityIntent(this, it)
                startActivity(intent)
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
                CustomColors(
                    interactiveFillInverse = 0x0e4e95,
                    textInverse = 0xFFFFFF,
                    supportNeutralFill = 0x3e71aa
                )
            )
            .build()
    }
}
