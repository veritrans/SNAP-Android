package com.midtrans.sdk.sample

import android.content.Intent
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
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.uikit.internal.view.SnapButton

class SampleUiActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SnapCore.Builder().withContext(this.applicationContext).build()
        setContent { SampleUi() }
    }

    @Composable
    @Preview
    fun SampleUi() {
        Column {
            var text by remember { mutableStateOf("") }
            Text("insert snap token", style = TextStyle(color = Color.Red))
            TextField(value = text, onValueChange = {
                text = it
            }, enabled = true, readOnly = false)
            SnapButton(
                enabled = true,
                text = "Primary Button",
                style = SnapButton.Style.PRIMARY
            ) {}

            SnapButton(
                enabled = true,
                text = "Tertiary Button",
                style = SnapButton.Style.TERTIARY
            ) {}

            SnapButton(
                enabled = false,
                text = "Primary Disabled Button",
                style = SnapButton.Style.PRIMARY
            ) {}

            SnapButton(
                enabled = false,
                text = "Tertiary Disabled Button",
                style = SnapButton.Style.TERTIARY
            ) {}
        }
    }
}