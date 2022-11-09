package com.midtrans.sdk.sample.presentation.shop

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import com.midtrans.sdk.sample.presentation.shop.component.ProductListPage

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
        setContent {
            Column {
                Text(text = inputColor)
                Text(text = installment)
                ProductListPage {
                    val intent = OrderReviewActivity.getOrderReviewActivityIntent(this@ProductListActivity, it)
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
}
