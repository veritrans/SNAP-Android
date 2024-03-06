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
import com.midtrans.sdk.corekit.core.PaymentMethod
import com.midtrans.sdk.sample.model.ListItem
import com.midtrans.sdk.sample.presentation.config.component.AlertDialogDropdownMenu
import com.midtrans.sdk.sample.presentation.config.component.BasicDropdownMenu
import com.midtrans.sdk.sample.presentation.config.component.CustomTextField
import com.midtrans.sdk.sample.presentation.shop.ProductListActivity
import com.midtrans.sdk.sample.util.DemoConstant.ACQUIRING_BANK
import com.midtrans.sdk.sample.util.DemoConstant.AUTH_3DS
import com.midtrans.sdk.sample.util.DemoConstant.AUTH_NONE
import com.midtrans.sdk.sample.util.DemoConstant.BCA
import com.midtrans.sdk.sample.util.DemoConstant.BLACKLIST_BINS
import com.midtrans.sdk.sample.util.DemoConstant.BNI
import com.midtrans.sdk.sample.util.DemoConstant.BNI_POINT_ONLY
import com.midtrans.sdk.sample.util.DemoConstant.BRI
import com.midtrans.sdk.sample.util.DemoConstant.CIMB
import com.midtrans.sdk.sample.util.DemoConstant.COLOR_BLUE
import com.midtrans.sdk.sample.util.DemoConstant.COLOR_DEFAULT
import com.midtrans.sdk.sample.util.DemoConstant.COLOR_GREEN
import com.midtrans.sdk.sample.util.DemoConstant.COLOR_RED
import com.midtrans.sdk.sample.util.DemoConstant.COLOR_THEME
import com.midtrans.sdk.sample.util.DemoConstant.CREDIT_CARD_AUTHENTICATION
import com.midtrans.sdk.sample.util.DemoConstant.CUSTOM_BCA_VA
import com.midtrans.sdk.sample.util.DemoConstant.CUSTOM_BNI_VA
import com.midtrans.sdk.sample.util.DemoConstant.CUSTOM_CIMB_VA
import com.midtrans.sdk.sample.util.DemoConstant.CUSTOM_EXPIRY
import com.midtrans.sdk.sample.util.DemoConstant.CUSTOM_PERMATA_VA
import com.midtrans.sdk.sample.util.DemoConstant.DIRECT_PAYMENT_SCREEN_ENABLED
import com.midtrans.sdk.sample.util.DemoConstant.DISABLED
import com.midtrans.sdk.sample.util.DemoConstant.ENABLED
import com.midtrans.sdk.sample.util.DemoConstant.FIVE_MINUTE
import com.midtrans.sdk.sample.util.DemoConstant.INSTALLMENT
import com.midtrans.sdk.sample.util.DemoConstant.IS_INSTALLMENT_REQUIRED
import com.midtrans.sdk.sample.util.DemoConstant.MANDIRI
import com.midtrans.sdk.sample.util.DemoConstant.MAYBANK
import com.midtrans.sdk.sample.util.DemoConstant.MEGA
import com.midtrans.sdk.sample.util.DemoConstant.NONE
import com.midtrans.sdk.sample.util.DemoConstant.NO_ACQUIRING_BANK
import com.midtrans.sdk.sample.util.DemoConstant.NO_INSTALLMENT
import com.midtrans.sdk.sample.util.DemoConstant.OFFLINE
import com.midtrans.sdk.sample.util.DemoConstant.ONE_HOUR
import com.midtrans.sdk.sample.util.DemoConstant.ONE_MINUTE
import com.midtrans.sdk.sample.util.DemoConstant.OPTIONAL
import com.midtrans.sdk.sample.util.DemoConstant.PRE_AUTH
import com.midtrans.sdk.sample.util.DemoConstant.REQUIRED
import com.midtrans.sdk.sample.util.DemoConstant.SAVED_CARD
import com.midtrans.sdk.sample.util.DemoConstant.SHOW_ALL
import com.midtrans.sdk.sample.util.DemoConstant.SHOW_ALL_PAYMENT_CHANNELS
import com.midtrans.sdk.sample.util.DemoConstant.SHOW_SELECTED_ONLY
import com.midtrans.sdk.sample.util.DemoConstant.WHITELIST_BINS
import com.midtrans.sdk.uikit.external.UiKitApi
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
        val paymentMethodList = listOf( DISABLED,
            PaymentMethod.CREDIT_CARD, PaymentMethod.BANK_TRANSFER, PaymentMethod.BANK_TRANSFER_BCA,
            PaymentMethod.BANK_TRANSFER_MANDIRI, PaymentMethod.BANK_TRANSFER_PERMATA, PaymentMethod.BANK_TRANSFER_BNI,
            PaymentMethod.BANK_TRANSFER_BRI, PaymentMethod.BANK_TRANSFER_CIMB, PaymentMethod.BANK_TRANSFER_OTHER,
            PaymentMethod.GO_PAY, PaymentMethod.SHOPEEPAY, PaymentMethod.KLIKBCA, PaymentMethod.BCA_KLIKPAY,
            PaymentMethod.CIMB_CLICKS, PaymentMethod.EPAY_BRI, PaymentMethod.DANAMON_ONLINE,
            PaymentMethod.UOB_EZPAY, PaymentMethod.UOB_EZPAY_APP, PaymentMethod.UOB_EZPAY_WEB,
            PaymentMethod.INDOMARET, PaymentMethod.ALFAMART, PaymentMethod.AKULAKU, PaymentMethod.KREDIVO
        ).map {
            it.toString()
        }
        val isRequiredList = listOf(OPTIONAL, REQUIRED)
        val acquringBankList =
            listOf(NO_ACQUIRING_BANK, MANDIRI, BCA, BNI, BRI, CIMB, MAYBANK, MEGA)
        val themeColorList = listOf(COLOR_DEFAULT, COLOR_RED, COLOR_BLUE, COLOR_GREEN)
        val customExpiryList = listOf(NONE, ONE_MINUTE, FIVE_MINUTE, ONE_HOUR)
        val authenticationList = listOf(AUTH_NONE, AUTH_3DS)
        val booleanList = listOf(DISABLED, ENABLED)
        val showPaymentList = listOf(SHOW_ALL, SHOW_SELECTED_ONLY)
        val state = remember {
            InputState(
                isRequired = false,
                installment = NO_INSTALLMENT,
                acquiringBank = NO_ACQUIRING_BANK,
                color = COLOR_DEFAULT,
                customExpiry = NONE,
                authenticationType = AUTH_NONE,
                isSavedCard = false,
                isPreAuth = false,
                isBniPointOnly = false,
                isShowAllPaymentChannels = true,
                allPaymentChannels = ArrayList(),
                whitelistBins = "",
                blacklistBins = "",
                bcaVa = "",
                bniVa = "",
                permataVa = "",
                cimbVa = "",
                directPaymentScreen = DISABLED
            )
        }
        val openDialog = remember { mutableStateOf(true) }

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
                title = CREDIT_CARD_AUTHENTICATION,
                optionList = authenticationList,
                state = state
            )
            BasicDropdownMenu(
                title = SAVED_CARD,
                optionList = booleanList,
                state = state
            )
            BasicDropdownMenu(
                title = PRE_AUTH,
                optionList = booleanList,
                state = state
            )
            BasicDropdownMenu(
                title = BNI_POINT_ONLY,
                optionList = booleanList,
                state = state
            )
            AlertDialogDropdownMenu(
                title = SHOW_ALL_PAYMENT_CHANNELS,
                optionList = showPaymentList,
                state = state,
                openDialog = openDialog
            )
            BasicDropdownMenu(
                title = DIRECT_PAYMENT_SCREEN_ENABLED,
                optionList = paymentMethodList,
                state = state
            )
            CustomTextField(
                title = WHITELIST_BINS,
                state = state
            )
            CustomTextField(
                title = BLACKLIST_BINS,
                state = state
            )
            CustomTextField(
                title = CUSTOM_BCA_VA,
                state = state
            )
            CustomTextField(
                title = CUSTOM_BNI_VA,
                state = state
            )
            CustomTextField(
                title = CUSTOM_PERMATA_VA,
                state = state
            )
            CustomTextField(
                title = CUSTOM_CIMB_VA,
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
                        state.authenticationType,
                        state.isSavedCard,
                        state.isPreAuth,
                        state.isBniPointOnly,
                        state.isShowAllPaymentChannels,
                        state.allPaymentChannels,
                        state.whitelistBins,
                        state.blacklistBins,
                        state.bcaVa,
                        state.bniVa,
                        state.permataVa,
                        state.cimbVa,
                        state.directPaymentScreen,
                        true
                    )
                    startActivity(intent)
                }
            )

            SnapButton(
                text = "Launch Demo App (Legacy)", style = SnapButton.Style.PRIMARY,
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
                        state.authenticationType,
                        state.isSavedCard,
                        state.isPreAuth,
                        state.isBniPointOnly,
                        state.isShowAllPaymentChannels,
                        state.allPaymentChannels,
                        state.whitelistBins,
                        state.blacklistBins,
                        state.bcaVa,
                        state.bniVa,
                        state.permataVa,
                        state.cimbVa,
                        state.directPaymentScreen,

                        false
                    )
                    startActivity(intent)
                }
            )
        }
    }

    private fun buildUiKit() {
        UiKitApi.Builder()
            .withContext(this.applicationContext)
            .withMerchantUrl("https://demo.midtrans.com/api/")
            .withMerchantClientKey("VT-client-yrHf-c8Sxr-ck8tx")
            .withFontFamily("fonts/SourceSansPro-Regular.ttf")
            .build()
    }
}

class InputState(
    color: String,
    installment: String,
    isRequired: Boolean,
    acquiringBank: String,
    customExpiry: String,
    authenticationType: String,
    isSavedCard: Boolean,
    isPreAuth: Boolean,
    isBniPointOnly: Boolean,
    isShowAllPaymentChannels: Boolean,
    allPaymentChannels: ArrayList<ListItem>,
    whitelistBins: String,
    blacklistBins: String,
    bcaVa: String,
    bniVa: String,
    permataVa: String,
    cimbVa: String,
    directPaymentScreen: String
) {
    var installment by mutableStateOf(installment)
    var color by mutableStateOf(color)
    var isRequired by mutableStateOf(isRequired)
    var acquiringBank by mutableStateOf(acquiringBank)
    var expiry by mutableStateOf(customExpiry)
    var authenticationType by mutableStateOf(authenticationType)
    var isSavedCard by mutableStateOf(isSavedCard)
    var isPreAuth by mutableStateOf(isPreAuth)
    var isBniPointOnly by mutableStateOf(isBniPointOnly)
    var isShowAllPaymentChannels by mutableStateOf(isShowAllPaymentChannels)
    var allPaymentChannels by mutableStateOf(allPaymentChannels)
    var whitelistBins by mutableStateOf(whitelistBins)
    var blacklistBins by mutableStateOf(blacklistBins)
    var bcaVa by mutableStateOf(bcaVa)
    var bniVa by mutableStateOf(bniVa)
    var permataVa by mutableStateOf(permataVa)
    var cimbVa by mutableStateOf(cimbVa)
    var directPaymentScreen by mutableStateOf(directPaymentScreen)
}