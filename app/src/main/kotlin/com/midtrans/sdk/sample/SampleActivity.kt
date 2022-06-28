package com.midtrans.sdk.sample

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.uikit.internal.view.SnapButton
import com.midtrans.sdk.uikit.internal.view.SnapButtonAttribute

class SampleActivity : AppCompatActivity() {

    private val viewModel: SampleViewModel by lazy {
        ViewModelProvider(this).get(SampleViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SnapCore.Builder().withContext(this.applicationContext).build()
        viewModel.getHelloFromSnap()
        setContent { Greeting() }
    }

    @Composable
    @Preview
    fun Greeting() {
        Column {
            var text by remember { mutableStateOf("") }
            Text("insert snap token", style = TextStyle(color = Color.Red))
            TextField(value = text, onValueChange = {
                text = it
            }, enabled = true, readOnly = false)
            SnapButton(
                enabled = true,
                text = "Bayar",
                style = SnapButtonAttribute.Style.PRIMARY
            ) {
                viewModel.chargeUsingCreditCard(text)
            }
            SnapButton(
                enabled = true,
                text = "Create card token",
                style = SnapButtonAttribute.Style.PRIMARY
            ) {
                viewModel.getCardTokenBasic()
            }
            SnapButton(
                enabled = true,
                text = "Delete card token",
                style = SnapButtonAttribute.Style.TERTIARY
            ) {
                viewModel.deleteSavedCard()
            }
        }
    }
}