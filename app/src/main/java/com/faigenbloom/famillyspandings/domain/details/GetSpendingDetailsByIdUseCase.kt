package com.faigenbloom.famillyspandings.domain.details

import com.faigenbloom.famillyspandings.comon.Identifiable
import com.faigenbloom.famillyspandings.comon.Mapper
import com.faigenbloom.famillyspandings.datasources.entities.SpendingDetailEntity
import com.faigenbloom.famillyspandings.repositories.DetailsRepository

class GetSpendingDetailsByIdUseCase<T : Identifiable>(
    private val detailsRepository: DetailsRepository,
    private val mapper: Mapper<T, SpendingDetailEntity>,
) {
    suspend operator fun invoke(spendingId: String) = if (spendingId.isNotBlank()) {
        detailsRepository.getSpendingDetails(spendingId).map { mapper.forUI(it) }
    } else {
        emptyList()
    }
}

