package com.faigenbloom.familybudget.domain.budget

import com.faigenbloom.familybudget.repositories.BudgetPageRepository
import com.faigenbloom.familybudget.ui.budget.BudgetLineUiData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveBudgetLinesUseCase(
    private val budgetPageRepository: BudgetPageRepository,
    private val budgetLineMapper: BudgetLineMapper,
) {
    suspend operator fun invoke(
        budget: List<BudgetLineUiData>,
        date: Long,
        isForMonth: Boolean,
        isForFamily: Boolean,
    ) {
        withContext(Dispatchers.IO) {
            budgetPageRepository.saveBudgetLines(
                budget.map {
                    budgetLineMapper.forDB(
                        it,
                        date,
                        isForMonth,
                        isForFamily,
                    )
                },
            )
        }
    }
}
