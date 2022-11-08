package com.midtrans.sdk.sample.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.midtrans.sdk.sample.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val name: String,
    val price: Double,
    @DrawableRes
    val image: Int,
    val color: Long
) : Parcelable

fun getData(): List<Product> {
    return listOf(
        Product("Beats Solo 1", 15000.00, R.drawable.headset4, 0xFFFF1A9B),
        Product("JBL Solo 2", 20000.00, R.drawable.headset1, 0xFF5C00FF),
        Product("Beats Solo 3", 25000.00, R.drawable.headset2, 0xFFFF0000),
        Product("JBL Solo 4", 30000.00, R.drawable.headset3, 0xFF447ACE),
        Product("Beats Solo 5", 35000.00, R.drawable.headset4, 0xFFFF1A9B),
        Product("JBL Solo 6", 40000.00, R.drawable.headset1, 0xFF5C00FF),
        Product("Beats Solo 7", 45000.00, R.drawable.headset2, 0xFFFF0000),
        Product("JBL Solo 8", 50000.00, R.drawable.headset3, 0xFF447ACE)
    )
}