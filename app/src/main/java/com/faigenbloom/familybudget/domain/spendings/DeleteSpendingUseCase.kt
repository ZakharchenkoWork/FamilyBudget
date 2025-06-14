package com.faigenbloom.familybudget.domain.spendings

import com.faigenbloom.familybudget.repositories.DetailsRepository
import com.faigenbloom.familybudget.repositories.SpendingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeleteSpendingUseCase(
    private val spendingsRepository: SpendingsRepository,
) {
    suspend operator fun invoke(spendingId: String) {
        withContext(Dispatchers.IO) {
            if (spendingId.isNotBlank()) {
                spendingsRepository.deleteSpending(spendingId)
            }
        }
    }
}
