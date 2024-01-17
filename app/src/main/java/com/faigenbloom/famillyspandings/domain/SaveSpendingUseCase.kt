package com.faigenbloom.famillyspandings.domain

import com.faigenbloom.famillyspandings.comon.Identifiable
import com.faigenbloom.famillyspandings.comon.Mapper
import com.faigenbloom.famillyspandings.datasources.entities.SpendingEntity
import com.faigenbloom.famillyspandings.repositories.SpendingsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveSpendingUseCase<T : Identifiable>(
    private val idGeneratorUseCase: GenerateIdUseCase,
    private val spendingsRepository: SpendingsRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val spendingsMapper: Mapper<T, SpendingEntity>,
) {
    suspend operator fun invoke(
        spending: T,
    ): String {
        return withContext(ioDispatcher) {
            val spendingId = idGeneratorUseCase(spending.id)
            spendingsRepository.saveSpending(
                spendingsMapper.forDB(spendingsMapper.copyChangingId(spending, spendingId)),
            )
            spendingId
        }
    }
}
