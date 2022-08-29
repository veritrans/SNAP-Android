package com.midtrans.sdk.uikit.internal.presentation.creditcard

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.midtrans.sdk.uikit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.di.DaggerUiKitComponent
import com.midtrans.sdk.uikit.internal.model.CustomerInfo
import com.midtrans.sdk.uikit.internal.view.*
import javax.inject.Inject

class CreditCardActivity : BaseActivity() {

    @Inject
    lateinit var creditCardviewModel: CreditCardViewModel

    private var previousEightDigitNumber = ""
    private var cardNumberWithoutSpace = ""
    private val supportedMaxBinNumber = 8

    private val totalAmount: String by lazy {
        intent.getStringExtra(CreditCardActivity.EXTRA_TOTAL_AMOUNT)
            ?: throw RuntimeException("Total amount must not be empty")
    }

    private val orderId: String by lazy {
        intent.getStringExtra(CreditCardActivity.EXTRA_ORDER_ID)
            ?: throw RuntimeException("Order ID must not be empty")
    }

    private val customerDetail: CustomerInfo? by lazy {
        intent.getParcelableExtra(CreditCardActivity.EXTRA_CUSTOMER_DETAIL) as? CustomerInfo
    }

    private val snapToken: String by lazy {
        intent.getStringExtra(CreditCardActivity.EXTRA_SNAP_TOKEN).orEmpty()
    }

    companion object {
        private const val EXTRA_SNAP_TOKEN = "card.extra.snap_token"
        private const val EXTRA_TOTAL_AMOUNT = "card.extra.total_amount"
        private const val EXTRA_ORDER_ID = "card.extra.order_id"
        private const val EXTRA_CUSTOMER_DETAIL = "card.extra.customer_detail"

        fun getIntent(
            activityContext: Context,
            snapToken: String,
            totalAmount: String,
            orderId: String,
            customerInfo: CustomerInfo? = null,
        ): Intent {
            return Intent(activityContext, CreditCardActivity::class.java).apply {
                putExtra(EXTRA_SNAP_TOKEN, snapToken)
                putExtra(EXTRA_TOTAL_AMOUNT, totalAmount)
                putExtra(EXTRA_ORDER_ID, orderId)
                putExtra(
                    EXTRA_CUSTOMER_DETAIL,
                    customerInfo
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO: ViewModel might be better be used as parameter following the codelabs to support unidirectional data flow
        DaggerUiKitComponent.builder().applicationContext(this.applicationContext).build().inject(this)
        setContent {
            CreditCardPageStateFull(
                totalAmount = totalAmount,
                orderId = orderId,
                customerDetail = customerDetail,
                viewModel = creditCardviewModel
            )
        }
    }

    @Composable
    private fun CreditCardPageStateFull(
        totalAmount: String,
        orderId: String,
        customerDetail: CustomerInfo? = null,
        viewModel: CreditCardViewModel
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
                principalIconId = null
            )
        }

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
            onCardNumberValueChange = {

                //TODO:Find a more elegant logic for exbin and to set/get livedata on viewModel
                state.cardNumber = it
                cardNumberWithoutSpace = it.text.replace(" ", "")
                if(cardNumberWithoutSpace.length >= supportedMaxBinNumber){
                    var eightDigitNumber = cardNumberWithoutSpace.substring(0, supportedMaxBinNumber)
                    if (eightDigitNumber != previousEightDigitNumber){
                        previousEightDigitNumber = eightDigitNumber
                        viewModel.getBankIconImage(
                            binNumber = eightDigitNumber
                        )
                    }
                } else {
                    viewModel.setBankIconToNull()
                    previousEightDigitNumber = cardNumberWithoutSpace
                }
            },
            onClick = {
                viewModel.chargeUsingCreditCard(
                    grossAmount = totalAmount,
                    cardNumber = state.cardNumber,
                    cardExpiry = state.expiry,
                    cardCvv = state.cvv,
                    orderId = orderId,
                    isSavedCard = state.isSavedCardChecked,
                    customerEmail = "johndoe@midtrans.com",
                    snapToken = snapToken
                )
            }
        )
    }

    @Composable
    private fun CreditCardPageStateLess(
        state: NormalCardItemState,
        isExpandingState: Boolean,
        totalAmount: String,
        orderId: String,
        customerDetail: CustomerInfo? = null,
        bankCodeState: Int?,
        onExpand: (Boolean) -> Unit,
        onCardNumberValueChange: (TextFieldValue) -> Unit,
        onClick: () -> Unit
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
                        remainingTime = null,
                        canExpand = customerDetail != null,
                    ) {
                        onExpand(it)
                    }
                },
                expandingContent = {
                    customerDetail?.let {
                        SnapCustomerDetail(
                            name = customerDetail.name,
                            phone = customerDetail.phone,
                            addressLines = customerDetail.addressLines
                        )
                    }
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
                                onCardNumberValueChange(it)
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
                            onClick = { onClick() }
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(all = 16.dp)
            )
        }
    }

    @Preview
    @Composable
    private fun forPreview() {
        CreditCardPageStateFull(
            totalAmount = "15000",
            orderId = "1234",
            customerDetail = CustomerInfo(
                "Ari Bhakti",
                "087788778212",
                listOf("Jl. ABC", "Rumah DEF")
            ),
            viewModel = creditCardviewModel
        )
    }
}