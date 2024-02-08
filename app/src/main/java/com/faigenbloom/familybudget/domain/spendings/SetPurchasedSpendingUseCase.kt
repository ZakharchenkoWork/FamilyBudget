package com.faigenbloom.familybudget.domain.spendings

import com.faigenbloom.familybudget.repositories.SpendingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SetPurchasedSpendingUseCase(
    private val spendingsRepository: SpendingsRepository,
) {
    suspend operator fun invoke(spendingId: String) {
        withContext(Dispatchers.IO) {
            spendingsRepository.markSpendingPurchased(spendingId)
        }
    }
}
