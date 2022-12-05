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
import androidx.compose.material.ExperimentalMaterialApi
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
import com.midtrans.sdk.corekit.api.model.TransactionResponse
import com.midtrans.sdk.corekit.api.model.TransactionResult
import com.midtrans.sdk.corekit.internal.network.model.response.Merchant
import com.midtrans.sdk.corekit.internal.network.model.response.TransactionDetails
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.api.model.PaymentType
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.base.BaseActivity
import com.midtrans.sdk.uikit.internal.model.CreditCardPromoInfo
import com.midtrans.sdk.uikit.internal.model.CustomerInfo
import com.midtrans.sdk.uikit.internal.model.ItemInfo
import com.midtrans.sdk.uikit.internal.model.PromoData
import com.midtrans.sdk.uikit.internal.presentation.errorcard.ErrorCard
import com.midtrans.sdk.uikit.internal.presentation.statusscreen.ErrorScreenActivity
import com.midtrans.sdk.uikit.internal.presentation.statusscreen.SuccessScreenActivity
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
        ViewModelProvider(this, viewModelFactory)[CreditCardViewModel::class.java]
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

    private val itemInfo: ItemInfo? by lazy {
        intent.getParcelableExtra(EXTRA_ITEM_INFO) as? ItemInfo
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

    private val merchant: Merchant? by lazy {
        intent.getParcelableExtra(EXTRA_MERCHANT_DATA) as? Merchant
    }

    private val currentStepNumber: Int by lazy {
        intent.getIntExtra(EXTRA_STEP_NUMBER, 0)
    }

    private val promos: List<Promo>? by lazy {
        intent.getParcelableArrayListExtra<Promo?>(EXTRA_PROMOS)
            ?.apply {
                sortByDescending { it.calculatedDiscountAmount }
            }
    }

    private val withCustomerPhoneEmail: Boolean by lazy {
        merchant?.showCreditCardCustomerInfo ?: false
    }

    private val allowRetry: Boolean by lazy {
        merchant?.allowRetry ?: false
    }

    private var onPromoReset: () -> Unit = {}

    private val savedTokenList: SnapshotStateList<FormData>? by lazy {
        creditCard?.savedTokens?.mapIndexed { index, savedToken ->
            SavedCreditCardFormData(
                savedCardIdentifier = SnapCreditCardUtil.SAVED_CARD_IDENTIFIER + index.toString(),
                inputTitle = getString(R.string.cc_dc_saved_card_enter_cvv),
                endIcon = R.drawable.ic_trash,
                startIcon = SnapCreditCardUtil.getBankIcon(savedToken.binDetail?.bankCode.toString()),
                errorText = mutableStateOf(""),
                maskedCardNumber = savedToken.maskedCard.orEmpty(),
                displayedMaskedCard = savedToken.maskedCard.orEmpty(),
                tokenType = savedToken.tokenType.toString(),
                bankCode = savedToken.binDetail?.bankCode.toString(),
                tokenId = savedToken.token.toString(),
                cvvSavedCardTextField = TextFieldValue(),
                isCvvSavedCardInvalid = false,
                isPointBankSavedCardChecked = false
            ) as FormData
        }
            ?.ifEmpty { null }
            ?.toMutableList()
            ?.apply {
                add(NewCardFormData(newCardIdentifier = SnapCreditCardUtil.NEW_CARD_FORM_IDENTIFIER))
            }
            ?.toMutableStateList()
        //For testing purpose: uncomment below to force non save card
//        null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UiKitApi.getDefaultInstance().daggerComponent.inject(this)
        viewModel.setExpiryTime(expiryTime)
        viewModel.setAllowRetry(allowRetry)
        viewModel.setPromos(promos = promos)
        viewModel.setTransactionDetails(transactionDetails)
        viewModel.setPointBanks(merchant?.pointBanks)
        viewModel.setCreditCardDetails(creditCard)
        viewModel.trackPageViewed(currentStepNumber)
        initTransactionResultScreenObserver()
        setContent {
            CreditCardPageStateFull(
                transactionDetails = transactionDetails,
                customerDetail = customerDetail,
                itemInfo = itemInfo,
                savedTokenListState = savedTokenList,
                creditCard = creditCard,
                viewModel = viewModel,
                bankCodeIdState = viewModel.bankIconId.observeAsState(null),
                binType = viewModel.binType.observeAsState(null),
                cardIssuerBank = viewModel.cardIssuerBank.observeAsState(null),
                totalAmount = viewModel.netAmountLiveData.observeAsState(initial = totalAmount),
                totalAmountWithoutRp = viewModel.netAmountWithoutCurrencyLiveData.observeAsState(0.0),
                remainingTimeState = updateExpiredTime().subscribeAsState(initial = "00:00"),
                withCustomerPhoneEmail = withCustomerPhoneEmail,
                errorTypeState = viewModel.errorTypeLiveData.observeAsState(initial = null),
                promoState = viewModel.promoDataLiveData.observeAsState(initial = null)
            )
        }
    }

    private fun launchSuccessScreen(it: TransactionResponse) {
        val intent = SuccessScreenActivity.getIntent(
            activityContext = this@CreditCardActivity,
            total = it.grossAmount?.currencyFormatRp().orEmpty(),
            orderId = it.orderId.toString(),
            transactionResult = TransactionResult(
                status = it.transactionStatus.orEmpty(),
                transactionId = it.transactionId.orEmpty(),
                paymentType = it.paymentType.orEmpty()
            ),
            stepNumber = currentStepNumber + 1
        )
        resultLauncher.launch(intent)
    }

    private fun launchErrorScreen(it: TransactionResponse) {
        val intent = ErrorScreenActivity.getIntent(
            activityContext = this@CreditCardActivity,
            title = it.validationMessages?.get(0).toString(),
            content = it.statusMessage.toString()
        )
        resultLauncher.launch(intent)
    }

    private fun initTransactionResultScreenObserver() {
        viewModel.transactionResponseLiveData.observe(this) {
            if (it.statusCode == UiKitConstants.STATUS_CODE_200) {
                launchSuccessScreen(it)
            } else if (it.statusCode != UiKitConstants.STATUS_CODE_201 && it.redirectUrl.isNullOrEmpty()) {
                launchErrorScreen(it)
            }
        }

        //TODO: handle error ui/dialog
        viewModel.errorLiveData.observe(this) {
            val data = Intent()
            data.putExtra(
                UiKitConstants.KEY_ERROR_NAME,
                it::class.java.simpleName
            )
            data.putExtra(
                UiKitConstants.KEY_ERROR_MESSAGE,
                it.message
            )
            setResult(Activity.RESULT_CANCELED, data)
        }
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            setResult(result.resultCode, result.data)
            finish()
        }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun CreditCardPageStateFull(
        transactionDetails: TransactionDetails? = null,
        customerDetail: CustomerInfo? = null,
        itemInfo: ItemInfo? = null,
        withCustomerPhoneEmail: Boolean = false,
        savedTokenListState: SnapshotStateList<FormData>?,
        creditCard: CreditCard?,
        totalAmount: State<String>,
        totalAmountWithoutRp: State<Double>,
        bankCodeIdState: State<Int?>,
        binType: State<String?>,
        cardIssuerBank: State<String?>,
        viewModel: CreditCardViewModel?,
        remainingTimeState: State<String>,
        promoState: State<List<PromoData>?>,
        errorTypeState: State<Pair<Int?, String>?>
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
                isPointBankChecked = false,
                isSaveCardChecked = getUikitSetting().saveCardChecked,
                principalIconId = null,
                customerEmail = TextFieldValue(),
                customerPhone = TextFieldValue(),
                promoId = 0L,
                promoName = null,
                promoAmount = null,
                isInstallmentAllowed = true
            )
        }

        var isPaymentUsingPointState by remember { mutableStateOf(false) }
        val pointBalanceAmount = viewModel?.pointBalanceAmount?.observeAsState(null)
        val isPointBankShownState = viewModel?.isPointBankShown?.observeAsState(false)
        val transactionResponse = viewModel?.transactionResponseLiveData?.observeAsState()
        val bankCodeId by bankCodeIdState
        var isExpanding by remember { mutableStateOf(false) }
        var selectedFormData: FormData? by remember { mutableStateOf(null) }
        var installmentTerm by remember { mutableStateOf("") }
        state.isBinBlocked = viewModel?.binBlockedLiveData?.observeAsState(false)?.value ?: false

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
                isPointBankShownState = isPointBankShownState,
                totalAmount = totalAmount.value,
                creditCard = creditCard,
                orderId = transactionDetails?.orderId.toString(),
                customerDetail = customerDetail,
                itemInfo = itemInfo,
                savedTokenListState = savedTokenListState,
                bankCodeState = bankCodeId,
                binType = binType.value,
                cardIssuerBank = cardIssuerBank.value,
                remainingTimeState = remainingTimeState,
                onExpand = { isExpanding = it },
                onCardNumberValueChange = {

                    state.cardNumber = it
                    val cardNumberWithoutSpace = SnapCreditCardUtil.getCardNumberFromTextField(it)
                    viewModel?.getPromosData(
                        binNumber = cardNumberWithoutSpace,
                        installmentTerm = installmentTerm
                    )
                    if (cardNumberWithoutSpace.length >= SnapCreditCardUtil.SUPPORTED_MAX_BIN_NUMBER) {
                        val eightDigitNumber = cardNumberWithoutSpace.substring(
                            0,
                            SnapCreditCardUtil.SUPPORTED_MAX_BIN_NUMBER
                        )
                        if (eightDigitNumber != previousEightDigitNumber) {
                            previousEightDigitNumber = eightDigitNumber
                            viewModel?.getBinData(binNumber = eightDigitNumber)
                            onPromoReset.invoke()
                        }
                    } else {
                        viewModel?.resetCardNumberAttribute()
                        previousEightDigitNumber = cardNumberWithoutSpace
                    }
                },
                onClick = {
                    viewModel?.trackSnapButtonClicked(
                        ctaName = getStringResourceInEnglish(R.string.cc_dc_main_screen_cta)
                    )
                    viewModel?.trackCustomerDataInput(
                        email = state.customerEmail.text,
                        phoneNumber = state.customerPhone.text,
                        displayField = withCustomerPhoneEmail
                    )

                    var grossAmount = 0.0
                    transactionDetails?.grossAmount?.let {
                        grossAmount = it
                    }
                    if (state.isPointBankChecked) {
                        if (state.cardItemType == CardItemState.CardItemType.SAVED_CARD) {
                            viewModel?.getBankPoint(
                                formData = selectedFormData as SavedCreditCardFormData,
                                snapToken = snapToken,
                                transactionDetails = transactionDetails,
                                cardNumber = state.cardNumber,
                                cardExpiry = state.expiry,
                                cardCvv = state.cvv,
                                promoId = state.promoId
                            )
                        } else {
                            viewModel?.getBankPoint(
                                snapToken = snapToken,
                                transactionDetails = transactionDetails,
                                cardNumber = state.cardNumber,
                                cardExpiry = state.expiry,
                                cardCvv = state.cvv,
                                promoId = state.promoId
                            )
                        }
                        isPaymentUsingPointState = true
                    } else {
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
                                installmentTerm = installmentTerm,
                                promoId = state.promoId
                            )
                        }
                    }
                },
                onInstallmentTermSelected = {
                    installmentTerm = it
                    viewModel?.hidePointBank(installmentTerm)
                    viewModel?.getPromosData(
                        binNumber = SnapCreditCardUtil.getCardNumberFromTextField(
                            state.cardNumber
                        ), installmentTerm = installmentTerm
                    )
                },
                withCustomerPhoneEmail = withCustomerPhoneEmail,
                promoState = promoState,
                onSavedCardRadioSelected = { selectedFormData = it },
                onSavedCardPointBankCheckedChange = { state.isPointBankChecked = it }
            )
        }

        var pointPayButtonClickedState by remember {
            mutableStateOf(false)
        }
        var justOpenedSheetState by remember {
            mutableStateOf(false)
        }

        pointBalanceAmount?.value?.let { pointBalance ->

            var pointAmountInputted by remember { mutableStateOf(TextFieldValue()) }
            var displayedTotalFinal by remember { mutableStateOf("") }
            var isError by remember { mutableStateOf(false) }
            val data = SnapPointRedeemDialogData(
                total = totalAmountWithoutRp.value,
                pointBalanceAmount = pointBalance
            )

            PointBankCard(
                data = data,
                pointAmountInputted = pointAmountInputted,
                displayedTotalFinal = displayedTotalFinal,
                isError = isError,
                onSheetStateChange = {
                    if (justOpenedSheetState && !it.isVisible) {
                        isPaymentUsingPointState = false
                        justOpenedSheetState = false
                    }
                },
                onClick = { pointInputted ->
                    viewModel?.chargeWithPoint(
                        isSavedCard = state.isSavedCardChecked,
                        customerEmail = state.customerEmail.text,
                        customerPhone = state.customerPhone.text,
                        promoId = state.promoId,
                        pointAmount = pointInputted,
                        snapToken = snapToken,
                    )
                    pointPayButtonClickedState = true
                },
                onPointValueChange = {
                    SnapCreditCardUtil.formatMaxPointDiscount(
                        input = it,
                        totalAmount = data.total.toLong(),
                        pointBalanceAmount = data.pointBalanceAmount
                    ).let { triple ->
                        triple.first.let {
                            pointAmountInputted = it
                        }
                        triple.second.let {
                            displayedTotalFinal = it
                        }
                        triple.third.let {
                            isError = it
                        }
                    }
                },
                onPointError = {
                    viewModel.trackSnapNotice(
                        statusText = getString(R.string.point_failed_title),
                        noticeMessage = it
                    )
                }
            ).apply {
                if (isPaymentUsingPointState) {
                    justOpenedSheetState = true
                    SnapCreditCardUtil.formatMaxPointDiscount(
                        input = TextFieldValue(pointBalance.toLong().toString()),
                        totalAmount = data.total.toLong(),
                        pointBalanceAmount = data.pointBalanceAmount
                    ).let { triple ->
                        triple.first.let {
                            pointAmountInputted = it
                        }
                        triple.second.let {
                            displayedTotalFinal = it
                        }
                        triple.third.let {
                            isError = it
                        }
                    }
                    show()
                } else if (pointPayButtonClickedState) {
                    pointPayButtonClickedState = false
                    hide()
                }
            }
        }

        val errorState by errorTypeState
        errorState?.let { pair ->
            pair.first?.let { type ->
                val clicked = remember {
                    mutableStateOf(false)
                }
                val errorCta = getErrorCta(
                    type = type,
                    transactionId = pair.second,
                    state = state,
                    installmentTerm = installmentTerm,
                    clicked = clicked
                )
                ErrorCard(
                    type = type,
                    onClick = {
                        viewModel?.trackSnapButtonClicked(getStringResourceInEnglish(it))
                        errorCta.invoke()
                    }
                ).apply {
                    show()
                    if (clicked.value) {
                        clicked.value = false
                        hide()
                        viewModel?.resetError()
                    }
                }
            }
        }
        viewModel?.setPromoId(state.promoId)
    }

    private fun getErrorCta(
        type: Int,
        transactionId: String,
        state: CardItemState,
        installmentTerm: String,
        clicked: MutableState<Boolean>
    ): () -> Unit {
        return when (type) {
            ErrorCard.CARD_ERROR_DECLINED_DISALLOW_RETRY,
            ErrorCard.SYSTEM_ERROR_DIALOG_DISALLOW_RETRY -> { ->
                Intent().also {
                    val transactionResult = TransactionResult(
                        status = UiKitConstants.STATUS_FAILED,
                        transactionId = transactionId,
                        paymentType = PaymentType.CREDIT_CARD
                    )
                    it.putExtra(UiKitConstants.KEY_TRANSACTION_RESULT, transactionResult)
                    setResult(RESULT_OK, it)
                    clicked.value = true
                    finish()
                }
            }

            ErrorCard.TID_MID_ERROR_OTHER_PAY_METHOD_AVAILABLE,
            ErrorCard.TIMEOUT_ERROR_DIALOG_FROM_BANK -> { ->
                Intent().also {
                    val transactionResult = TransactionResult(
                        status = UiKitConstants.STATUS_FAILED,
                        transactionId = transactionId,
                        paymentType = PaymentType.CREDIT_CARD
                    )
                    it.putExtra(UiKitConstants.KEY_TRANSACTION_RESULT, transactionResult)
                    setResult(RESULT_CANCELED, it)
                    clicked.value = true
                    finish()
                }
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
            else -> { ->
                clicked.value = true
            }
        }
    }

    @Composable
    private fun CreditCardPageStateLess(
        state: CardItemState,
        isExpandingState: Boolean,
        isPointBankShownState: State<Boolean>?,
        withCustomerPhoneEmail: Boolean = false,
        totalAmount: String,
        creditCard: CreditCard?,
        orderId: String,
        customerDetail: CustomerInfo? = null,
        itemInfo: ItemInfo? = null,
        savedTokenListState: SnapshotStateList<FormData>?,
        promoState: State<List<PromoData>?>,
        bankCodeState: Int?,
        binType: String?,
        cardIssuerBank: String?,
        remainingTimeState: State<String>,
        onExpand: (Boolean) -> Unit,
        onCardNumberValueChange: (TextFieldValue) -> Unit,
        onSavedCardRadioSelected: (item: FormData?) -> Unit,
        onSavedCardPointBankCheckedChange: (Boolean) -> Unit,
        onInstallmentTermSelected: (String) -> Unit,
        onClick: () -> Unit
    ) {
        Column(
            modifier = Modifier.background(SnapColors.getARGBColor(SnapColors.backgroundFillPrimary)),
        ) {
            val remainingTime by remember { remainingTimeState }

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
                        canExpand = customerDetail != null || itemInfo != null,
                        isPromo = state.promoId != 0L
                    ) {
                        onExpand(it)
                    }
                },
                expandingContent = {
                    viewModel.trackOrderDetailsViewed(totalAmount)
                    SnapPaymentOrderDetails(
                        customerInfo = customerDetail,
                        itemInfo = itemInfo,
                        creditCardPromoInfo = CreditCardPromoInfo(
                            promoName = state.promoName,
                            promoAmount = state.promoAmount,
                            discountedAmount = totalAmount
                        )
                    )
                },
                followingContent = {
                    Column(
                        modifier = Modifier
                            .verticalScroll(scrollState)
                            .background(SnapColors.getARGBColor(SnapColors.overlayWhite))
                            .padding(top = 24.dp)
                    ) {
                        if (withCustomerPhoneEmail) {
                            CustomerPhoneLayout(state = state)
                        }

                        savedTokenListState?.let {
                            SavedCardLayout(
                                viewModel = viewModel,
                                state = state,
                                isPointBankShownState = isPointBankShownState,
                                savedTokenListState = it,
                                bankCodeId = bankCodeState,
                                onCardNumberValueChange = onCardNumberValueChange,
                                onSavedCardRadioSelected = onSavedCardRadioSelected,
                                onSavedCardPointBankCheckedChange = onSavedCardPointBankCheckedChange
                            )
                        }

                        if (savedTokenListState == null) {
                            NormalCardFormLayout(
                                state = state,
                                creditCard = creditCard,
                                isPointBankShownState = isPointBankShownState,
                                bankCodeState = bankCodeState,
                                onCardNumberValueChange = onCardNumberValueChange
                            )
                        }

                        SnapInstallmentTermSelectionMenu(
                            creditCard = creditCard,
                            cardIssuerBank = cardIssuerBank,
                            binType = binType,
                            cardNumber = state.cardNumber,
                            isPointBankChecked = state.isPointBankChecked,
                            onInstallmentTermSelected = { onInstallmentTermSelected(it) },
                            onInstallmentAllowed = { state.isInstallmentAllowed = it },
                            onInstallmentSelectionError = { errorMessage ->
                                viewModel.trackSnapNotice(
                                    statusText = getString(R.string.installment_selection_error),
                                    noticeMessage = errorMessage
                                )
                            }
                        )

                        promoState.value?.also {
                            PromoLayout(
                                promoData = it,
                                cardItemState = state
                            ).also { onResetAction ->
                                onPromoReset = onResetAction
                            }
                        }
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
                enabled = isValidNormalCard(state)
                    .or(isValidSavedCard(state)),
                onClick = { onClick() }
            )
        }
    }

    private fun isValidNormalCard(state: CardItemState): Boolean {
        return isValidCardData(state)
            .or(isValidEmail(state.customerEmail))
            .and(state.isInstallmentAllowed)
    }

    private fun isValidCardData(state: CardItemState): Boolean {
        return !(state.isCardNumberInvalid ||
                state.isExpiryInvalid ||
                state.isCvvInvalid ||
                state.cardNumber.text.isEmpty() ||
                state.expiry.text.isEmpty() ||
                state.cvv.text.isEmpty()
                )
    }

    private fun isValidEmail(customerEmail: TextFieldValue): Boolean {
        return !SnapCreditCardUtil.isValidEmail(customerEmail.text)
            .or(customerEmail.text.isBlank())
    }

    private fun isValidSavedCard(state: CardItemState): Boolean {
        return (state.cardItemType == CardItemState.CardItemType.SAVED_CARD)
            .and(!state.isCvvInvalid)
            .and(state.cvv.text.isNotEmpty())
            .and(state.isInstallmentAllowed)
    }

    @Composable
    private fun NormalCardFormLayout(
        state: CardItemState,
        bankCodeState: Int?,
        isPointBankShownState: State<Boolean>?,
        creditCard: CreditCard?,
        onCardNumberValueChange: (TextFieldValue) -> Unit
    ) {
        NormalCardItem(
            state = state,
            bankIcon = bankCodeState,
            isPointBankShownState = isPointBankShownState,
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
            onSavedCardCheckedChange = { state.isSavedCardChecked = it },
            onPointBankCheckedChange = { state.isPointBankChecked = it },
            onInputError = { viewModel.trackSnapNotice(getStringResourceInEnglish(it)) }
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

        if (!emailAddressFieldFocused
            && state.customerEmail.text.isNotBlank()
            && !SnapCreditCardUtil.isValidEmail(state.customerEmail.text)
        ) {
            Text(
                text = stringResource(id = R.string.cc_dc_main_screen_email_invalid),
                style = SnapTypography.STYLES.snapTextSmallRegular,
                color = SnapColors.getARGBColor(SnapColors.supportDangerDefault)
            )
            viewModel.trackSnapNotice(getStringResourceInEnglish(R.string.cc_dc_main_screen_email_invalid))
        }
        Box(modifier = Modifier.padding(8.dp))
    }

    @Composable
    private fun SavedCardLayout(
        viewModel: CreditCardViewModel?,
        state: CardItemState,
        isPointBankShownState: State<Boolean>?,
        savedTokenListState: SnapshotStateList<FormData>,
        bankCodeId: Int?,
        onCardNumberValueChange: (TextFieldValue) -> Unit,
        onSavedCardRadioSelected: (item: FormData?) -> Unit,
        onSavedCardPointBankCheckedChange: (Boolean) -> Unit
    ) {
        SnapSavedCardRadioGroup(
            modifier = Modifier
                .padding(top = 24.dp),
            listStates = savedTokenListState,
            cardItemState = state,
            bankIconState = bankCodeId,
            isPointBankShownState = isPointBankShownState,
            isInstallmentActive = creditCard?.installment != null,
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
            onSavedCardCheckedChange = { state.isSavedCardChecked = it },
            onPointBankCheckedChange = onSavedCardPointBankCheckedChange,
            onInputError = { viewModel?.trackSnapNotice(getStringResourceInEnglish(it)) }
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
            totalAmountWithoutRp = remember { mutableStateOf(5000.0) },
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
        private const val EXTRA_PROMOS = "card.extra.promos"
        private const val EXTRA_ITEM_INFO = "card.extra.item_info"
        private const val EXTRA_STEP_NUMBER = "card.extra.step_number"

        fun getIntent(
            activityContext: Context,
            snapToken: String,
            totalAmount: String,
            transactionDetails: TransactionDetails?,
            customerInfo: CustomerInfo? = null,
            itemInfo: ItemInfo? = null,
            creditCard: CreditCard?,
            promos: List<Promo>? = null,
            expiryTime: String?,
            withMerchantData: Merchant? = null,
            stepNumber: Int
        ): Intent {
            return Intent(activityContext, CreditCardActivity::class.java).apply {
                putExtra(EXTRA_SNAP_TOKEN, snapToken)
                putExtra(EXTRA_TRANSACTION_DETAILS, transactionDetails)
                putExtra(EXTRA_TOTAL_AMOUNT, totalAmount)
                putExtra(EXTRA_CUSTOMER_DETAIL, customerInfo)
                putExtra(EXTRA_ITEM_INFO, itemInfo)
                putExtra(EXTRA_CREDIT_CARD, creditCard)
                putExtra(EXTRA_EXPIRY_TIME, expiryTime)
                putExtra(EXTRA_STEP_NUMBER, stepNumber)
                withMerchantData?.let { putExtra(EXTRA_MERCHANT_DATA, withMerchantData) }
                promos?.also {
                    putParcelableArrayListExtra(EXTRA_PROMOS, ArrayList(it))
                }
            }
        }
    }

}

