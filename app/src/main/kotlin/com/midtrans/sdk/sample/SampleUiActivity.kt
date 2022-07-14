package com.midtrans.sdk.sample

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.midtrans.sdk.corekit.SnapCore
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.view.*

class SampleUiActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SnapCore.Builder().withContext(this.applicationContext).build()
        setContent { SampleUi() }
    }

    @Composable
    @Preview
    fun SampleUi() {


        val ccvVisible = remember {
            mutableStateOf(true)
        }

        Column(
            modifier = Modifier
                .verticalScroll(
                    state = rememberScrollState()
                )
                .padding(start = 16.dp, end = 16.dp)
        ) {
            AnimatedIcon(resId = R.drawable.ic_midtrans_animated).start()

            var shouldReveal by remember {
                ccvVisible
            }
//            SnapCCDetailListItem(shouldReveal)
            SnapSingleIconListItem(
                iconResId = R.drawable.ic_bank_mandiri_40,
                title = "Mandiri"
            )
            var expanding by remember {
                mutableStateOf(false)
            }
            OverlayExpandingBox(
                isExpanded = expanding,
                mainContent = {
                    SnapTotal(
                        amount = "Rp399.000",
                        orderId = "#121231231231",
                        remainingTime = "22:22:22"
                    ) {
                        expanding = it
                    }
                },
                expandingContent = {
                    SnapCustomerDetail(
                        name = "Ari Bhakti S",
                        phone = "081123405678",
                        addressLines = listOf(
                            "The House BLok 3 No.2",
                            "Jalan Raya 123",
                            "Jakarta Selatan 123450"
                        )
                    )
                },
                followingContent = {
                    Column() {

                        SnapAppBar(
                            title = "App Bar",
                            iconResId = R.drawable.ic_arrow_left
                        ) {
                            Toast.makeText(
                                this@SampleUiActivity,
                                "Icon App Bar clicked!",
                                Toast.LENGTH_LONG
                            )
                                .show()
                            shouldReveal = !shouldReveal
                        }
                        SnapAppBar(
                            title = "App Bar",
                            iconResId = R.drawable.ic_cross
                        ) {
                            Toast.makeText(
                                this@SampleUiActivity,
                                "Icon App Bar clicked!",
                                Toast.LENGTH_LONG
                            )
                                .show()
                            shouldReveal = !shouldReveal
                        }

                        SnapButton(
                            enabled = false,
                            text = "Tertiary Disabled Button",
                            style = SnapButton.Style.TERTIARY
                        ) {}
                    }
                }
            )


            var list = mutableListOf(
                SavedCreditCardFormData(
                    title = "satu",
                    inputTitle = "Masukkan CVV",
                    endIcon = R.drawable.ic_trash,
                    startIcon = R.drawable.ic_outline_bca_24,
                    errorText = remember { mutableStateOf("") },
                    maskedCardNumber = "123***********345"
                ),
                SavedCreditCardFormData(
                    title = "dua",
                    inputTitle = "Masukkan CVV",
                    endIcon = R.drawable.ic_trash,
                    startIcon = R.drawable.ic_outline_bni_24,
                    errorText = remember { mutableStateOf("") },
                    maskedCardNumber = "123***********345"
                ),
                SavedCreditCardFormData(
                    title = "tiga",
                    inputTitle = "Masukkan CVV",
                    endIcon = R.drawable.ic_trash,
                    startIcon = R.drawable.ic_outline_mandiri_24,
                    errorText = remember { mutableStateOf("") },
                    maskedCardNumber = "123***********345"
                ),
                NewCardFormData(
                    title = "new",
                    isCardNumberInvalid = remember { mutableStateOf(false) },
                    bankIconId = remember { mutableStateOf(R.drawable.ic_outline_bri_24) },
                    isCvvInvalid = remember { mutableStateOf(false) },
                    isExpiryDateInvalid = remember { mutableStateOf(false) },
                    principalIconId = remember { mutableStateOf(R.drawable.ic_outline_visa_24) }
                )

            ).toMutableStateList()

            SavedCardRadioGroup(states = list, { selected: String, value: String ->
                Log.e("wahyu", "cardNumber: $selected  cvv: $value")

                list.find { it.identifier == selected }.let { member ->
                    member?.apply {
                        if (this is SavedCreditCardFormData) errorText.value =
                            if (value.length >= 3) "jangan salah" else ""
                    }
                }

            }, onItemRemoveClicked = { title ->
                list.find { it.identifier == title }.let { member ->
                    list.remove(member)
                }
            },
                onCardNumberValueChange = {
                    Toast.makeText(this@SampleUiActivity, "HA${it.length}X", Toast.LENGTH_SHORT)
                        .show()
                },
                onCvvValueChange = {
                    Toast.makeText(this@SampleUiActivity, "HI${it.length}X", Toast.LENGTH_SHORT)
                        .show()
                },
                onExpiryDateValueChange = {
                    Toast.makeText(this@SampleUiActivity, "HE${it.length}X", Toast.LENGTH_SHORT)
                        .show()
                }
            )
            SnapNumberedListItem(
                number = "1.",
                paragraph = "This is <b>bolt</b> <i>italic</i> <u>underline</u> Lorem ipsumgalksnfdlsan jklnlkjfnasd lkj nfaklsdjnf ljkasndf n lad"
            )

            SnapMultiIconListItem(
                title = "Bank Transfer",
                iconList = listOf(
                    R.drawable.ic_outline_bca_40,
                    R.drawable.ic_outline_mandiri_40,
                    R.drawable.ic_outline_bni_40,
                    R.drawable.ic_outline_bri_40,
                    R.drawable.ic_outline_permata_40
                )
            ) {
                Toast.makeText(
                    this@SampleUiActivity,
                    "Bank Transfer is C.L.I.C.K.E.D!!!",
                    Toast.LENGTH_LONG
                ).show()
            }


            val words = listOf<String>(
                "This is <b>bolt</b> <i>italic</i> <u>underline</u> Lorem ipsumgalksnfdlsan jklnlkjfnasd lkj nfaklsdjnf ljkasndf n lad lasldasdla halo halo bandung ibukkota priangan, kokwaowkeaowkeo awas pusing",
                "This is <b>bolt</b> <i>italic</i> <u>underline</u> Lorem ipsumgalksnfdlsan jklnlkjfnasd lkj nfaklsdjnf ljkasndf n lad, ga tau mau nulis apa, bingung semuanya",
                "This is <b>bolt</b> <i>italic</i> <u>underline</u> Lorem ipsumgalksnfdlsan jklnlkjfnasd lkj nfaklsdjnf ljkasndf n lad, halo hallo lagi, kamu lagi ngapain",
                "This is <b>bolt</b> <i>italic</i> <u>underline</u> Lorem ipsumgalksnfdlsan jklnlkjfnasd lkj nfaklsdjnf ljkasndf n lad",
                "This is <b>bolt</b> <i>italic</i> <u>underline</u> Lorem ipsumgalksnfdlsan jklnlkjfnasd lkj nfaklsdjnf ljkasndf n lad",
                "This is <b>bolt</b> <i>italic</i> <u>underline</u> Lorem ipsumgalksnfdlsan jklnlkjfnasd lkj nfaklsdjnf ljkasndf n lad",
            )
            SnapNumberedList(list = words)


            var text by remember { mutableStateOf("") }
            Text("insert snap token", style = TextStyle(color = Color.Red))
            TextField(value = text, onValueChange = {
                text = it
            }, enabled = true, readOnly = false)
            SnapButton(
                enabled = true,
                text = "Primary Button",
                style = SnapButton.Style.PRIMARY
            ) {}

            SnapButton(
                enabled = true,
                text = "Tertiary Button",
                style = SnapButton.Style.TERTIARY
            ) {}

            SnapButton(
                enabled = false,
                text = "Primary Disabled Button",
                style = SnapButton.Style.PRIMARY
            ) {}


            SnapText(text = "This is <b>bolt</b> <i>italic</i> <u>underline</u>")
            SnapText(
                text = "<ol>" +
                        "  <li>Coffee <b>asldjflasjdfalkdf</b> alsdf ladsfjlkasjdf jasdlf lasjdflk as alsdkjflalaksdf jalskdfj alskdjf ajsdf lalksd jfalkd sfjlaksd jflasdkjf aslkd fjlaksdj falkdsf jalkds jflaksdj falksdfj alksdfj aslkdfj alskdf</li>" +
                        "  <li>Tea</li>" +
                        "  <li>Milk</li>" +
                        "</ol>"
            )
            val promoList = listOf(
                PromoData(
                    leftText = "Summer sale",
                    rightText = "-Rp100.000"
                ),
                PromoData(
                    leftText = "Surprise 8.8",
                    rightText = "-Rp88.000"
                ),
                PromoData(
                    leftText = "Example of a very long long long long long long promo name from VISA",
                    rightText = "               ",
                    subLeftText = "This promo is only available for VISA card",
                    enabled = remember {
                        mutableStateOf(false)
                    }

                ),
                PromoData(
                    leftText = "Lanjut tanpa promo",
                    rightText = " ",
                    enabled = remember {
                        mutableStateOf(true)
                    }
                )
            )
            SnapPromoListRadioButton(
                states = promoList,
                onItemSelectedListener = {
                    Toast.makeText(this@SampleUiActivity, it.leftText, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}