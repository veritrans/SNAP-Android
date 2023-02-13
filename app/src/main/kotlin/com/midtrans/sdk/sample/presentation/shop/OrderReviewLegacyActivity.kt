package com.midtrans.sdk.sample.presentation.shop

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.core.UIKitCustomSetting
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.corekit.models.*
import com.midtrans.sdk.corekit.models.snap.*
import com.midtrans.sdk.sample.model.ListItem
import com.midtrans.sdk.sample.model.Product
import com.midtrans.sdk.sample.util.DemoConstant
import com.midtrans.sdk.sample.util.DemoConstant.FIVE_MINUTE
import com.midtrans.sdk.sample.util.DemoConstant.NONE
import com.midtrans.sdk.sample.util.DemoConstant.NO_INSTALLMENT
import com.midtrans.sdk.sample.util.DemoConstant.ONE_HOUR
import com.midtrans.sdk.sample.util.DemoUtils
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import com.midtrans.sdk.uikit.internal.view.SnapAppBar
import com.midtrans.sdk.uikit.internal.view.SnapButton
import com.midtrans.sdk.uikit.internal.view.SnapTextField
import com.midtrans.sdk.uikit.internal.view.SnapTypography
import java.util.*
import kotlin.collections.ArrayList

class OrderReviewLegacyActivity : ComponentActivity(), TransactionFinishedCallback {
    override fun onTransactionFinished(result: TransactionResult) {
        if (result.response != null) {
            when (result.status) {
                TransactionResult.STATUS_SUCCESS -> Toast.makeText(
                    this,
                    "Transaction Legacy Finished. ID: " + result.response.transactionId,
                    Toast.LENGTH_LONG
                ).show()
                TransactionResult.STATUS_PENDING -> Toast.makeText(
                    this,
                    "Transaction Legacy Pending. ID: " + result.response.transactionId,
                    Toast.LENGTH_LONG
                ).show()
                TransactionResult.STATUS_FAILED -> Toast.makeText(
                    this,
                    "Transaction Legacy Failed. ID: " + result.response.transactionId.toString() + ". Message: " + result.response.statusMessage,
                    Toast.LENGTH_LONG
                ).show()
            }
        } else if (result.isTransactionCanceled) {
            Toast.makeText(this, "Transaction Legacy Canceled", Toast.LENGTH_LONG).show()
        } else {
            if (result.status.equals(TransactionResult.STATUS_INVALID, true)) {
                Toast.makeText(
                    this,
                    "Transaction Legacy Invalid. ${result.statusMessage}",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(this, "Transaction Legacy Finished with failure.", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private val product: Product by lazy {
        intent.getParcelableExtra(EXTRA_PRODUCT) as? Product
            ?: throw RuntimeException("Order ID must not be empty")
    }

    private val installmentBank: String by lazy {
        intent.getStringExtra(EXTRA_INPUT_INSTALLMENT)
            ?: throw RuntimeException("Installment must not be empty")
    }

    private val isRequiredInstallment: Boolean by lazy {
        intent.getBooleanExtra(EXTRA_INPUT_ISREQUIRED, false)
    }

    private val acquiringBank: String by lazy {
        intent.getStringExtra(EXTRA_INPUT_ACQUIRINGBANK)
            ?: throw RuntimeException("Acquiring Bank must not be empty")
    }

    private val customExpiry: String by lazy {
        intent.getStringExtra(EXTRA_INPUT_EXPIRY)
            ?: throw RuntimeException("Expiry must not be empty")
    }

    private val authenticationType: String by lazy {
        intent.getStringExtra(EXTRA_INPUT_CCAUTHENTICATIONTYPE)
            ?: throw RuntimeException("CCPaymentType must not be empty")
    }

    private val isSavedCard: Boolean by lazy {
        intent.getBooleanExtra(EXTRA_INPUT_ISSAVEDCARD, false)
    }

    private val isPreAuth: Boolean by lazy {
        intent.getBooleanExtra(EXTRA_INPUT_ISPREAUTH, false)
    }

    private val isBniPointOnly: Boolean by lazy {
        intent.getBooleanExtra(EXTRA_INPUT_ISBNIPOINTS, false)
    }

    private val isShowAllPaymentChannels: Boolean by lazy {
        intent.getBooleanExtra(EXTRA_INPUT_ISSHOWALLPAYMENT, false)
    }

    private val paymentChannels: ArrayList<ListItem> by lazy {
        intent.getParcelableArrayListExtra(EXTRA_INPUT_PAYMENTCHANNELS)
            ?: throw RuntimeException("paymentChannels must not be empty")
    }

    private val inputColor: String by lazy {
        intent.getStringExtra(EXTRA_INPUT_COLOR)
            ?: throw RuntimeException("Input Color must not be empty")
    }

    private val whitelistBins: String by lazy {
        intent.getStringExtra(EXTRA_INPUT_WHITELISTBINS)
            ?: throw throw RuntimeException("Whitelist Bins must not be empty")
    }

    private val blacklistBins: String by lazy {
        intent.getStringExtra(EXTRA_INPUT_BLACKLISTBINS)
            ?: throw throw RuntimeException("Blacklist Bins must not be empty")
    }

    private val bcaVa: String by lazy {
        intent.getStringExtra(EXTRA_INPUT_BCAVA)
            ?: throw throw RuntimeException("BCAva must not be empty")
    }

    private val bniVa: String by lazy {
        intent.getStringExtra(EXTRA_INPUT_BNIVA)
            ?: throw throw RuntimeException("BNIva must not be empty")
    }

    private val permataVa: String by lazy {
        intent.getStringExtra(EXTRA_INPUT_PERMATAVA)
            ?: throw throw RuntimeException("PermataVA must not be empty")
    }

    private lateinit var customerDetailsLegacy: CustomerDetails
    private var installmentLegacy: Installment? = null
    private var expiryLegacy: ExpiryModel? = null
    private var bcaVaLegacy: BcaBankTransferRequestModel? = null
    private var permataVaLegacy: PermataBankTransferRequestModel? = null
    private var bniVaLegacy: BankTransferRequestModel? = null

    private var bank: String? = null
    private var ccAuthType: String? = null
    private var finalWhitelistBins: ArrayList<String> = arrayListOf()
    private var finalBlacklistBins: ArrayList<String> = arrayListOf()
    private var enabledPayment: List<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildLegacyUiKitStart()

        setContent {
            OrderListPage()
        }
    }

    @Preview
    @Composable
    fun OrderListPage() {
        val state = rememberScrollState()
        ShowChargeContent(state = state)
    }

    @Composable
    fun ShowChargeContent(
        state: ScrollState
    ) {
        Column(
            modifier = Modifier.verticalScroll(state)
        ) {
            OrderSummary()
            CustomerDetailsForm()
        }
    }

    @Composable
    fun OrderSummary(modifier: Modifier = Modifier) {
        SnapAppBar(title = "Order Review", iconResId = R.drawable.ic_arrow_left) {
            onBackPressed()
        }
        Column(
            modifier = modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Order Review & Delivery Options",
                style = SnapTypography.STYLES.snaTextBodySmall
            )
            Spacer(modifier = modifier.height(8.dp))
            Row {
                Image(
                    painter = painterResource(id = product.image),
                    contentDescription = product.name,
                    contentScale = ContentScale.FillBounds,
                    modifier = modifier
                        .height(50.dp)
                        .width(50.dp)
                        .padding(end = 10.dp)
                )
                Column {
                    Text(text = product.name, style = SnapTypography.STYLES.snapTextMediumMedium)
                    Text(text = "Qty 1", style = SnapTypography.STYLES.snapTextMediumRegular)
                }
                Spacer(
                    modifier
                        .weight(1f)
                        .fillMaxHeight()
                )

                Text(
                    text = "Rp.${(product.price).toString().dropLast(2)}",
                    style = SnapTypography.STYLES.snapTextMediumRegular
                )
            }
        }
    }

    @Composable
    fun CustomerDetailsForm(modifier: Modifier = Modifier) {
        var fullName by remember { mutableStateOf(TextFieldValue("Ferdian Julianto")) }
        var fullNameFieldFocused by remember { mutableStateOf(false) }

        var phoneNumber by remember { mutableStateOf(TextFieldValue("083812345678")) }
        var phoneNumberFieldFocused by remember { mutableStateOf(false) }

        var email by remember { mutableStateOf(TextFieldValue("hobinyabelajar@gmail.com")) }
        var emailFocused by remember { mutableStateOf(false) }

        var address by remember { mutableStateOf(TextFieldValue("Pasaraya Blok M Gedung B Lt. 3")) }
        var addressFocused by remember { mutableStateOf(false) }

        var snapToken by remember { mutableStateOf(TextFieldValue()) }
        var snapTokenFocused by remember { mutableStateOf(false) }

        Column(
            modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Customer Details",
                style = SnapTypography.STYLES.snaTextBodySmall
            )
            Text(
                text = "Full Name", style = SnapTypography.STYLES.snapTextSmallRegular,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            SnapTextField(
                value = fullName,
                onValueChange = { fullName = it },
                isFocused = fullNameFieldFocused,
                onFocusChange = {
                    fullNameFieldFocused = it
                },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            Text(
                text = "Phone Number", style = SnapTypography.STYLES.snapTextSmallRegular,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            SnapTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                isFocused = phoneNumberFieldFocused,
                onFocusChange = {
                    phoneNumberFieldFocused = it
                },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            Text(
                text = "Email", style = SnapTypography.STYLES.snapTextSmallRegular,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            SnapTextField(
                value = email,
                onValueChange = { email = it },
                isFocused = emailFocused,
                onFocusChange = {
                    emailFocused = it
                },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            Text(
                text = "Your Delivery Address",
                style = SnapTypography.STYLES.snapTextSmallRegular,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            SnapTextField(
                value = address,
                onValueChange = { address = it },
                isFocused = addressFocused,
                onFocusChange = {
                    addressFocused = it
                },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            Text(
                text = "Snap Token",
                style = SnapTypography.STYLES.snapTextSmallRegular,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            SnapTextField(
                value = snapToken,
                onValueChange = { snapToken = it },
                isFocused = snapTokenFocused,
                onFocusChange = {
                    snapTokenFocused = it
                },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            SnapButton(
                text = "pay with snap token", style = SnapButton.Style.TERTIARY,
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                onClick = {
                    buildLegacyUiKit()
                    payWithSnapToken(snapToken.text)
                }
            )

            bank = DemoUtils.populateAcquiringBank(acquiringBank)
            ccAuthType = DemoUtils.populateCCAuthType(isPreAuth)
            finalWhitelistBins = DemoUtils.populateWhitelistBins(whitelistBins, isBniPointOnly)
            finalBlacklistBins = DemoUtils.populateBlacklistBins(blacklistBins)
            enabledPayment = DemoUtils.populateEnabledPayment(paymentChannels, isShowAllPaymentChannels)

            SnapButton(
                text = "Pay Rp.${(product.price).toString().dropLast(2)} Legacy",
                style = SnapButton.Style.PRIMARY,
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                onClick = {
                    val name = fullName.text
                    val index = name.lastIndexOf(' ')
                    val firstName = index.let { name.substring(0, it) }
                    val lastName = index.plus(1).let { name.substring(it) }

                    val shippingAddress = ShippingAddress()
                    shippingAddress.setAddress("Jalan Andalas Gang Sebelah No. 1")
                    shippingAddress.setCity("Jakarta")
                    shippingAddress.setPostalCode("10220")

                    val billingAddress = BillingAddress()
                    billingAddress.setAddress("Jalan Andalas Gang Sebelah No. 1")
                    billingAddress.setCity("Jakarta")
                    billingAddress.setPostalCode("10220")

                    customerDetailsLegacy = CustomerDetails()
                    customerDetailsLegacy.setCustomerIdentifier("3A8788CE-B96F-449C-8180-B5901A08B50A")
                    customerDetailsLegacy.setFirstName(firstName)
                    customerDetailsLegacy.setLastName(lastName)
                    customerDetailsLegacy.setEmail(email.text)
                    customerDetailsLegacy.setPhone(phoneNumber.text)
                    customerDetailsLegacy.setBillingAddress(billingAddress)
                    customerDetailsLegacy.setShippingAddress(shippingAddress)

                    installmentLegacy = populateInstallmentLegacy()
                    expiryLegacy = populateExpiryLegacy()
                    bcaVaLegacy = populateBcaVaLegacy(bcaVa)
                    permataVaLegacy = populatePermataVaLegacy(permataVa)
                    bniVaLegacy = populateVaLegacy(bniVa)
                    buildLegacyUiKit()
                    payWithOldSnapLegacyApi()
                }
            )
        }
    }

    private fun payWithSnapToken(snapToken: String) {
        MidtransSDK.getInstance().startPaymentUiFlow(this@OrderReviewLegacyActivity, snapToken)
    }
    private fun payWithOldSnapLegacyApi() {
        val transactionRequest = TransactionRequest(
            UUID.randomUUID().toString(),
            product.price
        )
        val creditCard = com.midtrans.sdk.corekit.models.snap.CreditCard()
        creditCard.setSaveCard(isSavedCard)
        creditCard.setAuthentication(authenticationType)
        creditCard.setBank(bank)
        creditCard.setWhitelistBins(finalWhitelistBins)
        creditCard.setBlacklistBins(finalBlacklistBins)
        creditCard.setInstallment(installmentLegacy)
        creditCard.setType(ccAuthType)

        transactionRequest.creditCard = creditCard

        //Setting Snap token custom expiry
        transactionRequest.expiry = expiryLegacy
        transactionRequest.customerDetails = customerDetailsLegacy

        val itemDetails = arrayListOf(ItemDetails("Test01", product.price, 1, product.name))
        transactionRequest.itemDetails = itemDetails
        transactionRequest.bcaVa = bcaVaLegacy
        transactionRequest.bniVa = bniVaLegacy
        transactionRequest.permataVa = permataVaLegacy
        transactionRequest.enabledPayments = enabledPayment
        transactionRequest.gopay = Gopay("demo://snap")
        transactionRequest.shopeepay = Shopeepay("demo://snap")
        transactionRequest.uobEzpay = UobEzpay("demo://snap")
        transactionRequest.customField1 = "test1"
        transactionRequest.customField2 = "test2"
        transactionRequest.customField3 = "test3"
        MidtransSDK.getInstance().transactionRequest = transactionRequest

        val uisetting = UIKitCustomSetting()
        uisetting.setSaveCardChecked(true)
        MidtransSDK.getInstance().setUiKitCustomSetting(uisetting)

        MidtransSDK.getInstance().startPaymentUiFlow(this@OrderReviewLegacyActivity)
    }

    private fun populateInstallmentLegacy(): Installment? {
        var output: Installment? = null
        if (installmentBank != NO_INSTALLMENT) {
            val installment = Installment()
            installment.terms = mapOf(installmentBank to arrayListOf(3, 6, 12))
            installment.isRequired = isRequiredInstallment
            output = installment
        }
        return output
    }

    private fun populateExpiryLegacy(): ExpiryModel? {
        var expiryModel: ExpiryModel? = null
        if (customExpiry != NONE) {
            expiryModel = ExpiryModel()
            expiryModel.setStartTime(DemoUtils.getFormattedTime(System.currentTimeMillis()))
            expiryModel.setUnit(
                when (customExpiry) {
                    ONE_HOUR -> ExpiryModel.UNIT_HOUR
                    else -> ExpiryModel.UNIT_MINUTE
                }
            )
            expiryModel.setDuration(
                when (customExpiry) {
                    FIVE_MINUTE -> 5
                    else -> 1
                }
            )
        }
        return expiryModel
    }

    private fun populateBcaVaLegacy(va: String): BcaBankTransferRequestModel? {
        var vaTransferRequest: BcaBankTransferRequestModel? = null
        if (va.isNotEmpty()) {
            val SUB_COMPANY_CODE_BCA = "12321"
            val bcaRequestModel = BcaBankTransferRequestModel(
                va, FreeText(
                    listOf(
                        FreeTextLanguage(
                            "Text ID inquiry 0",
                            "Text EN inquiry 0"
                        )
                    ),
                    listOf(
                        FreeTextLanguage(
                            "Text ID inquiry 0",
                            "Text EN inquiry 0"
                        )
                    )
                )
            )
            bcaRequestModel.subCompanyCode = SUB_COMPANY_CODE_BCA
            vaTransferRequest = bcaRequestModel
        }
        return vaTransferRequest
    }

    private fun populatePermataVaLegacy(va: String): PermataBankTransferRequestModel? {
        val permataRecipient = "Sudarsono"
        var vaTransferRequest: PermataBankTransferRequestModel? = null
        if (va.isNotEmpty()) {
            val permataRequest = PermataBankTransferRequestModel(va)
            permataRequest.setRecipientName(permataRecipient)
            vaTransferRequest = permataRequest
        }
        return vaTransferRequest
    }

    private fun populateVaLegacy(va: String): BankTransferRequestModel? {
        var vaTransferRequest: BankTransferRequestModel? = null
        if (va.isNotEmpty()) {
            vaTransferRequest = BankTransferRequestModel(
                va
            )
        }
        return vaTransferRequest
    }

    private fun buildLegacyUiKit() {
        val builder = SdkUIFlowBuilder.init()
            .setClientKey("SB-Mid-client-hOWJXiCCDRvT0RGr")
            .setContext(this.applicationContext)
            .setTransactionFinishedCallback(this)
            .setMerchantBaseUrl("https://snap-merchant-server.herokuapp.com/api/")
            .setDefaultText("fonts/SourceSansPro-Regular.ttf")
            .setSemiBoldText("fonts/SourceSansPro-Semibold.ttf")
            .setBoldText("fonts/SourceSansPro-Bold.ttf")
            .setLanguage("en")

        getCustomColor(inputColor)?.let { builder.setColorTheme(it) }
        builder.buildSDK()
    }

    private fun buildLegacyUiKitStart() {
        val builder = SdkUIFlowBuilder.init()
            .setClientKey("SB-Mid-client-hOWJXiCCDRvT0RGr")
            .setContext(this.applicationContext)
            .setTransactionFinishedCallback(this)
            .setMerchantBaseUrl("https://snap-merchant-server.herokuapp.com/api/")
            .setDefaultText("fonts/SourceSansPro-Regular.ttf")
            .setSemiBoldText("fonts/SourceSansPro-Semibold.ttf")
            .setBoldText("fonts/SourceSansPro-Bold.ttf")
            .setLanguage("en")

        getCustomColor(inputColor)?.let { builder.setColorTheme(it) }
        builder.buildSDK()
    }

    private fun getCustomColor(inputColor: String): CustomColorTheme? {
        var color: CustomColorTheme? = null
        when (inputColor) {
            DemoConstant.COLOR_BLUE -> {
                color = CustomColorTheme(
                    "#0e4e95",
                    "#000000",
                    "#0x3e71aa"
                )
            }
            DemoConstant.COLOR_RED -> {
                color = CustomColorTheme(
                    "#b11235",
                    "#000000",
                    "#f36b89"
                )
            }
            DemoConstant.COLOR_GREEN -> {
                color = CustomColorTheme(
                    "#32ad4a",
                    "#000000",
                    "#5bbd6e"
                )
            }
        }
        return color
    }

    companion object {
        private const val EXTRA_PRODUCT = "orderReviewLegacy.extra.product"
        private const val EXTRA_INPUT_INSTALLMENT = "orderReviewLegacy.extra.installment"
        private const val EXTRA_INPUT_ISREQUIRED = "orderReviewLegacy.extra.isRequired"
        private const val EXTRA_INPUT_ACQUIRINGBANK = "orderReviewLegacy.extra.acquiringBank"
        private const val EXTRA_INPUT_EXPIRY = "orderReviewLegacy.extra.expiry"
        private const val EXTRA_INPUT_CCAUTHENTICATIONTYPE = "orderReviewLegacy.extra.ccAuthenticationType"
        private const val EXTRA_INPUT_WHITELISTBINS = "orderReviewLegacy.extra.whitelistBins"
        private const val EXTRA_INPUT_BLACKLISTBINS = "orderReviewLegacy.extra.blacklistBins"
        private const val EXTRA_INPUT_BCAVA = "orderReviewLegacy.extra.bcaVa"
        private const val EXTRA_INPUT_BNIVA = "orderReviewLegacy.extra.bniVa"
        private const val EXTRA_INPUT_PERMATAVA = "orderReviewLegacy.extra.permataVa"
        private const val EXTRA_INPUT_ISSAVEDCARD = "orderReviewLegacy.extra.isSavedCard"
        private const val EXTRA_INPUT_ISPREAUTH = "orderReviewLegacy.extra.isPreAuth"
        private const val EXTRA_INPUT_ISBNIPOINTS = "orderReviewLegacy.extra.isBniPoints"
        private const val EXTRA_INPUT_ISSHOWALLPAYMENT = "orderReviewLegacy.extra.isShowAllPayment"
        private const val EXTRA_INPUT_PAYMENTCHANNELS = "orderReviewLegacy.extra.paymentChannels"
        private const val EXTRA_INPUT_COLOR = "orderReviewLegacy.extra.inputColor"

        fun getOrderReviewLegacyActivityIntent(
            activityContext: Context,
            product: Product,
            installmentBank: String,
            isRequiredInstallment: Boolean,
            acquiringBank: String,
            customExpiry: String,
            authenticationType: String,
            isSavedCard: Boolean,
            isPreAuth: Boolean,
            isBniPointsOnly: Boolean,
            isShowAllPaymentChannels: Boolean,
            paymentChannels: ArrayList<ListItem>,
            whitelistBins: String,
            blacklistBins: String,
            bcaVa: String,
            bniVa: String,
            permataVa: String,
            color: String
        ): Intent {
            return Intent(activityContext, OrderReviewLegacyActivity::class.java).apply {
                putExtra(EXTRA_PRODUCT, product)
                putExtra(EXTRA_INPUT_INSTALLMENT, installmentBank)
                putExtra(EXTRA_INPUT_ISREQUIRED, isRequiredInstallment)
                putExtra(EXTRA_INPUT_ACQUIRINGBANK, acquiringBank)
                putExtra(EXTRA_INPUT_EXPIRY, customExpiry)
                putExtra(EXTRA_INPUT_CCAUTHENTICATIONTYPE, authenticationType)
                putExtra(EXTRA_INPUT_ISSAVEDCARD, isSavedCard)
                putExtra(EXTRA_INPUT_ISPREAUTH, isPreAuth)
                putExtra(EXTRA_INPUT_ISBNIPOINTS, isBniPointsOnly)
                putExtra(EXTRA_INPUT_ISSHOWALLPAYMENT, isShowAllPaymentChannels)
                putExtra(EXTRA_INPUT_PAYMENTCHANNELS, paymentChannels)
                putExtra(EXTRA_INPUT_WHITELISTBINS, whitelistBins)
                putExtra(EXTRA_INPUT_BLACKLISTBINS, blacklistBins)
                putExtra(EXTRA_INPUT_BCAVA, bcaVa)
                putExtra(EXTRA_INPUT_BNIVA, bniVa)
                putExtra(EXTRA_INPUT_PERMATAVA, permataVa)
                putExtra(EXTRA_INPUT_COLOR, color)
            }
        }
    }
}