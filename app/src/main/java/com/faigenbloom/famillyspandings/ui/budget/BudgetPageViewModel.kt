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
    private var currency: Currency = Currency.getInstance(Locale.getDefault())
    private var isMyBudgetOpened: Boolean = true
    private var filter: FilterType = FilterType.Monthly()
    private var onPageChanged: (Boolean) -> Unit = {
        isMyBudgetOpened = it
        updateUi()
    }

    private var onPlannedBudgetChanged: (String) -> Unit = {
        plannedBudget = it
        updateUi()
        saveBudget()
    }

    private var onTotalChanged: (String) -> Unit = {
        total = it
        updateUi()
        saveBudget()
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
            onPageChanged = onPageChanged,
            plannedBudget = plannedBudget,
            spent = spent,
            plannedSpendings = plannedSpendings,
            currency = currency,
            onTotalChanged = onTotalChanged,
            onPlannedBudgetChanged = onPlannedBudgetChanged,
        )

    private val _stateFlow = MutableStateFlow(state)
    val stateFlow = _stateFlow.asStateFlow()
        .apply {
            viewModelScope.launch(Dispatchers.IO) {
                val budgetData = repository.getBudgetData()
                total = budgetData.familyTotal.toReadableMoney()
                plannedBudget = budgetData.plannedBudget.toReadableMoney()
                spent = getSpentTotalUseCase(false, filter.from, filter.to)
                plannedSpendings = getSpentTotalUseCase(true, filter.from, filter.to)
                currency = getChosenCurrencyUseCase()
                updateUi()
            }
        }

    private fun updateUi() {
        _stateFlow.update { state }
    }
}

data class BudgetState(
    val total: String,
    val isMyBudgetOpened: Boolean,
    val onPageChanged: (Boolean) -> Unit,
    val plannedBudget: String,
    val spent: String,
    val plannedSpendings: String,
    val currency: Currency,
    val onTotalChanged: (String) -> Unit,
    val onPlannedBudgetChanged: (String) -> Unit,
)
