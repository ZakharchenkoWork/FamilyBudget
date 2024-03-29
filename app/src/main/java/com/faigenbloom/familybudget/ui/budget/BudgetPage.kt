package com.faigenbloom.familybudget.ui.budget


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.faigenbloom.familybudget.R
import com.faigenbloom.familybudget.common.BaseTextField
import com.faigenbloom.familybudget.common.StripeBar
import com.faigenbloom.familybudget.common.TextFieldType
import com.faigenbloom.familybudget.common.TopBar
import com.faigenbloom.familybudget.common.isMoneyBlank
import com.faigenbloom.familybudget.common.toReadableDate
import com.faigenbloom.familybudget.common.ui.AnimateTabs
import com.faigenbloom.familybudget.common.ui.DateSwitcherBar
import com.faigenbloom.familybudget.common.ui.Loading
import com.faigenbloom.familybudget.domain.statistics.FilterType
import com.faigenbloom.familybudget.ui.spendings.detail.OK_BUTTON
import com.faigenbloom.familybudget.ui.theme.FamillySpandingsTheme
import java.util.Currency
import java.util.Locale

@Composable
fun BudgetPage(
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
        DateSwitcherBar(
            title = stringResource(
                id = R.string.statistics_small_title_range,
                state.filterType.from.toReadableDate(),
                state.filterType.to.toReadableDate(),
            ),
            onDateMoved = state.onDateMoved,
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
    if (state.budgetChangeState.isShown) {
        BudgetLineChangeDialog(state.budgetChangeState)
    }
    val isLoading by remember { state.isLoading }
    Loading(isShown = isLoading)
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
                title = getPlateTitle(it),
                amount = it.amount,
                currencyCode = state.currency.currencyCode,
                canEdit = it.repeatableId != BudgetLabels.BALANCE.name,
                onEditClicked = {
                    if (it.repeatableId == BudgetLabels.SPENT.name ||
                        it.repeatableId == BudgetLabels.PLANNED_SPENDINGS.name
                    ) {
                        state.onToSpendings()
                    } else {
                        state.onEditClicked(it)
                    }
                },
            )
        }
        item {
            NewDataPlate(
                onEditClicked = { state.onEditClicked(null) },
            )
        }
    }
}


@Composable
private fun DataPlate(
    title: String,
    amount: String,
    currencyCode: String,
    canEdit: Boolean,
    onEditClicked: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .aspectRatio(1f)
            .clickable {
                if (canEdit) {
                    onEditClicked()
                }
            }
            .background(
                color = colorScheme.primary,
                shape = shapes.medium,
            ),
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {

            Text(
                modifier = Modifier
                    .weight(0.4f),
                text = title,
                maxLines = 2,
                textAlign = TextAlign.Center,
                style = typography.titleSmall,
                color = colorScheme.onBackground,
            )
            if (canEdit.not() || amount.isMoneyBlank().not()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(0.3f),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = amount,
                        textAlign = TextAlign.Center,
                        style = typography.titleMedium,
                        maxLines = 1,
                        color = colorScheme.onPrimary,
                    )
                }
                Text(
                    modifier = Modifier
                        .weight(0.3f),
                    text = currencyCode,
                    textAlign = TextAlign.Center,
                    style = typography.titleMedium,
                    color = colorScheme.onPrimary,
                )
            } else if (canEdit && amount.isMoneyBlank()) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(0.6f),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .aspectRatio(1f),
                        painter = painterResource(id = R.drawable.icon_plus),
                        contentDescription = "",
                    )
                }

            }
        }
        if (canEdit && amount.isMoneyBlank().not()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.End,
            ) {
                Spacer(
                    modifier = Modifier.weight(0.6f),
                )
                Image(
                    modifier = Modifier
                        .weight(0.4f)
                        .padding(16.dp)
                        .aspectRatio(1f),
                    painter = painterResource(id = R.drawable.icon_edit),
                    contentDescription = "",
                )
            }
        }
    }
}

@Composable
private fun NewDataPlate(
    onEditClicked: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .aspectRatio(1f)
            .clickable {
                onEditClicked()
            }
            .background(
                color = colorScheme.onSecondaryContainer,
                shape = shapes.medium,
            ),
        contentAlignment = Alignment.BottomEnd,
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            painter = painterResource(id = R.drawable.icon_plus),
            contentDescription = "",
        )
    }
}

@Composable
fun BudgetLineChangeDialog(state: BudgetLineChangeDialogState) {
    Dialog(
        onDismissRequest = { state.onCloseDialogClicked() },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(
                    color = colorScheme.background,
                ),
        ) {
            if (state.budgetLine.isDefault) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    text = getPlateTitle(state.budgetLine),
                    color = colorScheme.onBackground,
                )
            } else {
                BaseTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    text = state.budgetLine.name,
                    labelId = R.string.name,
                    onTextChange = state.onBudgetLineNameChanged,
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BaseTextField(
                    modifier = Modifier
                        .fillMaxWidth(0.8f),
                    text = state.budgetLine.amount,
                    textFieldType = TextFieldType.Money(state.currency),
                    labelId = R.string.spending_details_amount,
                    onTextChange = state.onAmountValueChanged,
                )
                Image(
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            state.onOperationChanged()
                        },
                    painter = painterResource(
                        id = when (state.operation) {
                            Operation.NONE -> R.drawable.icon_calculator
                            Operation.Addition -> R.drawable.icon_plus
                            Operation.Subtraction -> R.drawable.icon_minus
                            Operation.Multiplication -> R.drawable.icon_multiply
                            Operation.Division -> R.drawable.icon_divide
                        },
                    ),
                    contentDescription = "",
                )
            }

            if (state.operation != Operation.NONE) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    BaseTextField(
                        modifier = Modifier
                            .fillMaxWidth(0.8f),
                        text = state.additionalAmount,
                        labelId = R.string.budget_income,
                        onTextChange = state.onAdditionalAmountValueChanged,
                    )
                    Image(
                        modifier = Modifier
                            .size(32.dp)
                            .clickable {
                                state.onOperationApplyClicked()
                            },
                        painter = painterResource(R.drawable.icon_ok),
                        contentDescription = "",
                    )
                }
            }
            BottomButtons(
                onDismiss = state.onCloseDialogClicked,
                onOkClicked = state.onOkDialogClicked,
            )
        }
    }
}

@Composable
private fun BottomButtons(
    onDismiss: () -> Unit,
    onOkClicked: () -> Unit,
) {
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
                .clickable { onOkClicked() },
            text = stringResource(id = R.string.button_ok),
            color = colorScheme.onBackground,
        )
    }
}

@Composable
private fun getPlateTitle(budgetLineUiData: BudgetLineUiData): String {
    return if (BudgetLabels.contains(budgetLineUiData.repeatableId)) {
        stringResource(id = BudgetLabels.valueOf(budgetLineUiData.repeatableId).nameId)
    } else {
        budgetLineUiData.name
    }
}

@Preview
@Composable
fun AlternativeBudgetPagePreview() {
    FamillySpandingsTheme {
        Surface {
            BudgetPage(
                state = BudgetState(
                    budgetLines = mockBudgetLines,
                    currency = Currency.getInstance(Locale.getDefault()),
                    isBalanceError = false,
                    isMyBudgetOpened = true,
                    isSaveVisible = false,
                    filterType = FilterType.Yearly(),
                    onPageChanged = {},
                    monthlyClicked = {},
                    yearlyClicked = {},
                    onSave = {},
                    onEditClicked = {},
                    onDateMoved = {},
                    onToSpendings = {},
                    budgetChangeState = BudgetLineChangeDialogState(
                        onAmountValueChanged = {},
                        onAdditionalAmountValueChanged = {},
                        additionalAmount = "",
                        budgetLine = mockBudgetLines[0],
                        isShown = true,
                        onCloseDialogClicked = {},
                        onBudgetLineNameChanged = {},
                        onOkDialogClicked = {},
                        operation = Operation.NONE,
                        onOperationChanged = {},
                        onOperationApplyClicked = {},
                    ),
                ),
            )
        }
    }
}


@Preview
@Composable
fun BudgetPagePreview() {
    FamillySpandingsTheme {
        Surface {
            BudgetPage(
                state = BudgetState(
                    budgetLines = mockBudgetLines,
                    isBalanceError = false,
                    isMyBudgetOpened = true,
                    isSaveVisible = false,
                    filterType = FilterType.Yearly(),
                    onPageChanged = {},
                    monthlyClicked = {},
                    yearlyClicked = {},
                    onSave = {},
                    onEditClicked = {},
                    onDateMoved = {},
                    onToSpendings = {},
                    budgetChangeState = BudgetLineChangeDialogState(
                        onAmountValueChanged = {},
                        onAdditionalAmountValueChanged = {},
                        additionalAmount = "",
                        budgetLine = mockBudgetLines[0],
                        isShown = false,
                        onCloseDialogClicked = {},
                        onBudgetLineNameChanged = {},
                        onOkDialogClicked = {},
                        operation = Operation.NONE,
                        onOperationChanged = {},
                        onOperationApplyClicked = {},

                        ),

                    ),
            )
        }
    }
}
