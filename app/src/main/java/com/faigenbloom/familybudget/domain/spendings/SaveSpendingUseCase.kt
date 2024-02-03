package com.faigenbloom.familybudget.domain.spendings

import com.faigenbloom.familybudget.common.Identifiable
import com.faigenbloom.familybudget.common.Mapper
import com.faigenbloom.familybudget.datasources.entities.SpendingEntity
import com.faigenbloom.familybudget.domain.GenerateIdUseCase
import com.faigenbloom.familybudget.repositories.SpendingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveSpendingUseCase<T : Identifiable>(
    private val idGeneratorUseCase: GenerateIdUseCase,
    private val spendingsRepository: SpendingsRepository,
    private val spendingsMapper: Mapper<T, SpendingEntity>,
) {
    suspend operator fun invoke(
        spending: T,
    ): String {
        return withContext(Dispatchers.IO) {
            val spendingId = idGeneratorUseCase(spending.id)
            spendingsRepository.saveSpending(
                spendingsMapper.forDB(spendingsMapper.copyChangingId(spending, spendingId)),
            )
            spendingId
        }
    }
}
