package com.midtrans.sdk.sample

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
            modifier = Modifier.verticalScroll(
                state = rememberScrollState()
            )
        ) {
            var shouldReveal by remember {
                ccvVisible
            }
//            SnapCCDetailListItem(shouldReveal)

            var expanding by remember {
              mutableStateOf(false)
            }
            OverlayExpandingBox(
                isExpanded = expanding,
                mainContent = {
                    SnapTotal {
                        expanding = !expanding
                    }
                },
                expandingContent = { Text(text = "fsadfjlsakjflkasjflkjaskldfjksalfjklasjdfklsadjfksajdfklasjdklfjsadlkfjaslkdfjlasdfj") },
                followingContent = {
                    SnapAppBar(
                        title = "App Bar",
                        iconResId = R.drawable.psdk_ic_gopay
                    ) {
                        Toast.makeText(
                            this@SampleUiActivity,
                            "Icon App Bar clicked!",
                            Toast.LENGTH_LONG
                        )
                            .show()
                        shouldReveal = !shouldReveal
                    }
                }
            )
            SnapAppBar(
                title = "App Bar",
                iconResId = R.drawable.psdk_ic_gopay
            ) {
                Toast.makeText(this@SampleUiActivity, "Icon App Bar clicked!", Toast.LENGTH_LONG)
                    .show()
                shouldReveal = !shouldReveal
            }

            var list = mutableListOf(
                SavedCreditCardFormData(
                    title = "satu",
                    inputTitle = "Masukkan CVV",
                    endIcon = android.R.drawable.ic_delete,
                    startIcon = R.drawable.ic_bri,
                    errorText = remember { mutableStateOf("") },
                    maskedCardNumber = "123***********345"
                ),
                SavedCreditCardFormData(
                    title = "dua",
                    inputTitle = "Masukkan CVV",
                    endIcon = android.R.drawable.ic_delete,
                    startIcon = R.drawable.ic_bri,
                    errorText = remember { mutableStateOf("") },
                    maskedCardNumber = "123***********345"
                ),
                SavedCreditCardFormData(
                    title = "tiga",
                    inputTitle = "Masukkan CVV",
                    endIcon = android.R.drawable.ic_delete,
                    startIcon = R.drawable.ic_bri,
                    errorText = remember { mutableStateOf("") },
                    maskedCardNumber = "123***********345"
                ),
                NewCardFormData(
                    title = "new",
                    isCardNumberInvalid = remember { mutableStateOf(false) },
                    bankIconId = remember { mutableStateOf(R.drawable.ic_bri) },
                    isCvvInvalid = remember { mutableStateOf(false) },
                    isExpiryDateInvalid = remember { mutableStateOf(false) },
                    principalIconId = remember { mutableStateOf(R.drawable.psdk_ic_gopay) }
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
                    R.drawable.ic_bri,
                    R.drawable.ic_bri,
                    R.drawable.ic_bri,
                    R.drawable.ic_bri
                )
            ) {
                Toast.makeText(
                    this@SampleUiActivity,
                    "Bank Transfer is C.L.I.C.K.E.D!!!",
                    Toast.LENGTH_LONG
                ).show()
            }


//            val words = listOf<String>(
//                "This is <b>bolt</b> <i>italic</i> <u>underline</u> Lorem ipsumgalksnfdlsan jklnlkjfnasd lkj nfaklsdjnf ljkasndf n lad lasldasdla halo halo bandung ibukkota priangan, kokwaowkeaowkeo awas pusing",
//                "This is <b>bolt</b> <i>italic</i> <u>underline</u> Lorem ipsumgalksnfdlsan jklnlkjfnasd lkj nfaklsdjnf ljkasndf n lad, ga tau mau nulis apa, bingung semuanya",
//                "This is <b>bolt</b> <i>italic</i> <u>underline</u> Lorem ipsumgalksnfdlsan jklnlkjfnasd lkj nfaklsdjnf ljkasndf n lad, halo hallo lagi, kamu lagi ngapain",
//                "This is <b>bolt</b> <i>italic</i> <u>underline</u> Lorem ipsumgalksnfdlsan jklnlkjfnasd lkj nfaklsdjnf ljkasndf n lad",
//                "This is <b>bolt</b> <i>italic</i> <u>underline</u> Lorem ipsumgalksnfdlsan jklnlkjfnasd lkj nfaklsdjnf ljkasndf n lad",
//                "This is <b>bolt</b> <i>italic</i> <u>underline</u> Lorem ipsumgalksnfdlsan jklnlkjfnasd lkj nfaklsdjnf ljkasndf n lad",
//            )
//            SnapNumberedList(list = words)


//            var text by remember { mutableStateOf("") }
//            Text("insert snap token", style = TextStyle(color = Color.Red))
//            TextField(value = text, onValueChange = {
//                text = it
//            }, enabled = true, readOnly = false)
//            SnapButton(
//                enabled = true,
//                text = "Primary Button",
//                style = SnapButton.Style.PRIMARY
//            ) {}
//
//            SnapButton(
//                enabled = true,
//                text = "Tertiary Button",
//                style = SnapButton.Style.TERTIARY
//            ) {}
//
//            SnapButton(
//                enabled = false,
//                text = "Primary Disabled Button",
//                style = SnapButton.Style.PRIMARY
//            ) {}
//
//            SnapButton(
//                enabled = false,
//                text = "Tertiary Disabled Button",
//                style = SnapButton.Style.TERTIARY
//            ) {}
//
//            SnapText(text = "This is <b>bolt</b> <i>italic</i> <u>underline</u>")
//            SnapText(
//                text = "<ol>" +
//                    "  <li>Coffee <b>asldjflasjdfalkdf</b> alsdf ladsfjlkasjdf jasdlf lasjdflk as alsdkjflalaksdf jalskdfj alskdjf ajsdf lalksd jfalkd sfjlaksd jflasdkjf aslkd fjlaksdj falkdsf jalkds jflaksdj falksdfj alksdfj aslkdfj alskdf</li>" +
//                    "  <li>Tea</li>" +
//                    "  <li>Milk</li>" +
//                    "</ol>"
//            )
        }
    }
}