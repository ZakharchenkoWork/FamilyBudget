package com.faigenbloom.famillyspandings.domain.spendings

import com.faigenbloom.famillyspandings.common.Identifiable
import com.faigenbloom.famillyspandings.common.Mapper
import com.faigenbloom.famillyspandings.datasources.entities.SpendingEntity
import com.faigenbloom.famillyspandings.repositories.SpendingsRepository

class GetAllSpendingsUseCase<T : Identifiable>(
    private val spendingsRepository: SpendingsRepository,
    private val mapper: Mapper<T, SpendingEntity>,
) {

    suspend operator fun invoke(isPlanned: Boolean): List<T> {
        return spendingsRepository.getSpendings(isPlanned)
            .map { mapper.forUI(it) }
    }
}


