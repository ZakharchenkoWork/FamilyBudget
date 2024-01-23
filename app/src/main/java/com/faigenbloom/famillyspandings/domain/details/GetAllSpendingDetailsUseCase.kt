package com.faigenbloom.famillyspandings.domain.details

import com.faigenbloom.famillyspandings.common.Identifiable
import com.faigenbloom.famillyspandings.common.Mapper
import com.faigenbloom.famillyspandings.datasources.entities.SpendingDetailEntity
import com.faigenbloom.famillyspandings.repositories.DetailsRepository

class GetAllSpendingDetailsUseCase<T : Identifiable>(
    private val detailsRepository: DetailsRepository,
    private val mapper: Mapper<T, SpendingDetailEntity>,
) {
    suspend operator fun invoke() =
        detailsRepository.getAllDetails().map { mapper.forUI(it) }
}

