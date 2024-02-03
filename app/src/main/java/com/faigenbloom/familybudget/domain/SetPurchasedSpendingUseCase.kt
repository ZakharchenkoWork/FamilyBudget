package com.faigenbloom.familybudget.domain

import com.faigenbloom.familybudget.repositories.SpendingsRepository

class SetPurchasedSpendingUseCase(
    private val spendingsRepository: SpendingsRepository,
) {
    suspend operator fun invoke(spendingId: String) {
        spendingsRepository.markSpendingPurchased(spendingId)
    }
}
