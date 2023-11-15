package com.faigenbloom.famillyspandings.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faigenbloom.famillyspandings.comon.toReadableMoney
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BudgetPageViewModel(
    private val repository: BadgetPageRepository,
) : ViewModel() {

    private var familyTotal = "151"
    private var plannedBudget = "20000 UAH"
    private var spent = "15000 UAH"
    private var plannedSpendings = "2000 UAH"
    private var isMyBudgetOpened: Boolean = true
    private var onPageChanged: (Boolean) -> Unit = {
        isMyBudgetOpened = it
        _budgetStateFlow.update { budgetState }
    }
    private var onPlannedBudgetChanged: (String) -> Unit = {
        plannedBudget = it
        _budgetStateFlow.update { budgetState }
    }
    private var onTotalChanged: (String) -> Unit = {
        familyTotal = it
        _budgetStateFlow.update { budgetState }
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
            viewModelScope.launch {
                val budgetData = repository.getBudgetData()
                familyTotal = budgetData.familyTotal.toReadableMoney()
                plannedBudget = budgetData.plannedBudget.toReadableMoney()
                spent = budgetData.spent.toReadableMoney()
                plannedSpendings = budgetData.plannedSpendings.toReadableMoney()
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
