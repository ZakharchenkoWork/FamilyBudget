package com.faigenbloom.familybudget.ui.budget


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faigenbloom.familybudget.R
import com.faigenbloom.familybudget.common.StripeBar
import com.faigenbloom.familybudget.common.TopBar
import com.faigenbloom.familybudget.common.ui.AnimateTabs
import com.faigenbloom.familybudget.domain.statistics.FilterType
import com.faigenbloom.familybudget.ui.theme.FamillySpandingsTheme
import java.util.Currency
import java.util.Locale

@Composable
fun AlternativeBudgetPage(
    state: BudgetState,
) {
    Column {
        TopBar(
            title = stringResource(id = R.string.budget_title),
        )
        StripeBar(
            textId = R.string.budget_personal,
            secondTabTextId = R.string.budget_family,
            isLeftSelected = state.isMyBudgetOpened,
            onSelectionChanged = state.onPageChanged,
        )
        AnimateTabs(isLeftTab = state.isMyBudgetOpened) { isMyBudgetOpened ->
            if (isMyBudgetOpened) {
                Screen(
                    state = state,
                )
            } else {
                Screen(
                    state = state,
                )
            }
        }

    }
}

@Composable
private fun Screen(
    state: BudgetState,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        items(state.budgetLines) {
            DataPlate(
                modifier = Modifier.padding(16.dp),
                title = getPlateTitle(it),
                amount = it.amount,
                currencyCode = state.currency.currencyCode,
            )
        }
    }
}

@Composable
private fun getPlateTitle(budgetLineUiData: BudgetLineUiData): String {
    return if (BudgetLabels.contains(budgetLineUiData.id)) {
        stringResource(id = BudgetLabels.valueOf(budgetLineUiData.id).nameId)
    } else {
        budgetLineUiData.name
    }
}

@Composable
private fun DataPlate(
    modifier: Modifier = Modifier,
    title: String,
    amount: String,
    currencyCode: String,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(
                color = colorScheme.primary,
                shape = shapes.medium,
            ),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                modifier = Modifier
                    .weight(0.33f),
                text = title,
                maxLines = 2,
                textAlign = TextAlign.Center,
                style = typography.bodySmall,
                color = colorScheme.onBackground,
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.33f),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = amount,
                    textAlign = TextAlign.Center,
                    style = typography.titleLarge,
                    color = colorScheme.onPrimary,
                )
            }

            Text(
                modifier = Modifier
                    .weight(0.33f),
                text = currencyCode,
                textAlign = TextAlign.Center,
                style = typography.titleLarge,
                color = colorScheme.onPrimary,
            )
        }
    }
}

@Preview
@Composable
fun AlternativeBudgetPagePreview() {
    FamillySpandingsTheme {
        Surface {
            AlternativeBudgetPage(
                state = BudgetState(
                    budgetLines = mockBudgetLines,
                    currentBalance = "4755",
                    totalBalance = "3242",
                    plannedBudget = "20000",
                    spent = "15245",
                    plannedSpendings = "3100",
                    additionalAmount = "44044",
                    currency = Currency.getInstance(Locale.getDefault()),
                    isBalanceError = false,
                    isMyBudgetOpened = true,
                    isSaveVisible = false,
                    onAdditionalAmountValueChanged = {},
                    onIncomeAddClicked = {},
                    onPageChanged = {},
                    onTotalBalanceChanged = {},
                    onPlannedBudgetChanged = {},
                    monthlyClicked = {},
                    yearlyClicked = {},
                    onSave = {},
                    filter = FilterType.Yearly(),
                ),
            )
        }
    }
}
