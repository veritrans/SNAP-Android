package com.midtrans.sdk.sample.presentation.config

import android.os.Bundle
import android.os.Parcelable
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.midtrans.sdk.sample.presentation.shop.ProductListActivity
import com.midtrans.sdk.uikit.api.model.CustomColors
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.AssetFontLoader
import com.midtrans.sdk.uikit.internal.view.SnapTextField
import com.midtrans.sdk.uikit.internal.view.SnapTypography
import kotlinx.parcelize.Parcelize


class DemoConfigurationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildUiKit()
        setContent {
            MainContent()
        }
    }

    @Composable
    fun MainContent() {
        val state = remember {
            InputState(
                color = "Blue",
                installment = "No Installment"
            )
        }
        Column(Modifier.padding(16.dp)) {
            DropdownMenu(
                title = "Installment",
                optionList = listOf(
                    "No Installment",
                    "Mandiri",
                    "BCA",
                    "BNI",
                    "BRI",
                    "CIMB",
                    "MayBank",
                    "Offline"
                ),
                state = state
            )
            DropdownMenu(
                title = "Color Theme",
                optionList = listOf("Blue", "Red", "Green"),
                state = state
            )
            Text(text = state.installment)
            Text(text = state.color)

            Button(onClick = {
                startActivity(
                    ProductListActivity.getProductListActivity(
                        this@DemoConfigurationActivity,
                        state.color,
                        state.installment
                    )
                )
            }) {
                Text(text = "Simple Button")
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun DropdownMenu(title: String, optionList: List<String>, state: InputState) {
        val options by remember { mutableStateOf(optionList) }
        var expanded by remember { mutableStateOf(false) }
        var selectedOptionText by remember { mutableStateOf(options[0]) }

        Column(modifier = Modifier.fillMaxWidth(1f), horizontalAlignment = Alignment.Start) {
            Text(
                modifier = Modifier.padding(top = 16.dp, bottom = 20.dp),
                text = title,
                style = SnapTypography.STYLES.snapTextMediumMedium
            )
            ExposedDropdownMenuBox(
                modifier = Modifier.padding(bottom = 10.dp),
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                SnapTextField(
                    modifier = Modifier.fillMaxWidth(1f),
                    readOnly = true,
                    value = TextFieldValue(selectedOptionText),
                    onValueChange = {},
                    isFocused = false,
                    enabled = true,
                    onFocusChange = {},
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                    textStyle = SnapTypography.STYLES.snapTextMediumRegular
                )
                ExposedDropdownMenu(
                    modifier = Modifier.fillMaxWidth(1f),
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    options.forEach { selectionOption ->
                        DropdownMenuItem(
                            onClick = {
                                selectedOptionText = selectionOption
                                expanded = false
                                when (title) {
                                    "Installment" -> state.installment = selectedOptionText
                                    "Color Theme" -> state.color = selectedOptionText
                                }
                            },
                            enabled = true
                        ) {
                            Text(
                                text = selectionOption,
                                style = SnapTypography.STYLES.snapTextMediumRegular
                            )
                        }
                    }
                }
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

class InputState(
    color: String,
    installment: String
) {
    var installment by mutableStateOf(installment)
    var color by mutableStateOf(color)
}
