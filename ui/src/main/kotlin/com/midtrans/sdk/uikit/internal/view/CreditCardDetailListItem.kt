package com.midtrans.sdk.uikit.internal.view

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.view.SnapColors.BACKGROUND_BORDER_SOLID_SECONDARY
import com.midtrans.sdk.uikit.internal.view.SnapColors.INTERACTIVE_BORDER_SUPPORT
import com.midtrans.sdk.uikit.internal.view.SnapColors.LINK_HOVER
import com.midtrans.sdk.uikit.internal.view.SnapColors.SUPPORT_DANGER_DEFAULT
import com.midtrans.sdk.uikit.internal.view.SnapColors.SUPPORT_NEUTRAL_FILL

object CreditCardDetailListItem {
}

@Composable
fun SnapCCDetailListItem(
    @DrawableRes startIconId: Int,
    @DrawableRes endIconId: Int,
    itemTitle: String,
    shouldReveal: Boolean,
    inputTitle: String,
    isInputError: Boolean,
    errorTitle: String,
    onValueChange: (String) -> Unit,
    onEndIconClicked: () -> Unit
) {
    Column {
        Row(
            verticalAlignment = CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                painter = painterResource(id = startIconId),
                contentDescription = null,
                tint = Color.Unspecified
            )
            Text(
                text = itemTitle,
                style = SnapTypography.STYLES.snapTextBig,
                modifier = Modifier
                    .weight(weight = 1.0f)
            )
            if (shouldReveal) {
                IconButton(onClick = { onEndIconClicked() }) {
                    Icon(
                        painter = painterResource(id = endIconId),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = shouldReveal,
            enter = expandVertically()
        ) {
            Column(
                modifier = Modifier
                    .background(
                        SnapColors.getARGBColor(SUPPORT_NEUTRAL_FILL),
                        RoundedCornerShape(4.dp)
                    )
                    .fillMaxWidth(1f)
                    .padding(start = 16.dp, bottom = 8.dp, top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                var text by remember { mutableStateOf("") }
                Text(
                    text = inputTitle,
                    style = SnapTypography.STYLES.snapTextSmall
                )
                BasicTextField(
                    value = text,
                    onValueChange = { value ->
                        if (value.length <= 3) {
                            text = value.filter { it.isDigit() }
                            onValueChange(value.filter { it.isDigit() })
                        }
                    },
                    modifier = Modifier
                        .border(
                            1.dp,
                            if (isInputError) {
                                SnapColors.getARGBColor(INTERACTIVE_BORDER_SUPPORT)
                            } else {
                                SnapColors.getARGBColor(LINK_HOVER)
                            },
                            RoundedCornerShape(4.dp)
                        )
                        .background(Color.White, RoundedCornerShape(4.dp))
                        .width(69.dp)
                        .height(39.dp)
                        .padding(top = 8.dp, bottom = 8.dp, start = 12.dp, end = 12.dp),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                if (isInputError)
                    Text(
                        text = errorTitle,
                        color = SnapColors.getARGBColor(SUPPORT_DANGER_DEFAULT),
                        style = SnapTypography.STYLES.snapTextSmall
                    )
            }
        }
    }
}

@Composable
fun InputNewCardItem() {
    val iconIdList = mutableListOf(R.drawable.ic_bri, R.drawable.ic_bri, R.drawable.ic_bri, R.drawable.ic_bri)
    Column() {
        Text(text = "Gunakan kartu lain")
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = "Nomor kartu",
                modifier = Modifier.weight(1f)
            )

            iconIdList.forEach {
                Icon(
                    painter = painterResource(id = it),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
            }
        }
    }
}

@Composable
fun CcRadioGroup(
    states: List<FormData>,
    onValueChange: (item: String, cvv: String) -> Unit,
    onItemRemoveClicked: (item: String) -> Unit
) {
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(states[0].identifier) }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        states.forEach { item ->
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.selectable(
                        selected = (item.identifier == selectedOption),
                        onClick = {
                            onOptionSelected(item.identifier)
                        },
                        role = Role.RadioButton
                    ),
                    verticalAlignment = CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    RadioButton(
                        selected = item.identifier == selectedOption,
                        onClick = null,
                        colors = RadioButtonDefaults.colors(selectedColor = Color.Black)
                    )
                    when(item) {
                        is SavedCreditCardFormData -> {
                            val errorText by item.errorText
                            SnapCCDetailListItem(
                                startIconId = item.startIcon,
                                endIconId = item.endIcon,
                                itemTitle = item.maskedCardNumber,
                                shouldReveal = item.identifier == selectedOption,
                                inputTitle = item.inputTitle,
                                isInputError = errorText.isNotBlank(),
                                errorTitle = errorText,
                                onValueChange = { onValueChange(selectedOption, it) },
                                onEndIconClicked = { onItemRemoveClicked(item.identifier) }
                            )
                        }
                    }
                }

                Divider(
                    thickness = 1.dp,
                    color = SnapColors.getARGBColor(BACKGROUND_BORDER_SOLID_SECONDARY)
                )
            }
        }
    }
}

data class SavedCreditCardFormData(
    val startIcon: Int,
    val endIcon: Int,
    val maskedCardNumber: String,
    var errorText: MutableState<String>,
    val inputTitle: String,
    var title: String
): FormData(title){

}


class NewCardFormData(
)

open class FormData(
    public val identifier: String
)

