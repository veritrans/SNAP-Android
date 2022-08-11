package com.midtrans.sdk.uikit.internal.presentation.creditcard

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.midtrans.sdk.corekit.api.callback.Callback
import com.midtrans.sdk.corekit.api.exception.SnapError
import com.midtrans.sdk.corekit.api.model.CardTokenResponse
import com.midtrans.sdk.corekit.api.model.PaymentType
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.api.requestbuilder.cardtoken.NormalCardTokenRequestBuilder
import com.midtrans.sdk.corekit.api.requestbuilder.payment.CreditCardPaymentRequestBuilder
import com.midtrans.sdk.corekit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.di.DaggerUiKitComponent
import com.midtrans.sdk.uikit.internal.view.*
import kotlinx.android.parcel.Parcelize
import javax.inject.Inject

class CreditCardActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: CreditCardViewModel

    var previousEightDigitNumber = ""
    var cardNumber = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO: ViewModel need to be fix later
        DaggerUiKitComponent.builder().applicationContext(this.applicationContext).build().inject(this)
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
                isSavedCardChecked = true,
                principalIconId = null,
                bankIconId = null,
            )
        }

//        val bankCode: Int by viewModel.bankIconId.observeAsState(null)
        val bankCodeId by viewModel.bankIconId.observeAsState(null)

        var isExpanding by remember { mutableStateOf(false) }

        CreditCardPageStateLess(
            state = state,
            isExpandingState = isExpanding,
            totalAmount = totalAmount,
            orderId = orderId,
            customerDetail = customerDetail,
            bankCodeState = bankCodeId,
            onExpand = { isExpanding = it },
        )
    }

    @Composable
    private fun CreditCardPageStateLess(
        state: NormalCardItemState,
        isExpandingState: Boolean,
        totalAmount: String,
        orderId: String,
        customerDetail: CustomerDetail,
        bankCodeState: Int?,
        onExpand: (Boolean) -> Unit
    ){
        Column() {
            SnapAppBar(title = "Credit Card", iconResId = R.drawable.ic_arrow_left) {
                onBackPressed()
            }
            val scrollState = rememberScrollState()

            SnapOverlayExpandingBox(
                isExpanded = isExpandingState,
                mainContent = {
                    SnapTotal(
                        amount = totalAmount,
                        orderId = orderId,
                        remainingTime = null
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
                        modifier = Modifier
                            .verticalScroll(scrollState)
                            .background(SnapColors.getARGBColor(SnapColors.OVERLAY_WHITE))
                    ) {

                        NormalCardItem(
                            state = state,
                            bankIcon = bankCodeState,
                            onCardNumberValueChange = {
                                state.cardNumber = it
                                cardNumber = it.text.replace(" ", "")

                                //TODO:Find a more elegant logic for exbin and to set/get livedata on viewModel
                                if(cardNumber.length >= 8){
                                    var eightDigitNumber = cardNumber.substring(0, 8)
                                    if (eightDigitNumber != previousEightDigitNumber){
                                        previousEightDigitNumber = eightDigitNumber
                                        viewModel.getBankName(
                                            binNumber = eightDigitNumber,
                                            clientKey = "VT-client-yrHf-c8Sxr-ck8tx"
                                        )
                                    }
                                } else {
                                    viewModel.bankIconId.value = null
                                    previousEightDigitNumber = cardNumber
                                }
                            },
                            onExpiryDateValueChange = { state.expiry = it },
                            onCvvValueChange = { state.cvv = it },
                            onCardTextFieldFocusedChange = { state.isCardTexFieldFocused = it },
                            onExpiryTextFieldFocusedChange = { state.isExpiryTextFieldFocused = it },
                            onCvvTextFieldFocusedChange = { state.isCvvTextFieldFocused = it },
                            onSavedCardCheckedChange = { state.isSavedCardChecked = it }
                        )

                        SnapButton(
                            text = "Bayar",
                            style = SnapButton.Style.PRIMARY,
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(16.dp),
                            enabled = !(state.isCardNumberInvalid ||
                                    state.isExpiryInvalid ||
                                    state.isCvvInvalid ||
                                    state.cardNumber.text.isEmpty() ||
                                    state.expiry.text.isEmpty() ||
                                    state.cvv.text.isEmpty()),
                            onClick = {

                                viewModel.getCardToken(
                                    cardTokenRequestBuilder = NormalCardTokenRequestBuilder()
                                        .withClientKey("VT-client-yrHf-c8Sxr-ck8tx")
                                        .withGrossAmount(150000.0)
                                        .withCardNumber(getCardNumberFromTextField(state.cardNumber))
                                        .withCardExpMonth(getExpMonthFromTextField(state.expiry))
                                        .withCardExpYear(getExpYearFromTextField(state.expiry))
                                        .withCardCvv(state.cvv.text)
                                        .withOrderId("cobacoba-4")
                                        .withCurrency("IDR"),
                                    callback = object : Callback<CardTokenResponse>{
                                        override fun onSuccess(result: CardTokenResponse) {

                                            var ccRequestBuilder = CreditCardPaymentRequestBuilder()
                                                .withSaveCard(state.isSavedCardChecked)
                                                .withPaymentType(PaymentType.CREDIT_CARD)
                                                .withCustomerEmail("belajar@example.com")

                                            result?.tokenId?.let {
                                                ccRequestBuilder.withCardToken(it)
                                            }
                                            viewModel.chargeUsingCard(
                                                snapToken = "b8e891f7-4515-49be-9b4d-95243e93140e",
                                                paymentRequestBuilder = ccRequestBuilder,
                                                callback = object : Callback<TransactionResponse> {
                                                    override fun onSuccess(result: TransactionResponse) {
                                                        Toast.makeText(
                                                            this@CreditCardActivity,
                                                            "payment is done, and the status is ${result.transactionStatus}",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                    override fun onError(error: SnapError) {
                                                        Log.e("error, error, error", "error, error, error")
                                                    }
                                                }
                                            )
                                        }
                                        override fun onError(error: SnapError) {
                                            Log.e("error, error, error", "error, error, error")
                                        }
                                    }
                                )
                            }
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(all = 16.dp)
            )
        }
    }

    private  fun getCardNumberFromTextField(value: TextFieldValue) : String{
        return value.text.replace(" ", "")
    }
    private  fun getExpMonthFromTextField(value: TextFieldValue) : String{
        return value.text.substring(0, 2)
    }
    private  fun getExpYearFromTextField(value: TextFieldValue) : String{
        return return value.text.substring(3, 5)
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