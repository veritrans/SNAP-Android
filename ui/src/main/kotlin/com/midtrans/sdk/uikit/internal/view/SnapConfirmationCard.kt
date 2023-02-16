package com.midtrans.sdk.uikit.internal.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.midtrans.sdk.uikit.R
import com.midtrans.sdk.uikit.internal.view.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ConfirmationCard(
    onConfirmClicked: () -> Unit = {},
    onCancelClicked: () -> Unit = {},
    onSheetStateChange: (ModalBottomSheetState) -> Unit
): DialogToggle {

    var errorSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Expanded,
        confirmStateChange = { false }
    )
    return SnapBottomSheet(
        sheetState = errorSheetState,
        onSheetStateChange = {
            onSheetStateChange(it)
        }
    ) {
        ConfirmationContent(onConfirmClicked = onConfirmClicked, onCancelClicked = onCancelClicked)
    }
}

@Composable
private fun ConfirmationContent(
    onConfirmClicked: () -> Unit = {},
    onCancelClicked: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .background(SnapColors.getARGBColor(SnapColors.backgroundFillPrimary))
            .padding(16.dp)
    ) {
        Text(
            stringResource(id = R.string.remove_card_title),
            style = SnapTypography.STYLES.snapTextLabelMedium
        )
        Text(
            text = stringResource(id = R.string.remove_card_desc),
            style = SnapTypography.STYLES.snaTextBodySmall,
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            SnapButton(
                text = stringResource(id = R.string.remove_card_cta_1),
                modifier = Modifier.weight(1f),
                style = SnapButton.Style.TERTIARY,
                onClick = onCancelClicked
            )
            SnapButton(
                text = stringResource(id = R.string.remove_card_cta_2),
                modifier = Modifier.weight(1f),
                style = SnapButton.Style.NEGATIVE,
                onClick = onConfirmClicked
            )
        }
    }
}

@Composable
@Preview
private fun forPreview() {
    ConfirmationContent()
}
