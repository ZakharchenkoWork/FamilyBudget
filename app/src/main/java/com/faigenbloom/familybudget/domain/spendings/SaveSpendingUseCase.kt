package com.faigenbloom.familybudget.domain.spendings

import com.faigenbloom.familybudget.datasources.ID
import com.faigenbloom.familybudget.datasources.IdSource
import com.faigenbloom.familybudget.domain.GenerateIdUseCase
import com.faigenbloom.familybudget.domain.mappers.RepeatableOptionsMapper
import com.faigenbloom.familybudget.domain.mappers.SpendingsMapper
import com.faigenbloom.familybudget.repositories.SpendingsRepository
import com.faigenbloom.familybudget.ui.spendings.SpendingUiData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveSpendingUseCase(
    private val idGeneratorUseCase: GenerateIdUseCase,
    private val spendingsRepository: SpendingsRepository,
    private val spendingsMapper: SpendingsMapper,
    private val repeatableOptionsMapper: RepeatableOptionsMapper,
    private val idSource: IdSource,
) {
    suspend operator fun invoke(
        spending: SpendingUiData,
    ): String {
        return withContext(Dispatchers.IO) {
            val spendingId = idGeneratorUseCase(spending.id)
            val repeatableOptionsId = spending.repeatOptions?.let {
                idGeneratorUseCase(spending.repeatOptions.id)
            }
            spending.repeatOptions?.let {
                spendingsRepository.saveRepeatableOptions(
                    repeatableOptionsMapper.forDB(it.copy(id = repeatableOptionsId?:"")),
                )
            }
            spendingsRepository.saveSpending(
                spendingsMapper.forDB(
                    spending.copy(
                        id = spendingId,
                        ownerId = spending.ownerId.ifBlank { idSource[ID.USER] },
                        repeatOptions = if (spending.isPlanned.not()) {
                            null
                        } else spending.repeatOptions?.copy(id = repeatableOptionsId?:""),
                    ),
                ),
            )

            spendingId
        }
    }
}
