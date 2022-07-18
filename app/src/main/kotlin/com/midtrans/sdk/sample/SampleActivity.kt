package com.midtrans.sdk.sample

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.uikit.internal.view.SnapButton

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
        var text by remember { mutableStateOf("") }
        ShowChargeContent(text = text, onTextFieldValueChange = { text = it})
    }

    @Composable
    fun ShowChargeContent(text: String, onTextFieldValueChange: (String)-> Unit){
        Column {
            Text("insert snap token", style = TextStyle(color = Color.Red))
            TextField(value = text, onValueChange = onTextFieldValueChange, enabled = true, readOnly = false)

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

            Button(onClick = {
                viewModel.getBinData(text)
            }) {
                Text(text = "get Bin data")
            }

            SnapButton(
                enabled = true,
                text = "To Sample UI",
                style = SnapButton.Style.PRIMARY
            ) {
                startActivity(Intent(this@SampleActivity, SampleUiActivity::class.java))
            }

        }
    }
}