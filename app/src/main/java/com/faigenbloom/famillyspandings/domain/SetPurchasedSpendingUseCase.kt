package com.faigenbloom.famillyspandings.domain

import com.faigenbloom.famillyspandings.repositories.SpendingsRepository

class SetPurchasedSpendingUseCase(
    private val spendingsRepository: SpendingsRepository,
) {
    suspend operator fun invoke(spendingId: String) {
        spendingsRepository.markSpendingPurchased(spendingId)
    }
}
