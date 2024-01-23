package com.faigenbloom.famillyspandings.domain.spendings

import com.faigenbloom.famillyspandings.common.Identifiable
import com.faigenbloom.famillyspandings.common.Mapper
import com.faigenbloom.famillyspandings.datasources.entities.SpendingEntity
import com.faigenbloom.famillyspandings.repositories.SpendingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetSpendingUseCase<T : Identifiable>(
    private val spendingsRepository: SpendingsRepository,
    private val mapper: Mapper<T, SpendingEntity>,
) {

    suspend operator fun invoke(id: String): T {
        return withContext(Dispatchers.IO) {
            mapper.forUI(spendingsRepository.getSpending(id))
        }
    }
}
