package com.faigenbloom.famillyspandings.budget

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BudgetPageViewModel(private val repository: BadgetPageRepository) : ViewModel() {

    private var familyTotal = "151"
    private var plannedBudget = "20000 UAH"
    private var spended = "15000 UAH"
    private var plannedSpendings = "2000 UAH"
    private var onChangePlannedBudget = { }
    private var isMyBudgetOpened: Boolean = true
    private var onPageChanged: (Boolean) -> Unit = {
        isMyBudgetOpened = it
        _budgetStateFlow.update { budgetState }
    }
    private val budgetState: BudgetState
        get() = BudgetState(
            familyTotal = familyTotal,
            isMyBudgetOpened = isMyBudgetOpened,
            onPageChanged = onPageChanged,
            plannedBudget = plannedBudget,
            spended = spended,
            plannedSpendings = plannedSpendings,
            onChangePlannedBudget = onChangePlannedBudget,
        )

    private val _budgetStateFlow = MutableStateFlow(budgetState)
    val budgetStateFlow = _budgetStateFlow.asStateFlow()
}

data class BudgetState(
    val familyTotal: String,
    val isMyBudgetOpened: Boolean,
    val onPageChanged: (Boolean) -> Unit,
    val plannedBudget: String,
    val spended: String,
    val plannedSpendings: String,
    val onChangePlannedBudget: () -> Unit,
)
