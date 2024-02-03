@file:OptIn(ExperimentalPermissionsApi::class)

package com.faigenbloom.familybudget.ui.spendings.detail

import android.Manifest
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faigenbloom.familybudget.R
import com.faigenbloom.familybudget.common.BaseTextField
import com.faigenbloom.familybudget.common.SimpleTextField
import com.faigenbloom.familybudget.common.TextFieldType
import com.faigenbloom.familybudget.common.ui.MoneyTextTransformation
import com.faigenbloom.familybudget.ui.camera.BarCodeCamera
import com.faigenbloom.familybudget.ui.theme.FamillySpandingsTheme
import com.faigenbloom.familybudget.ui.theme.transparent
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.util.Currency
import java.util.Locale

@Composable
fun DetailDialog(
    state: DetailDialogState,
    onDismiss: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .wrapContentSize()
            .background(
                color = colorScheme.background,
            ),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Title()

        DetailsList(state = state)
        ManualInputs(state = state)
        SuggestionsList(state = state)
        BottomButtons(
            state = state,
            onDismiss = onDismiss,
        )
    }
}

@Composable
private fun Title() {
    Text(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp),
        text = stringResource(
            id = R.string.detail_dialog_title,
        ),
        color = colorScheme.onBackground,
    )
}

@Composable
fun DetailsList(state: DetailDialogState) {
    Box(
        modifier = Modifier.height(300.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (state.spendingDetails.isEmpty()) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.detail_dialog_details_hint),
                textAlign = TextAlign.Center,
                color = colorScheme.onBackground,
                style = typography.titleLarge,
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                items(state.spendingDetails.size) { index ->
                    val spendingDetail = state.spendingDetails[index]
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clickable { state.onDetailClicked(index) },
                        colors = CardDefaults.cardColors(
                            containerColor = if (state.detailChosen == index) {
                                colorScheme.primaryContainer
                            } else {
                                colorScheme.primary
                            },
                        ),
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                modifier = Modifier
                                    .semantics {
                                        if (index == 0) {
                                            contentDescription = DETAIL_NAME_TITLE
                                        }
                                    }
                                    .weight(0.6f),
                                text = spendingDetail.name,
                                color = colorScheme.onBackground,
                            )
                            Text(
                                modifier = Modifier
                                    .weight(0.3f),
                                textAlign = TextAlign.End,
                                text = MoneyTextTransformation(state.currency.currencyCode)
                                    .filter(spendingDetail.amount),
                                color = colorScheme.onBackground,
                            )
                            Image(
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .aspectRatio(1f)
                                    .weight(0.1f)
                                    .clickable {
                                        state.onDeleteDetail(index)
                                    },
                                painter = painterResource(id = R.drawable.icon_delete),
                                contentDescription = "",
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ManualInputs(state: DetailDialogState) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BaseTextField(
                modifier = Modifier
                    .weight(0.45f)
                    .semantics { contentDescription = DETAIL_DIALOG_NAME_INPUT },
                text = state.name,
                labelId = R.string.spending_details_name,
                onTextChange = state.onNameChanged,
            )
            BaseTextField(
                modifier = Modifier
                    .weight(0.45f)
                    .semantics { contentDescription = DETAIL_DIALOG_AMOUNT_INPUT },
                text = state.amount,
                labelId = R.string.spending_details_amount,
                textFieldType = TextFieldType.Money(state.currency),
                onTextChange = state.onAmountChanged,
            )
            Image(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .aspectRatio(1f)
                    .weight(0.1f)
                    .clickable {
                        state.addDetailToList()
                    }
                    .semantics { contentDescription = DETAIL_DIALOG_ADD_TO_LIST },
                painter = painterResource(id = R.drawable.icon_ok),
                contentDescription = "",
            )
        }
        SimpleTextField(
            modifier = Modifier.padding(start = 16.dp),
            text = state.barcodeText,
            label = stringResource(id = R.string.spending_details_barcode),
            onValueChange = {},
        )
    }

}


@Composable
private fun SuggestionsList(
    state: DetailDialogState,
) {
    Box(
        modifier = Modifier.height(300.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (state.suggestions.isEmpty() && !state.isBarcodeScannerVisible) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.detail_dialog_suggestions_hint),
                textAlign = TextAlign.Center,
                color = colorScheme.onBackground,
                style = typography.titleLarge,
            )
        } else {
            if (state.isBarcodeScannerVisible) {
                BarCodeCamera(
                    modifier = Modifier.requiredHeight(300.dp),
                    onBarcodeCaptured = state.onBarCodeFound,
                )
            }
            LazyColumn(
                modifier = Modifier
                    .background(
                        color = colorScheme.transparent(),
                    )
                    .fillMaxSize(),
            ) {
                items(state.suggestions.size) { index ->
                    val spendingDetail = state.suggestions[index]
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clickable { state.onSuggestionClicked(index) },
                        colors = CardDefaults.cardColors(
                            containerColor = if (state.suggestionChosen == index) {
                                colorScheme.primary
                            } else {
                                colorScheme.tertiaryContainer
                            },
                        ),
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                modifier = Modifier
                                    .semantics {
                                        if (index == 0) {
                                            contentDescription = SUGGESTION_TITLE
                                        }
                                    }
                                    .weight(0.7f),
                                text = spendingDetail.name,
                                color = colorScheme.onBackground,
                            )
                            Text(
                                modifier = Modifier
                                    .weight(0.3f),
                                textAlign = TextAlign.End,
                                text = MoneyTextTransformation(state.currency.currencyCode)
                                    .filter(spendingDetail.amount),
                                color = colorScheme.onBackground,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BottomButtons(
    state: DetailDialogState,
    onDismiss: () -> Unit,
) {
    val cameraPermissionState = rememberPermissionState(
        Manifest.permission.CAMERA,
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(color = colorScheme.background),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier
                .padding(end = 16.dp)
                .size(32.dp)
                .clickable {
                    if (cameraPermissionState.status.isGranted) {
                        state.onBarCodeVisibilityChange()
                    } else {
                        cameraPermissionState.launchPermissionRequest()
                    }
                },
            painter =
            painterResource(id = R.drawable.icon_barcode),
            contentDescription = "",
        )
        Text(
            modifier = Modifier
                .padding(end = 16.dp)
                .clickable {
                    onDismiss()
                },
            text = stringResource(id = R.string.button_cancel),
            color = colorScheme.onBackground,
        )
        Text(
            modifier = Modifier
                .padding(end = 16.dp)
                .semantics { contentDescription = OK_BUTTON }
                .clickable { state.onOkClicked() },
            text = stringResource(id = R.string.button_ok),
            color = colorScheme.onBackground,
        )
    }
}

@Preview
@Composable
fun DetailDialogEmptyPreview() {
    FamillySpandingsTheme {
        DetailDialog(
            state = DetailDialogState(
                "",
                name = "",
                amount = "",
                barcodeText = "",
                spendingDetails = emptyList(),
                suggestions = emptyList(),
                suggestionChosen = 3,
                currency = Currency.getInstance(Locale.getDefault()),
                onNameChanged = { },
                onAmountChanged = { },
                onSuggestionClicked = { },
                addDetailToList = { },
                onOkClicked = { },
                onDeleteDetail = { },
                detailChosen = 0,
                onDetailClicked = { },
                onBarCodeFound = { },
                onBarCodeVisibilityChange = { },
                isBarcodeScannerVisible = false,
            ),
            onDismiss = { },
        )
    }
}

@Preview
@Composable
fun DetailDialogPreview() {
    FamillySpandingsTheme {
        DetailDialog(
            state = DetailDialogState(
                "",
                name = "ut",
                amount = "15.00",
                barcodeText = "938432424",
                spendingDetails = mockSuggestions,
                suggestions = mockSuggestions,
                suggestionChosen = 3,
                currency = Currency.getInstance(Locale.getDefault()),
                onNameChanged = { },
                onAmountChanged = { },
                onSuggestionClicked = { },
                addDetailToList = { },
                onOkClicked = { },
                onDeleteDetail = { },
                detailChosen = 0,
                onDetailClicked = { },
                onBarCodeFound = { },
                onBarCodeVisibilityChange = { },
                isBarcodeScannerVisible = false,
            ),
            onDismiss = { },
        )
    }
}
