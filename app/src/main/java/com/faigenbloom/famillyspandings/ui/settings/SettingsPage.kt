package com.faigenbloom.famillyspandings.ui.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.faigenbloom.famillyspandings.R
import com.faigenbloom.famillyspandings.common.BaseTextField
import com.faigenbloom.famillyspandings.common.StripeBar
import com.faigenbloom.famillyspandings.common.TopBar
import com.faigenbloom.famillyspandings.ui.theme.FamillySpandingsTheme
import com.faigenbloom.famillyspandings.ui.theme.hint
import java.util.Currency
import java.util.Locale

@Composable
fun SettingsPage(
    state: SettingsState,
    onFamilyPageClicked: () -> Unit,
) {
    Column {
        TopBar()
        StripeBar(
            textId = R.string.settings_title,
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier.size(100.dp),
                painter = painterResource(id = R.drawable.icon_photo),
                contentDescription = "",
            )
            Column(
                verticalArrangement = Arrangement.Center,
            ) {
                BaseTextField(
                    labelId = R.string.name,
                    text = state.name,
                    onTextChange = state.onNameChanged,
                )
                BaseTextField(
                    labelId = R.string.surname,
                    text = state.surname,
                    onTextChange = state.onSurnameChanged,
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(color = MaterialTheme.colorScheme.primary),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier
                    .padding(
                        vertical = 8.dp,
                        horizontal = 16.dp,
                    ),
                text = stringResource(R.string.settings_personal),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleMedium,
            )
        }
        Box {
            Column(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .fillMaxWidth()
                    .clickable { state.onCurrenciesDropdownVisibilityChanged(true) },
            ) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    text = stringResource(R.string.settings_currency),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    text = "(" + state.chosenCurrency.symbol + ") " +
                        state.chosenCurrency.getDisplayName(Locale.getDefault()),
                    color = MaterialTheme.colorScheme.hint(),
                    style = MaterialTheme.typography.titleSmall,
                )
            }
            CurrencyChooseDropDown(state = state)
        }
        Row(
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier
                    .weight(1f),
                text = stringResource(R.string.settings_notifications),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
            )
            Switch(
                checked = state.isNotificationsEnabled,
                onCheckedChange = state.onNotificationsCheckChanged,
            )
        }
        Row(
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier
                    .weight(1f),
                text = stringResource(R.string.settings_password_protection),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
            )
            Switch(
                checked = state.isNotificationsEnabled,
                onCheckedChange = state.onNotificationsCheckChanged,
            )
        }
        Column(
            modifier = Modifier
                .padding(top = 32.dp)
                .fillMaxWidth()
                .clickable {
                    onFamilyPageClicked()
                },
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                text = stringResource(R.string.settings_family),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Composable
fun CurrencyChooseDropDown(state: SettingsState) {
    DropdownMenu(
        expanded = state.isCurrenciesDropdownVisible,
        onDismissRequest = { state.onCurrenciesDropdownVisibilityChanged(false) },
        offset = DpOffset(16.dp, 0.dp),
        content = {
            Box(modifier = Modifier.size(width = 300.dp, height = 300.dp)) {
                LazyColumn {
                    items(state.currenciesList.size) { index ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "(" + state.currenciesList[index].symbol + ") " +
                                        state.currenciesList[index].getDisplayName(Locale.getDefault()),
                                )
                            },
                            onClick = {
                                state.onCurrencyChanged(state.currenciesList[index])
                            },
                        )
                    }
                }
            }
        },
    )
}

@Preview
@Composable
fun StatisticsPagePreview() {
    FamillySpandingsTheme {
        Surface {
            SettingsPage(
                state = SettingsState(
                    name = "Natalia",
                    surname = "Zakharchenko",
                    onNameChanged = {},
                    onSurnameChanged = {},
                    currenciesList = Currency.getAvailableCurrencies().toList(),
                    isNotificationsEnabled = true,
                    onNotificationsCheckChanged = { },
                    isCurrenciesDropdownVisible = true,
                    onCurrenciesDropdownVisibilityChanged = {},
                    chosenCurrency = Currency.getInstance(Locale.getDefault()),
                    onCurrencyChanged = { },
                ),
                onFamilyPageClicked = {},
            )
        }
    }
}
