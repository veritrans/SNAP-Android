package com.midtrans.sdk.sample.presentation.config

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.midtrans.sdk.sample.model.ListItem
import com.midtrans.sdk.sample.presentation.config.component.BasicDropdownMenu
import com.midtrans.sdk.sample.presentation.config.component.CustomTextField
import com.midtrans.sdk.sample.presentation.shop.ProductListActivity
import com.midtrans.sdk.sample.util.DemoConstant.ACQUIRING_BANK
import com.midtrans.sdk.sample.util.DemoConstant.BCA
import com.midtrans.sdk.sample.util.DemoConstant.BNI
import com.midtrans.sdk.sample.util.DemoConstant.BNI_POINT_ONLY
import com.midtrans.sdk.sample.util.DemoConstant.BRI
import com.midtrans.sdk.sample.util.DemoConstant.CIMB
import com.midtrans.sdk.sample.util.DemoConstant.COLOR_BLUE
import com.midtrans.sdk.sample.util.DemoConstant.COLOR_DEFAULT
import com.midtrans.sdk.sample.util.DemoConstant.COLOR_GREEN
import com.midtrans.sdk.sample.util.DemoConstant.COLOR_RED
import com.midtrans.sdk.sample.util.DemoConstant.COLOR_THEME
import com.midtrans.sdk.sample.util.DemoConstant.CREDIT_CARD_PAYMENT_TYPE
import com.midtrans.sdk.sample.util.DemoConstant.CUSTOM_BCA_VA
import com.midtrans.sdk.sample.util.DemoConstant.CUSTOM_BNI_VA
import com.midtrans.sdk.sample.util.DemoConstant.CUSTOM_EXPIRY
import com.midtrans.sdk.sample.util.DemoConstant.CUSTOM_PERMATA_VA
import com.midtrans.sdk.sample.util.DemoConstant.DISABLED
import com.midtrans.sdk.sample.util.DemoConstant.ENABLED
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
import com.midtrans.sdk.sample.util.DemoConstant.PRE_AUTH
import com.midtrans.sdk.sample.util.DemoConstant.REQUIRED
import com.midtrans.sdk.sample.util.DemoConstant.SHOW_ALL
import com.midtrans.sdk.sample.util.DemoConstant.SHOW_ALL_PAYMENT_CHANNELS
import com.midtrans.sdk.sample.util.DemoConstant.SHOW_SELECTED_ONLY
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
        val booleanList = listOf(DISABLED, ENABLED)
        val showPaymentList = listOf(SHOW_ALL, SHOW_SELECTED_ONLY)

        val state = remember {
            InputState(
                isRequired = false,
                installment = NO_INSTALLMENT,
                acquiringBank = NO_ACQUIRING_BANK,
                color = COLOR_DEFAULT,
                customExpiry = NONE,
                ccPaymentType = NORMAL_CC_PAYMENT,
                isPreAuth = false,
                isBniPointOnly = false,
                isShowAllPaymentChannels = true,
                allPaymentChannels = ArrayList(),
                bcaVa = "",
                bniVa = "",
                permataVa = ""
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
            BasicDropdownMenu(
                title = SHOW_ALL_PAYMENT_CHANNELS,
                optionList = showPaymentList,
                state = state
            )
            val openDialog = remember { mutableStateOf(true) }
            var items by remember {
                mutableStateOf(
                    listOf(
                        ListItem("Credit Card","credit_card", isSelected = false),
                        ListItem("BCA VA","bca_va", isSelected = false),
                        ListItem("Mandiri VA","echannel", isSelected = false),
                        ListItem("BNI VA","bni_va", isSelected = false),
                        ListItem("Permata VA","permata_va", isSelected = false),
                        ListItem("BRI VA","bri_va", isSelected = false),
                        ListItem("Other VA","other_va", isSelected = false),
                        ListItem("Gopay","gopay", isSelected = false),
                        ListItem("ShopeePay","shopeepay", isSelected = false),
                        ListItem("KlikBCA","bca_klikbca", isSelected = false),
                        ListItem("BCA KlikPay","bca_klikpay", isSelected = false),
                        ListItem("Mandiri Clickpay","mandiri_ecash", isSelected = false),
                        ListItem("BriMo","bri_epay", isSelected = false),
                        ListItem("CimbClicks","cimb_clicks", isSelected = false),
                        ListItem("Danamon Online Banking","danamon_online", isSelected = false),
                        ListItem("Indomaret","indomaret", isSelected = false),
                        ListItem("Alfamart","alfamart", isSelected = false),
                        ListItem("Akulaku","akulaku", isSelected = false),
                    )
                )
            }
            var list by remember {
                mutableStateOf(
                    arrayListOf<String>()
                )
            }

            if (!state.isShowAllPaymentChannels) {

                if (openDialog.value) {
                    AlertDialog(
                        onDismissRequest = {
                            openDialog.value = false
                        },
                        title = {
                            Text(text = "Title")
                        },
                        text = {
                            LazyColumn(Modifier.weight(1f)) {
                                items(items.size) { i ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                items = items.mapIndexed { j, item ->
                                                    if (i == j) {
                                                        item.copy(isSelected = !item.isSelected)
                                                    } else item
                                                }
                                            }
                                            .padding(16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = items[i].title)
                                        if (items[i].isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = "Selected",
                                                tint = Color.Green,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                }
                            }
                            val itemList = items.filter { it.isSelected }
                            for (i in itemList) {
                                list.add(i.type)
                            }
                        },
                        buttons = {
                            Row(
                                modifier = Modifier.padding(all = 8.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Button(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = { openDialog.value = false }
                                ) {
                                    Text("Confirm")
                                }
                            }
                        }
                    )
                }
                Text(text = ArrayList(list.distinct()).toString())
                state.allPaymentChannels = ArrayList(list.distinct())
            } else {
                list = arrayListOf()
            }

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
                        state.ccPaymentType,
                        state.isPreAuth,
                        state.isBniPointOnly,
                        state.isShowAllPaymentChannels,
                        state.allPaymentChannels,
                        state.bcaVa,
                        state.bniVa,
                        state.permataVa,
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
    ccPaymentType: String,
    isPreAuth: Boolean,
    isBniPointOnly: Boolean,
    isShowAllPaymentChannels: Boolean,
    allPaymentChannels: ArrayList<String>,
    bcaVa: String,
    bniVa: String,
    permataVa: String
) {
    var installment by mutableStateOf(installment)
    var color by mutableStateOf(color)
    var isRequired by mutableStateOf(isRequired)
    var acquiringBank by mutableStateOf(acquiringBank)
    var expiry by mutableStateOf(customExpiry)
    var ccPaymentType by mutableStateOf(ccPaymentType)
    var isPreAuth by mutableStateOf(isPreAuth)
    var isBniPointOnly by mutableStateOf(isBniPointOnly)
    var isShowAllPaymentChannels by mutableStateOf(isShowAllPaymentChannels)
    var allPaymentChannels by mutableStateOf(allPaymentChannels)
    var bcaVa by mutableStateOf(bcaVa)
    var bniVa by mutableStateOf(bniVa)
    var permataVa by mutableStateOf(permataVa)
}