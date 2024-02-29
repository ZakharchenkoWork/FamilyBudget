package com.faigenbloom.familybudget.ui.budget

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.familybudget.common.toLongMoney
import com.faigenbloom.familybudget.common.toReadableMoney
import com.faigenbloom.familybudget.domain.budget.CalculateMoneyUseCase
import com.faigenbloom.familybudget.domain.budget.GetBudgetLinesUseCase
import com.faigenbloom.familybudget.domain.budget.ReCalculateFormulasUseCase
import com.faigenbloom.familybudget.domain.budget.SaveBudgetLinesUseCase
import com.faigenbloom.familybudget.domain.currency.GetChosenCurrencyUseCase
import com.faigenbloom.familybudget.domain.statistics.FilterType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Currency
import java.util.Locale


class BudgetPageViewModel(
    private val saveBudgetUseCase: SaveBudgetLinesUseCase,
    private val getBudgetLinesUseCase: GetBudgetLinesUseCase,
    private val getChosenCurrencyUseCase: GetChosenCurrencyUseCase,
    private val reCalculateFormulasUseCase: ReCalculateFormulasUseCase,
    private val calculateMoneyUseCase: CalculateMoneyUseCase,
) : ViewModel() {

    var toSpendings: (from: Long, to: Long) -> Unit = { _, _ -> }

    private fun onNewClicked() {
        _stateFlow.update { state ->
            state.copy(
                budgetChangeState = state.budgetChangeState.copy(
                    budgetLine = BudgetLineUiData(
                        id = "",
                        repeatableId = "",
                        name = "",
                        amount = "",
                        isDefault = false,
                    ),
                    operation = Operation.NONE,
                    currency = state.currency,
                    additionalAmount = 0L.toReadableMoney(),
                    isShown = true,
                ),
            )
        }
    }

    private fun onEditClicked(clickedBudgetLine: BudgetLineUiData?) {
        clickedBudgetLine?.let {
            _stateFlow.update { state ->
                state.copy(
                    budgetChangeState = state.budgetChangeState.copy(
                        budgetLine = clickedBudgetLine.copy(),
                        operation = Operation.NONE,
                        additionalAmount = 0L.toReadableMoney(),
                        currency = state.currency,
                        isShown = true,
                    ),
                )
            }
        } ?: onNewClicked()
    }

    private fun onBudgetLineNameChanged(name: String) {
        _stateFlow.update { state ->
            state.copy(
                budgetChangeState = state.budgetChangeState.copy(
                    budgetLine = state.budgetChangeState
                        .budgetLine.copy(name = name),
                ),
            )
        }
    }

    private fun onAmountValueChanged(amount: String) {
        _stateFlow.update { state ->
            state.copy(
                budgetChangeState = state.budgetChangeState.copy(
                    budgetLine = state.budgetChangeState.budgetLine.copy(amount = amount),
                ),
            )
        }
    }

    private fun onAdditionalAmountValueChanged(amount: String) {
        _stateFlow.update { state ->
            state.copy(
                budgetChangeState = state.budgetChangeState.copy(
                    additionalAmount = amount,
                ),
            )
        }
    }

    private fun onOperationChanged() {
        val operation = state.budgetChangeState.operation.let {
            val nextIndex = it.ordinal + 1
            val operations = Operation.values()
            if (nextIndex < operations.size) {
                operations[nextIndex]
            } else {
                Operation.NONE
            }
        }

        _stateFlow.update { state ->
            state.copy(
                budgetChangeState = state.budgetChangeState.copy(
                    operation = operation,
                ),
            )
        }
    }

    private fun onOkDialogClicked() {
        viewModelScope.launch {
            val budgetLines = ArrayList(state.budgetLines).apply {
                val budgetLine = state.budgetChangeState.budgetLine
                if (budgetLine.id.isBlank()) {
                    add(budgetLine)
                } else {
                    replaceAll {
                        if (it.id == budgetLine.id) {
                            budgetLine
                        } else it
                    }
                }
            }

            _stateFlow.update { state ->
                state.copy(
                    budgetLines = reCalculateFormulasUseCase(budgetLines),
                    budgetChangeState = state.budgetChangeState.copy(
                        isShown = false,
                    ),
                    isSaveVisible = true,
                )
            }
        }
    }

    private fun onCloseDialogClicked() {
        _stateFlow.update { state ->
            state.copy(
                budgetChangeState = state.budgetChangeState.copy(
                    isShown = false,
                ),
            )
        }
    }

    private fun onOperationApplyClicked() {
        viewModelScope.launch {
            val dialogState = state.budgetChangeState
            val budgetLine = dialogState.budgetLine.copy(
                amount = calculateMoneyUseCase(
                    firstValue = dialogState.budgetLine.amount.toLongMoney(),
                    operation = dialogState.operation,
                    secondValue = dialogState.additionalAmount.toLongMoney(),
                ).toReadableMoney(),
            )
            _stateFlow.update { state ->
                state.copy(
                    budgetChangeState = state.budgetChangeState.copy(
                        budgetLine = budgetLine,
                        additionalAmount = 0L.toReadableMoney(),
                    ),
                )
            }
        }
    }

    private fun monthlyClicked() {
        _stateFlow.update { state ->
            state.copy(
                filterType = FilterType.Monthly(),
            )
        }
        reload()
    }

    private fun yearlyClicked() {
        _stateFlow.update { state ->
            state.copy(
                filterType = FilterType.Yearly(),
            )
        }
        reload()
    }

    private fun onDateMoved(isRight: Boolean) {
        _stateFlow.update { state ->
            state.copy(
                filterType = state.filterType.move(isRight),
            )
        }
        reload()
    }

    private fun onToSpendings() {
        toSpendings(state.filterType.from, state.filterType.to)
    }

    private fun onPageChanged(isOpen: Boolean) {
        _stateFlow.update { state ->
            state.copy(
                isMyBudgetOpened = isOpen,
            )
        }
    }


    private fun saveBudget() {
        state.isLoading.value = true
        viewModelScope.launch {
            saveBudgetUseCase(
                budget = state.budgetLines,
                date = state.filterType.from,
                isForMonth = state.filterType is FilterType.Monthly,
                isForFamily = state.isMyBudgetOpened.not(),
            )
            reload()
        }
    }

    private fun reload() {
        state.isLoading.value = true
        viewModelScope.launch {
            val budgetLines = getBudgetLinesUseCase(
                isForMonth = state.filterType is FilterType.Monthly,
                isForFamily = state.isMyBudgetOpened.not(),
                from = state.filterType.from,
                to = state.filterType.to,
            )

            _stateFlow.update { state ->
                state.copy(
                    budgetLines = budgetLines,
                    currency = getChosenCurrencyUseCase(),
                    isSaveVisible = false,
                )
            }
            state.isLoading.value = false
        }
    }

    private val state: BudgetState
        get() = _stateFlow.value

    private val _stateFlow = MutableStateFlow(
        BudgetState(
            budgetChangeState = BudgetLineChangeDialogState(
                onAdditionalAmountValueChanged = ::onAdditionalAmountValueChanged,
                onAmountValueChanged = ::onAmountValueChanged,
                onCloseDialogClicked = ::onCloseDialogClicked,
                onBudgetLineNameChanged = ::onBudgetLineNameChanged,
                onOperationApplyClicked = ::onOperationApplyClicked,
                onOperationChanged = ::onOperationChanged,
                onOkDialogClicked = ::onOkDialogClicked,
            ),
            onPageChanged = ::onPageChanged,
            monthlyClicked = ::monthlyClicked,
            yearlyClicked = ::yearlyClicked,
            onEditClicked = ::onEditClicked,
            onSave = ::saveBudget,
            onToSpendings = ::onToSpendings,
            onDateMoved = ::onDateMoved,
        ),
    )
    val stateFlow = _stateFlow.asStateFlow()

    init {
        reload()
    }

}

data class BudgetState(
    val budgetChangeState: BudgetLineChangeDialogState,
    val budgetLines: List<BudgetLineUiData> = emptyList(),
    val currency: Currency = Currency.getInstance(Locale.getDefault()),
    val isMyBudgetOpened: Boolean = true,
    val isBalanceError: Boolean = false,
    val isSaveVisible: Boolean = false,
    val filterType: FilterType = FilterType.Monthly(),
    val isLoading: MutableState<Boolean> = mutableStateOf(true),
    val onPageChanged: (Boolean) -> Unit,
    val onDateMoved: (isRight: Boolean) -> Unit,
    val monthlyClicked: () -> Unit,
    val yearlyClicked: () -> Unit,
    val onEditClicked: (BudgetLineUiData?) -> Unit,
    val onToSpendings: () -> Unit,
    val onSave: () -> Unit,
)

data class BudgetLineChangeDialogState(
    val additionalAmount: String = "",
    val budgetLine: BudgetLineUiData = BudgetLineUiData(
        id = "",
        repeatableId = "",
        name = "",
        amount = "",
        isDefault = false,
    ),
    val currency: Currency = Currency.getInstance(Locale.getDefault()),
    val operation: Operation = Operation.NONE,
    val isShown: Boolean = false,
    val onOperationApplyClicked: () -> Unit,
    val onAmountValueChanged: (String) -> Unit,
    val onAdditionalAmountValueChanged: (String) -> Unit,
    val onBudgetLineNameChanged: (String) -> Unit,
    val onCloseDialogClicked: () -> Unit,
    val onOkDialogClicked: () -> Unit,
    val onOperationChanged: () -> Unit,
)


