package com.midtrans.sdk.uikit.internal.presentation.creditcard

import android.content.Context
import android.content.Intent
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.midtrans.sdk.corekit.api.model.CreditCard
import com.midtrans.sdk.corekit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.di.DaggerUiKitComponent
import com.midtrans.sdk.uikit.internal.model.CustomerInfo
import com.midtrans.sdk.uikit.internal.view.*
import kotlinx.android.parcel.Parcelize
import javax.inject.Inject


//TODO: Need to fix the UI implementation later when implementing Saved Card
class SavedCardActivity: BaseActivity() {

    @Inject
    lateinit var viewModel: SavedCardViewModel

    private val creditCard: CreditCard? by lazy {
        intent.getParcelableExtra(SavedCardActivity.EXTRA_CREDIT_CARD) as? CreditCard
    }

    private val snapToken: String by lazy {
        intent.getStringExtra(SavedCardActivity.EXTRA_SNAP_TOKEN).orEmpty()
    }

    companion object {
        private const val EXTRA_SNAP_TOKEN = "savedCard.extra.snap_token"
        private const val EXTRA_TOTAL_AMOUNT = "savedCard.extra.total_amount"
        private const val EXTRA_ORDER_ID = "savedCard.extra.order_id"
        private const val EXTRA_CUSTOMER_DETAIL = "savedCard.extra.customer_detail"
        private const val EXTRA_CREDIT_CARD = "savedCard.extra.credit_card"

        fun getIntent(
            activityContext: Context,
            snapToken: String,
            totalAmount: String,
            orderId: String,
            customerInfo: CustomerInfo? = null,
            creditCard: CreditCard?,
        ): Intent {
            return Intent(activityContext, SavedCardActivity::class.java).apply {
                putExtra(EXTRA_SNAP_TOKEN, snapToken)
                putExtra(EXTRA_TOTAL_AMOUNT, totalAmount)
                putExtra(EXTRA_ORDER_ID, orderId)
                putExtra(EXTRA_CUSTOMER_DETAIL, customerInfo)
                putExtra(EXTRA_CREDIT_CARD, creditCard)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerUiKitComponent.builder().applicationContext(this.applicationContext).build().inject(this)
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
        var cvvTextFieldValue by remember { mutableStateOf(TextFieldValue()) }

        CreditCardPageStateLess(
            state = state,
            isExpandingState = isExpanding,
            totalAmount = totalAmount,
            orderId = orderId,
            customerDetail = customerDetail,
            cvvTextFieldValue = cvvTextFieldValue,
            onCvvTextFieldValueChange = {
                cvvTextFieldValue = it
            },
            onExpand = { isExpanding = it }
        )
    }


    private fun getBankIcon(bank: String): Int? {

        return when (bank.lowercase()) {
            "bri" -> R.drawable.ic_outline_bri_24
            "bni" -> R.drawable.ic_bank_bni_24
            "mandiri" -> R.drawable.ic_bank_mandiri_24
            "bca" -> R.drawable.ic_bank_bca_24
            "cimb" -> R.drawable.ic_bank_cimb_24
            "mega" -> R.drawable.ic_bank_mega_24
            else -> null
        }
    }

    private fun formatMaskedCard(maskedCard: String): String {
        val lastFourDigit = maskedCard.substring(startIndex = maskedCard.length - 4, endIndex = maskedCard.length)
        return "**** **** **** $lastFourDigit"
    }

    @Composable
    private fun CreditCardPageStateLess(
        state: NormalCardItemState,
        isExpandingState: Boolean,
        totalAmount: String,
        orderId: String,
        cvvTextFieldValue: TextFieldValue,
        onCvvTextFieldValueChange: (TextFieldValue) -> Unit,
        customerDetail: SavedCardActivity.CustomerDetail,
        onExpand: (Boolean) -> Unit,
    ){

        //TODO: Need to find a better way to create the savedTokenListState
        var savedTokenList = mutableListOf<FormData>()
        creditCard?.savedTokens?.forEachIndexed { index, savedToken ->
            savedTokenList.add(
                SavedCreditCardFormData(
                    title = "test$index",
                    inputTitle = "Masukkan CVV",
                    endIcon = R.drawable.ic_trash,
                    startIcon = getBankIcon(savedToken.binDetail?.bankCode.toString()),
                    errorText = remember { mutableStateOf("") },
                    maskedCardNumber = formatMaskedCard(savedToken.maskedCard.toString()),
                    displayedMaskedCard = savedToken.maskedCard.toString(),
                    tokenType = savedToken.tokenType.toString()
                )
            )
        }
        savedTokenList.add(NewCardFormData(
            title = "new",
            isCardNumberInvalid = remember { mutableStateOf(false) },
            bankIconId = remember { mutableStateOf(R.drawable.ic_outline_bri_24) },
            isCvvInvalid = remember { mutableStateOf(false) },
            isExpiryDateInvalid = remember { mutableStateOf(false) },
            principalIconId = remember { mutableStateOf(R.drawable.ic_outline_visa_24) }
        ))
        var savedTokenListState = savedTokenList.toMutableStateList()

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
                            states = savedTokenListState,
                            state = state,
                            cvvTextField = cvvTextFieldValue,
                            onValueChange ={selected: String, value: String ->
                                Toast.makeText(
                                    this@SavedCardActivity,
                                    "radio clicked, selected = $selected dan value = $value",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } ,
                            onItemRemoveClicked = {

                                viewModel.deleteSavedCard(snapToken = snapToken, maskedCard = it.displayedMaskedCard)
                                savedTokenListState.remove(it)
                                Toast.makeText(
                                    this@SavedCardActivity,
                                    "delete clicked",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onCvvValueChange = {
                                onCvvTextFieldValueChange(it)
                            },
                            onCardNumberValueChange = {},
                            onExpiryDateValueChange = {},
                            onCardTextFieldFocusedChange = {},
                            onExpiryTextFieldFocusedChange = {},
                            onCvvTextFieldFocusedChange = {},
                        )
                        SnapButton(
                            text = stringResource(id = R.string.cc_dc_main_screen_cta),
                            style = SnapButton.Style.PRIMARY,
                            modifier = Modifier
                                .fillMaxWidth(1f),
                            enabled = true,
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