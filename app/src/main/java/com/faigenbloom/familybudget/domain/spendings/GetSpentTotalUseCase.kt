package com.faigenbloom.familybudget.domain.spendings

import com.faigenbloom.familybudget.repositories.SpendingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetSpentTotalUseCase(
    private val spendingsRepository: SpendingsRepository,
) {
    suspend operator fun invoke(isPlanned: Boolean, from: Long, to: Long): Long {
        return withContext(Dispatchers.IO) {
            spendingsRepository.getSpendingsTotalSpent(isPlanned, from, to)
        }
    }
}
