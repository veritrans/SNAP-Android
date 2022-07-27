package com.midtrans.sdk.sample

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.uikit.external.model.PaymentList
import com.midtrans.sdk.uikit.external.model.PaymentMethod
import com.midtrans.sdk.uikit.internal.presentation.paymentoption.PaymentOptionActivity.Companion.openPaymentOptionPage
import com.midtrans.sdk.uikit.internal.view.SnapButton

class SampleActivity : AppCompatActivity() {

    private val viewModel: SampleViewModel by lazy {
        ViewModelProvider(this).get(SampleViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SnapCore.Builder().withContext(this.applicationContext).build()
        viewModel.getHelloFromSnap()
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
            Text("insert snap token", style = TextStyle(color = Color.Red))
            TextField(
                value = text,
                onValueChange = onTextFieldValueChange,
                enabled = true,
                readOnly = false
            )

            Button(onClick = {
                viewModel.chargeUsingCreditCard(text)
            }) {
                Text(text = "Charge")
            }

            Button(onClick = {
                viewModel.chargeUsingCreditCardWithPromo(text)
            }) {
                Text(text = "Charge + promo")
            }

            Button(onClick = {
                viewModel.getCardTokenBasic()
            }) {
                Text(text = "normal card token")
            }

            Button(onClick = {
                viewModel.getTwoClickToken()
            }) {
                Text(text = "2click card token")
            }

            Button(onClick = {
                viewModel.getCardTokenNormalWithInstallment()
            }) {
                Text(text = "get ccToken + installment")
            }

            Button(onClick = {
                viewModel.getTwoClickTokenWithInstallment()
            }) {
                Text(text = "get 2lick ccToken + installment")
            }

            Button(onClick = {
                viewModel.chargeUsingCreditCardWithBniPoint(text)
            }) {
                Text(text = "charge + point")
            }

            Button(onClick = {
                viewModel.chargeUsingCreditCardWithBniPointAndPromo(text)
            }) {
                Text(text = "charge + point + promo")
            }

            Button(onClick = {
                viewModel.chargeUsingInstallment(text)
            }) {
                Text(text = "charge + installment")
            }

            Button(onClick = {
                viewModel.chargeUsingInstallmentAndPromo(text)
            }) {
                Text(text = "charge + installment + promo")
            }

            Button(onClick = {
                viewModel.chargeUsingOneClickCard(text)
            }) {
                Text(text = "charge with One Click")
            }

            Button(onClick = {
                viewModel.chargeUsingOneClickCardWithPromo(text)
            }) {
                Text(text = "charge + One Click + Promo")
            }

            Button(onClick = {
                viewModel.chargeUsingOneClickCardWithPromoAndInstallment(text)
            }) {
                Text(text = "charge + One Click + Promo + Installment")
            }

            Button(onClick = {
                viewModel.deleteSavedCard()
            }) {
                Text(text = "Delete card token")
            }

            Button(onClick = {
                viewModel.getBinData(text)
            }) {
                Text(text = "get Bin data")
            }

            Button(onClick = {
                viewModel.getBankPoint(text)
            }) {
                Text(text = "get Bank Point data")
            }

            SnapButton(
                enabled = true,
                text = "To Sample UI",
                style = SnapButton.Style.PRIMARY
            ) {
//                startActivity(Intent(this@SampleActivity, SampleUiActivity::class.java))
                val intent = openPaymentOptionPage(
                    this@SampleActivity,
                    "Rp33.990",
                    "#00-11-22-33",
                    PaymentList(
                        listOf(
                            PaymentMethod(
                                type = "shopeepay",
                                status = "up"
                            ),
                            PaymentMethod(
                                type = "qris",
                                acquirer = "shopeepay",
                                status = "up"
                            ),
                            PaymentMethod(
                                type = "uob_ezpay",
                                mode = listOf(
                                    "uob_ezpay_web",
                                    "uob_ezpay_deeplink"
                                ),
                                status = "up"
                            ),
                            PaymentMethod(
                                type = "cimb_clicks",
                                status = "up"
                            ),
                            PaymentMethod(
                                type = "bca_klikbca",
                                status = "up"
                            ),
                            PaymentMethod(
                                type = "bri_epay",
                                status = "up"
                            ),
                            PaymentMethod(
                                type = "bca_klikpay",
                                status = "up"
                            ),
                            PaymentMethod(
                                type = "credit_card",
                                status = "up"
                            ),
                            PaymentMethod(
                                type = "bca_va",
                                category = "bank_transfer",
                                status = "up"
                            ),
                            PaymentMethod(
                                type = "bni_va",
                                category = "bank_transfer",
                                status = "up"
                            ),
                            PaymentMethod(
                                type = "echannel",
                                category = "bank_transfer",
                                status = "up"
                            )
                        )
                    ),
                    "Ari Bhakti",
                    "087788778212",
                    listOf("Jl. ABC", "Rumah DEF")
                )
                startActivity(
                    intent
                )
            }
        }
    }
}