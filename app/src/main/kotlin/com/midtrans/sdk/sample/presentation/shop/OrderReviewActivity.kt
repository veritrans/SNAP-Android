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
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.corekit.models.ExpiryModel
import com.midtrans.sdk.sample.model.Product
import com.midtrans.sdk.sample.util.DemoConstant.FIVE_MINUTE
import com.midtrans.sdk.sample.util.DemoConstant.NONE
import com.midtrans.sdk.sample.util.DemoConstant.NORMAL_CC_PAYMENT
import com.midtrans.sdk.sample.util.DemoConstant.NO_ACQUIRING_BANK
import com.midtrans.sdk.sample.util.DemoConstant.NO_INSTALLMENT
import com.midtrans.sdk.sample.util.DemoConstant.ONE_CLICK_TYPE
import com.midtrans.sdk.sample.util.DemoConstant.ONE_HOUR
import com.midtrans.sdk.sample.util.DemoUtils
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import com.midtrans.sdk.uikit.api.model.*
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import com.midtrans.sdk.uikit.internal.view.SnapAppBar
import com.midtrans.sdk.uikit.internal.view.SnapButton
import com.midtrans.sdk.uikit.internal.view.SnapTextField
import com.midtrans.sdk.uikit.internal.view.SnapTypography
import java.util.*


class OrderReviewActivity : ComponentActivity() {

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result?.resultCode == RESULT_OK) {
                result.data?.let {
                    val transactionResult = it.getParcelableExtra<TransactionResult>(
                        UiKitConstants.KEY_TRANSACTION_RESULT
                    )
                    Toast.makeText(
                        this@OrderReviewActivity,
                        "Coba trxid ${transactionResult?.transactionId.orEmpty()}",
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
            Toast.makeText(
                this@OrderReviewActivity,
                "Transaction ${transactionResult?.transactionId.orEmpty()} status ${transactionResult?.status.orEmpty()}",
                Toast.LENGTH_LONG
            ).show()
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

    private val ccPaymentType: String by lazy {
        intent.getStringExtra(EXTRA_INPUT_CCPAYMENTTYPE)
            ?: throw RuntimeException("CCPaymentType must not be empty")
    }

    private val bcaVa: String by lazy {
        intent.getStringExtra(EXTRA_INPUT_BCAVA)
            ?: throw throw RuntimeException("BCAva must not be empty")
    }

    private val bniVa: String by lazy {
        intent.getStringExtra(EXTRA_INPUT_BNIVA)
            ?: throw throw RuntimeException("BNIva must not be empty")
    }

    private val uiKitApi: UiKitApi by lazy {
        UiKitApi.getDefaultInstance()
    }

    private lateinit var customerDetails: CustomerDetails
    private lateinit var itemDetails: List<ItemDetails>
    private lateinit var transactionDetails: SnapTransactionDetail
    private var installment: Installment? = null
    private var expiry: Expiry? = null
    private var bank: String? = null
    private var isSavedCard: Boolean = false
    private var isSecure: Boolean = false
    private var bcaVaRequest: BankTransferRequest? = null
    private var bniVaRequest: BankTransferRequest? = null

    private fun setLocaleNew(languageCode: String?) {
        val locales = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(locales)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setLocaleNew("id") // commented for now. conflict with buildLegacyUiKit

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
                    payWithAndroidxActivityResultLauncherToken(snapToken.text)
                }
            )
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
                    bank = populateAcquiringBank()
                    expiry = populateExpiry()
                    isSecure = populateIsSecure()
                    isSavedCard = populateIsSavedCard()
                    bcaVaRequest = populateBcaVa()
                    bniVaRequest = populateBniVa()
                    payWithAndroidxActivityResultLauncher()
                }
            )
        }
    }

    private fun populateBniVa(): BankTransferRequest? {
        var bniTransferRequest: BankTransferRequest? = null
        if (bniVa != "") {
            bniTransferRequest = BankTransferRequest(
                vaNumber = bniVa
            )
        }
        return bniTransferRequest
    }

    private fun populateBcaVa(): BankTransferRequest? {
        var bcaTransferRequest: BankTransferRequest? = null
        if (bcaVa != "") {
            bcaTransferRequest = BankTransferRequest(
                vaNumber = bcaVa,
                freeText = FreeText(
                    inquiry = listOf(
                        FreeTextLanguage(
                            "Text ID inquiry 0",
                            "Text EN inquiry 0"
                        )
                    ), payment = listOf(FreeTextLanguage("Text ID inquiry 0", "Text EN inquiry 0"))
                )
            )
        }
        return bcaTransferRequest
    }

    private fun populateIsSavedCard(): Boolean {
        var isSaved = false
        if (ccPaymentType != NORMAL_CC_PAYMENT) {
            isSaved = true
        }
        return isSaved
    }

    private fun populateIsSecure(): Boolean {
        var isSecure = false
        if (ccPaymentType == ONE_CLICK_TYPE) {
            isSecure = true
        }
        return isSecure
    }

    private fun populateAcquiringBank(): String? {
        var bank: String? = null
        if (acquiringBank != NO_ACQUIRING_BANK) {
            bank = acquiringBank
        }
        return bank
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

    private fun buildLegacyUiKit() {
        SdkUIFlowBuilder.init()
            ?.setContext(this.applicationContext)
            ?.setMerchantBaseUrl("https://fiesta-point-sample.herokuapp.com/")
            ?.setClientKey("SB-Mid-client-hOWJXiCCDRvT0RGr")
//            .setExternalScanner { _, _ -> TODO("Not yet implemented") }
            ?.enableLog(true)
            ?.setDefaultText("fonts/SourceSansPro-Regular.ttf")
            ?.setBoldText("fonts/SourceSansPro-Bold.ttf")
            ?.setSemiBoldText("fonts/SourceSansPro-Semibold.ttf")
            ?.setColorTheme(CustomColorTheme("#0e4e95", "#0b3b70", "#3e71aa"))
            ?.setLanguage("en") //setLanguage to either "en" for english or "id" for bahasa
            ?.buildSDK()
    }

    private fun payWithLegacyActivityStartActivityForResult() {
        uiKitApi.startPaymentWithLegacyAndroid(
            activity = this@OrderReviewActivity,
            requestCode = 1151,
            transactionDetails = SnapTransactionDetail(
                orderId = UUID.randomUUID().toString(),
                grossAmount = 15005.00
            ),
            creditCard = CreditCard(
                saveCard = true,
                secure = true,
                installment = Installment(
                    isRequired = false,
                    terms = mapOf("BNI" to listOf(3, 6, 12))
                )
            ),
            userId = "3A8788CE-B96F-449C-8180-B5901A08B50A",
            customerDetails = CustomerDetails(
                firstName = "Aris",
                lastName = "Bhaktis",
                email = "arisbhaktis@email.com",
                phone = "087788778212"
            ),
            itemDetails = itemDetails,
            bcaVa = bcaVaRequest,
            bniVa = bniVaRequest
        )
    }

    private fun payWithAndroidxActivityResultLauncher() {
        uiKitApi.startPaymentWithAndroidX(
            activity = this@OrderReviewActivity,
            launcher = launcher,
            transactionDetails = transactionDetails,
            creditCard = CreditCard(
                saveCard = isSavedCard,
                secure = isSecure,
                installment = installment,
                bank = bank
            ),
            snapTokenExpiry = expiry,
            userId = "3A8788CE-B96F-449C-8180-B5901A08B50A",
            customerDetails = customerDetails,
            itemDetails = itemDetails,
            bcaVa = bcaVaRequest,
            bniVa = bniVaRequest
        )
    }

    private fun payWithAndroidxActivityResultLauncherToken(token: String?) {
        uiKitApi.startPaymentWithAndroidXToken(
            activity = this@OrderReviewActivity,
            launcher = launcher,
            snapToken = token
        )
    }

    private fun payWithOldSnapLegacyApi() {
        val transactionRequest = TransactionRequest(
            UUID.randomUUID().toString(),
            3000.0
        )
        transactionRequest.customerDetails = com.midtrans.sdk.corekit.models.CustomerDetails(
            "3A8788CE-B96F-449C-8180-B5901A08B50A",
            "Ari",
            "Bhakti",
            "aribhakti@email.com",
            "087788778212"
        )
        transactionRequest.creditCard = com.midtrans.sdk.corekit.models.snap.CreditCard(
            true,
            null,
            true,
            null,
            "MANDIRI", //Acquiring Bank by Mandiri
            null,
            null,
            null,
            com.midtrans.sdk.corekit.models.snap.Installment(
                false,
                null
            ),
            null,
            null
        )
        //Setting Snap token custon expiry
        transactionRequest.expiry = ExpiryModel(
            DemoUtils.getFormattedTime(System.currentTimeMillis()),
            "hour",
            1
        )
        MidtransSDK.getInstance().uiKitCustomSetting.setSaveCardChecked(true)
        MidtransSDK.getInstance().transactionRequest = transactionRequest
        MidtransSDK.getInstance().startPaymentUiFlow(this.applicationContext)
    }

    companion object {
        private const val EXTRA_PRODUCT = "orderReview.extra.product"
        private const val EXTRA_INPUT_INSTALLMENT = "orderReview.extra.installment"
        private const val EXTRA_INPUT_ISREQUIRED = "orderReview.extra.isRequired"
        private const val EXTRA_INPUT_ACQUIRINGBANK = "orderReview.extra.acquiringBank"
        private const val EXTRA_INPUT_EXPIRY = "orderReview.extra.expiry"
        private const val EXTRA_INPUT_CCPAYMENTTYPE = "orderReview.extra.ccPaymentType"
        private const val EXTRA_INPUT_BCAVA = "orderReview.extra.bcaVa"
        private const val EXTRA_INPUT_BNIVA = "orderReview.extra.bniVa"

        fun getOrderReviewActivityIntent(
            activityContext: Context,
            product: Product,
            installmentBank: String,
            isRequiredInstallment: Boolean,
            acquiringBank: String,
            customExpiry: String,
            ccPaymentType: String,
            bcaVa: String,
            bniVa: String
        ): Intent {
            return Intent(activityContext, OrderReviewActivity::class.java).apply {
                putExtra(EXTRA_PRODUCT, product)
                putExtra(EXTRA_INPUT_INSTALLMENT, installmentBank)
                putExtra(EXTRA_INPUT_ISREQUIRED, isRequiredInstallment)
                putExtra(EXTRA_INPUT_ACQUIRINGBANK, acquiringBank)
                putExtra(EXTRA_INPUT_EXPIRY, customExpiry)
                putExtra(EXTRA_INPUT_CCPAYMENTTYPE, ccPaymentType)
                putExtra(EXTRA_INPUT_BCAVA, bcaVa)
                putExtra(EXTRA_INPUT_BNIVA, bniVa)
            }
        }
    }
}