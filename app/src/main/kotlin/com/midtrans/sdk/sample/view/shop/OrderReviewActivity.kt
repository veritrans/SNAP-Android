package com.midtrans.sdk.sample.view.shop

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.os.LocaleListCompat
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.sample.model.Product
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import com.midtrans.sdk.uikit.api.model.*
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.AssetFontLoader
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import com.midtrans.sdk.uikit.internal.view.SnapButton
import java.util.*


class OrderReviewActivity : ComponentActivity() {

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result?.resultCode == RESULT_OK) {
            result.data?.let {
                val transactionResult = it.getParcelableExtra<TransactionResult>(
                    UiKitConstants.KEY_TRANSACTION_RESULT
                )
                Toast.makeText(this@OrderReviewActivity, "Coba trxid ${transactionResult?.transactionId.orEmpty()}", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            val transactionResult = data?.getParcelableExtra<TransactionResult>(
                UiKitConstants.KEY_TRANSACTION_RESULT)
            Toast.makeText(this@OrderReviewActivity, "Transaction ${transactionResult?.transactionId.orEmpty()} status ${transactionResult?.status.orEmpty()}", Toast.LENGTH_LONG).show()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private val product: Product by lazy {
        intent.getParcelableExtra(EXTRA_PRODUCT) as? Product
            ?: throw RuntimeException("Order ID must not be empty")
    }

    private val uiKitApi: UiKitApi by lazy {
        UiKitApi.getDefaultInstance()
    }

    private fun setLocaleNew(languageCode: String?) {
        val locales = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(locales)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLocaleNew("id") // commented for now. conflict with buildLegacyUiKit

        buildUiKit()

        setContent { Greeting() }
    }

    @Preview
    @Composable
    fun Greeting() {
        var text by remember { mutableStateOf("") }
        val state = rememberScrollState()
        ShowChargeContent(text = text, state = state, onTextFieldValueChange = { text = it })
    }

    @Composable
    fun ShowChargeContent(
        text: String,
        state: ScrollState,
        onTextFieldValueChange: (String) -> Unit
    ) {

        Column(
            modifier = Modifier.verticalScroll(state)
        ) {

            Text(text = product.name)

            SnapButton(
                enabled = true,
                text = "To Payment Options",
                style = SnapButton.Style.TERTIARY
            ) {
//                payWithOldSnapLegacyApi()
                payWithAndroidxActivityResultLauncher()
            }

            SnapButton(
                enabled = true,
                text = "To WebView",
                style = SnapButton.Style.PRIMARY
            ) {
            }
        }
    }

    private fun buildUiKit(){
        UiKitApi.Builder()
            .withContext(this.applicationContext)
            .withMerchantUrl("https://fiesta-point-sample.herokuapp.com/")
            .withMerchantClientKey("SB-Mid-client-hOWJXiCCDRvT0RGr")
            .withFontFamily(AssetFontLoader.fontFamily("fonts/SourceSansPro-Regular.ttf", this))
            .withCustomColors(
                CustomColors(
                    interactiveFillInverse = 0xff0000,
                    textInverse = 0x00ff00,
                    supportNeutralFill = 0xffdddd
                )
            )
            .build()
    }

    private fun buildLegacyUiKit(){
        SdkUIFlowBuilder.init()
            ?.setContext(this.applicationContext)
            ?.setMerchantBaseUrl("https://fiesta-point-sample.herokuapp.com/")
            ?.setClientKey("SB-Mid-client-hOWJXiCCDRvT0RGr")
//            .setExternalScanner { _, _ -> TODO("Not yet implemented") }
            ?.enableLog(true)
            ?.setDefaultText("fonts/SourceSansPro-Regular.ttf")
            ?.setBoldText("fonts/SourceSansPro-Bold.ttf")
            ?.setSemiBoldText("fonts/SourceSansPro-Semibold.ttf")
            ?.setColorTheme(CustomColorTheme("#0e4e95","#0b3b70", "#3e71aa"))
            ?.setLanguage("en") //setLanguage to either "en" for english or "id" for bahasa
            ?.buildSDK()
    }

    private fun payWithLegacyActivityStartActivityForResult(){
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
                            terms = mapOf("bni" to listOf(3,6,9,12))
                )
            ),
            userId = "3A8788CE-B96F-449C-8180-B5901A08B50A",
            customerDetails = CustomerDetails(
                        firstName = "Aris",
                        lastName = "Bhaktis",
                        email = "arisbhaktis@email.com",
                phone = "087788778212"
            )
        )
    }

    private fun payWithAndroidxActivityResultLauncher() {
        uiKitApi.startPaymentWithAndroidX(
            activity = this@OrderReviewActivity,
            launcher = launcher,
            transactionDetails = SnapTransactionDetail(
                orderId = UUID.randomUUID().toString(),
                grossAmount = 15005.00
            ),
            creditCard = CreditCard(
                saveCard = true,
                secure = true
            ),
            userId = "3A8788CE-B96F-449C-8180-B5901A08B50A",
            customerDetails = CustomerDetails(
                firstName = "Ari",
                lastName = "Bhakti",
                email = "aribhakti@email.com",
                phone = "087788778212"
            )
        )
    }

    private fun payWithOldSnapLegacyApi() {
        val transactionRequest = TransactionRequest(
            UUID.randomUUID().toString(),
            3000.0
        )
        transactionRequest.customerDetails = com.midtrans.sdk.corekit.models.CustomerDetails(
            "Ari", "Bhakti", "aribhakti@email.com", "087788778212"
        )
        transactionRequest.creditCard = com.midtrans.sdk.corekit.models.snap.CreditCard(
            true,
            null,
            true,
            null,
            null,
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
        MidtransSDK.getInstance().setTransactionRequest(transactionRequest)
        MidtransSDK.getInstance().startPaymentUiFlow(this.applicationContext)
    }

    companion object {
        private const val EXTRA_PRODUCT = "orderReview.extra.product"

        fun getOrderReviewActivityIntent(
            activityContext: Context,
            product: Product
        ): Intent {
            return Intent(activityContext, OrderReviewActivity::class.java).apply {
                putExtra(EXTRA_PRODUCT, product)
            }
        }
    }
}