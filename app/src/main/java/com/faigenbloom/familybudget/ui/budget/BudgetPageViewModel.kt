package com.faigenbloom.familybudget.ui.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.familybudget.common.addAsMoney
import com.faigenbloom.familybudget.common.decAsMoney
import com.faigenbloom.familybudget.common.toLongMoney
import com.faigenbloom.familybudget.datasources.db.entities.BudgetEntity
import com.faigenbloom.familybudget.domain.budget.GetBudgetLinesUseCase
import com.faigenbloom.familybudget.domain.budget.SaveBudgetUseCase
import com.faigenbloom.familybudget.domain.currency.GetChosenCurrencyUseCase
import com.faigenbloom.familybudget.domain.statistics.FilterType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Currency
import java.util.Locale


class BudgetPageViewModel(
    private val saveBudgetUseCase: SaveBudgetUseCase,
    private val getBudgetLinesUseCase: GetBudgetLinesUseCase,

    private val getChosenCurrencyUseCase: GetChosenCurrencyUseCase,

    ) : ViewModel() {
    private fun onAdditionalAmountValueChanged(amount: String) {
        _stateFlow.update { state ->
            state.copy(
                additionalAmount = amount,
            )
        }
    }

    private fun onIncomeAddClicked() {
        _stateFlow.update { state ->
            state.copy(
                totalBalance = state.totalBalance.addAsMoney(state.additionalAmount),
                isSaveVisible = true,
            )
        }
    }

    private fun monthlyClicked() {
        _stateFlow.update { state ->
            state.copy(
                filter = FilterType.Monthly(),
            )
        }
        reload()
    }

    private fun yearlyClicked() {
        _stateFlow.update { state ->
            state.copy(
                filter = FilterType.Yearly(),
            )
        }
        reload()
    }

    private fun onPageChanged(isOpen: Boolean) {
        _stateFlow.update { state ->
            state.copy(
                isMyBudgetOpened = isOpen,
            )
        }
    }

    private fun onPlannedBudgetChanged(amount: String) {
        _stateFlow.update { state ->
            state.copy(
                plannedBudget = amount,
                currentBalance = calculateBalance(),
                isSaveVisible = true,
            )
        }
    }

    private fun onTotalBalanceChanged(amount: String) {
        _stateFlow.update { state ->
            state.copy(
                totalBalance = amount,
                isSaveVisible = true,
            )
        }
    }

    private fun saveBudget() {
        viewModelScope.launch {
            saveBudgetUseCase(
                BudgetEntity(
                    id = 0L,
                    familyTotal = state.totalBalance.toLongMoney(),
                    personalTotal = state.totalBalance.toLongMoney(),
                    plannedBudgetMonth = state.plannedBudget.toLongMoney(),
                    plannedBudgetYear = state.plannedBudget.toLongMoney(),
                ),
            )
            _stateFlow.update { state ->
                state.copy(
                    isSaveVisible = false,
                )
            }
        }
    }

    private fun calculateBalance(): String {
        return state.plannedBudget.decAsMoney(state.spent)
    }

    private fun reload() {
        viewModelScope.launch {
            val budgetLines = getBudgetLinesUseCase(state.filter.from, state.filter.to)
            _stateFlow.update { state ->
                state.copy(
                    budgetLines = budgetLines,
                    currency = getChosenCurrencyUseCase(),
                )
            }
            /*  val currentBalance = state.plannedBudget.decAsMoney(state.spent)
              _stateFlow.update { state ->
                  state.copy(
                      totalBalance = budgetData.personalTotal.toReadableMoney(),
                      plannedBudget = if (state.filter is FilterType.Monthly) {
                          budgetData.plannedBudgetMonth.toReadableMoney()
                      } else {
                          budgetData.plannedBudgetYear.toReadableMoney()
                      },
                      spent = getSpentTotalUseCase(false, state.filter.from, state.filter.to),
                      plannedSpendings = getSpentTotalUseCase(
                          true,
                          state.filter.from,
                          state.filter.to,
                      ),
                      currentBalance = currentBalance,
                      isBalanceError = currentBalance.toLongMoney() < 0L,

                  )
              }*/
        }
    }

    private val state: BudgetState
        get() = _stateFlow.value

    private val _stateFlow = MutableStateFlow(
        BudgetState(
            onAdditionalAmountValueChanged = ::onAdditionalAmountValueChanged,
            onIncomeAddClicked = ::onIncomeAddClicked,
            onPageChanged = ::onPageChanged,
            onTotalBalanceChanged = ::onTotalBalanceChanged,
            onPlannedBudgetChanged = ::onPlannedBudgetChanged,
            monthlyClicked = ::monthlyClicked,
            yearlyClicked = ::yearlyClicked,
            onSave = ::saveBudget,
        ),
    )
    val stateFlow = _stateFlow.asStateFlow()

    init {
        reload()
    }
}

data class BudgetState(
    val budgetLines: List<BudgetLineUiData> = emptyList(),
    val currentBalance: String = "",
    val totalBalance: String = "",
    val plannedBudget: String = "",
    val spent: String = "",
    val plannedSpendings: String = "",
    val additionalAmount: String = "",
    val currency: Currency = Currency.getInstance(Locale.getDefault()),
    val isMyBudgetOpened: Boolean = true,
    val isBalanceError: Boolean = false,
    val isSaveVisible: Boolean = false,
    val filter: FilterType = FilterType.Monthly(),
    val onAdditionalAmountValueChanged: (String) -> Unit,
    val onIncomeAddClicked: () -> Unit,
    val onPageChanged: (Boolean) -> Unit,
    val onTotalBalanceChanged: (String) -> Unit,
    val onPlannedBudgetChanged: (String) -> Unit,
    val monthlyClicked: () -> Unit,
    val yearlyClicked: () -> Unit,
    val onSave: () -> Unit,
)
