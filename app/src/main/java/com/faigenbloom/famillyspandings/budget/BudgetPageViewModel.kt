package com.faigenbloom.famillyspandings.budget

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class BudgetPageViewModel(private val repository: BadgetPageRepository) : ViewModel() {

    private val budgetState: BudgetState
        get() = BudgetState(
            familyTotal = "151",
        )

    private val _budgetStateFlow = MutableStateFlow(budgetState)
    val budgetStateFlow = _budgetStateFlow.asStateFlow()
}

data class BudgetState(val familyTotal: String)
