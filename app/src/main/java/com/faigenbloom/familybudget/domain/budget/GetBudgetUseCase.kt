package com.faigenbloom.familybudget.domain.budget

import com.faigenbloom.familybudget.datasources.db.entities.BudgetEntity
import com.faigenbloom.familybudget.repositories.BudgetPageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetBudgetUseCase(
    private val budgetPageRepository: BudgetPageRepository,
) {
    suspend operator fun invoke(): BudgetEntity {
        return withContext(Dispatchers.IO) {
            budgetPageRepository.getBudgetData()
        }
    }
}
