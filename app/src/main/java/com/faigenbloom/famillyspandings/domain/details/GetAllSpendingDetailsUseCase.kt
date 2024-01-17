package com.faigenbloom.famillyspandings.domain.details

import com.faigenbloom.famillyspandings.comon.Identifiable
import com.faigenbloom.famillyspandings.comon.Mapper
import com.faigenbloom.famillyspandings.datasources.entities.SpendingDetailEntity
import com.faigenbloom.famillyspandings.repositories.DetailsRepository

class GetAllSpendingDetailsUseCase<T : Identifiable>(
    private val detailsRepository: DetailsRepository,
    private val mapper: Mapper<T, SpendingDetailEntity>,
) {
    suspend operator fun invoke() =
        detailsRepository.getAllDetails().map { mapper.forUI(it) }
}

