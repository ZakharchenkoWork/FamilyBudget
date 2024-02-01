package com.faigenbloom.famillyspandings.ui.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.famillyspandings.common.addAsMoney
import com.faigenbloom.famillyspandings.common.decAsMoney
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

    private var totalBalance = ""
    private var familyTotal = ""
    private var currentBalance = ""
    private var plannedBudgetMonth = ""
    private var plannedBudgetYear = ""
    private var spent = ""
    private var plannedSpendings = ""
    private var additionalAmount: String = ""

    private var currency: Currency = Currency.getInstance(Locale.getDefault())
    private var isMyBudgetOpened: Boolean = true
    private var isBalanceError: Boolean = false
    private var isSaveVisible: Boolean = false

    private var filter: FilterType = FilterType.Monthly()

    private fun onAdditionalAmountValueChanged(amount: String) {
        additionalAmount = amount
        updateUi()
    }

    private fun onIncomeAddClicked() {
        totalBalance = totalBalance.addAsMoney(additionalAmount)
        isSaveVisible = true
        updateUi()
    }

    private fun monthlyClicked() {
        filter = FilterType.Monthly()
        reload()
    }

    private fun yearlyClicked() {
        filter = FilterType.Yearly()
        reload()
    }

    private fun onPageChanged(isOpen: Boolean) {
        isMyBudgetOpened = isOpen
        updateUi()
    }

    private fun onPlannedBudgetChanged(amount: String) {
        if (filter is FilterType.Monthly) {
            plannedBudgetMonth = amount
        } else {
            plannedBudgetYear = amount
        }
        currentBalance = calculateBalance()
        isSaveVisible = true
        updateUi()
    }

    private fun onTotalBalanceChanged(amount: String) {
        totalBalance = amount
        isSaveVisible = true
        updateUi()
    }

    private fun saveBudget() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveBudgetData(
                BudgetEntity(
                    id = 0L,
                    familyTotal = familyTotal.toLongMoney(),
                    personalTotal = totalBalance.toLongMoney(),
                    plannedBudgetMonth = plannedBudgetMonth.toLongMoney(),
                    plannedBudgetYear = plannedBudgetYear.toLongMoney(),
                ),
            )
            isSaveVisible = false
            updateUi()
        }
    }

    private val state: BudgetState
        get() = BudgetState(
            total = currentBalance,
            totalBalance = totalBalance,
            plannedBudget = if (filter is FilterType.Monthly) plannedBudgetMonth else plannedBudgetYear,
            spent = spent,
            plannedSpendings = plannedSpendings,
            currency = currency,
            isBalanceError = isBalanceError,
            isMyBudgetOpened = isMyBudgetOpened,
            additionalAmount = additionalAmount,
            isSaveVisible = isSaveVisible,
            filter = filter,
            onAdditionalAmountValueChanged = ::onAdditionalAmountValueChanged,
            onIncomeAddClicked = ::onIncomeAddClicked,
            onPageChanged = ::onPageChanged,
            onTotalBalanceChanged = ::onTotalBalanceChanged,
            onPlannedBudgetChanged = ::onPlannedBudgetChanged,
            monthlyClicked = ::monthlyClicked,
            yearlyClicked = ::yearlyClicked,
            onSave = ::saveBudget,
        )

    private val _stateFlow = MutableStateFlow(state)
    val stateFlow = _stateFlow.asStateFlow()
        .apply {
            reload()
        }

    private fun reload() {
        viewModelScope.launch(Dispatchers.IO) {
            val budgetData = repository.getBudgetData()
            totalBalance = budgetData.personalTotal.toReadableMoney()
            plannedBudgetMonth = budgetData.plannedBudgetMonth.toReadableMoney()
            familyTotal = budgetData.familyTotal.toReadableMoney()
            plannedBudgetYear = budgetData.plannedBudgetYear.toReadableMoney()

            spent = getSpentTotalUseCase(false, filter.from, filter.to)
            plannedSpendings = getSpentTotalUseCase(true, filter.from, filter.to)

            currentBalance = calculateBalance()

            isBalanceError = currentBalance.toLongMoney() < 0L
            currency = getChosenCurrencyUseCase()

            updateUi()
        }
    }

    private fun calculateBalance(): String {
        val budget: String = if (filter is FilterType.Monthly) {
            plannedBudgetMonth
        } else {
            plannedBudgetYear
        }
        return budget.decAsMoney(spent)
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
    val isSaveVisible: Boolean,
    val filter: FilterType,
    val onAdditionalAmountValueChanged: (String) -> Unit,
    val onIncomeAddClicked: () -> Unit,
    val onPageChanged: (Boolean) -> Unit,
    val onTotalBalanceChanged: (String) -> Unit,
    val onPlannedBudgetChanged: (String) -> Unit,
    val monthlyClicked: () -> Unit,
    val yearlyClicked: () -> Unit,
    val onSave: () -> Unit,
)
