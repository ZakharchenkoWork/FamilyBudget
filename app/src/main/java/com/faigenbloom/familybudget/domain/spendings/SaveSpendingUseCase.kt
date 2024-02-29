package com.faigenbloom.familybudget.domain.spendings

import com.faigenbloom.familybudget.datasources.ID
import com.faigenbloom.familybudget.datasources.IdSource
import com.faigenbloom.familybudget.domain.GenerateIdUseCase
import com.faigenbloom.familybudget.repositories.SpendingsRepository
import com.faigenbloom.familybudget.ui.spendings.SpendingUiData
import com.faigenbloom.familybudget.ui.spendings.mappers.SpendingsMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveSpendingUseCase(
    private val idGeneratorUseCase: GenerateIdUseCase,
    private val spendingsRepository: SpendingsRepository,
    private val spendingsMapper: SpendingsMapper,
    private val idSource: IdSource,
) {
    suspend operator fun invoke(
        spending: SpendingUiData,
    ): String {
        return withContext(Dispatchers.IO) {
            val spendingId = idGeneratorUseCase(spending.id)

            spendingsRepository.saveSpending(
                spendingsMapper.forDB(
                    spending.copy(
                        id = spendingId,
                        ownerId = spending.ownerId.ifBlank { idSource[ID.USER] },
                    ),
                ),
            )

            spendingId
        }
    }
}
