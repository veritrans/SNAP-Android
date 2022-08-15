package com.midtrans.sdk.uikit.internal.presentation.creditcard

import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.midtrans.sdk.corekit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.view.*
import kotlinx.android.parcel.Parcelize


//TODO: Need to fix this later when implementing Saved Card
class SavedCardActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO: ViewModel need to be fix later
//        observeBankIconLiveData()
        setContent {
            CrediCardPageStateFull(
                totalAmount = "10000",
                orderId = "Order ID #34345445554",
                customerDetail = SavedCardActivity.CustomerDetail(
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
        customerDetail: SavedCardActivity.CustomerDetail
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
                isSavedCardChecked = true
            )
        }

        var isExpanding by remember { mutableStateOf(false) }

        CreditCardPageStateLess(
            state = state,
            isExpandingState = isExpanding,
            totalAmount = totalAmount,
            orderId = orderId,
            customerDetail = customerDetail,
            onExpand = { isExpanding = it }
        )
    }

    @Composable
    private fun CreditCardPageStateLess(
        state: NormalCardItemState,
        isExpandingState: Boolean,
        totalAmount: String,
        orderId: String,
        customerDetail: SavedCardActivity.CustomerDetail,
        onExpand: (Boolean) -> Unit,
    ){

        var list = mutableListOf(

            SavedCreditCardFormData(
                title = "satu",
                inputTitle = "Masukkan CVV",
                endIcon = R.drawable.ic_trash,
                startIcon = R.drawable.ic_outline_bca_24,
                errorText = remember { mutableStateOf("") },
                maskedCardNumber = "123***********345"
            ),
            SavedCreditCardFormData(
                title = "dua",
                inputTitle = "Masukkan CVV",
                endIcon = R.drawable.ic_trash,
                startIcon = R.drawable.ic_outline_bni_24,
                errorText = remember { mutableStateOf("") },
                maskedCardNumber = "123***********345"
            ),
            SavedCreditCardFormData(
                title = "tiga",
                inputTitle = "Masukkan CVV",
                endIcon = R.drawable.ic_trash,
                startIcon = R.drawable.ic_outline_mandiri_24,
                errorText = remember { mutableStateOf("") },
                maskedCardNumber = "123***********345"
            ),
            SavedCreditCardFormData(
                title = "satu",
                inputTitle = "Masukkan CVV",
                endIcon = R.drawable.ic_trash,
                startIcon = R.drawable.ic_outline_bca_24,
                errorText = remember { mutableStateOf("") },
                maskedCardNumber = "123***********345"
            ),
            SavedCreditCardFormData(
                title = "dua",
                inputTitle = "Masukkan CVV",
                endIcon = R.drawable.ic_trash,
                startIcon = R.drawable.ic_outline_bni_24,
                errorText = remember { mutableStateOf("") },
                maskedCardNumber = "123***********345"
            ),
            SavedCreditCardFormData(
                title = "tiga",
                inputTitle = "Masukkan CVV",
                endIcon = R.drawable.ic_trash,
                startIcon = R.drawable.ic_outline_mandiri_24,
                errorText = remember { mutableStateOf("") },
                maskedCardNumber = "123***********345"
            ),
            NewCardFormData(
                title = "new",
                isCardNumberInvalid = remember { mutableStateOf(false) },
                bankIconId = remember { mutableStateOf(R.drawable.ic_outline_bri_24) },
                isCvvInvalid = remember { mutableStateOf(false) },
                isExpiryDateInvalid = remember { mutableStateOf(false) },
                principalIconId = remember { mutableStateOf(R.drawable.ic_outline_visa_24) }
            )

        ).toMutableStateList()

        Column(
            modifier = Modifier.background(SnapColors.getARGBColor(SnapColors.OVERLAY_WHITE))
        ) {
            SnapAppBar(
                title = "Credit Card",
                iconResId = R.drawable.ic_arrow_left
            ) {
                onBackPressed()
            }
            var scrollState = rememberScrollState()
            SnapOverlayExpandingBox(
                isExpanded = isExpandingState,
                mainContent = {
                    SnapTotal(
                        amount = totalAmount,
                        orderId = orderId,
                        remainingTime = null,
                        canExpand = isExpandingState
                    ) {
                        onExpand(it)
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
                        modifier = Modifier.verticalScroll(scrollState)
                    ) {
                        SnapSavedCardRadioGroup(
                            modifier = Modifier
                                .padding(top = 24.dp),
                            states = list,
                            state = state,
                            onValueChange ={selected: String, value: String ->} ,
                            onItemRemoveClicked = {},
                            onCvvValueChange = {},
                            onCardNumberValueChange = {},
                            onExpiryDateValueChange = {},
                            onCardTextFieldFocusedChange = {},
                            onExpiryTextFieldFocusedChange = {},
                            onCvvTextFieldFocusedChange = {}
                        )
                        SnapButton(
                            text = "Bayar",
                            style = SnapButton.Style.PRIMARY,
                            modifier = Modifier
                                .fillMaxWidth(1f),
                            enabled = !(state.isCardNumberInvalid ||
                                    state.isExpiryInvalid ||
                                    state.isCvvInvalid ||
                                    state.cardNumber.text.isEmpty() ||
                                    state.expiry.text.isEmpty() ||
                                    state.cvv.text.isEmpty()),
                            onClick = {
                                Toast.makeText(
                                    this@SavedCardActivity,
                                    "button clicked",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxHeight(1f)
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            )
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