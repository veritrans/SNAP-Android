package com.midtrans.sdk.sample

import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.UiComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModelProvider
import com.midtrans.sdk.corekit.SnapCore

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
            val uriHandler = LocalUriHandler.current
            var text by remember { mutableStateOf("") }
            Text("insert snap token", style = TextStyle(color = Color.Red))
            TextField(value = text, onValueChange = {
                text = it
            }, enabled = true, readOnly = false)

            Button(onClick = {
                viewModel.chargeUsingCreditCardWithPromo(text)
            }) {
                Text(text = "Charge")
            }

            Button(onClick = {
                viewModel.getTwoClickToken()
            }) {
                Text(text = "2click card token")
            }

            Button(onClick = {
                viewModel.getCardTokenBasic()
            }) {
                Text(text = "normal card token")
            }

            Button(onClick = {
                viewModel.deleteSavedCard()
            }) {
                Text(text = "Delete card token")
            }

        }
    }
}