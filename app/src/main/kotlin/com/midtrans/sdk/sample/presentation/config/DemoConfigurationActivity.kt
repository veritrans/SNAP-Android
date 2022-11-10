package com.midtrans.sdk.sample.presentation.config

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.midtrans.sdk.sample.presentation.config.component.BasicDropdownMenu
import com.midtrans.sdk.sample.presentation.shop.ProductListActivity
import com.midtrans.sdk.sample.util.DemoConstant.BCA
import com.midtrans.sdk.sample.util.DemoConstant.BNI
import com.midtrans.sdk.sample.util.DemoConstant.BRI
import com.midtrans.sdk.sample.util.DemoConstant.CIMB
import com.midtrans.sdk.sample.util.DemoConstant.COLOR_BLUE
import com.midtrans.sdk.sample.util.DemoConstant.COLOR_DEFAULT
import com.midtrans.sdk.sample.util.DemoConstant.COLOR_GREEN
import com.midtrans.sdk.sample.util.DemoConstant.COLOR_RED
import com.midtrans.sdk.sample.util.DemoConstant.COLOR_THEME
import com.midtrans.sdk.sample.util.DemoConstant.INSTALLMENT
import com.midtrans.sdk.sample.util.DemoConstant.MANDIRI
import com.midtrans.sdk.sample.util.DemoConstant.MAYBANK
import com.midtrans.sdk.sample.util.DemoConstant.NO_INSTALLMENT
import com.midtrans.sdk.sample.util.DemoConstant.OFFLINE
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.AssetFontLoader
import com.midtrans.sdk.uikit.internal.view.SnapButton


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
                color = COLOR_DEFAULT,
                installment = NO_INSTALLMENT
            )
        }
        Column(Modifier.padding(16.dp)) {
            BasicDropdownMenu(
                title = INSTALLMENT,
                optionList = listOf(
                    NO_INSTALLMENT,
                    MANDIRI,
                    BCA,
                    BNI,
                    BRI,
                    CIMB,
                    MAYBANK,
                    OFFLINE
                ),
                state = state
            )
            BasicDropdownMenu(
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
                        state.color,
                        state.installment
                    )
                    startActivity(intent)
                }
            )
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
}

class InputState(
    color: String,
    installment: String
) {
    var installment by mutableStateOf(installment)
    var color by mutableStateOf(color)
}
