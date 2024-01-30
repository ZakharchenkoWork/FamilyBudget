package com.faigenbloom.famillyspandings.domain.spendings

import com.faigenbloom.famillyspandings.common.toReadableMoney
import com.faigenbloom.famillyspandings.repositories.SpendingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetSpentTotalUseCase(
    private val spendingsRepository: SpendingsRepository,
) {
    suspend operator fun invoke(isPlanned: Boolean, from: Long, to: Long): String {
        return withContext(Dispatchers.IO) {
            spendingsRepository.getSpendingsTotalSpent(isPlanned, from, to).toReadableMoney()
        }
    }
}
