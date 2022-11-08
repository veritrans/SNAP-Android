package com.midtrans.sdk.sample.view.shop

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.midtrans.sdk.sample.view.shop.component.ProductList

class ProductListActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ProductList {
                Log.i("Test", "${it.name} ${it.price}")
            }
        }
    }
}
