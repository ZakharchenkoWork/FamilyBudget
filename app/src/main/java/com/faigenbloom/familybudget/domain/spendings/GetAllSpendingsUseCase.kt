package com.faigenbloom.familybudget.domain.spendings

import com.faigenbloom.familybudget.repositories.SpendingsRepository
import com.faigenbloom.familybudget.ui.spendings.SpendingUiData
import com.faigenbloom.familybudget.ui.spendings.mappers.SpendingsMapper

class GetAllSpendingsUseCase(
    private val spendingsRepository: SpendingsRepository,
    private val mapper: SpendingsMapper,
) {
    suspend operator fun invoke(isPlanned: Boolean): List<SpendingUiData> {
        return spendingsRepository.getSpendings(isPlanned)
            .map { mapper.forUI(it) }
    }
}


