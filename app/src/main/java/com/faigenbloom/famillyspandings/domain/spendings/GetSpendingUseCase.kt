package com.faigenbloom.famillyspandings.domain.spendings

import com.faigenbloom.famillyspandings.comon.Identifiable
import com.faigenbloom.famillyspandings.comon.Mapper
import com.faigenbloom.famillyspandings.datasources.entities.SpendingEntity
import com.faigenbloom.famillyspandings.repositories.SpendingsRepository

class GetSpendingUseCase<T : Identifiable>(
    private val spendingsRepository: SpendingsRepository,
    private val mapper: Mapper<T, SpendingEntity>,
) {

    suspend operator fun invoke(id: String): T {
        return mapper.forUI(spendingsRepository.getSpending(id))
    }
}
