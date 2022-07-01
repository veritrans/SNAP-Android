package com.midtrans.sdk.sample

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.view.SnapAppBar
import com.midtrans.sdk.uikit.internal.view.SnapButton
import com.midtrans.sdk.uikit.internal.view.SnapNumberedListItem
import com.midtrans.sdk.uikit.internal.view.SnapText

class SampleUiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SnapCore.Builder().withContext(this.applicationContext).build()
        setContent { SampleUi() }
    }

    @Composable
    @Preview
    fun SampleUi() {
        Column(
            modifier = Modifier.verticalScroll(
                state = rememberScrollState()
            )
        ) {
            SnapAppBar(
                title = "App Bar",
                iconResId = R.drawable.psdk_ic_gopay
            ) {
                Toast.makeText(this@SampleUiActivity, "Icon App Bar clicked!", Toast.LENGTH_LONG).show()
            }

            SnapNumberedListItem(number = "1.", paragraph = "This is <b>bolt</b> <i>italic</i> <u>underline</u> Lorem ipsumgalksnfdlsan jklnlkjfnasd lkj nfaklsdjnf ljkasndf n lad")

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

            SnapText(text = "This is <b>bolt</b> <i>italic</i> <u>underline</u>")
            SnapText(
                text = "<ol>" +
                    "  <li>Coffee <b>asldjflasjdfalkdf</b> alsdf ladsfjlkasjdf jasdlf lasjdflk as alsdkjflalaksdf jalskdfj alskdjf ajsdf lalksd jfalkd sfjlaksd jflasdkjf aslkd fjlaksdj falkdsf jalkds jflaksdj falksdfj alksdfj aslkdfj alskdf</li>" +
                    "  <li>Tea</li>" +
                    "  <li>Milk</li>" +
                    "</ol>"
            )
        }
    }
}