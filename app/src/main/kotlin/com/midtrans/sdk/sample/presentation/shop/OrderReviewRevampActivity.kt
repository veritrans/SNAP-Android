package com.midtrans.sdk.sample.presentation.shop

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
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
import androidx.core.os.LocaleListCompat
import com.midtrans.sdk.corekit.core.PaymentMethod
import com.midtrans.sdk.sample.model.ListItem
import com.midtrans.sdk.sample.model.Product
import com.midtrans.sdk.sample.util.DemoConstant
import com.midtrans.sdk.sample.util.DemoConstant.DISABLED
import com.midtrans.sdk.sample.util.DemoConstant.FIVE_MINUTE
import com.midtrans.sdk.sample.util.DemoConstant.NONE
import com.midtrans.sdk.sample.util.DemoConstant.NO_INSTALLMENT
import com.midtrans.sdk.sample.util.DemoConstant.ONE_HOUR
import com.midtrans.sdk.sample.util.DemoUtils
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.api.model.*
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import com.midtrans.sdk.uikit.internal.util.UiKitConstants.STATUS_CANCELED
import com.midtrans.sdk.uikit.internal.util.UiKitConstants.STATUS_FAILED
import com.midtrans.sdk.uikit.internal.util.UiKitConstants.STATUS_INVALID
import com.midtrans.sdk.uikit.internal.util.UiKitConstants.STATUS_PENDING
import com.midtrans.sdk.uikit.internal.util.UiKitConstants.STATUS_SUCCESS
import com.midtrans.sdk.uikit.internal.view.SnapAppBar
import com.midtrans.sdk.uikit.internal.view.SnapButton
import com.midtrans.sdk.uikit.internal.view.SnapTextField
import com.midtrans.sdk.uikit.internal.view.SnapTypography
import java.util.*

class OrderReviewRevampActivity : ComponentActivity() {

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result?.resultCode == RESULT_OK) {
                result.data?.let {
                    val transactionResult = it.getParcelableExtra<TransactionResult>(
                        UiKitConstants.KEY_TRANSACTION_RESULT
                    )
                    Toast.makeText(
                        this@OrderReviewRevampActivity,
                        "Coba trxid ${transactionResult?.transactionId.orEmpty()} trx status ${transactionResult?.status.orEmpty()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            val transactionResult = data?.getParcelableExtra<TransactionResult>(
                UiKitConstants.KEY_TRANSACTION_RESULT
            )
            if (transactionResult != null) {
                when (transactionResult.status) {
                    STATUS_SUCCESS -> {
                        Toast.makeText(
                            this,
                            "Transaction Finished. ID: " + transactionResult.transactionId,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    STATUS_PENDING -> {
                        Toast.makeText(
                            this,
                            "Transaction Pending. ID: " + transactionResult.transactionId,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    STATUS_FAILED -> {
                        Toast.makeText(
                            this,
                            "Transaction Failed. ID: " + transactionResult.transactionId + ". Message: " + transactionResult.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    STATUS_CANCELED -> {
                        Toast.makeText(this, "Transaction Cancelled", Toast.LENGTH_LONG).show()
                    }
                    STATUS_INVALID -> {
                        Toast.makeText(
                            this,
                            "Transaction Invalid. ${transactionResult.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    else -> {
                        Toast.makeText(
                            this,
                            "Transaction ID: " + transactionResult.transactionId + ". Message: " + transactionResult.status,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } else {
                Toast.makeText(this, "Transaction Invalid", Toast.LENGTH_LONG).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
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

    private val cimbVa: String by lazy {
        intent.getStringExtra(EXTRA_INPUT_CIMBVA)
            ?: throw throw RuntimeException("CimbVA must not be empty")
    }

    private val directPaymentScreen: String by lazy {
        intent.getStringExtra(EXTRA_INPUT_DIRECT_PAYMENT_SCREEN)
            ?: throw throw RuntimeException("Direct Payment Screen value error")
    }

    private val uiKitApi: UiKitApi by lazy {
        UiKitApi.getDefaultInstance()
    }

    private lateinit var customerDetails: CustomerDetails
    private lateinit var itemDetails: List<ItemDetails>
    private lateinit var transactionDetails: SnapTransactionDetail
    private var installment: Installment? = null
    private var expiry: Expiry? = null
    private var bcaVaRequest: BankTransferRequest? = null
    private var bniVaRequest: BankTransferRequest? = null
    private var permataVaRequest: BankTransferRequest? = null
    private var cimbVaRequest: BankTransferRequest? = null

    private var bank: String? = null
    private var ccAuthType: String? = null
    private var finalWhitelistBins: ArrayList<String> = arrayListOf()
    private var finalBlacklistBins: ArrayList<String> = arrayListOf()
    private var enabledPayment: List<String>? = null

    private fun setLocaleNew(languageCode: String?) {
        val locales = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(locales)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLocaleNew("id")

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
                    buildUiKit()
                    payWithAndroidxActivityResultLauncherToken(snapToken.text)
                }
            )

            bank = DemoUtils.populateAcquiringBank(acquiringBank)
            ccAuthType = DemoUtils.populateCCAuthType(isPreAuth)
            finalWhitelistBins = DemoUtils.populateWhitelistBins(whitelistBins, isBniPointOnly)
            finalBlacklistBins = DemoUtils.populateBlacklistBins(blacklistBins)
            enabledPayment = DemoUtils.populateEnabledPayment(paymentChannels, isShowAllPaymentChannels)

            SnapButton(
                text = "Pay Rp.${(product.price).toString().dropLast(2)}",
                style = SnapButton.Style.PRIMARY,
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                onClick = {
                    val name = fullName.text
                    val index = name.lastIndexOf(' ')
                    val firstName = index.let { name.substring(0, it) }
                    val lastName = index.plus(1).let { name.substring(it) }

                    transactionDetails = SnapTransactionDetail(
                        orderId = UUID.randomUUID().toString(),
                        grossAmount = product.price
                    )
                    customerDetails = CustomerDetails(
                        firstName = firstName,
                        lastName = lastName,
                        email = email.text,
                        phone = phoneNumber.text,
                        shippingAddress = Address(address = address.text)
                    )
                    itemDetails = listOf(ItemDetails("test-01", product.price, 1, product.name))
                    installment = populateInstallment()
                    expiry = populateExpiry()
                    bcaVaRequest = populateVa(bcaVa)
                    bniVaRequest = populateVa(bniVa)
                    permataVaRequest = populateVa(permataVa)
                    cimbVaRequest = populateVa(cimbVa)
                    buildUiKit()
                    payWithAndroidxActivityResultLauncher()
                }
            )
        }
    }

    private fun populateVa(va: String): BankTransferRequest? {
        var vaTransferRequest: BankTransferRequest? = null
        if (va.isNotEmpty()) {
            vaTransferRequest = BankTransferRequest(
                vaNumber = va
            )
        }
        return vaTransferRequest
    }

    private fun populateExpiry(): Expiry? {
        var expiry: Expiry? = null
        if (customExpiry != NONE) {
            expiry = Expiry(
                startTime = DemoUtils.getFormattedTime(System.currentTimeMillis()),
                unit = when (customExpiry) {
                    ONE_HOUR -> Expiry.UNIT_HOUR
                    else -> Expiry.UNIT_MINUTE
                },
                duration = when (customExpiry) {
                    FIVE_MINUTE -> 5
                    else -> 1
                }
            )
        }
        return expiry
    }

    private fun populateInstallment(): Installment? {
        var installment: Installment? = null
        if (installmentBank != NO_INSTALLMENT) {
            installment = Installment(
                isRequired = isRequiredInstallment,
                terms = mapOf(installmentBank to listOf(3, 6, 12))
            )
        }
        return installment
    }

    private fun buildUiKit() {
        val builder = UiKitApi.Builder()
            .withContext(this.applicationContext)
            .withMerchantUrl("https://demo.midtrans.com/api/")
            .withMerchantClientKey("VT-client-yrHf-c8Sxr-ck8tx")
            .withFontFamily("fonts/SourceSansPro-Italic.ttf")
            .enableLog(true)

        getCustomColor(inputColor)?.let { builder.withColorTheme(it) }
        builder.build()
    }

    private fun uiKitCustomSetting() {
        val uiKitCustomSetting = uiKitApi.uiKitSetting
        uiKitCustomSetting.saveCardChecked = false
    }

    private fun payWithAndroidxActivityResultLauncher() {
        uiKitApi.startPaymentUiFlow(
            activity = this@OrderReviewRevampActivity,
            launcher = launcher,
            transactionDetails = transactionDetails,
            creditCard = CreditCard(
                saveCard = isSavedCard,
                authentication = authenticationType,
                installment = installment,
                bank = bank,
                type = ccAuthType,
                whitelistBins = finalWhitelistBins,
                blacklistBins = finalBlacklistBins
            ),
            snapTokenExpiry = expiry,
            userId = "3A8788CE-B96F-449C-8180-B5901A08B50A",
            customerDetails = customerDetails,
            itemDetails = itemDetails,
            bcaVa = bcaVaRequest,
            bniVa = bniVaRequest,
            permataVa = permataVaRequest,
            cimbVa = cimbVaRequest,
            enabledPayment = enabledPayment,
            gopayCallback = handleGopayCallbackUrlCreation(),
            shopeepayCallback = handleShopeePayCallbackUrlCreation(),
            uobEzpayCallback = handleUOBCallbackUrlCreation(),
            customField1 = "lalalala1",
            customField2 = "lalalala2",
            customField3 = "lalalala3",
            paymentMethod = handleDirectPaymentMethodSelection()
        )
        uiKitCustomSetting()
    }

    private fun handleGopayCallbackUrlCreation() : GopayPaymentCallback? {
        if (enabledPayment?.contains(PaymentType.GOPAY) == true) {
            return  GopayPaymentCallback("demo://snap")
        }
        return  null
    }
    private fun handleShopeePayCallbackUrlCreation() : PaymentCallback? {
        if (enabledPayment?.contains(PaymentType.SHOPEEPAY) == true) {
            return  PaymentCallback("demo://snap")
        }
        return  null
    }

    private fun handleUOBCallbackUrlCreation() : PaymentCallback? {
        if (enabledPayment?.contains(PaymentType.UOB_EZPAY) == true) {
            return  PaymentCallback("demo://snap")
        }
        return  null
    }

    private fun handleDirectPaymentMethodSelection(): PaymentMethod? {
        return if (directPaymentScreen == DISABLED){
            null
        } else {
            PaymentMethod.valueOf(directPaymentScreen)
        }
    }

    private fun payWithAndroidxActivityResultLauncherToken(token: String?) {
        uiKitApi.startPaymentUiFlow(
            activity = this@OrderReviewRevampActivity,
            launcher = launcher,
            snapToken = token,
            paymentMethod = handleDirectPaymentMethodSelection()
        )
    }
    private fun getCustomColor(inputColor: String): CustomColorTheme? {
        var color: CustomColorTheme? = null
        when (inputColor) {
            DemoConstant.COLOR_BLUE -> {
                color = CustomColorTheme("#0e4e95", "#000000", "#0x3e71aa")
            }
            DemoConstant.COLOR_RED -> {
                color = CustomColorTheme(
                    colorPrimaryHex = "#b11235",
                    colorPrimaryDarkHex = "#000000",
                    colorSecondaryHex = "#f36b89"
                )
            }
            DemoConstant.COLOR_GREEN -> {
                color = CustomColorTheme(
                    colorPrimaryHex = "#32ad4a",
                    colorPrimaryDarkHex = "#000000",
                    colorSecondaryHex = "#5bbd6e"
                )
            }
        }
        return color
    }

    companion object {
        private const val EXTRA_PRODUCT = "orderReviewRevamp.extra.product"
        private const val EXTRA_INPUT_INSTALLMENT = "orderReviewRevamp.extra.installment"
        private const val EXTRA_INPUT_ISREQUIRED = "orderReviewRevamp.extra.isRequired"
        private const val EXTRA_INPUT_ACQUIRINGBANK = "orderReviewRevamp.extra.acquiringBank"
        private const val EXTRA_INPUT_EXPIRY = "orderReviewRevamp.extra.expiry"
        private const val EXTRA_INPUT_CCAUTHENTICATIONTYPE = "orderReviewRevamp.extra.ccAuthenticationType"
        private const val EXTRA_INPUT_WHITELISTBINS = "orderReviewRevamp.extra.whitelistBins"
        private const val EXTRA_INPUT_BLACKLISTBINS = "orderReviewRevamp.extra.blacklistBins"
        private const val EXTRA_INPUT_BCAVA = "orderReviewRevamp.extra.bcaVa"
        private const val EXTRA_INPUT_BNIVA = "orderReviewRevamp.extra.bniVa"
        private const val EXTRA_INPUT_PERMATAVA = "orderReviewRevamp.extra.permataVa"
        private const val EXTRA_INPUT_CIMBVA = "orderReviewRevamp.extra.cimbVa"
        private const val EXTRA_INPUT_ISSAVEDCARD = "orderReviewRevamp.extra.isSavedCard"
        private const val EXTRA_INPUT_ISPREAUTH = "orderReviewRevamp.extra.isPreAuth"
        private const val EXTRA_INPUT_ISBNIPOINTS = "orderReviewRevamp.extra.isBniPoints"
        private const val EXTRA_INPUT_ISSHOWALLPAYMENT = "orderReviewRevamp.extra.isShowAllPayment"
        private const val EXTRA_INPUT_PAYMENTCHANNELS = "orderReviewRevamp.extra.paymentChannels"
        private const val EXTRA_INPUT_COLOR = "orderReviewRevamp.extra.inputColor"
        private const val EXTRA_INPUT_DIRECT_PAYMENT_SCREEN = "orderReviewRevamp.extra.directPaymentScreen"

        fun getOrderReviewRevampActivityIntent(
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
            cimbVa: String,
            color: String,
            directPaymentScreen: String
        ): Intent {
            return Intent(activityContext, OrderReviewRevampActivity::class.java).apply {
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
                putExtra(EXTRA_INPUT_CIMBVA, cimbVa)
                putExtra(EXTRA_INPUT_COLOR, color)
                putExtra(EXTRA_INPUT_DIRECT_PAYMENT_SCREEN, directPaymentScreen)
            }
        }
    }
}