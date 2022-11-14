package com.midtrans.sdk.sample.presentation.config

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.midtrans.sdk.sample.presentation.config.component.BasicDropdownMenu
import com.midtrans.sdk.sample.presentation.shop.ProductListActivity
import com.midtrans.sdk.sample.util.DemoConstant.ACQUIRING_BANK
import com.midtrans.sdk.sample.util.DemoConstant.BCA
import com.midtrans.sdk.sample.util.DemoConstant.BNI
import com.midtrans.sdk.sample.util.DemoConstant.BRI
import com.midtrans.sdk.sample.util.DemoConstant.CIMB
import com.midtrans.sdk.sample.util.DemoConstant.COLOR_BLUE
import com.midtrans.sdk.sample.util.DemoConstant.COLOR_DEFAULT
import com.midtrans.sdk.sample.util.DemoConstant.COLOR_GREEN
import com.midtrans.sdk.sample.util.DemoConstant.COLOR_RED
import com.midtrans.sdk.sample.util.DemoConstant.COLOR_THEME
import com.midtrans.sdk.sample.util.DemoConstant.CREDIT_CARD_PAYMENT_TYPE
import com.midtrans.sdk.sample.util.DemoConstant.CUSTOM_EXPIRY
import com.midtrans.sdk.sample.util.DemoConstant.FIVE_MINUTE
import com.midtrans.sdk.sample.util.DemoConstant.INSTALLMENT
import com.midtrans.sdk.sample.util.DemoConstant.IS_INSTALLMENT_REQUIRED
import com.midtrans.sdk.sample.util.DemoConstant.MANDIRI
import com.midtrans.sdk.sample.util.DemoConstant.MAYBANK
import com.midtrans.sdk.sample.util.DemoConstant.MEGA
import com.midtrans.sdk.sample.util.DemoConstant.NONE
import com.midtrans.sdk.sample.util.DemoConstant.NORMAL_CC_PAYMENT
import com.midtrans.sdk.sample.util.DemoConstant.NO_ACQUIRING_BANK
import com.midtrans.sdk.sample.util.DemoConstant.NO_INSTALLMENT
import com.midtrans.sdk.sample.util.DemoConstant.OFFLINE
import com.midtrans.sdk.sample.util.DemoConstant.ONE_CLICK_TYPE
import com.midtrans.sdk.sample.util.DemoConstant.ONE_HOUR
import com.midtrans.sdk.sample.util.DemoConstant.ONE_MINUTE
import com.midtrans.sdk.sample.util.DemoConstant.OPTIONAL
import com.midtrans.sdk.sample.util.DemoConstant.REQUIRED
import com.midtrans.sdk.sample.util.DemoConstant.TWO_CLICK_TYPE
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.AssetFontLoader
import com.midtrans.sdk.uikit.internal.view.SnapButton


class DemoConfigurationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildUiKit()
        setContent {
            val scrollState = rememberScrollState()
            DemoConfigurationScreen(scrollState)
        }
    }

    @Composable
    fun DemoConfigurationScreen(scrollState: ScrollState) {
        val installmentTerm = listOf(NO_INSTALLMENT, MANDIRI, BCA, BNI, BRI, CIMB, MAYBANK, OFFLINE)
        val isRequiredList = listOf(OPTIONAL, REQUIRED)
        val acquringBankList =
            listOf(NO_ACQUIRING_BANK, MANDIRI, BCA, BNI, BRI, CIMB, MAYBANK, MEGA)
        val themeColorList = listOf(COLOR_DEFAULT, COLOR_RED, COLOR_BLUE, COLOR_GREEN)
        val customExpiryList = listOf(NONE, ONE_MINUTE, FIVE_MINUTE, ONE_HOUR)
        val ccPaymentTypeList = listOf(NORMAL_CC_PAYMENT, TWO_CLICK_TYPE, ONE_CLICK_TYPE)

        val state = remember {
            InputState(
                isRequired = false,
                installment = NO_INSTALLMENT,
                acquiringBank = NO_ACQUIRING_BANK,
                color = COLOR_DEFAULT,
                customExpiry = NONE,
                ccPaymentType = NORMAL_CC_PAYMENT
            )
        }

        Column(
            Modifier
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            BasicDropdownMenu(
                title = INSTALLMENT,
                optionList = installmentTerm,
                state = state
            )
            BasicDropdownMenu(
                title = IS_INSTALLMENT_REQUIRED,
                optionList = isRequiredList,
                state = state
            )
            BasicDropdownMenu(
                title = ACQUIRING_BANK,
                optionList = acquringBankList,
                state = state
            )
            BasicDropdownMenu(
                title = COLOR_THEME,
                optionList = themeColorList,
                state = state
            )
            BasicDropdownMenu(
                title = CUSTOM_EXPIRY,
                optionList = customExpiryList,
                state = state
            )
            BasicDropdownMenu(
                title = CREDIT_CARD_PAYMENT_TYPE,
                optionList = ccPaymentTypeList,
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
                        state.installment,
                        state.isRequired,
                        state.acquiringBank,
                        state.expiry,
                        state.ccPaymentType
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
    installment: String,
    isRequired: Boolean,
    acquiringBank: String,
    customExpiry: String,
    ccPaymentType: String
) {
    var installment by mutableStateOf(installment)
    var color by mutableStateOf(color)
    var isRequired by mutableStateOf(isRequired)
    var acquiringBank by mutableStateOf(acquiringBank)
    var expiry by mutableStateOf(customExpiry)
    var ccPaymentType by mutableStateOf(ccPaymentType)
}