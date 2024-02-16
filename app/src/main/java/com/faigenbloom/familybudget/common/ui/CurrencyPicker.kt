package com.faigenbloom.familybudget.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.chargemap.compose.numberpicker.ListItemPicker
import com.faigenbloom.familybudget.R
import com.faigenbloom.familybudget.ui.theme.FamillySpandingsTheme
import java.util.Currency
import java.util.Locale


@Composable
fun CurrencyPicker(
    currencies: List<Currency>,
    chosenCurrency: Currency,
    onCurrencyPicked: (Currency) -> Unit,
) {
    var currencyState by rememberSaveable {
        mutableStateOf(chosenCurrency)
    }
    Dialog(
        onDismissRequest = { onCurrencyPicked(chosenCurrency) },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(color = MaterialTheme.colorScheme.surface),
        ) {
            Text(
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        top = 16.dp,
                    ),
                text = stringResource(R.string.currency_dialog_title),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge,
            )
            ListItemPicker(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp),
                value = currencyState,
                onValueChange = { currencyState = it },
                list = currencies,
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onClick = { onCurrencyPicked(currencyState) },
            ) {
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        modifier = Modifier,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleSmall,
                        text = stringResource(R.string.button_ok),
                        color = MaterialTheme.colorScheme.onSecondary,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CurrencyChooserPreview() {
    FamillySpandingsTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface,
        ) {
            CurrencyPicker(
                currencies = ArrayList(Currency.getAvailableCurrencies()),
                chosenCurrency = Currency.getInstance(Locale.getDefault()),
                onCurrencyPicked = { },
            )
        }
    }
}
