package com.midtrans.sdk.sample.presentation.shop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.midtrans.sdk.sample.presentation.shop.component.ProductListPage

class ProductListActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ProductListPage {
                val intent = OrderReviewActivity.getOrderReviewActivityIntent(this, it)
                startActivity(intent)
            }
        }
    }
}
