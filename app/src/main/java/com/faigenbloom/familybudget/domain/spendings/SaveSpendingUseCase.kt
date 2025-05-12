package com.faigenbloom.familybudget.domain.spendings

import com.faigenbloom.familybudget.datasources.ID
import com.faigenbloom.familybudget.datasources.IdSource
import com.faigenbloom.familybudget.datasources.REPEAT_INFIX
import com.faigenbloom.familybudget.domain.GenerateIdUseCase
import com.faigenbloom.familybudget.domain.mappers.RepeatableOptionsMapper
import com.faigenbloom.familybudget.domain.mappers.SpendingsMapper
import com.faigenbloom.familybudget.repositories.SpendingsRepository
import com.faigenbloom.familybudget.ui.spendings.RepeatableOptionDataUi
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
            val repeatOptions = prepareRepeatOptions(spendingId, spending)

            spendingsRepository.saveSpending(
                spendingsMapper.forDB(
                    spending.copy(
                        id = spendingId,
                        ownerId = spending.ownerId.ifBlank { idSource[ID.USER] },
                        repeatOptions = repeatOptions,
                    ),
                ),
            )

            spendingId
        }
    }

    private suspend fun prepareRepeatOptions(spendingId: String, spending: SpendingUiData): RepeatableOptionDataUi? {

        val repeatableOptionsId = if (spendingId.contains(REPEAT_INFIX)) null else spending.repeatOptions?.let {
            idGeneratorUseCase(spending.repeatOptions.id)
        }
        val repeatOptions = if (spendingId.contains(REPEAT_INFIX).not()) {
            spending.repeatOptions?.let {
                spendingsRepository.saveRepeatableOptions(
                    repeatableOptionsMapper.forDB(it.copy(id = repeatableOptionsId ?: "")),
                )
                it.copy(id = repeatableOptionsId!!)
            }
        } else {
            spending.repeatOptions?.let {
                spendingsRepository.saveRepeatableOptions(
                    repeatableOptionsMapper.forDB(spending.repeatOptions.copy(excludedIDs = spending.repeatOptions.excludedIDs + spendingId))
                )
            }
            null
        }
        return repeatOptions
    }
}
