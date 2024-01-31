package com.faigenbloom.famillyspandings.ui.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.famillyspandings.common.toLongMoney
import com.faigenbloom.famillyspandings.common.toReadableMoney
import com.faigenbloom.famillyspandings.datasources.entities.BudgetEntity
import com.faigenbloom.famillyspandings.domain.GetChosenCurrencyUseCase
import com.faigenbloom.famillyspandings.domain.spendings.GetSpentTotalUseCase
import com.faigenbloom.famillyspandings.domain.statistics.FilterType
import com.faigenbloom.famillyspandings.repositories.BudgetPageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Currency
import java.util.Locale


class BudgetPageViewModel(
    private val repository: BudgetPageRepository,
    private val getChosenCurrencyUseCase: GetChosenCurrencyUseCase,
    private val getSpentTotalUseCase: GetSpentTotalUseCase,
) : ViewModel() {

    private var total = ""
    private var plannedBudget = ""
    private var spent = ""
    private var plannedSpendings = ""
    private var totalBalance = ""
    private var additionalAmount: String = ""

    private var currency: Currency = Currency.getInstance(Locale.getDefault())
    private var isMyBudgetOpened: Boolean = true
    private var isBalanceError: Boolean = false
    private var filter: FilterType = FilterType.Monthly()

    private fun onAdditionalAmountValueChanged(amount: String) {
        additionalAmount = amount
        updateUi()
    }

    private fun onIncomeAddClicked() {

    }

    private fun onPageChanged(isOpen: Boolean) {
        isMyBudgetOpened = isOpen
        updateUi()
    }

    private fun onPlannedBudgetChanged(amount: String) {
        plannedBudget = amount
        updateUi()
    }

    private fun onTotalChanged(amount: String) {
        total = amount
        updateUi()
    }

    private fun onTotalBalanceChanged(amount: String) {
        totalBalance = amount
        updateUi()
    }

    private fun saveBudget() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveBudgetData(
                BudgetEntity(
                    familyTotal = total.toLongMoney(),
                    plannedBudget = plannedBudget.toLongMoney(),
                    spent = spent.toLongMoney(),
                    plannedSpendings = plannedSpendings.toLongMoney(),
                ),
            )
        }
    }

    private val state: BudgetState
        get() = BudgetState(
            total = total,
            isMyBudgetOpened = isMyBudgetOpened,
            onPageChanged = ::onPageChanged,
            plannedBudget = plannedBudget,
            spent = spent,
            plannedSpendings = plannedSpendings,
            totalBalance = totalBalance,
            isBalanceError = isBalanceError,
            currency = currency,
            onTotalChanged = ::onTotalChanged,
            onTotalBalanceChanged = ::onTotalBalanceChanged,
            onPlannedBudgetChanged = ::onPlannedBudgetChanged,
            additionalAmount = additionalAmount,
            onAdditionalAmountValueChanged = ::onAdditionalAmountValueChanged,
            onIncomeAddClicked = ::onIncomeAddClicked,
        )

    private val _stateFlow = MutableStateFlow(state)
    val stateFlow = _stateFlow.asStateFlow()
        .apply {
            viewModelScope.launch(Dispatchers.IO) {
                val budgetData = repository.getBudgetData()
                plannedBudget = budgetData.plannedBudget.toReadableMoney()
                spent = getSpentTotalUseCase(false, filter.from, filter.to)
                plannedSpendings = getSpentTotalUseCase(true, filter.from, filter.to)
                total = (plannedBudget.toLongMoney() - spent.toLongMoney()).toReadableMoney()
                isBalanceError = total.toLongMoney() < 0L
                currency = getChosenCurrencyUseCase()
                totalBalance = "0"
                updateUi()
            }
        }

    private fun updateUi() {
        _stateFlow.update { state }
    }
}
data class BudgetState(
    val total: String,
    val totalBalance: String,
    val plannedBudget: String,
    val spent: String,
    val plannedSpendings: String,
    val currency: Currency,
    val isBalanceError: Boolean,
    val isMyBudgetOpened: Boolean,
    val additionalAmount: String,
    val onAdditionalAmountValueChanged: (String) -> Unit,
    val onIncomeAddClicked: () -> Unit,
    val onPageChanged: (Boolean) -> Unit,
    val onTotalChanged: (String) -> Unit,
    val onTotalBalanceChanged: (String) -> Unit,
    val onPlannedBudgetChanged: (String) -> Unit,
)
