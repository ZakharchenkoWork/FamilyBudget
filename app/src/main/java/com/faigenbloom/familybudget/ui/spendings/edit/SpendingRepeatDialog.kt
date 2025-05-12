package com.faigenbloom.familybudget.ui.spendings.edit

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.faigenbloom.familybudget.R
import com.faigenbloom.familybudget.ui.spendings.RepeatOptionsUi
import com.faigenbloom.familybudget.ui.spendings.RepeatableOptionDataUi
import com.faigenbloom.familybudget.ui.spendings.detail.OK_BUTTON
import com.faigenbloom.familybudget.ui.theme.FamillySpandingsTheme

@Composable
fun SpendingRepeatDialog(
    chosenOptions: RepeatableOptionDataUi,
    onDismiss: () -> Unit,
    onChoose: (RepeatableOptionDataUi) -> Unit,
    onCalendarRequested: ()->Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(color = colorScheme.background)
                .padding(16.dp),
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = stringResource(R.string.repeat_dialog_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )

            var selected by remember { mutableStateOf(chosenOptions) }
            var selectedType by remember { mutableStateOf(chosenOptions.repeatType) }
            var hasEndDate by remember { mutableStateOf(chosenOptions.endDate.isNotBlank()) }
            Item(
                R.string.spendings_filter_never,
                isSelected = selectedType == RepeatOptionsUi.NONE,
                onSelect = { selectedType = RepeatOptionsUi.NONE },
            )
            Item(
                R.string.spendings_filter_daily,
                semantics = SPENDING_DAILY_OPTION,
                isSelected = selectedType == RepeatOptionsUi.DAILY,
                onSelect = { selectedType = RepeatOptionsUi.DAILY },
            )
            Item(
                R.string.spendings_filter_weekly,
                isSelected = selectedType == RepeatOptionsUi.WEEKLY,
                onSelect = { selectedType = RepeatOptionsUi.WEEKLY },
            )
            Item(
                R.string.spendings_filter_monthly,
                isSelected = selectedType == RepeatOptionsUi.MONTHLY,
                onSelect = { selectedType = RepeatOptionsUi.MONTHLY },
            )
            Item(
                R.string.spendings_filter_yearly,
                isSelected = selectedType == RepeatOptionsUi.YEARLY,
                onSelect = { selectedType = RepeatOptionsUi.YEARLY },
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(color = colorScheme.background),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
            Text(
                modifier = Modifier
                    .clickable {
                        onDismiss()
                    },
                text = if (hasEndDate)stringResource(id = R.string.repeatable_end_date_till, chosenOptions.endDate) else stringResource(id = R.string.repeatable_end_date),
                color = colorScheme.onBackground,
            )
            Switch(hasEndDate, onCheckedChange = {
                hasEndDate = it
                if (hasEndDate) {
                    onCalendarRequested()
                }
            })
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .background(color = colorScheme.background),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
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
                        .semantics { contentDescription = SPENDING_REPEATABLE_OPTIONS_OK }
                        .clickable { onChoose(selected.copy(repeatType = selectedType)) },
                    text = stringResource(id = R.string.button_ok),
                    color = colorScheme.onBackground,
                )
            }
        }
    }
}

@Composable
fun Item(
    @StringRes id: Int,
    isSelected: Boolean,
    semantics: String = "",
    onSelect: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onSelect)
            .semantics{contentDescription = semantics},
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                colorScheme.primary
            } else {
                colorScheme.tertiaryContainer
            },
        ),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                modifier = Modifier
                    .weight(0.7f),
                text = stringResource(id),
                color = colorScheme.onBackground,
            )
        }
    }
}

@Preview
@Composable
fun SpendingRepeatDialogPreview() {
    FamillySpandingsTheme {
        SpendingRepeatDialog(
            chosenOptions = RepeatableOptionDataUi(repeatType = RepeatOptionsUi.DAILY),
            onDismiss = {},
            onChoose = {},
            onCalendarRequested = {}
        )
    }

}
