package com.faigenbloom.famillyspandings.ui.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.famillyspandings.common.toLongMoney
import com.faigenbloom.famillyspandings.common.toReadableMoney
import com.faigenbloom.famillyspandings.datasources.entities.BudgetEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BudgetPageViewModel(
    private val repository: BudgetPageRepository,
) : ViewModel() {

    private var familyTotal = ""
    private var plannedBudget = ""
    private var spent = ""
    private var plannedSpendings = ""
    private var isMyBudgetOpened: Boolean = true
    private var onPageChanged: (Boolean) -> Unit = {
        isMyBudgetOpened = it
        _budgetStateFlow.update { budgetState }
    }
    private var onPlannedBudgetChanged: (String) -> Unit = {
        plannedBudget = it
        _budgetStateFlow.update { budgetState }
        saveBudget()
    }

    private var onTotalChanged: (String) -> Unit = {
        familyTotal = it
        _budgetStateFlow.update { budgetState }
        saveBudget()
    }

    private val budgetState: BudgetState
        get() = BudgetState(
            familyTotal = familyTotal,
            isMyBudgetOpened = isMyBudgetOpened,
            onPageChanged = onPageChanged,
            plannedBudget = plannedBudget,
            spent = spent,
            plannedSpendings = plannedSpendings,
            onTotalChanged = onTotalChanged,
            onPlannedBudgetChanged = onPlannedBudgetChanged,
        )

    private val _budgetStateFlow = MutableStateFlow(budgetState)
    val budgetStateFlow = _budgetStateFlow.asStateFlow()
        .apply {
            viewModelScope.launch(Dispatchers.IO) {
                val budgetData = repository.getBudgetData()
                familyTotal = budgetData.familyTotal.toReadableMoney()
                plannedBudget = budgetData.plannedBudget.toReadableMoney()
                spent = budgetData.spent.toReadableMoney()
                plannedSpendings = budgetData.plannedSpendings.toReadableMoney()
                _budgetStateFlow.update { budgetState }
            }
        }

    private fun saveBudget() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveBudgetData(
                BudgetEntity(
                    familyTotal = familyTotal.toLongMoney(),
                    plannedBudget = plannedBudget.toLongMoney(),
                    spent = spent.toLongMoney(),
                    plannedSpendings = plannedSpendings.toLongMoney(),
                ),
            )
        }
    }
}

data class BudgetState(
    val familyTotal: String,
    val isMyBudgetOpened: Boolean,
    val onPageChanged: (Boolean) -> Unit,
    val plannedBudget: String,
    val spent: String,
    val plannedSpendings: String,
    val onTotalChanged: (String) -> Unit,
    val onPlannedBudgetChanged: (String) -> Unit,
)
