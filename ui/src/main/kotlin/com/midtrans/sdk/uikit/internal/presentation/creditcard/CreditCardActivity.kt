package com.midtrans.sdk.uikit.internal.presentation.creditcard

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rxjava2.subscribeAsState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.midtrans.sdk.corekit.api.model.CreditCard
import com.midtrans.sdk.corekit.api.model.Promo
import com.midtrans.sdk.corekit.internal.network.model.response.Merchant
import com.midtrans.sdk.corekit.internal.network.model.response.TransactionDetails
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.internal.di.DaggerUiKitComponent
import com.midtrans.sdk.uikit.internal.model.CustomerInfo
import com.midtrans.sdk.uikit.internal.presentation.SuccessScreenActivity
import com.midtrans.sdk.uikit.internal.presentation.errorcard.ErrorCard
import com.midtrans.sdk.uikit.internal.util.CurrencyFormat.currencyFormatRp
import com.midtrans.sdk.uikit.internal.util.SnapCreditCardUtil
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import com.midtrans.sdk.uikit.internal.view.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

//TODO: refactor, state on value change listener is not required, compose will do the magic
internal class CreditCardActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: CreditCardViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(CreditCardViewModel::class.java)
    }

    private var previousEightDigitNumber = ""

    private val transactionDetails: TransactionDetails? by lazy {
        intent.getParcelableExtra(EXTRA_TRANSACTION_DETAILS) as? TransactionDetails
    }

    private val totalAmount: String by lazy {
        intent.getStringExtra(EXTRA_TOTAL_AMOUNT)
            ?: throw RuntimeException("Total amount must not be empty")
    }

    private val customerDetail: CustomerInfo? by lazy {
        intent.getParcelableExtra(EXTRA_CUSTOMER_DETAIL) as? CustomerInfo
    }

    private val creditCard: CreditCard? by lazy {
        intent.getParcelableExtra(EXTRA_CREDIT_CARD) as? CreditCard
    }

    private val snapToken: String by lazy {
        intent.getStringExtra(EXTRA_SNAP_TOKEN)
            ?: throw RuntimeException("Snap token must not be empty")
    }

    private val expiryTime: String? by lazy {
        intent.getStringExtra(EXTRA_EXPIRY_TIME)
    }

    private val withCustomerPhoneEmail: Boolean by lazy {
        merchant?.showCreditCardCustomerInfo ?: false
    }

    private val allowRetry: Boolean by lazy {
        merchant?.allowRetry ?: false
    }

    private val merchant: Merchant? by lazy {
        intent.getParcelableExtra(EXTRA_MERCHANT_DATA) as? Merchant
    }

    private val promos: List<Promo>? by lazy {
        intent.getParcelableArrayListExtra(EXTRA_PROMOS)
    }

    private val savedTokenList: SnapshotStateList<FormData>? by lazy {
        mutableListOf<FormData>()
            .apply {
                creditCard?.savedTokens?.forEachIndexed { index, savedToken ->
                    add(
                        SavedCreditCardFormData(
                            savedCardIdentifier = SnapCreditCardUtil.SAVED_CARD_IDENTIFIER + index.toString(),
                            inputTitle = getString(R.string.cc_dc_saved_card_enter_cvv),
                            endIcon = R.drawable.ic_trash,
                            startIcon = SnapCreditCardUtil.getBankIcon(savedToken.binDetail?.bankCode.toString()),
                            errorText = mutableStateOf(""),
                            maskedCardNumber = savedToken.maskedCard.orEmpty(),
                            displayedMaskedCard = savedToken.maskedCard.orEmpty(),
                            tokenType = savedToken.tokenType.toString(),
                            tokenId = savedToken.token.toString(),
                            cvvSavedCardTextField = TextFieldValue(),
                            isCvvSavedCardInvalid = false
                        )
                    )
                }
                add(NewCardFormData(newCardIdentifier = SnapCreditCardUtil.NEW_CARD_FORM_IDENTIFIER))
            }
            .ifEmpty { null }?.toMutableStateList()
        //For testing purpose: uncomment below to force non save card
//        null
    }

    private val noPromo by lazy {
        PromoData(
            identifier = "0",
            leftText = getString(R.string.cant_continue_promo_dont_want_to_use_promo),
            rightText = ""
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerUiKitComponent.builder().applicationContext(this.applicationContext).build()
            .inject(this)
        viewModel.setExpiryTime(expiryTime)
        viewModel.setAllowRetry(allowRetry)
        viewModel.setPromos(promos = promos)
        viewModel.setTransactionDetails(transactionDetails)
        viewModel.creditCard = creditCard
        initTransactionResultScreenObserver()
        setContent {
            CreditCardPageStateFull(
                transactionDetails = transactionDetails,
                customerDetail = customerDetail,
                savedTokenListState = savedTokenList,
                creditCard = creditCard,
                viewModel = viewModel,
                bankCodeIdState = viewModel.bankIconId.observeAsState(null),
                binType = viewModel.binType.observeAsState(null),
                cardIssuerBank = viewModel.cardIssuerBank.observeAsState(null),
                totalAmount = viewModel.netAmountLiveData.observeAsState(initial = totalAmount),
                remainingTimeState = updateExpiredTime().subscribeAsState(initial = "00:00"),
                withCustomerPhoneEmail = withCustomerPhoneEmail,
                errorTypeState = viewModel.errorLiveData.observeAsState(initial = null),
                promoState = viewModel.promoDataLiveData.observeAsState(initial = null)
            )
        }
    }

    private fun initTransactionResultScreenObserver() {
        viewModel.transactionResponseLiveData.observe(this) {
            if (it.statusCode != UiKitConstants.STATUS_CODE_201 && it.redirectUrl.isNullOrEmpty()) {
                val intent = SuccessScreenActivity.getIntent(
                    activityContext = this@CreditCardActivity,
                    total = it.grossAmount?.currencyFormatRp().orEmpty(),
                    orderId = it?.orderId.toString()
                )
                resultLauncher.launch(intent)
            }
        }
        viewModel.transactionStatusLiveData.observe(this) {
            val intent: Intent
            when (it.statusCode) {
                UiKitConstants.STATUS_CODE_200 -> {
                    intent = SuccessScreenActivity.getIntent(
                        activityContext = this@CreditCardActivity,
                        total = it.grossAmount?.currencyFormatRp().orEmpty(),
                        orderId = it?.orderId.toString()
                    )
                    resultLauncher.launch(intent)
                }
            }
        }
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                setResult(RESULT_OK)
                finish()
            }
        }

    @Composable
    private fun CreditCardPageStateFull(
        transactionDetails: TransactionDetails? = null,
        customerDetail: CustomerInfo? = null,
        withCustomerPhoneEmail: Boolean = false,
        savedTokenListState: SnapshotStateList<FormData>?,
        creditCard: CreditCard?,
        totalAmount: State<String>,
        bankCodeIdState: State<Int?>,
        binType: State<String?>,
        cardIssuerBank: State<String?>,
        viewModel: CreditCardViewModel?,
        remainingTimeState: State<String>,
        promoState: State<List<PromoData>?>,
        errorTypeState: State<Int?>
    ) {
        val state = remember {
            CardItemState(
                cardNumber = TextFieldValue(),
                expiry = TextFieldValue(),
                cvv = TextFieldValue(),
                isCardNumberInvalid = false,
                isExpiryInvalid = false,
                isCvvInvalid = false,
                isCardTexFieldFocused = false,
                isExpiryTextFieldFocused = false,
                isCvvTextFieldFocused = false,
                isSaveCardChecked = true,
                principalIconId = null,
                customerEmail = TextFieldValue(),
                customerPhone = TextFieldValue(),
                promoId = 0L
            )
        }
        val transactionResponse = viewModel?.transactionResponseLiveData?.observeAsState()
        val bankCodeId by bankCodeIdState
        var isExpanding by remember { mutableStateOf(false) }
        var selectedFormData: FormData? by remember { mutableStateOf(null) }
        var installmentTerm by remember { mutableStateOf("") }

        if (transactionResponse?.value?.statusCode == UiKitConstants.STATUS_CODE_201 && !transactionResponse.value?.redirectUrl.isNullOrEmpty()) {
            transactionResponse.value?.redirectUrl?.let {
                SnapThreeDsWebView(
                    url = it,
                    transactionResponse = transactionResponse.value,
                    onPageStarted = {},
                    onPageFinished = {
                        viewModel.getTransactionStatus(snapToken)
                    }
                )
            }
        } else {
            CreditCardPageStateLess(
                state = state,
                isExpandingState = isExpanding,
                totalAmount = totalAmount.value,
                creditCard = creditCard,
                orderId = transactionDetails?.orderId.toString(),
                customerDetail = customerDetail,
                savedTokenListState = savedTokenListState,
                bankCodeState = bankCodeId,
                binType = binType.value,
                cardIssuerBank = cardIssuerBank.value,
                remainingTimeState = remainingTimeState,
                onExpand = { isExpanding = it },
                onCardNumberValueChange = {

                    state.cardNumber = it
                    val cardNumberWithoutSpace = SnapCreditCardUtil.getCardNumberFromTextField(it)
                    viewModel?.getPromosData(binNumber = cardNumberWithoutSpace)
                    if (cardNumberWithoutSpace.length >= SnapCreditCardUtil.SUPPORTED_MAX_BIN_NUMBER) {
                        val eightDigitNumber = cardNumberWithoutSpace.substring(
                            0,
                            SnapCreditCardUtil.SUPPORTED_MAX_BIN_NUMBER
                        )
                        if (eightDigitNumber != previousEightDigitNumber) {
                            previousEightDigitNumber = eightDigitNumber
                            viewModel?.getBinData(binNumber = eightDigitNumber)
                        }
                    } else {
                        viewModel?.setBankIconToNull()
                        previousEightDigitNumber = cardNumberWithoutSpace
                    }
                },
                onClick = {
                    if (selectedFormData == null) {
                        viewModel?.chargeUsingCreditCard(
                            transactionDetails = transactionDetails,
                            cardNumber = state.cardNumber,
                            cardExpiry = state.expiry,
                            cardCvv = state.cvv,
                            isSavedCard = state.isSavedCardChecked,
                            customerEmail = state.customerEmail.text,
                            customerPhone = state.customerPhone.text,
                            installmentTerm = installmentTerm,
                            snapToken = snapToken,
                            promoId = state.promoId
                        )
                    } else {
                        viewModel?.chargeUsingCreditCard(
                            formData = selectedFormData as SavedCreditCardFormData,
                            snapToken = snapToken,
                            cardCvv = state.cvv,
                            customerEmail = state.customerEmail.text,
                            transactionDetails = transactionDetails,
                            promoId = state.promoId
                        )
                    }
                },
                onInstallmentTermSelected = { installmentTerm = it },
                withCustomerPhoneEmail = withCustomerPhoneEmail,
                promoState = promoState,
                onSavedCardRadioSelected = { selectedFormData = it }
            )
        }
        val errorState by errorTypeState
        errorState?.let {
            val clicked = remember {
                mutableStateOf(false)
            }
            ErrorCard(
                type = it,
                getErrorCta(
                    type = it,
                    state = state,
                    clicked = clicked,
                    installmentTerm = installmentTerm
                )
            ).apply {
                show()
                if (clicked.value) {
                    clicked.value = false
                    hide()
                    viewModel?.resetError()
                }
            }
        }
        viewModel?.setPromoId(state.promoId)
    }

    private fun getErrorCta(
        type: Int,
        state: CardItemState,
        installmentTerm: String,
        clicked: MutableState<Boolean>
    ): () -> Unit {
        return when (type) {
            ErrorCard.CARD_ERROR_DECLINED_DISALLOW_RETRY, ErrorCard.SYSTEM_ERROR_DIALOG_DISALLOW_RETRY -> { ->
                setResult(RESULT_OK)
                clicked.value = true
                finish()
            }

            ErrorCard.TID_MID_ERROR_OTHER_PAY_METHOD_AVAILABLE, ErrorCard.TIMEOUT_ERROR_DIALOG_FROM_BANK -> { ->
                setResult(RESULT_CANCELED)
                clicked.value = true
                finish()
            }

            ErrorCard.SYSTEM_ERROR_DIALOG_ALLOW_RETRY -> { ->
                viewModel.chargeUsingCreditCard(
                    transactionDetails = transactionDetails,
                    cardNumber = state.cardNumber,
                    cardExpiry = state.expiry,
                    cardCvv = state.cvv,
                    isSavedCard = state.isSavedCardChecked,
                    customerEmail = state.customerEmail.text,
                    customerPhone = state.customerPhone.text,
                    installmentTerm = installmentTerm,
                    snapToken = snapToken,
                    promoId = state.promoId
                )
                clicked.value = true
            }
            else -> { -> clicked.value = true }
        }
    }

    @Composable
    private fun CreditCardPageStateLess(
        state: CardItemState,
        isExpandingState: Boolean,
        withCustomerPhoneEmail: Boolean = false,
        totalAmount: String,
        creditCard: CreditCard?,
        orderId: String,
        customerDetail: CustomerInfo? = null,
        savedTokenListState: SnapshotStateList<FormData>?,
        promoState: State<List<PromoData>?>,
        bankCodeState: Int?,
        binType: String?,
        cardIssuerBank: String?,
        remainingTimeState: State<String>,
        onExpand: (Boolean) -> Unit,
        onCardNumberValueChange: (TextFieldValue) -> Unit,
        onSavedCardRadioSelected: (item: FormData?) -> Unit,
        onInstallmentTermSelected: (String) -> Unit,
        onClick: () -> Unit
    ) {
        Column(
            modifier = Modifier.background(SnapColors.getARGBColor(SnapColors.BACKGROUND_FILL_PRIMARY)),
        ) {
            val remainingTime by remember { remainingTimeState }
            var installmentStatus by remember { mutableStateOf(true) }
            SnapAppBar(
                title = stringResource(id = R.string.payment_summary_cc_dc),
                iconResId = R.drawable.ic_arrow_left
            ) {
                onBackPressed()
            }
            val scrollState = rememberScrollState()
            SnapOverlayExpandingBox(
                isExpanded = isExpandingState,
                mainContent = {
                    SnapTotal(
                        amount = totalAmount,
                        orderId = orderId,
                        remainingTime = remainingTime,
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
                            .padding(top = 24.dp)
                    ) {
                        if (withCustomerPhoneEmail) {
                            CustomerPhoneLayout(state = state)
                        }

                        savedTokenListState?.let {
                            SavedCardLayout(
                                viewModel = viewModel,
                                state = state,
                                savedTokenListState = it,
                                bankCodeId = bankCodeState,
                                onCardNumberValueChange = onCardNumberValueChange,
                                onSavedCardRadioSelected = onSavedCardRadioSelected
                            )
                        }
                        if (savedTokenListState == null) {
                            NormalCardFormLayout(
                                state = state,
                                creditCard = creditCard,
                                bankCodeState = bankCodeState,
                                onCardNumberValueChange = onCardNumberValueChange
                            )
                        }

                        promoState.value?.let {
                            PromoLayout(promoData = it, cardItemState = state)
                        }

                        SnapInstallmentTermSelectionMenu(
                            creditCard = creditCard,
                            cardIssuerBank = cardIssuerBank,
                            binType = binType,
                            cardNumber = state.cardNumber,
                            onInstallmentTermSelected = { onInstallmentTermSelected(it) },
                            onInstallmentAllowed = {
                                installmentStatus = it
                            }
                        )
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(all = 16.dp)
            )

            SnapButton(
                text = stringResource(id = R.string.cc_dc_main_screen_cta),
                style = SnapButton.Style.PRIMARY,
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                enabled = (!(state.isCardNumberInvalid ||
                    state.isExpiryInvalid ||
                    state.isCvvInvalid ||
                    state.cardNumber.text.isEmpty() ||
                    state.expiry.text.isEmpty() ||
                    state.cvv.text.isEmpty())
                    .or(
                        !SnapCreditCardUtil.isValidEmail(state.customerEmail.text)
                            .or(state.customerEmail.text.isBlank())
                    ))
                    .or((state.cardItemType == CardItemState.CardItemType.SAVED_CARD).and(!state.isCvvInvalid))
                    .or(!installmentStatus),
                onClick = { onClick() }
            )
        }
    }

    @Composable
    private fun NormalCardFormLayout(
        state: CardItemState,
        bankCodeState: Int?,
        creditCard: CreditCard?,
        onCardNumberValueChange: (TextFieldValue) -> Unit
    ) {
        NormalCardItem(
            state = state,
            bankIcon = bankCodeState,
            creditCard = creditCard,
            onCardNumberValueChange = {
                onCardNumberValueChange(it)
            },
            onExpiryDateValueChange = { state.expiry = it },
            onCvvValueChange = { state.cvv = it },
            onCardTextFieldFocusedChange = { state.isCardTexFieldFocused = it },
            onExpiryTextFieldFocusedChange = {
                state.isExpiryTextFieldFocused = it
            },
            onCvvTextFieldFocusedChange = { state.isCvvTextFieldFocused = it },
            onSavedCardCheckedChange = { state.isSavedCardChecked = it }
        )
    }

    @Composable
    private fun PromoLayout(
        promoData: List<PromoData>,
        cardItemState: CardItemState
    ) {
        Divider(
            color = SnapColors.getARGBColor(SnapColors.BACKGROUND_BORDER_SOLID_SECONDARY),
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(top = 16.dp)
        )
        Text(
            text = stringResource(id = R.string.promo_select_promo_title),
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
            style = SnapTypography.STYLES.snapTextMediumRegular
        )

        SnapPromoListRadioButton(
            states = promoData.toMutableList().apply { add(noPromo) },
            onItemSelectedListener = {
                cardItemState.promoId = it.identifier.orEmpty().toLong()
            }
        )
    }

    @Composable
    private fun CustomerPhoneLayout(
        state: CardItemState
    ) {
        var phoneNumber by remember { mutableStateOf(TextFieldValue()) }
        var phoneNumberFieldFocused by remember { mutableStateOf(false) }
        var emailAddressFieldFocused by remember { mutableStateOf(false) }
        Text(
            text = stringResource(id = R.string.cc_dc_main_screen_tlp),
            modifier = Modifier.padding(top = 16.dp, bottom = 4.dp),
            style = SnapTypography.STYLES.snapTextSmallRegular
        )
        SnapTextField(
            value = phoneNumber,
            onValueChange = {
                phoneNumber = it
                state.customerPhone = it
            },
            isFocused = phoneNumberFieldFocused,
            onFocusChange = { phoneNumberFieldFocused = it },
            modifier = Modifier.fillMaxWidth(1f),
            hint = stringResource(id = R.string.cc_dc_main_screen_placeholder_phone),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Text(
            text = stringResource(id = R.string.cc_dc_main_screen_email),
            modifier = Modifier.padding(top = 16.dp, bottom = 4.dp),
            style = SnapTypography.STYLES.snapTextSmallRegular
        )
        SnapTextField(
            value = state.customerEmail,
            onValueChange = {
                state.customerEmail = it
            },
            isFocused = emailAddressFieldFocused,
            onFocusChange = { emailAddressFieldFocused = it },
            modifier = Modifier
                .fillMaxWidth(1f),
            hint = stringResource(id = R.string.cc_dc_main_screen_placeholder_email),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        if (!emailAddressFieldFocused && state.customerEmail.text.isNotBlank() && !SnapCreditCardUtil.isValidEmail(
                state.customerEmail.text
            )
        ) {
            Text(
                text = stringResource(id = R.string.cc_dc_main_screen_email_invalid),
                style = SnapTypography.STYLES.snapTextSmallRegular,
                color = SnapColors.getARGBColor(SnapColors.SUPPORT_DANGER_DEFAULT)
            )
        }
        Box(modifier = Modifier.padding(8.dp))
    }

    @Composable
    private fun SavedCardLayout(
        viewModel: CreditCardViewModel?,
        state: CardItemState,
        savedTokenListState: SnapshotStateList<FormData>,
        bankCodeId: Int?,
        onCardNumberValueChange: (TextFieldValue) -> Unit,
        onSavedCardRadioSelected: (item: FormData?) -> Unit
    ) {
        SnapSavedCardRadioGroup(
            modifier = Modifier
                .padding(top = 24.dp),
            listStates = savedTokenListState,
            cardItemState = state,
            bankIconState = bankCodeId,
            creditCard = creditCard,
            onItemRemoveClicked = {
                viewModel?.deleteSavedCard(
                    snapToken = snapToken,
                    maskedCard = it.displayedMaskedCard
                )
                savedTokenListState.remove(it)
            },
            onCardNumberOtherCardValueChange = onCardNumberValueChange,
            onExpiryOtherCardValueChange = { state.expiry = it },
            onSavedCardRadioSelected = onSavedCardRadioSelected,
            onIsCvvSavedCardInvalidValueChange = { state.isCvvInvalid = it },
            onCvvValueChange = {
                state.cvv = it
            },
            onSavedCardCheckedChange = { state.isSavedCardChecked = it }
        )
    }

    private fun updateExpiredTime(): Observable<String> {
        return Observable
            .interval(1L, TimeUnit.SECONDS)
            .map { viewModel.getExpiredHour() }
            .observeOn(AndroidSchedulers.mainThread())
    }

    @Preview
    @Composable
    private fun ForPreview() {
        CreditCardPageStateFull(
            transactionDetails = TransactionDetails(
                orderId = "orderID",
                grossAmount = 5000.0,
                currency = "IDR"
            ),
            customerDetail = CustomerInfo(
                "Ari Bhakti",
                "087788778212",
                listOf("Jl. ABC", "Rumah DEF")
            ),
            savedTokenListState = null,
            binType = remember { mutableStateOf(null) },
            viewModel = null,
            bankCodeIdState = remember { mutableStateOf(null) },
            cardIssuerBank = remember { mutableStateOf(null) },
            totalAmount = remember { mutableStateOf("5000") },
            remainingTimeState = remember { mutableStateOf("00:00") },
            withCustomerPhoneEmail = true,
            errorTypeState = remember { mutableStateOf(null) },
            promoState = remember { mutableStateOf(null) },
            creditCard = CreditCard()
        )
    }

    companion object {
        private const val EXTRA_SNAP_TOKEN = "card.extra.snap_token"
        private const val EXTRA_TRANSACTION_DETAILS = "card.extra.transaction_details"
        private const val EXTRA_TOTAL_AMOUNT = "card.extra.total_amount"
        private const val EXTRA_CUSTOMER_DETAIL = "card.extra.customer_detail"
        private const val EXTRA_CREDIT_CARD = "card.extra.credit_card"
        private const val EXTRA_EXPIRY_TIME = "card.extra.expiry_time"
        private const val EXTRA_MERCHANT_DATA = "card.extra.merchant_data"
        private const val EXTRA_PROMOS = "card.extra.extra.promos"

        fun getIntent(
            activityContext: Context,
            snapToken: String,
            totalAmount: String,
            transactionDetails: TransactionDetails?,
            customerInfo: CustomerInfo? = null,
            creditCard: CreditCard?,
            promos: List<Promo>? = null,
            expiryTime: String?,
            withMerchantData: Merchant? = null
        ): Intent {
            return Intent(activityContext, CreditCardActivity::class.java).apply {
                putExtra(EXTRA_SNAP_TOKEN, snapToken)
                putExtra(EXTRA_TRANSACTION_DETAILS, transactionDetails)
                putExtra(EXTRA_TOTAL_AMOUNT, totalAmount)
                putExtra(
                    EXTRA_CUSTOMER_DETAIL,
                    customerInfo
                )
                putExtra(EXTRA_CREDIT_CARD, creditCard)
                putExtra(EXTRA_EXPIRY_TIME, expiryTime)
                withMerchantData?.let { putExtra(EXTRA_MERCHANT_DATA, withMerchantData) }
                promos?.let {
                    putParcelableArrayListExtra(EXTRA_PROMOS, ArrayList(it))
                }
            }
        }
    }

}

