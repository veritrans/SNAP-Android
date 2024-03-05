package com.midtrans.sdk.sample.presentation.config.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.midtrans.sdk.corekit.core.PaymentMethod
import com.midtrans.sdk.sample.model.ListItem
import com.midtrans.sdk.sample.presentation.config.InputState
import com.midtrans.sdk.sample.util.DemoConstant.ACQUIRING_BANK
import com.midtrans.sdk.sample.util.DemoConstant.BLACKLIST_BINS
import com.midtrans.sdk.sample.util.DemoConstant.BNI_POINT_ONLY
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
import com.midtrans.sdk.sample.util.DemoConstant.INSTALLMENT
import com.midtrans.sdk.sample.util.DemoConstant.IS_INSTALLMENT_REQUIRED
import com.midtrans.sdk.sample.util.DemoConstant.OPTIONAL
import com.midtrans.sdk.sample.util.DemoConstant.PRE_AUTH
import com.midtrans.sdk.sample.util.DemoConstant.SAVED_CARD
import com.midtrans.sdk.sample.util.DemoConstant.SHOW_ALL
import com.midtrans.sdk.sample.util.DemoConstant.SHOW_ALL_PAYMENT_CHANNELS
import com.midtrans.sdk.sample.util.DemoConstant.WHITELIST_BINS
import com.midtrans.sdk.uikit.api.model.PaymentType
import com.midtrans.sdk.uikit.internal.view.SnapTextField
import com.midtrans.sdk.uikit.internal.view.SnapTypography

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BasicDropdownMenu(title: String, optionList: List<String>, state: InputState, paymentMethod: PaymentMethod? = null) {
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
                                IS_INSTALLMENT_REQUIRED -> state.isRequired = selectedOptionText != OPTIONAL
                                INSTALLMENT-> state.installment = selectedOptionText
                                ACQUIRING_BANK-> state.acquiringBank = selectedOptionText
                                COLOR_THEME -> state.color = selectedOptionText
                                CUSTOM_EXPIRY -> state.expiry = selectedOptionText
                                CREDIT_CARD_AUTHENTICATION -> state.authenticationType = selectedOptionText
                                SAVED_CARD -> state.isSavedCard = selectedOptionText == ENABLED
                                PRE_AUTH -> state.isPreAuth = selectedOptionText == ENABLED
                                BNI_POINT_ONLY -> state.isBniPointOnly = selectedOptionText == ENABLED
                                SHOW_ALL_PAYMENT_CHANNELS -> state.isShowAllPaymentChannels = selectedOptionText == SHOW_ALL
                                DIRECT_PAYMENT_SCREEN_ENABLED -> state.directPaymentScreen = selectedOptionText

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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AlertDialogDropdownMenu(
    title: String,
    optionList: List<String>,
    state: InputState,
    openDialog: MutableState<Boolean>
) {
    val options by remember { mutableStateOf(optionList) }
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }
    var items by remember {
        mutableStateOf(
            listOf(
                ListItem("Credit Card", PaymentType.CREDIT_CARD, isSelected = false),

                ListItem("BCA VA", PaymentType.BCA_VA, isSelected = false),
                ListItem("Mandiri VA", PaymentType.E_CHANNEL, isSelected = false),
                ListItem("BNI VA", PaymentType.BNI_VA, isSelected = false),
                ListItem("Permata VA", PaymentType.PERMATA_VA, isSelected = false),
                ListItem("BRI VA", PaymentType.BRI_VA, isSelected = false),
                ListItem("CIMB VA", PaymentType.CIMB_VA, isSelected = false),
                ListItem("Other VA", PaymentType.OTHER_VA, isSelected = false),

                ListItem("Gopay", PaymentType.GOPAY, isSelected = false),
                ListItem("ShopeePay", PaymentType.SHOPEEPAY, isSelected = false),

                ListItem("KlikBCA", PaymentType.KLIK_BCA, isSelected = false),
                ListItem("BCA KlikPay", PaymentType.BCA_KLIKPAY, isSelected = false),
                ListItem("BriMo", PaymentType.BRI_EPAY, isSelected = false),
                ListItem("CimbClicks", PaymentType.CIMB_CLICKS, isSelected = false),
                ListItem("Danamon Online Banking", PaymentType.DANAMON_ONLINE, isSelected = false),

                ListItem("Indomaret", PaymentType.INDOMARET, isSelected = false),
                ListItem("Alfamart", PaymentType.ALFAMART, isSelected = false),

                ListItem("Akulaku", PaymentType.AKULAKU, isSelected = false),
                ListItem("UOB", PaymentType.UOB_EZPAY, isSelected = false),
            )
        )
    }
    var list by remember {
        mutableStateOf(arrayListOf<String>())
    }
    var itemList by remember {
        mutableStateOf(listOf<ListItem>())
    }

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
                                SHOW_ALL_PAYMENT_CHANNELS -> {
                                    state.isShowAllPaymentChannels = selectedOptionText == SHOW_ALL
                                    if (!state.isShowAllPaymentChannels) {
                                        openDialog.value = true
                                    }
                                }
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


    if (!state.isShowAllPaymentChannels) {
        if (openDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    openDialog.value = false
                },
                title = {
                    Text(text = "Payment Channels")
                },
                text = {
                    Column {
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
                    }
                },
                buttons = {
                    Row(
                        modifier = Modifier.padding(all = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                openDialog.value = false
                            }
                        ) {
                            Text("Confirm")
                        }
                    }
                }
            )
        }
        itemList = items.filter { it.isSelected }
        for(i in itemList){
            Text(text = i.title)
        }
        state.allPaymentChannels = ArrayList(itemList.distinct())
    } else {
        list = arrayListOf()
    }
}

@Composable
fun CustomTextField(title: String, state: InputState, modifier: Modifier = Modifier) {
    var textField by remember { mutableStateOf(TextFieldValue()) }
    var textFieldFocused by remember { mutableStateOf(false) }

    Column {
        Text(
            modifier = Modifier.padding(top = 16.dp, bottom = 20.dp),
            text = title,
            style = SnapTypography.STYLES.snapTextMediumMedium
        )
        SnapTextField(
            value = textField,
            onValueChange = {
                textField = it
                when (title) {
                    WHITELIST_BINS -> state.whitelistBins = textField.text
                    BLACKLIST_BINS -> state.blacklistBins = textField.text
                    CUSTOM_BCA_VA -> state.bcaVa = textField.text
                    CUSTOM_BNI_VA -> state.bniVa = textField.text
                    CUSTOM_PERMATA_VA -> state.permataVa = textField.text
                    CUSTOM_CIMB_VA -> state.cimbVa = textField.text
                }
            },
            isFocused = textFieldFocused,
            onFocusChange = {
                textFieldFocused = it
            },
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        )

        if (state.bcaVa.length > 11 && title == CUSTOM_BCA_VA){
            Text(text = "Numbers only. Length should be within 1 to 11.", color = Color.Red)
        }
        if (state.bniVa.length > 8 && title == CUSTOM_BNI_VA){
            Text(text = "Numbers only. Length should be within 1 to 8.", color = Color.Red)
        }
        if (state.permataVa.isNotEmpty() && state.permataVa.length != 10 && title == CUSTOM_PERMATA_VA){
            Text(text = "Numbers only. Length should be 10.", color = Color.Red)
        }
        if (state.bcaVa.length > 16 && title == CUSTOM_CIMB_VA){
            Text(text = "Numbers only. Length should be within 1 to 16.", color = Color.Red)
        }
    }
}