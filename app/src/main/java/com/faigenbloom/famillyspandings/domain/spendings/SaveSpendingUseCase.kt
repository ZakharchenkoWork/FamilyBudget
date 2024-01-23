package com.faigenbloom.famillyspandings.domain.spendings

import com.faigenbloom.famillyspandings.common.Identifiable
import com.faigenbloom.famillyspandings.common.Mapper
import com.faigenbloom.famillyspandings.datasources.entities.SpendingEntity
import com.faigenbloom.famillyspandings.domain.GenerateIdUseCase
import com.faigenbloom.famillyspandings.repositories.SpendingsRepository
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
