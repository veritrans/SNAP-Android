package com.midtrans.sdk.sample

import android.app.Activity
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.os.LocaleListCompat
import com.midtrans.sdk.uikit.api.callback.Callback
import com.midtrans.sdk.uikit.api.exception.SnapError
import com.midtrans.sdk.uikit.api.model.*
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.AssetFontLoader
import com.midtrans.sdk.uikit.internal.view.SnapButton
import java.util.*


class SampleActivity : AppCompatActivity() {

    private val uiKitApi: UiKitApi by lazy {
        UiKitApi.getDefaultInstance()
    }

    private fun setLocaleNew(languageCode: String?) {
        val locales = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(locales)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLocaleNew("id")
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

            SnapButton(
                enabled = true,
                text = "To Payment Options",
                style = SnapButton.Style.TERTIARY
            ) {

                uiKitApi.startPayment(
                    activityContext = this@SampleActivity,
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
                    ),
                    uobEzpayCallback = PaymentCallback(callbackUrl = "demo://snap"),
                    paymentCallback = object : Callback<TransactionResult> {
                        override fun onSuccess(result: TransactionResult) {
                            Toast.makeText(
                                this@SampleActivity,
                                "Transaction Pending. ID: " + result.transactionId,
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        override fun onError(error: SnapError) {
                            Toast.makeText(
                                this@SampleActivity,
                                "Error: " + error.javaClass.name,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                )
                startActivity(
                    intent
                )
            }

            SnapButton(
                enabled = true,
                text = "To WebView",
                style = SnapButton.Style.PRIMARY
            ) {
            }
        }
    }
}