package com.faigenbloom.familybudget.domain.spendings

import com.faigenbloom.familybudget.repositories.SpendingsRepository
import com.faigenbloom.familybudget.ui.spendings.SpendingUiData
import com.faigenbloom.familybudget.ui.spendings.mappers.SpendingsMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetSpendingUseCase(
    private val spendingsRepository: SpendingsRepository,
    private val mapper: SpendingsMapper,
) {
    suspend operator fun invoke(id: String): SpendingUiData {
        return withContext(Dispatchers.IO) {
            mapper.forUI(spendingsRepository.getSpending(id))
        }
    }
}
