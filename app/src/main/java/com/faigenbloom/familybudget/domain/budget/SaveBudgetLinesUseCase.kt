package com.faigenbloom.familybudget.domain.budget

import com.faigenbloom.familybudget.domain.GenerateIdUseCase
import com.faigenbloom.familybudget.repositories.BudgetRepository
import com.faigenbloom.familybudget.ui.budget.BudgetLineUiData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveBudgetLinesUseCase(
    private val budgetPageRepository: BudgetRepository,
    private val budgetLineMapper: BudgetLineMapper,
    private val generateIdUseCase: GenerateIdUseCase,
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
                    ).let { entity ->
                        if (entity.id.isBlank()) {
                            entity.copy(id = generateIdUseCase())
                        } else {
                            entity
                        }
                    }
                },
            )
        }
    }
}
