package com.faigenbloom.familybudget.domain.spendings

import com.faigenbloom.familybudget.domain.mappers.SpendingsMapper
import com.faigenbloom.familybudget.repositories.SpendingsRepository
import com.faigenbloom.familybudget.ui.spendings.SpendingUiData

class GetAllSpendingsUseCase(
    private val spendingsRepository: SpendingsRepository,
    private val mapper: SpendingsMapper,
) {
    suspend operator fun invoke(isPlanned: Boolean): List<SpendingUiData> {
        return spendingsRepository.getSpendings(isPlanned)//TODO: Probasbly need to add repeatables too
            .map { mapper.forUI(it) }
    }
}


