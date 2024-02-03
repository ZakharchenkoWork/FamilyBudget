package com.faigenbloom.familybudget.domain.spendings

import com.faigenbloom.familybudget.common.Identifiable
import com.faigenbloom.familybudget.common.Mapper
import com.faigenbloom.familybudget.datasources.entities.SpendingEntity
import com.faigenbloom.familybudget.repositories.SpendingsRepository
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
