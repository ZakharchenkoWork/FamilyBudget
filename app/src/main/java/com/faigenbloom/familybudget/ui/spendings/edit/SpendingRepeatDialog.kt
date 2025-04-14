package com.faigenbloom.familybudget.ui.spendings.edit

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.faigenbloom.familybudget.R
import com.faigenbloom.familybudget.datasources.db.entities.RepeatOptions
import com.faigenbloom.familybudget.ui.spendings.RepeatOptionsUi
import com.faigenbloom.familybudget.ui.spendings.detail.OK_BUTTON
import com.faigenbloom.familybudget.ui.theme.FamillySpandingsTheme
import com.google.accompanist.permissions.isGranted

@Composable
fun SpendingRepeatDialog(
    chosenOptions: RepeatOptionsUi,
    onDismiss: () -> Unit,
    onChoose: (RepeatOptionsUi) -> Unit,
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
            Item(
                R.string.spendings_filter_never,
                isSelected = selected == RepeatOptionsUi.NONE,
                onSelect = { selected = RepeatOptionsUi.NONE },
            )
            Item(
                R.string.spendings_filter_daily,
                isSelected = selected == RepeatOptionsUi.DAILY,
                onSelect = { selected = RepeatOptionsUi.DAILY },
            )
            Item(
                R.string.spendings_filter_weekly,
                isSelected = selected == RepeatOptionsUi.WEEKLY,
                onSelect = { selected = RepeatOptionsUi.WEEKLY },
            )
            Item(
                R.string.spendings_filter_monthly,
                isSelected = selected == RepeatOptionsUi.MONTHLY,
                onSelect = { selected = RepeatOptionsUi.MONTHLY },
            )
            Item(
                R.string.spendings_filter_yearly,
                isSelected = selected == RepeatOptionsUi.YEARLY,
                onSelect = { selected = RepeatOptionsUi.YEARLY },
            )
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
                        .semantics { contentDescription = OK_BUTTON }
                        .clickable { onChoose(selected) },
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
    onSelect: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onSelect),
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
                .padding(horizontal = 16.dp, vertical = 16.dp),
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
            chosenOptions = RepeatOptionsUi.DAILY,
            onDismiss = {},
            onChoose = {}
        )
    }

}
