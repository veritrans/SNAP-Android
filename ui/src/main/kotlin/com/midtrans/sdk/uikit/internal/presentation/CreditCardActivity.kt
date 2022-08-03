package com.midtrans.sdk.uikit.internal.presentation

import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.midtrans.sdk.corekit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.view.*
import kotlinx.android.parcel.Parcelize
import kotlin.math.min

class CreditCardActivity : BaseActivity() {

    private val viewModel: CreditCardViewModel by lazy {
        ViewModelProvider(this).get(CreditCardViewModel::class.java)
    }

    //TODO: need remember or not?
    val state = NormalCardItemState(
            cardNumber = TextFieldValue(),
            expiry = TextFieldValue(),
            cvv = TextFieldValue(),
            isCardNumberInvalid = false,
            isExpiryInvalid = false,
            isCvvInvalid = false,
            isCardTexFieldFocused = false,
            isExpiryTextFieldFocused = false,
            isCvvTextFieldFocused = false,
            principalIconId = null,
            bankIconId = null
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO: ViewModel need to be fix later
//        observeBankIconLiveData()
        setContent {
            CrediCardPage(
                totalAmount = "10000",
                orderId = "Order ID #34345445554",
                customerDetail = CustomerDetail(
                    "Ari Bhakti",
                    "087788778212",
                    listOf("Jl. ABC", "Rumah DEF")
                )
            )
        }
    }

    @Composable
    private fun CrediCardPage(

        totalAmount: String,
        orderId: String,
        customerDetail: CustomerDetail,
    ) {

        var isExpand by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .background(SnapColors.getARGBColor(SnapColors.OVERLAY_WHITE))
        ) {
            SnapAppBar(title = "Credit Card", iconResId = R.drawable.ic_arrow_left) {
                onBackPressed()
            }
            SnapOverlayExpandingBox(
                isExpanded = isExpand,
                mainContent = {
                    SnapTotal(
                        amount = totalAmount,
                        orderId = orderId,
                        remainingTime = null
                    ) {
                        isExpand = it
                    }
                },
                expandingContent = {
                    SnapCustomerDetail(
                        name = customerDetail.name,
                        phone = customerDetail.phone,
                        addressLines = customerDetail.addressLines
                    )
                },
                followingContent = {
                    Column(
                    ) {
                        Spacer(modifier = Modifier.padding(24.dp))
                        NormalCardItem(
                            state = state,
                            onCardNumberValueChange = {
                                state.cardNumber = it
//
                                var cardNumber = it.text.replace(" ", "")

                                if(cardNumber.length >= 8){
                                    var eightDigitNumber = cardNumber.substring(8)

                                    //TODO: Fix when corekit available
                                    state.bankIconId = getBankIcon(getBankName(it).toString())
//                                    viewModel.updateBankIcon(eightDigitNumber)
                                }
                            },
                            onExpiryDateValueChange = {
                                state.expiry = it
                            },
                            onCvvValueChange = {
                                state.cvv = it
                            },
                            onCardTextFieldFocusedChange = { state.isCardTexFieldFocused = it},
                            onExpiryTextFieldFocusedChange = { state.isExpiryTextFieldFocused = it},
                            onCvvTextFieldFocusedChange = { state.isCvvTextFieldFocused = it}
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        SnapButton(
                            text = "Bayar",
                            style = SnapButton.Style.PRIMARY,
                            modifier = Modifier.fillMaxWidth(1f),
                            enabled = !(state.isCardNumberInvalid ||
                                    state.isExpiryInvalid ||
                                    state.isCvvInvalid ||
                                    state.cardNumber.text.isEmpty() ||
                                    state.expiry.text.isEmpty() ||
                                    state.cvv.text.isEmpty()),
                            onClick = {
                                Toast.makeText(
                                    this@CreditCardActivity,
                                    "button clicked",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxHeight(1f)
                    .padding(all = 16.dp)
                    .background(SnapColors.getARGBColor(SnapColors.OVERLAY_WHITE))
            )
        }
    }

    private fun observeBankIconLiveData(){
        viewModel.bankIconLiveData.observe(this) {
            state.bankIconId = getBankIcon(it)
        }
    }

    fun getBankName(cardNumber: TextFieldValue): String? {
        val length = min(cardNumber.text.length, 8)
        val output = cardNumber.copy(cardNumber.text.substring(0 until length), TextRange(length))

        return when (output.text) {
            "4111" -> "bri"
            "41111" -> "bni"
            "4111 1" -> "mandiri"
            "41" -> "cimb"
            "42" -> "mega"
            "43" -> "bca"
            else -> null
        }
    }

    fun getBankIcon(bank: String): Int? {
        return when (bank) {
            "bri" -> R.drawable.ic_outline_bri_24
            "bni" -> R.drawable.ic_bank_bni_24
            "mandiri" -> R.drawable.ic_bank_mandiri_24
            "bca" -> R.drawable.ic_bank_bca_24
            "cimb" -> R.drawable.ic_bank_cimb_24
            "mega" -> R.drawable.ic_bank_mega_24
            else -> null
        }
    }

    @Preview
    @Composable
    private fun forPreview() {
        CrediCardPage(
            totalAmount = "10000",
            orderId = "Order ID #34345445554",
            customerDetail = CustomerDetail(
                "Ari Bhakti",
                "087788778212",
                listOf("Jl. ABC", "Rumah DEF")
            )
        )
    }

    @Parcelize
    private data class CustomerDetail(
        val name: String,
        val phone: String,
        val addressLines: List<String>
    ) : Parcelable
}