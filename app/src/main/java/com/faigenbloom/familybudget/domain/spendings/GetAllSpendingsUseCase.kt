package com.faigenbloom.familybudget.domain.spendings

import com.faigenbloom.familybudget.common.Identifiable
import com.faigenbloom.familybudget.common.Mapper
import com.faigenbloom.familybudget.datasources.entities.SpendingEntity
import com.faigenbloom.familybudget.repositories.SpendingsRepository

class GetAllSpendingsUseCase<T : Identifiable>(
    private val spendingsRepository: SpendingsRepository,
    private val mapper: Mapper<T, SpendingEntity>,
) {

    suspend operator fun invoke(isPlanned: Boolean): List<T> {
        return spendingsRepository.getSpendings(isPlanned)
            .map { mapper.forUI(it) }
    }
}


