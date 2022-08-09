package com.midtrans.sdk.uikit.internal.presentation.creditcard

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
import androidx.constraintlayout.compose.ConstraintLayout
import com.midtrans.sdk.corekit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.view.*
import kotlinx.android.parcel.Parcelize
import kotlin.math.min

class CreditCardActivity : BaseActivity() {

//    @Inject
//    lateinit var viewModel: CreditCardViewModel

    //TODO: need remember or not?
//    val state = NormalCardItemState(
//        cardNumber = TextFieldValue(),
//        expiry = TextFieldValue(),
//        cvv = TextFieldValue(),
//        isCardNumberInvalid = false,
//        isExpiryInvalid = false,
//        isCvvInvalid = false,
//        isCardTexFieldFocused = false,
//        isExpiryTextFieldFocused = false,
//        isCvvTextFieldFocused = false,
//        principalIconId = null,
//        bankIconId = null
//    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO: ViewModel need to be fix later
//        observeBankIconLiveData()
        setContent {
            CrediCardPageStateFull(
                totalAmount = "10000",
                orderId = "Order ID #34345445554",
                customerDetail = CustomerDetail(
                    "Ari Bhakti",
                    "087788778212",
                    listOf("Jl. ABC", "Rumah DEF")
                )
            )
        }
//        viewModel.getBankName("48111111", clientKet = "VT-client-yrHf-c8Sxr-ck8tx")
    }

    @Composable
    private fun CrediCardPageStateFull(
        totalAmount: String,
        orderId: String,
        customerDetail: CustomerDetail
    ) {
        val state = remember {
            NormalCardItemState(
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
        }
        var isExpandingState by remember { mutableStateOf(false) }
        var isCheckedState by remember { mutableStateOf(true) }

        CreditCardPageStateLess(
            normalCardItemState = state,
            isSnapOverlayExpandingState = isExpandingState,
            isCheckedState = isCheckedState,
            totalAmount = totalAmount,
            orderId = orderId,
            customerDetail = customerDetail,
            onExpandingChange = {
                isExpandingState = it
            },
            onCheckedChange = {
                isCheckedState = it
            }
        )
    }

    @Composable
    private fun CreditCardPageStateLess(
        normalCardItemState: NormalCardItemState,
        isSnapOverlayExpandingState: Boolean,
        isCheckedState: Boolean,
        totalAmount: String,
        orderId: String,
        customerDetail: CustomerDetail,
        onExpandingChange: (Boolean) -> Unit,
        onCheckedChange: (Boolean) -> Unit
    ){
        Column(
            modifier = Modifier
                .background(SnapColors.getARGBColor(SnapColors.OVERLAY_WHITE))
        ) {
            SnapAppBar(title = "Credit Card", iconResId = R.drawable.ic_arrow_left) {
                onBackPressed()
            }
            SnapOverlayExpandingBox(
                isExpanded = isSnapOverlayExpandingState,
                mainContent = {
                    SnapTotal(
                        amount = totalAmount,
                        orderId = orderId,
                        remainingTime = null
                    ) {
                        onExpandingChange(it)
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
                    ConstraintLayout(
                    ) {
                        Spacer(modifier = Modifier
                            .padding(24.dp)
                            .fillMaxHeight())

                        val (snapButton, checkBox, normalCardItem) = createRefs()
                        NormalCardItem(
                            modifier = Modifier
                                .padding(top = 24.dp)
                                .constrainAs(normalCardItem){
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                },
                            state = normalCardItemState,
                            onCardNumberValueChange = {
                                normalCardItemState.cardNumber = it
                                var cardNumber = it.text.replace(" ", "")
                                if(cardNumber.length >= 8){
                                    var eightDigitNumber = cardNumber.substring(8)

                                    //TODO: Fix when corekit available
                                    normalCardItemState.bankIconId = getBankIcon(getBankName(it).toString())
//                                    viewModel.getBankName(
//                                        binNumber = eightDigitNumber,
//                                        clientKet = "VT-client-yrHf-c8Sxr-ck8tx"
//                                    )
                                }
                            },
                            onExpiryDateValueChange = { normalCardItemState.expiry = it },
                            onCvvValueChange = { normalCardItemState.cvv = it },
                            onCardTextFieldFocusedChange = { normalCardItemState.isCardTexFieldFocused = it },
                            onExpiryTextFieldFocusedChange = { normalCardItemState.isExpiryTextFieldFocused = it },
                            onCvvTextFieldFocusedChange = { normalCardItemState.isCvvTextFieldFocused = it }
                        )
                        Row(
                            modifier = Modifier
                                .padding(top = 19.dp)
                                .constrainAs(checkBox) {
                                    top.linkTo(normalCardItem.bottom)
                                    start.linkTo(parent.start)
                                }
                        ) {
                            LabelledCheckBox(checked = isCheckedState,
                                onCheckedChange = {
                                    onCheckedChange(it)
                                },
                                label = "Save this card"
                            )
                        }

                        SnapButton(
                            text = "Bayar",
                            style = SnapButton.Style.PRIMARY,
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .constrainAs(snapButton) {
                                    bottom.linkTo(parent.bottom)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                },
                            enabled = !(normalCardItemState.isCardNumberInvalid ||
                                    normalCardItemState.isExpiryInvalid ||
                                    normalCardItemState.isCvvInvalid ||
                                    normalCardItemState.cardNumber.text.isEmpty() ||
                                    normalCardItemState.expiry.text.isEmpty() ||
                                    normalCardItemState.cvv.text.isEmpty()),
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

//    private fun observeBankIconLiveData(){
//        viewModel.bankIconLiveData.observe(this) {
//            state.bankIconId = getBankIcon(it)
//        }
//    }


    //TODO: Might need to move this to viewmodel
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

    //TODO: Might need to move this to viewmodel
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
        CrediCardPageStateFull(
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