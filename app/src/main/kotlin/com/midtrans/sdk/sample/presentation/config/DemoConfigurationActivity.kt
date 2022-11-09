package com.midtrans.sdk.sample.presentation.config

import android.os.Bundle
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
import com.midtrans.sdk.sample.util.Constant.COLOR_BLUE
import com.midtrans.sdk.sample.util.Constant.COLOR_DEFAULT
import com.midtrans.sdk.sample.util.Constant.COLOR_GREEN
import com.midtrans.sdk.sample.util.Constant.COLOR_RED
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.AssetFontLoader
import com.midtrans.sdk.uikit.internal.view.SnapButton
import com.midtrans.sdk.uikit.internal.view.SnapTextField
import com.midtrans.sdk.uikit.internal.view.SnapTypography


class DemoConfigurationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildUiKit()
        setContent {
            DemoConfigurationScreen()
        }
    }

    @Composable
    fun DemoConfigurationScreen() {
        val state = remember {
            InputState(
                color = COLOR_DEFAULT
            )
        }
        Column(Modifier.padding(16.dp)) {
            DropdownMenu(
                title = COLOR_THEME,
                optionList = listOf(COLOR_DEFAULT, COLOR_RED, COLOR_BLUE, COLOR_GREEN),
                state = state
            )

            SnapButton(
                text = "Launch Demo App", style = SnapButton.Style.PRIMARY,
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                onClick = {
                    val intent = ProductListActivity.getProductListActivity(
                        this@DemoConfigurationActivity,
                        state.color
                    )
                    startActivity(intent)
                }
            )
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
                                    COLOR_THEME -> state.color = selectedOptionText
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
            .build()
    }

    companion object {
        private const val COLOR_THEME = "Color Theme"
    }
}

class InputState(
    color: String
) {
    var color by mutableStateOf(color)
}
